package com.happy.auction.entity;

/**
 * Created by LiuCongshan on 17-8-18.
 * 参与记录
 */

public class CountdownEvent extends BaseEvent{
    public String current_bidder;
    public String current_price;
    public long expire;
    public int status;
}
