package com.happy.auction.entity.param;

import com.happy.auction.utils.GsonSingleton;

/**
 * Created by LiuCongshan on 17-8-21.
 * 请求参数基类
 */

public class BaseRequest<T extends BaseParam> {
    public String action;

    private T data;

    public BaseRequest(T data)
    {
        this.data = data;
        action = data.action;
    }

    @Override
    public String toString() {
        return GsonSingleton.get().toJson(this);
    }
}
