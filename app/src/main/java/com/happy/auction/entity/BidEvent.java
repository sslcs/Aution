package com.happy.auction.entity;

import com.happy.auction.entity.response.BaseEvent;

/**
 * Created by LiuCongshan on 17-8-18.
 * 出价事件
 */

public class BidEvent extends BaseEvent {
    public int sid;
    public int current_price;
    public long bid_expire_time;

    public String uid;
    public String username;
    public String headimg;
    public String ip_address;
}
