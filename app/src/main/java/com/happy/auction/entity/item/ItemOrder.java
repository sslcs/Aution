package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-9-12.
 * 订单item
 */

public class ItemOrder {
    /**
     * 晒单编号id
     */
    public String sid;
    /**
     * 商品的编号id
     */
    public String gid;
    /**
     * 期号
     */
    public String period;
    public String title;
    /**
     * 订单号
     */
    public String ordernum;
    /**
     * 商品图片
     */
    public String icon;
    /**
     * 市场价格，单位：分
     */
    public String market_price;
    /**
     * 当前价格，单位：分
     */
    public String current_price;
    /**
     * 我已出价多少次
     */
    public String buy;
    /**
     * 开奖时间，单位：毫秒
     */
    public String prize_time;
    /**
     * 记录状态:<br/>
     * 1：正在拍，2：已拍中，3：待付款，4: 已确认领奖方式，5:已晒单，6:已结束
     */
    public String records_status;
}
