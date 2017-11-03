package com.happy.auction.entity.response;

/**
 * 接收数据类型
 *
 * @author LiuCongshan
 * @date 17-8-21
 */

public class BaseEvent {
    public final static String EVENT_BID = "goods_new_bid";
    public final static String EVENT_AUCTION_END = "goods_bid_end";
    public final static String EVENT_OFFLINE = "user_other_login";
    public final static String EVENT_WIN = "push_win_prize";

    /**
     * 事件代码
     */
    public String event;
}
