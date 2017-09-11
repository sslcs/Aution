package com.happy.auction.utils;

import android.preference.PreferenceManager;

import com.happy.auction.AppInstance;

/**
 * Created by LiuCongshan on 17-9-11.
 * SharedPreferences存储数据
 */

public class Preference {
    private static final String KEY_UID = "uid";
    private static final String KEY_TOKEN = "token";

    private static void saveString(String key, String value) {
        PreferenceManager
                .getDefaultSharedPreferences(AppInstance.getInstance())
                .edit()
                .putString(key, value)
                .apply();
    }

    private static String getString(String key, String defaultValue) {
        return PreferenceManager
                .getDefaultSharedPreferences(AppInstance.getInstance())
                .getString(key, defaultValue);
    }

    public static String getUid() {
        return getString(KEY_UID, null);
    }

    public static void setUid(String uid) {
        saveString(KEY_UID, uid);
    }

    public static String getToken() {
        return getString(KEY_TOKEN, null);
    }

    public static void setToken(String token) {
        saveString(KEY_TOKEN, token);
    }

    public static void logout() {
        setUid("");
        setToken("");
    }
}
