package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-8-21.
 * 接收数据类型
 */

public class BaseEvent {
    public final static String EVENT_BID = "goods_new_bid";
    public final static String EVENT_AUCTION_END = "goods_bid_end";

    /**
     * 事件代码
     */
    public String event;
}
