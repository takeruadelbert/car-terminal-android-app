package com.stn.carterminal.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.zyapi.CommonApi;

import androidx.appcompat.app.AppCompatActivity;

import com.magicrf.uhfreaderlib.reader.Tools;
import com.magicrf.uhfreaderlib.reader.UhfReader;
import com.stn.carterminal.R;
import com.stn.carterminal.activity.CheckVehicle.CheckVehicleActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.magicrf.uhfreader.ScreenStateReceiver;
import com.stn.carterminal.magicrf.uhfreader.UhfReaderDevice;

import java.util.List;

public class ScanVehicleActivity extends AppCompatActivity {

    private boolean runFlag = true;
    public static boolean startFlag = false;

    private UhfReader reader;
    private UhfReaderDevice readerDevice;
    private ScreenStateReceiver screenReceiver;
    static CommonApi mCommonApi;
    private static int mComFd = -1;
    MediaPlayer player;

    private static String serialPortPath = "/dev/ttyMT2";
    private static final Integer DELAY_TIME_OPEN_GPIO = 500; // in milliseconds
    private static final Integer DELAY_TIME_SCAN = 1000;
    private String EPC;
    private Long providedServiceId;
    private String menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vehicle);

        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        menu = getIntent().getStringExtra("menu");
        if (menu.equals("detailManifest") && providedServiceId == 0L) {
            Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
            throw new Resources.NotFoundException();
        }

        setupUHFReader();
    }

    private void setupUHFReader() {
        SharedPreferences sharedPortPath = getSharedPreferences("portPath", 0);

        serialPortPath = sharedPortPath.getString("portPath", "/dev/ttyMT1");

        mCommonApi = new CommonApi();

        openGPIO();

        UhfReader.setPortPath(serialPortPath);
        reader = UhfReader.getInstance();
        readerDevice = UhfReaderDevice.getInstance();
        if (reader == null) {
            Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_READER_SERIAL_PORT, Toast.LENGTH_SHORT).show();
            return;
        }
        if (readerDevice == null) {
            Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_READER_POWER, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SharedPreferences shared = getSharedPreferences("power", 0);
        int value = shared.getInt("value", 26);
        Log.d("", "value" + value);
        reader.setOutputPower(value);

        screenReceiver = new ScreenStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);

        Thread thread = new InventoryThread();
        thread.start();
        player = MediaPlayer.create(this, R.raw.msg);
    }

    public static void openGPIO() {
        mCommonApi.setGpioDir(86, 0);
        mCommonApi.getGpioIn(86);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCommonApi.setGpioDir(86, 1);
                mCommonApi.setGpioOut(86, 1);

                startFlag = true;
            }
        }, DELAY_TIME_OPEN_GPIO);
    }

    public static void closeGPIO() {
        mCommonApi.setGpioDir(86, 1);
        mCommonApi.setGpioOut(86, 0);
    }

    @Override
    protected void onDestroy() {
        destroy();
        super.onDestroy();
    }

    private void destroy() {
        runFlag = false;
        if (reader != null) {
            reader.close();
        }
        if (readerDevice != null) {
            readerDevice.powerOff();
        }
        unregisterReceiver(screenReceiver);
        closeGPIO();
        mCommonApi.closeCom(mComFd);
    }

    private void addToList(final String epc) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("EPC = " + epc);
                EPC = epc;

                if (!EPC.isEmpty()) {
                    changeActivity();
                }
            }
        });
    }

    private void changeActivity() {
        switch (menu) {
            case "home":
                Intent checkVehicle = new Intent(getApplicationContext(), CheckVehicleActivity.class);
                checkVehicle.putExtra("EPC", EPC);
                startActivity(checkVehicle);
                finish();
                break;
            case "detailManifest":
                Intent searchVehicle = new Intent(getApplicationContext(), SearchVehicleActivity.class);
                searchVehicle.putExtra("providedServiceId", providedServiceId);
                searchVehicle.putExtra("EPC", EPC);
                startActivity(searchVehicle);
                finish();
                break;
            default:
                break;
        }
    }

    class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while (runFlag) {
                if (startFlag) {
                    epcList = reader.inventoryRealTime();
                    if (epcList != null && !epcList.isEmpty()) {
                        //播放提示音
                        player.start();
                        for (byte[] epc : epcList) {
                            if (epc != null) {
                                String epcStr = Tools.Bytes2HexString(epc, epc.length);
                                addToList(epcStr);
                            }
                        }
                    }
                    epcList = null;
                    try {
                        Thread.sleep(DELAY_TIME_SCAN); // scan delay period
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
