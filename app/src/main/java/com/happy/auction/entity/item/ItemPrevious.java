package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;

/**
 *
 * 往期成交item
 * @author LiuCongshan
 * @date 17-9-26
 */

public class ItemPrevious {
    /**
     * 晒单编号id
     */
    public int sid;
    /**
     * 用户编号id
     */
    public int uid;
    /**
     * 用户名
     */
    public String username;
    /**
     * 用户头像
     */
    @SerializedName("headimg")
    public String avatar;
    /**
     * 出价，单位：分
     */
    public int bid_price;
    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;
    /**
     * 中奖用户节省百分比 * 100
     */
    public int save;
}
