package com.stn.carterminal.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.stn.carterminal.R;
import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.constant.sharedPreference.SharedPreferenceDataKey;
import com.stn.carterminal.helper.SharedPreferencesHelper;
import com.stn.carterminal.network.ServiceGenerator;
import com.stn.carterminal.network.request.Login;
import com.stn.carterminal.network.response.User;
import com.stn.carterminal.network.service.UserService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private static final Integer SPLASH_SCREEN_DELAY_TIME = 2000; // in milliseconds
    private static final String INVALID_USERNAME_MESSAGE = "Invalid Field 'Username'.";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid Field 'Password'.";
    private static final String PROGRESS_DIALOG_MESSAGE = "Signing In ...";

    RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
            relativeLayout3.setVisibility(View.INVISIBLE);
        }
    };
    private UserService userService;
    public static SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(SignInActivity.this, R.style.MyDialogTheme);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userService = ServiceGenerator.createBaseService(this, UserService.class);

        setupSplashScreen();
        String username = SharedPreferencesHelper.getData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_USERNAME);
        String password = SharedPreferencesHelper.getData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_PASSWORD);
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            requestAPIGetDataUser();
            ((EditText) findViewById(R.id.txtUsername)).setText(username);
            ((EditText) findViewById(R.id.txtPassword)).setText(password);
        }
        setSettingOnClickListener();
        setSignInOnClickListener();

        progressDialog = new ProgressDialog(SignInActivity.this);
    }

    private void setupSplashScreen() {
        relativeLayout1 = findViewById(R.id.relay1);
        relativeLayout2 = findViewById(R.id.relay2);
        relativeLayout3 = findViewById(R.id.relay3);
        handler.postDelayed(runnable, SPLASH_SCREEN_DELAY_TIME);
    }

    private void setSettingOnClickListener() {
        Button settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener((View view) -> {
            Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(settingIntent);
        });
    }

    private void setSignInOnClickListener() {
        Button signInButton = findViewById(R.id.btnSignIn);
        signInButton.setOnClickListener((View view) -> {
            String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
            String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
            if (validateFields(username, password)) {
                progressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);
                progressDialog.show();
                requestAPILogin(username, password);
            }
        });
    }

    private boolean validateFields(String username, String password) {
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), INVALID_USERNAME_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), INVALID_PASSWORD_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void requestAPILogin(String username, String password) {
        Call<ResponseBody> call = userService.apiSignIn(new Login(username, password));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_USERNAME, username);
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_PASSWORD, password);
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_TOKEN_BEARER, response.headers().get("Authorization"));
                    requestAPIGetDataUser();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_LOGIN_FAILED, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_LOGIN_FAILED, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void requestAPIGetDataUser() {
        Call<User> userCall = userService.apiGetDataSession();
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user = response.body();

                    Gson gson = new Gson();
                    String dataUser = gson.toJson(user);
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_DATA_USER, dataUser);

                    progressDialog.dismiss();

                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                    homeIntent.putExtra("user", user);
                    startActivity(homeIntent);
                    finish();

                } else if (response.code() == 403) {
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_USERNAME, Constant.EMPTY_STRING);
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_PASSWORD, Constant.EMPTY_STRING);
                    SharedPreferencesHelper.storeData(sharedPreferences, SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_TOKEN_BEARER, Constant.EMPTY_STRING);
                    Toast.makeText(getApplicationContext(), Constant.API_LOGIN_FAILED_TOKEN_EXPIRED, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), Constant.API_LOGIN_FAILED_TO_RETRIEVE_DATA_SESSION, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), Constant.API_LOGIN_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forgotPasswordClick(View view) {
        builder.setMessage(R.string.forgotPasswordMessage)
                .setPositiveButton("OK", ((DialogInterface dialog, int which) -> {
                    dialog.dismiss();
                }));

        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(R.string.forgotPasswordTitle);
        alertDialog.show();
    }
}
