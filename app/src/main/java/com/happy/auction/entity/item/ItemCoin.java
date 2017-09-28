package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-9-28.
 * 拍币或赠币的变化记录item
 */

public class ItemCoin {
    public int id;
    /**
     * 拍币或赠币的变化数量，根据查询的币的类型返回
     */
    public int amount;
    /**
     * 记录生成时间戳
     */
    public long create_time;
    /**
     * 记录类型说明
     */
    public String record_type;
}
