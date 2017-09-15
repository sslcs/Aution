package com.happy.auction.net;

/**
 * Created by LiuCongshan on 17-9-15.
 * 返回数据处理基类
 */

public abstract class ResponseHandler {
    public String action;

    public void onError(int code, String message) {

    }

    public abstract void onSuccess(String response, String message);
}
