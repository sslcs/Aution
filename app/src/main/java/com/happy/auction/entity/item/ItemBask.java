package com.happy.auction.entity.item;

import android.databinding.ObservableBoolean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 晒单分享item<br/>
 * Created by LiuCongshan on 17-9-12.
 *
 * @author LiuCongshan
 */
public class ItemBask {
    /**
     * 晒单id
     */
    public int bid;
    /**
     * 商品动态编号id
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
    public ArrayList<String> s_img;
    /**
     * 晒单图片
     */
    public ArrayList<String> img;

    public final ObservableBoolean expand = new ObservableBoolean(false);
    public final ObservableBoolean show = new ObservableBoolean(false);
}
