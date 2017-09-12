package com.happy.auction.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.happy.auction.AppInstance;

/**
 * Created by LiuCongshan on 17-9-8.
 * 显示Toast
 */

public class ToastUtil {
    public static void show(String s) {
        Toast.makeText(AppInstance.getInstance(), s, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int resId) {
        Toast.makeText(AppInstance.getInstance(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String s) {
        Toast.makeText(AppInstance.getInstance(), s, Toast.LENGTH_LONG).show();
    }

    public static void showLong(@StringRes int resId) {
        Toast.makeText(AppInstance.getInstance(), resId, Toast.LENGTH_LONG).show();
    }
}
