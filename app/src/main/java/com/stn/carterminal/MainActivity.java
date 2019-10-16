package com.stn.carterminal;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

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

        relativeLayout1 = findViewById(R.id.relay1);
        relativeLayout2 = findViewById(R.id.relay2);

        handler.postDelayed(runnable, SPLASH_SCREEN_DELAY_TIME);
    }
}
