package com.stn.carterminal.magicrf.uhfreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.stn.carterminal.activity.ScanVehicleActivity;

public class ScreenStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            ScanVehicleActivity.openGPIO();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            ScanVehicleActivity.closeGPIO();
        }
    }
}
