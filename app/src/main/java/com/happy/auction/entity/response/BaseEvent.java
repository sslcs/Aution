package com.happy.auction.entity.response;

/**
 * Created by LiuCongshan on 17-8-21.
 * 接收数据类型
 */

public class BaseEvent {
    public final static String EVENT_UPDATE = "update_countdown";
    public final static String EVENT_FINISH = "bid_complete";
    public final static String EVENT_DETAIL = "good_detail";

    public final static String RESPONSE_LOGIN = "user_login";
    public final static String RESPONSE_USER_INFO = "user_info_get";

    /**
     * 事件代码
     */
    public String event;
}
