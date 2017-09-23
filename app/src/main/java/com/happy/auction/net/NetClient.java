package com.happy.auction.net;

import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.utils.RxBus;

/**
 * Created by LiuCongshan on 17-9-22.
 * 网络请求client
 */

public class NetClient {
    public static void query(BaseRequest request, NetCallback callback) {
        callback.action = request.action;
        RxBus.getDefault().post(new RequestEvent(request, callback));
    }


}
