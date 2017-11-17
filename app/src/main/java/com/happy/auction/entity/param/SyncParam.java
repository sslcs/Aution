package com.happy.auction.entity.param;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 同步客户端参数
 *
 * @author LiuCongshan
 * @date 17-9-11
 */

public class SyncParam extends BaseParam {
    public String sys_ver = Build.VERSION.RELEASE;
    public String net_type;
    public String net_info;
    public String model = Build.MODEL;
    public String imei;
    public String imsi;
    public String android_id;
    public String mac;
    public String channel = AppInstance.getInstance().getChannel();
    private String app_ver = BuildConfig.VERSION_NAME;
    private String app_ver_num = String.valueOf(BuildConfig.VERSION_CODE);
    private String token = AppInstance.getInstance().token;
    private String uid = AppInstance.getInstance().uid;
    private String platform = "android";

    public SyncParam() {
        action = "sync_client";
        if (!AppInstance.getInstance().isLogin()) {
            uid = null;
            token = null;
        }

        TelephonyManager tm = (TelephonyManager) AppInstance.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            getImei(tm);
            getImsi(tm);
        }
        getMac();
        getAndroidId();
    }

    private void getImei(TelephonyManager tm) {
        try {
            imei = tm.getDeviceId();
            if (imei != null) {
                imei = imei.trim();
                imei = imei.replace(" ", "");
                imei = imei.replace("-", "");
                imei = imei.replace("\n", "");

                String meidStr = "MEID:";
                int index = imei.indexOf(meidStr);
                if (index != -1) {
                    imei = imei.substring(index + meidStr.length());
                }

                imei = imei.toLowerCase();
                if (imei.length() < 10) {
                    imei = null;
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private void getImsi(TelephonyManager tm) {
        try {
            imsi = tm.getSubscriberId();
            if (imsi != null) {
                imsi = imsi.trim();
                imsi = imsi.toLowerCase();
                if (imsi.length() < 10) {
                    imsi = null;
                }
            }
        } catch (Throwable ignored) {
        }
    }

    private void getMac() {
        try {
            FileReader reader = null;
            try {
                reader = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                reader = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(reader, 1024);
                mac = in.readLine();
                if (mac != null) {
                    mac = mac.trim();
                    mac = mac.replace(":", "");
                    mac = mac.toLowerCase();
                }
            } catch (IOException ignored) {
            } finally {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void getAndroidId() {
        try {
            android_id = android.provider.Settings.Secure.getString(
                    AppInstance.getInstance().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            if (android_id != null) {
                android_id = android_id.trim();
                android_id = android_id.toLowerCase();
            }
        } catch (Throwable ignored) {
        }
    }
}
