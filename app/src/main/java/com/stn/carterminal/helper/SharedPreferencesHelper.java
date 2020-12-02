package com.stn.carterminal.helper;

import android.content.SharedPreferences;

import com.stn.carterminal.constant.Constant;
import com.stn.carterminal.constant.sharedPreference.SharedPreferenceDataKey;

public class SharedPreferencesHelper {
    private static final String DEFAULT_VALUE_HOST = "localhost:4003";

    public static void storeData(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getData(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SharedPreferenceDataKey.KEY_SHARED_PREFERENCES_HOST)) {
            return sharedPreferences.getString(key, String.format("%s%s", Constant.HTTP_PROTOCOL, DEFAULT_VALUE_HOST));
        }
        return sharedPreferences.getString(key, Constant.EMPTY_STRING);
    }
}
