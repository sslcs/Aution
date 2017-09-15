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
    public final static String RESPONSE_RECORD = "purchase_my_records";
    public final static String RESPONSE_BANNER = "goods_banners";
    public final static String RESPONSE_MENU = "config_index_items";
    public final static String RESPONSE_ANNOUNCE = "prize_show";
    public final static String RESPONSE_GOODS = "goods_bid";

    /**
     * 事件代码
     */
    public String event;
}
