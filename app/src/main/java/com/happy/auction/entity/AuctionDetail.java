package com.happy.auction.entity;

import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemGoods;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情
 */

public class AuctionDetail extends ItemGoods {
    /**
     * "start_time": 1504506671111, // 开始时间，单位：毫秒
     */

    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;

    /**
     * 最新出价人用户名
     */
    public String username;

    public ArrayList<BidRecord> bid_records;

    /**
     *
     */
    public int bid_times;

    /**
     * 起拍价，单位：分
     */
    public int bid_start_price;
    /**
     * 加价幅度，单位：分
     */
    public int bid_increment;
    /**
     * 手续费，虚拟币的数量
     */
    public int bid_fee;
    /**
     * 竞拍倒计时，单位：毫秒
     */
    public int bid_countdown;
    /**
     * 退币比例
     */
    public String bid_refund;
}
