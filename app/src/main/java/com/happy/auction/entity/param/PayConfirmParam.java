package com.happy.auction.entity.param;

/**
 * Created by LiuCongshan on 17-10-11.
 * 支付方式确认参数
 */

public class PayConfirmParam extends BaseParam {
    /**
     * 商品动态 id
     */
    public int sid;
    /**
     * 支付方式，由支付方式列表返回
     */
    public int pay_type;

    public PayConfirmParam() {
        action = "purchase_good_pay_confirm";
    }
}
