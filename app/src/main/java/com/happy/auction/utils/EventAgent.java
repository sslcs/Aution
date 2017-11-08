package com.happy.auction.utils;

import android.support.annotation.StringRes;

import com.happy.auction.AppInstance;
import com.umeng.analytics.MobclickAgent;

/**
 * 事件代理
 *
 * @author LiuCongshan
 * @date 17-11-7
 */

public class EventAgent {
    public static void onEvent(@StringRes int resId) {
        onEvent(AppInstance.getInstance().getString(resId));
    }

    public static void onEvent(String event) {
        MobclickAgent.onEvent(AppInstance.getInstance(), event);
    }
}
