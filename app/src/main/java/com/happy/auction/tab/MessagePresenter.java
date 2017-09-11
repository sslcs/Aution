package com.happy.auction.tab;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.response.BaseEvent;
import com.happy.auction.entity.response.BaseResponse;
import com.happy.auction.entity.CountdownEvent;
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

/**
 * Created by LiuCongshan on 17-9-8.
 * 处理接收到的消息
 */

public class MessagePresenter {
    public void handle(String message) {
        DebugLog.e("onMessage : " + message);
        Type type = new TypeToken<BaseResponse>() {}.getType();
        BaseResponse base = GsonSingleton.get().fromJson(message, type);
        if (!TextUtils.isEmpty(base.msg)) ToastUtil.show(base.msg);
        if(!base.isSuccess()) return;
        if (BaseEvent.EVENT_UPDATE.equals(base.event)) {
            type = new TypeToken<DataResponse<CountdownEvent>>() {}.getType();
            DataResponse<CountdownEvent> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.EVENT_DETAIL.equals(base.event)) {
            type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
            DataResponse<AuctionDetail> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        } else if (BaseEvent.RESPONSE_LOGIN.equals(base.event)) {
            type = new TypeToken<DataResponse<LoginResponse>>() {}.getType();
            DataResponse<LoginResponse> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        }else if (BaseEvent.RESPONSE_USER_INFO.equals(base.event)) {
            type = new TypeToken<DataResponse<UserInfo>>() {}.getType();
            DataResponse<UserInfo> response = GsonSingleton.get().fromJson(message, type);
            onEvent(response.data);
        }
    }

    private <T> void onEvent(T data) {
        RxBus.getDefault().post(data);
    }
}
