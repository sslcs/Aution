package com.happy.auction.utils;

import android.preference.PreferenceManager;

import com.happy.auction.AppInstance;

/**
 * SharedPreferences存储数据
 *
 * @author LiuCongshan
 * @date 17-9-11
 */

public class PreferenceUtil {
    private static final String KEY_UID = "uid";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TIP_CAPTCHA = "tip_captcha";
    private static final String KEY_MODIFY_PASSWORD = "modify_password";

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

    private static void saveBoolean(String key, boolean value) {
        PreferenceManager
                .getDefaultSharedPreferences(AppInstance.getInstance())
                .edit()
                .putBoolean(key, value)
                .apply();
    }

    private static boolean getBoolean(String key, boolean defaultValue) {
        return PreferenceManager
                .getDefaultSharedPreferences(AppInstance.getInstance())
                .getBoolean(key, defaultValue);
    }

    public static void closeTipCaptcha() {
        saveBoolean(KEY_TIP_CAPTCHA, false);
    }

    public static boolean showTipCaptcha() {
        return getBoolean(KEY_TIP_CAPTCHA, true);
    }

    public static boolean showSetPassword() {
        if (getBoolean(KEY_MODIFY_PASSWORD, true)) {
            saveBoolean(KEY_MODIFY_PASSWORD, false);
            return true;
        }
        return false;
    }
}
