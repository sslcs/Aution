package com.happy.auction.entity.response;

/**
 * 查询支付状态返回数据
 *
 * @author LiuCongshan
 * @date 17-11-7
 */

public class ChargeStatusResponse extends BaseResponse {
    /**
     * 订单支付状态， 1:未付款， 2:确定中， 3:已支付， -1：失败
     */
    public int status;
}
