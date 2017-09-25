package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-8-21.
 * 获取竞拍出价币数参数
 */

public class AuctionCancelParam extends BaseParam {
    public int sid;

    public AuctionCancelParam() {
        action = "purchase_order_pay_cancel";
    }
}
