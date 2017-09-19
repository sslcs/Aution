package com.happy.auction.entity.item;

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
    public String headmimg;
    /**
     * ip 地址
     */
    public String ip_addredd;
    /**
     * 出价，单位：分
     */
    public int bid_price;
    /**
     * 时间
     */
    public long create_time;
}
