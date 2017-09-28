package com.happy.auction.entity.param;

import android.os.SystemClock;

import com.happy.auction.utils.GsonSingleton;

/**
 * Created by LiuCongshan on 17-8-21.
 * 请求参数基类
 */

public class BaseRequest<T extends BaseParam> {
    public String action;
    /**
     * 请求标识
     */
    public String tag;

    private T data;

    public BaseRequest(T data) {
        this.data = data;
        action = data.action;
        tag = String.valueOf(SystemClock.elapsedRealtime());
    }

    @Override
    public String toString() {
        return GsonSingleton.get().toJson(this);
    }
}
