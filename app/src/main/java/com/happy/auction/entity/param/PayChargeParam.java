package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-12.
 * 充值参数
 */

public class PayChargeParam extends BaseParam {
    /**
     * 充值数量
     */
    public int coin;
    /**
     * 支付方式，由支付方式列表返回
     */
    public int pay_type;

    public PayChargeParam() {
        action = "charge_buy";
    }
}
