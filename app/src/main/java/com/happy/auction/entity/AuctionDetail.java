package com.happy.auction.entity;

import com.happy.auction.entity.response.BaseEvent;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情
 */

public class AuctionDetail extends BaseEvent {
    /**
     * "original_price": "6699.00",
     * "expire": 1503303896249,
     * "img": ["https://img14.360buyimg.com/n0/jfs/t4276/257/2416766721/125228/ba72a107/58d1d078N20e18b62.jpg",
     * "https://img14.360buyimg.com/n0/jfs/t4594/177/571351438/47428/6706aecc/58d1d012N38872961.jpg",
     * "https://img14.360buyimg.com/n0/jfs/t4402/145/562043557/97164/a3ba70c5/58d1d011N2d519cb0.jpg",
     * "https://img14.360buyimg.com/n0/jfs/t4330/60/2426524648/52871/f2746e57/58d1d011N47817be3.jpg"],
     * "good_name": "iPhone7 Plus 128G",
     * "out_bidders": [],
     * "current_price": "100.00",
     * "current_bidder": "",
     * "bid_times": 0,
     * "event": "good_detail"
     */

    /**
     * 商品名称
     */
    public String good_name;
    public String goods_image;
    public String current_bidder;
    public ArrayList<String> img;
    public long expire;

    public ArrayList<JoinRecord> out_bidders;

    /**
     * 市场价
     */
    public String original_price;
    /**
     * 当前竞拍价
     */
    public float current_price;
    /**
     * 0:正在进行中; 1:已结束
     */
    public int status;
    /**
     *
     */
    public int bid_times;
}
