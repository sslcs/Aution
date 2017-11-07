package com.happy.auction.entity.event;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.entity.response.BaseEvent;

/**
 * 出价事件
 *
 * @author LiuCongshan
 * @date 17-8-18
 */

public class BidEvent extends BaseEvent {
    public int sid;
    public int current_price;
    @SerializedName("bid_expire_time")
    public int countdown;

    public String uid;
    public String username;
    @SerializedName("headimg")
    public String avatar;
    @SerializedName("ip_address")
    public String address;
}
