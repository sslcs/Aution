package com.happy.auction.module.main;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.event.LogoutEvent;
import com.happy.auction.entity.event.WinEvent;
import com.happy.auction.entity.response.BaseEvent;
import com.happy.auction.entity.response.BaseResponse;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 处理接收到的消息
 *
 * @author LiuCongshan
 * @date 17-9-8
 */

public class MessageHandler {
    private final List<NetCallback> callbacks = Collections.synchronizedList(new ArrayList<NetCallback>());

    public void addHandler(NetCallback handler) {
        if (handler == null) {
            return;
        }
        callbacks.add(0, handler);
    }

    public void handle(String message) {
        Type type = new TypeToken<BaseResponse>() {}.getType();
        BaseResponse base = GsonSingleton.get().fromJson(message, type);
        if (base == null) {
            return;
        }

        if (BaseEvent.EVENT_BID.equals(base.event)) {
            type = new TypeToken<DataResponse<BidEvent>>() {}.getType();
            DataResponse<BidEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.EVENT_AUCTION_END.equals(base.event)) {
            DebugLog.e("onEnd : " + message);
            type = new TypeToken<DataResponse<AuctionEndEvent>>() {}.getType();
            DataResponse<AuctionEndEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.EVENT_OFFLINE.equals(base.event)) {
            DebugLog.e("offline : " + message);
            ToastUtil.show(base.msg);
            cancelAll();
            AppInstance.getInstance().logout();
            onEvent(new LogoutEvent());
        } else if (BaseEvent.EVENT_WIN.equals(base.event)) {
            DebugLog.e("onWin : " + message);
            type = new TypeToken<DataResponse<WinEvent>>() {}.getType();
            DataResponse<WinEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else {
            DebugLog.e("onMessage : " + message);
            handleResponse(base, message);
        }
    }

    private synchronized void handleResponse(BaseResponse base, String response) {
        if (callbacks.isEmpty()) {
            return;
        }
        if (base.isSuccess()) {
            for (int i = callbacks.size() - 1; i >= 0; i--) {
                NetCallback callback = callbacks.get(i);
                if (callback.tag.equals(base.tag)) {
                    callback.onSuccess(response, base.msg);
                    if (i < callbacks.size()) {
                        callbacks.remove(i);
                    }
                    break;
                }
            }
        } else {
            for (int i = callbacks.size() - 1; i >= 0; i--) {
                NetCallback callback = callbacks.get(i);
                if (callback.tag.equals(base.tag)) {
                    callback.onError(base.code, base.msg);
                    if (i < callbacks.size()) {
                        callbacks.remove(i);
                    }
                    break;
                }
            }
        }
    }

    private <T> void onEvent(T data) {
        RxBus.getDefault().post(data);
    }

    public void clear() {
        callbacks.clear();
    }

    public void cancelAll() {
        if (callbacks.isEmpty()) {
            return;
        }

        for (NetCallback callback : callbacks) {
            callback.onError(-1, "");
        }
        clear();
    }
}
