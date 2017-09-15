package com.happy.auction.entity;

import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.net.ResponseHandler;

/**
 * Created by LiuCongshan on 17-9-8.
 * 请求事件
 */

public class RequestEvent<T extends BaseParam> {
    public String message;

    public ResponseHandler handler;

    public RequestEvent(BaseRequest<T> request, ResponseHandler handler) {
        this.message = request.toString();
        handler.action = request.action;
        this.handler = handler;
    }
}
