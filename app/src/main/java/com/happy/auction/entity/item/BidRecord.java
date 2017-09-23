package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.entity.BidEvent;

/**
 * Created by LiuCongshan on 17-9-19.
 * 竞拍记录
 */

public class BidRecord {
    /**
     * 用户id
     */
    public String uid;
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
     * ip 地址
     */
    public String ip_address;
    /**
     * 出价，单位：分
     */
    public int bid_price;
    /**
     * 时间
     */
    public long create_time;

    public BidRecord(BidEvent event) {
        uid = event.uid;
        username = event.username;
        avatar = event.headimg;
        ip_address = event.ip_address;
        bid_price = event.current_price;
    }
}
