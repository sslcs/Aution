package com.happy.auction.main;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.CountdownEvent;
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.response.BaseEvent;
import com.happy.auction.entity.response.BaseResponse;
import com.happy.auction.net.ResponseHandler;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LiuCongshan on 17-9-8.
 * 处理接收到的消息
 */

public class MessagePresenter {
    private final List<ResponseHandler> handlers = Collections.synchronizedList(new ArrayList<ResponseHandler>());

    public void addHandler(ResponseHandler handler) {
        handlers.add(handler);
    }

    public void handle(String message) {
        DebugLog.e("onMessage : " + message);
        Type type = new TypeToken<BaseResponse>() {}.getType();
        BaseResponse base = GsonSingleton.get().fromJson(message, type);

        if (BaseEvent.EVENT_UPDATE.equals(base.event)) {
            type = new TypeToken<DataResponse<CountdownEvent>>() {}.getType();
            DataResponse<CountdownEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.EVENT_DETAIL.equals(base.event)) {
            type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
            DataResponse<AuctionDetail> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else {
            handleResponse(base, message);
        }
    }

    private void handleResponse(BaseResponse base, String response) {
        synchronized (handlers) {
            if(handlers.isEmpty()) return;
            if (base.isSuccess()) {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    ResponseHandler handler = handlers.get(i);
                    if (base.event.equals(handler.action)) {
                        handler.onSuccess(response, base.msg);
                        handlers.remove(i);
                    }
                }
            } else {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    ResponseHandler handler = handlers.get(i);
                    if (base.event.equals(handler.action)) {
                        handler.onError(base.code, base.msg);
                        handlers.remove(i);
                    }
                }
            }
        }
    }

    private <T> void onEvent(T data) {
        RxBus.getDefault().post(data);
    }
}
