package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-9-12.
 * 订单item
 */

public class ItemOrder extends BaseGoods {
    /**
     * 订单号
     */
    public String ordernum;
    /**
     * 我已出价多少次
     */
    public String buy;
    /**
     * 开奖时间，单位：毫秒
     */
    public String prize_time;
    /**
     * 记录状态:<br/>
     * 1：正在拍，2：已拍中，3：待付款，4: 已确认领奖方式，5:已晒单，6:已结束
     */
    public String records_status;
}
