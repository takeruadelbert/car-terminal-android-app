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
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.request.Login;
import com.stn.carterminal.network.service.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {

    private static final String MESSAGE_HOST_NOT_BLANK = "Field 'Host' must not be empty.";
    private static final String MESSAGE_INVALID_HOST = "Host/IP Address must be ended with '/'";
    private static final String MESSAGE_INVALID_PROTOCOL_HOST = "Invalid Host/IP Address Protocol";
    private static final String MESSAGE_PROGRESS_DIALOG = "Loading ...";
    private static final String MESSAGE_TEST_CONNECTION_FAILED = "Fail to Connect.";
    private static final String MESSAGE_TEST_CONNECTION_SUCCESS = "Connection Established.";

    private String host;
    private ProgressDialog progressDialog;

    private static final Integer TEST_CONNECTION_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setHost();
        setOnClickSubmitButton();
        setOnClickTestConnectionButton();

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

    private void setOnClickTestConnectionButton() {
        Button testConnection = findViewById(R.id.testConnection);
        testConnection.setOnClickListener((View view) -> {
            progressDialog.setMessage(MESSAGE_PROGRESS_DIALOG);
            progressDialog.show();
            String inputHost = ((EditText) findViewById(R.id.host)).getText().toString();
            if (validate(inputHost)) {
                testConnection(inputHost);
            }
        });
    }

    private boolean validate(String inputHost) {
        if (inputHost.isEmpty()) {
            Toast.makeText(getApplicationContext(), MESSAGE_HOST_NOT_BLANK, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return false;
        }
        boolean test1 = inputHost.startsWith(Constant.HTTP_PROTOCOL);
        boolean test2 = inputHost.startsWith(Constant.HTTP_PROTOCOL_SECURE);
        if (!(inputHost.startsWith(Constant.HTTP_PROTOCOL) || inputHost.startsWith(Constant.HTTP_PROTOCOL_SECURE))) {
            Toast.makeText(getApplicationContext(), MESSAGE_INVALID_PROTOCOL_HOST, Toast.LENGTH_SHORT).show();
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

    private void testConnection(String host) {
        String dataHost = String.format("%s%s", SharedPreferencesHelper.getData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_HOST), "login");
        if (!dataHost.equals(host)) {
            SharedPreferencesHelper.storeData(SignInActivity.sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_HOST, host);
        }
        UserService userService = ServiceGenerator.createBaseService(this, UserService.class);
        Call<ResponseBody> call = userService.apiSignIn(new Login(Constant.EMPTY_STRING, Constant.EMPTY_STRING));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), MESSAGE_TEST_CONNECTION_SUCCESS, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), MESSAGE_TEST_CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), MESSAGE_TEST_CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
