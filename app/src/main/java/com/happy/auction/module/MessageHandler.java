package com.happy.auction.module;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.response.BaseEvent;
import com.happy.auction.entity.response.BaseResponse;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
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

public class MessageHandler {
    private final List<NetCallback> handlers = Collections.synchronizedList(new ArrayList<NetCallback>());

    public void addHandler(NetCallback handler) {
        if (handler == null) return;
        handlers.add(0, handler);
    }

    public void handle(String message) {
        Type type = new TypeToken<BaseResponse>() {}.getType();
        BaseResponse base = GsonSingleton.get().fromJson(message, type);
        if(base==null) return;

        if (BaseEvent.EVENT_BID.equals(base.event)) {
//            DebugLog.e("onBid : " + message);
            type = new TypeToken<DataResponse<BidEvent>>() {}.getType();
            DataResponse<BidEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.EVENT_AUCTION_END.equals(base.event)) {
            DebugLog.e("onEnd : " + message);
            type = new TypeToken<DataResponse<AuctionEndEvent>>() {}.getType();
            DataResponse<AuctionEndEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else {
            DebugLog.e("onMessage : " + message);
            handleResponse(base, message);
        }
    }

    private synchronized void handleResponse(BaseResponse base, String response) {
        if (handlers.isEmpty()) return;
        if (base.isSuccess()) {
            for (int i = handlers.size() - 1; i >= 0; i--) {
                NetCallback handler = handlers.get(i);
                if (handler.tag.equals(base.tag)) {
                    handler.onSuccess(response, base.msg);
                    handlers.remove(i);
                    break;
                }
            }
        } else {
            for (int i = handlers.size() - 1; i >= 0; i--) {
                NetCallback handler = handlers.get(i);
                if (handler.tag.equals(base.tag)) {
                    handler.onError(base.code, base.msg);
                    handlers.remove(i);
                    break;
                }
            }
        }
    }

    private <T> void onEvent(T data) {
        RxBus.getDefault().post(data);
    }

    public void clear() {
        handlers.clear();
    }
}
