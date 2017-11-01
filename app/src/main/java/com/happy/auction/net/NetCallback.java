package com.happy.auction.net;

import com.happy.auction.utils.ToastUtil;

/**
 * 网络请求结果回调
 *
 * @author LiuCongshan
 * @date 17-9-15
 */

public abstract class NetCallback {
    public String action;
    public String tag;

    public void onError(int code, String message) {
        ToastUtil.show(message);
    }

    /**
     * 请求成功回调
     *
     * @param response 响应结果
     * @param message  响应消息
     */
    public abstract void onSuccess(String response, String message);
}
