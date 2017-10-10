package com.happy.auction.entity.item;

/**
 * Created by LiuCongshan on 17-10-10.
 * 首页 banner item
 */

public class ItemBanner {
    /**
     * banner类型，1：商品（gid参数有效）；2：活动（url参数有效）
     */
    public int type;
    /**
     * 晒单编号id
     */
    public int sid;
    /**
     * 晒单编号
     */
    public int gid;

    /**
     * banner 标题
     */
    public String title;
    /**
     * 直接跳转
     */
    public String url;
    /**
     * banner图片
     */
    public String img;
}
