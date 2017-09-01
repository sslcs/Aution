package com.happy.auction;

import android.app.Application;

/**
 * Created by LiuCongshan on 17-8-16.
 * AppInstance
 */

public class AppInstance extends Application {
    private static AppInstance instance;

    public static AppInstance getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
