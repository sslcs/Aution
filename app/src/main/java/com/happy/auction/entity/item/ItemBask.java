package com.happy.auction.entity.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LiuCongshan on 17-9-12.
 * 晒单分享item
 */

public class ItemBask {
    /**
     * 晒单编号id
     */
    public int sid;
    /**
     * 中奖者 uid
     */
    public int uid;
    /**
     * 中奖者用户名
     */
    public String username;
    /**
     * 中奖者头像
     */
    @SerializedName("headimg")
    public String avatar;
    /**
     * 分享时间，单位：毫秒
     */
    public long bask_time;

    public String goods_title;
    /**
     * 评论内容
     */
    public String content;

    /**
     * 商品图片
     */
    public String icon;
    /**
     * 晒单缩略图
     */
    public String s_img;
    /**
     * 晒单图片
     */
    public String img;
}
