package com.stn.carterminal.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stn.carterminal.R;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.constant.sharedPreference.SharedPreferenceDataKey;
import com.stn.carterminal.helper.SharedPreferencesHelper;

public class SettingActivity extends AppCompatActivity {

    private static final String MESSAGE_HOST_NOT_BLANK = "Field 'Host' must not be empty.";
    private static final String MESSAGE_INVALID_HOST = "Host/IP Address must be ended with '/'";
    private static final String MESSAGE_PROGRESS_DIALOG = "Saving ...";

    private String host;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setHost();
        setOnClickSubmitButton();

        progressDialog = new ProgressDialog(SettingActivity.this);
    }

    private void setHost() {
        this.host = SharedPreferencesHelper.getData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_HOST);
        ((EditText) findViewById(R.id.host)).setText(this.host);
    }

    private void setOnClickSubmitButton() {
        Button save = findViewById(R.id.saveBtnSetting);
        save.setOnClickListener((View view) -> {
            progressDialog.setMessage(MESSAGE_PROGRESS_DIALOG);
            progressDialog.show();
            String inputHost = ((EditText) findViewById(R.id.host)).getText().toString();
            if (validate(inputHost)) {
                SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_HOST, inputHost);
                Toast.makeText(getApplicationContext(), Constant.SUCCESS_SAVE_DATA_MESSAGE, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Intent signIn = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signIn);
                finish();
            }
        });
    }

    private boolean validate(String inputHost) {
        if (inputHost.isEmpty()) {
            Toast.makeText(getApplicationContext(), MESSAGE_HOST_NOT_BLANK, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }
        char lastChar = inputHost.charAt(inputHost.length() - 1);
        if (lastChar != '/') {
            Toast.makeText(getApplicationContext(), MESSAGE_INVALID_HOST, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }
        return true;
    }
}
