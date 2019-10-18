package com.stn.carterminal.helper;

import android.content.SharedPreferences;

import com.stn.carterminal.constant.Constant;

public class SharedPreferencesHelper {
    public static void storeData(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getData(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, Constant.EMPTY_STRING);
    }
}
