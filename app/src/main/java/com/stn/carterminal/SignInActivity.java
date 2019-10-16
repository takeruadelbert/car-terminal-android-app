package com.stn.carterminal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    private static final Integer SPLASH_SCREEN_DELAY_TIME = 2000; // in milliseconds
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
}
