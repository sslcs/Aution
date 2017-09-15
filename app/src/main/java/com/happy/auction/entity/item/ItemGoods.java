package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-9-12.
 * 商品item
 */

public class ItemGoods extends BaseGoods{
    /**
     * 竞拍过期时间戳，单位：毫秒
     */
    public long bid_expire_time;
    /**
     * 状态:<br/>
     * 0:已结束，1：未开始，2：正在进行
     */
    public int status;
}
