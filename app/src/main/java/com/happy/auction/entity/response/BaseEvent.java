package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-8-21.
 * 接收数据类型
 */

public class BaseEvent {
    public final static String EVENT_UPDATE = "update_countdown";
    public final static String EVENT_FINISH = "bid_complete";
    public final static String EVENT_DETAIL = "good_detail";

    /**
     * 事件代码
     */
    public String event;
}
