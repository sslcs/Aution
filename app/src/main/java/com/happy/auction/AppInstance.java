package com.happy.auction;

import android.app.Application;

import com.happy.auction.tab.me.UserDataVM;

/**
 * Created by LiuCongshan on 17-8-16.
 * AppInstance
 */

public class AppInstance extends Application {
    private static AppInstance instance;

    private UserDataVM userData;

    public static AppInstance getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public UserDataVM getUser() {
        return userData;
    }

    public void setUser(UserDataVM userData) {
        this.userData = userData;
    }
}
