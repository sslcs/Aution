package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-10-11.
 * 支付确认返回数据
 */

public class PayConfirmResponse {
    /**
     * 流水号
     */
    public String tradenum;
    /**
     * 支付方式
     */
    public int pay_type;
    /**
     * 支付链接
     */
    public String params;
    /**
     * 支付金额，单位：分
     */
    public int amount;
}
