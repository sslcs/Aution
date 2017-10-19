package com.happy.auction.entity.param;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;

/**
 * Created by LiuCongshan on 17-9-11.
 * 同步客户端参数
 */

public class SyncParam extends BaseParam {
    public String sys_ver;
    public String virtual_ip;
    public String jpush_regid;
    public String net_type;
    public String net_info;
    public String model;
    public String imei;
    public String imsi;
    public String android_id;
    public String mac;
    public String channel = "0";
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
    }
}
