package com.happy.auction.entity.param;

/**
 * 支付状态查询参数
 *
 * @author LiuCongshan
 * @date 17-11-7
 */

public class ChargeStatusParam extends BaseParam {
    /**
     * 流水号
     */
    public String exorderno;

    public ChargeStatusParam() {
        action = "charge_order_status_query";
    }
}
