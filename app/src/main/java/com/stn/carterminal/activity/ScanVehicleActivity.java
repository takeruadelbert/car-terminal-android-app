package com.stn.carterminal.activity;

import android.app.ProgressDialog;
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
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.magicrf.uhfreaderlib.reader.Tools;
import com.magicrf.uhfreaderlib.reader.UhfReader;
import com.stn.carterminal.R;
import com.stn.carterminal.activity.changemanifest.ChangeManifestActivity;
import com.stn.carterminal.activity.changeuhftag.ChangeUHFTagActivity;
import com.stn.carterminal.activity.checkvehicle.CheckVehicleActivity;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.magicrf.uhfreader.ScreenStateReceiver;
import com.stn.carterminal.magicrf.uhfreader.UhfReaderDevice;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.response.UhfTag;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.service.VehicleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private String target;

    private ProgressDialog progressDialog;

    private static final String TOOLBAR_TITLE = "Scan Kendaraan";
    private static final String PROGRESS_BAR_MESSAGE = "Loading ...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_vehicle);

        Toolbar toolbar = findViewById(R.id.toolbarScanVehicle);
        toolbar.setTitle(TOOLBAR_TITLE);

        providedServiceId = getIntent().getLongExtra("providedServiceId", 0L);
        menu = getIntent().getStringExtra("menu");
        target = getIntent().getStringExtra("target");
        if (menu.equals("detailManifest") && providedServiceId == 0L) {
            Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_SEARCH_PROVIDED_SERVICE, Toast.LENGTH_SHORT).show();
            throw new Resources.NotFoundException();
        }

        setupUHFReader();

        progressDialog = new ProgressDialog(ScanVehicleActivity.this);
        progressDialog.setMessage(PROGRESS_BAR_MESSAGE);
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
                progressDialog.show();

                System.out.println("EPC = " + epc);
                EPC = epc;

                if (!EPC.isEmpty()) {
                    progressDialog.dismiss();
                    if (menu.equals("detailManifest")) {
                        checkUhfTag(EPC);
                    } else {
                        changeActivity();
                    }
                }
            }
        });
    }

    private void changeActivity() {
        switch (menu) {
            case "home":
                if (target.equals("checkVehicle")) {
                    Intent checkVehicle = new Intent(getApplicationContext(), CheckVehicleActivity.class);
                    checkVehicle.putExtra("EPC", EPC);
                    startActivity(checkVehicle);
                    finish();
                } else if (target.equals("changeManifest")) {
                    Intent changeManifest = new Intent(getApplicationContext(), ChangeManifestActivity.class);
                    changeManifest.putExtra("EPC", EPC);
                    startActivity(changeManifest);
                    finish();
                } else if (target.equals("changeUhfTag")) {
                    checkUhfTag(EPC);

                    Intent changeUhfTag = new Intent(getApplicationContext(), ChangeUHFTagActivity.class);
                    changeUhfTag.putExtra("EPC", EPC);
                    startActivity(changeUhfTag);
                    finish();
                }
                break;
            case "detailManifest":
                Intent searchVehicle = new Intent(getApplicationContext(), SearchVehicleActivity.class);
                searchVehicle.putExtra("providedServiceId", providedServiceId);
                searchVehicle.putExtra("menu", "scanVehicle");
                searchVehicle.putExtra("EPC", EPC);
                startActivity(searchVehicle);
                finish();
                break;
            default:
                break;
        }
    }

    public void checkUhfTag(String uhfTag) {
        VehicleService vehicleService = ServiceGenerator.createBaseService(this, VehicleService.class);
        Call<UhfTag> uhfTagCall = vehicleService.apiCheckUhfTag(uhfTag);
        uhfTagCall.enqueue(new Callback<UhfTag>() {
            @Override
            public void onResponse(Call<UhfTag> call, Response<UhfTag> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus().equals("inactive")) {
                        changeActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_TAG_ALREADY_USED, Toast.LENGTH_SHORT).show();
                        backToHome();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_TAG_ALREADY_USED, Toast.LENGTH_SHORT).show();
                    backToHome();
                }
            }

            @Override
            public void onFailure(Call<UhfTag> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.ERROR_MESSAGE_UHF_TAG_ALREADY_USED, Toast.LENGTH_SHORT).show();
                backToHome();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToHome();
    }

    private void backToHome() {
        Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);

        Gson gson = new Gson();
        String dataUser = SignInActivity.sharedPreferences.getString("user", "");
        User user = gson.fromJson(dataUser, User.class);

        homeIntent.putExtra("user", user);
        startActivity(homeIntent);
        finish();
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
