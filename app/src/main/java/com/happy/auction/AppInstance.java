package com.happy.auction;

import android.app.Application;
import android.databinding.ObservableInt;
import android.text.TextUtils;

import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.utils.PreferenceUtil;
import com.squareup.leakcanary.LeakCanary;

/**
 * Application instance
 *
 * @author LiuCongshan
 * @date 17-8-16
 */
public class AppInstance extends Application {
    private static AppInstance mInstance;
    public final ObservableInt mMessageCount = new ObservableInt();
    public String uid;
    public String token;
    private UserInfo user;

    public static AppInstance getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        uid = PreferenceUtil.getUid();
        token = PreferenceUtil.getToken();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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

    public void setUsername(String username) {
        if (user == null) {
            return;
        }
        user.username = username;
    }

    public void setAvatar(String avatar) {
        if (user == null) {
            return;
        }
        user.avatar = avatar;
    }

    public int getBalance() {
        if (user == null) {
            return -1;
        }
        return user.free_coin + user.auction_coin;
    }

    public void setBalance(UserBalance data) {
        if (user == null) {
            user = new UserInfo();
        }
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

    public String getChannel() {
        return "";
    }

    public int getResColor(int resId) {
        return getResources().getColor(resId);
    }

    public void minusMessageCount() {
        mMessageCount.set(mMessageCount.get() - 1);
    }
}
