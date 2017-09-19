package com.happy.auction.entity.param;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-9-11.
 * 订单记录请求参数
 */

public class OrderParam extends BaseParam {
    /**
     * 记录类型，0：全部，1：正在拍，2：已拍中，3：待付款
     */
    public int records_status;
    public int start;
    public int limit = DEFAULT_LIMIT;

    public OrderParam() {
        action = "purchase_my_records";
    }
}
