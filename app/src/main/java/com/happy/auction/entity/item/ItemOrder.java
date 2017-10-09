package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-9-12.
 * 订单item
 */

public class ItemOrder extends BaseGoods {
    /**
     * 订单 id
     */
    public int pid;
    /**
     * 订单号
     */
    @SerializedName("ordernum")
    public String order_num;
    /**
     * 我已出价多少次
     */
    public int buy;
    /**
     * 退币数量
     */
    public int refund_coin;
    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;
    /**
     * 记录状态:<br/>
     * 1：正在拍，2：已拍中，3：待付款，4: 已确认领奖方式，5:已晒单，6:已结束
     */
    public int status;
}
