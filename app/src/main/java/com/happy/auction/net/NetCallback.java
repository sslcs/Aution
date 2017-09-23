package com.happy.auction.net;

/**
 * Created by LiuCongshan on 17-9-15.
 * 网络请求结果回调
 */

public abstract class NetCallback {
    public String action;

    public void onError(int code, String message) { }

    public abstract void onSuccess(String response, String message);
}
