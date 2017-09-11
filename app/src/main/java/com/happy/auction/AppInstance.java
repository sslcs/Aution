package com.happy.auction;

import android.app.Application;
import android.text.TextUtils;

import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.utils.Preference;

/**
 * Created by LiuCongshan on 17-8-16.
 * AppInstance
 */

public class AppInstance extends Application {
    private static AppInstance instance;
    public String uid;
    public String token;
    private UserInfo user;

    public static AppInstance getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        uid = Preference.getUid();
        token = Preference.getToken();
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(uid);
    }

    public void setLoginResponse(LoginResponse response) {
        uid = response.uid;
        token = response.token;
        Preference.setUid(uid);
        Preference.setToken(token);
    }
}
