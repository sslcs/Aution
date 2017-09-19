package com.happy.auction.entity.item;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by LiuCongshan on 17-9-12.
 * 商品item基类
 */

public class BaseGoods implements Serializable{
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
    /**
     * 商品标题
     */
    public String title;
    /**
     * 商品描述
     */
    public String description;
    /**
     * 商品图片
     */
    public String icon;
    /**
     * 市场价格，单位：分
     */
    public int market_price;
    /**
     * 当前价格，单位：分
     */
    public int current_price;

    public String currentPrice() {
        return "￥" + String.format(Locale.CHINA, "%.2f", current_price / 100f);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemGoods) {
            ItemGoods item = (ItemGoods) obj;
            return item.gid != null && item.gid.equals(this.gid);
        }
        return super.equals(obj);
    }
}
