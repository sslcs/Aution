package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-10-11.
 * 支付方式 item
 */

public class ItemPayType {
    //支付宝
    public static final int ALIPAY = 1;

    //银联支付
    public static final int UNIONPAY = 9;
    /**
     * 威富通
     */
    public static final int WFT_PAY = 204;

    public static final int TH_PAY = 209;

    public static final int WEB_ALIPAY = 208;

    public static final int SDK_ALIPAY = 214;

    public static final int SDK_WX = 217;

    /**
     * 支付方式id
     */
    public int pay_type;
    /**
     * 0未定义 1:sdk 2:内嵌浏览器(显示网页) 3:内嵌浏览器(不显示网页) 4:打开中转页跳转 pay_url
     */
    public int open_type;
    /**
     * 支付方式标题
     */
    public String name;
    /**
     * 描述
     */
    public String description;
    /**
     * 图标链接
     */
    public String icon;
    /**
     * 支付链接
     */
    public String pay_url;
    /**
     * 跳转链接
     */
    public String transfer_url;
    /**
     * 第三方支付id
     */
    public String app_id;
}
