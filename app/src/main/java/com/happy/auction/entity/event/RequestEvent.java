package com.happy.auction.entity.event;

import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.net.NetCallback;

/**
 * Created by LiuCongshan on 17-9-8.
 * 请求事件
 */

public class RequestEvent {
    public String message;

    public NetCallback callback;

    public <T extends BaseParam> RequestEvent(BaseRequest<T> request, NetCallback callback) {
        this.message = request.toString();
        this.callback = callback;
    }
}
