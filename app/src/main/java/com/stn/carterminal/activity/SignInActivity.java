package com.stn.carterminal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stn.carterminal.R;

public class SignInActivity extends AppCompatActivity {

    private static final Integer SPLASH_SCREEN_DELAY_TIME = 2000; // in milliseconds
    private static final String INVALID_USERNAME_MESSAGE = "Invalid Field 'Username'.";
    private static final String INVALID_PASSWORD_MESSAGE = "Invalid Field 'Password'.";

    RelativeLayout relativeLayout1, relativeLayout2;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout1.setVisibility(View.VISIBLE);
            relativeLayout2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSplashScreen();
        setSettingOnClickListener();
        setSignInOnClickListener();
    }

    private void setupSplashScreen() {
        relativeLayout1 = findViewById(R.id.relay1);
        relativeLayout2 = findViewById(R.id.relay2);
        handler.postDelayed(runnable, SPLASH_SCREEN_DELAY_TIME);
    }

    private void setSettingOnClickListener() {
        Button settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(settingIntent);
            }
        });
    }

    private void setSignInOnClickListener() {
        Button signInButton = findViewById(R.id.btnSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.txtUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
                if (validateFields(username, password)) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                }
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
}
