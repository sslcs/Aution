package com.happy.auction.entity.event;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.entity.response.BaseEvent;

/**
 * @author LiuCongshan
 * @date 17-11-3
 */

public class WinEvent extends BaseEvent {
    /**
     * 单位：分
     */
    public int price;

    /**
     * 名称
     */
    @SerializedName("goods_name")
    public String title;
}
