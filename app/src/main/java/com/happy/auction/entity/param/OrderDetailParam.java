package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-9-11.
 * 获取订单详情参数
 */

public class OrderDetailParam extends BaseParam {
    public int pid;

    public OrderDetailParam() {
        action = "purchase_my_record_detail";
    }
}
