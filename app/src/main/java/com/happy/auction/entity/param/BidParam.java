package com.happy.auction.entity.param;

import java.io.Serializable;

/**
 * Created by LiuCongshan on 17-8-21.
 * 参与竞拍参数
 */

public class BidParam extends BaseParam implements Serializable{
    /**
     * 商品动态sid
     */
    public int sid;

    /**
     * 出价次数
     */
    public int buy = 1;

    /**
     * 支付方式，由支付列表列表返回，可选
     */
    public int pay_type;

    /**
     * 消费的拍币数量，可选
     */
    public int take_coin;

    public BidParam() {
        action = "purchase_order_pay_confirm";
    }
}
