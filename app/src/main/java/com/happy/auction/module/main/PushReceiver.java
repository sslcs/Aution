package com.happy.auction.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.happy.auction.utils.DebugLog;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义JPush接收器
 *
 * @author LiuCongshan
 */
public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            DebugLog.e("JPush PushReceiver : " + JPushInterface.getRegistrationID(context));
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        // ACTION_NOTIFICATION_RECEIVED 收到通知
        // ACTION_MESSAGE_RECEIVED 收到自定义消息 bundle.getString(JPushInterface.EXTRA_MESSAGE))
        // ACTION_NOTIFICATION_OPENED 打开通知 JPushInterface.EXTRA_NOTIFICATION_TITLE
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            context.startActivity(MainActivity.newInstance());
        }
    }
}
