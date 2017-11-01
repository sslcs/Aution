package com.happy.auction.net;

import com.happy.auction.entity.event.RequestEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.utils.RxBus;

/**
 * 网络请求client
 *
 * @author LiuCongshan
 * @date 17-9-22
 */
public class NetClient {
    public static void query(BaseRequest request, NetCallback callback) {
        if (callback != null) {
            callback.action = request.action;
            callback.tag = request.tag;
        }
        RxBus.getDefault().post(new RequestEvent(request, callback));
    }


}
