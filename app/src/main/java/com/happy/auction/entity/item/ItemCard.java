package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.utils.StringUtil;

import java.util.ArrayList;

/**
 * 卡密item<br/>
 * Created by LiuCongshan on 17-10-20.
 *
 * @author LiuCongshan
 */

public class ItemCard {
    public boolean isExpand;
    public boolean isSelected;
    /**
     * 商品动态id
     */
    public int sid;
    /**
     * 期号
     */
    public int period;
    /**
     * 图片
     */
    public String icon;
    /**
     * 标题
     */
    public String title;
    /**
     * 领取时间，单位：豪秒
     */
    public long create_time;
    /**
     * 卡密
     */
    @SerializedName("card_info")
    public ArrayList<ItemCardPassword> card;

    public String getDate() {
        return StringUtil.formatTimeMinute(create_time);
    }
}
