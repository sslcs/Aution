package com.happy.auction;

import android.app.Application;
import android.text.TextUtils;

import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.utils.PreferenceUtil;

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
        uid = PreferenceUtil.getUid();
        token = PreferenceUtil.getToken();
    }

    public int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public void setBalance(UserBalance data) {
        if (user == null) return;
        user.free_coin = data.free_coin;
        user.auction_coin = data.auction_coin;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(uid);
    }

    public void logout() {
        PreferenceUtil.logout();
        uid = "";
        token = "";
    }

    public void setLoginResponse(LoginResponse response) {
        uid = response.uid;
        token = response.token;
        PreferenceUtil.setUid(uid);
        PreferenceUtil.setToken(token);
    }

    public String getChannel(){
        return "";
    }

    public int getResColor(int resId) {
        return getResources().getColor(resId);
    }
}
