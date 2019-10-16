package com.stn.carterminal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stn.carterminal.Constant.Constant;
import com.stn.carterminal.Helper.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity {

    private static final String MESSAGE_HOST_NOT_BLANK = "Field 'Host' must not be empty.";
    private static final String MESSAGE_INVALID_HOST = "Invalid Host/IP Address.";
    private static final String KEY_SHARED_PREFERENCES_HOST = "host";

    private String host;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        setHost();
        setOnClickSubmitButton();
    }

    private void setHost() {
        this.host = SharedPreferencesHelper.getData(sharedPreferences, KEY_SHARED_PREFERENCES_HOST);
        ((EditText) findViewById(R.id.host)).setText(this.host);
    }

    private void setOnClickSubmitButton() {
        Button save = findViewById(R.id.saveBtnSetting);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String inputHost = ((EditText) findViewById(R.id.host)).getText().toString();
                if (inputHost.isEmpty()) {
                    Toast.makeText(getApplicationContext(), MESSAGE_HOST_NOT_BLANK, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!validateHost(inputHost)) {
                    Toast.makeText(getApplicationContext(), MESSAGE_INVALID_HOST, Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferencesHelper.storeData(sharedPreferences, KEY_SHARED_PREFERENCES_HOST, inputHost);
                Toast.makeText(getApplicationContext(), Constant.SUCCESS_SAVE_DATA_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateHost(String inputHost) {
        return inputHost.equals(Constant.LOCALHOST_VALUE);
    }
}
