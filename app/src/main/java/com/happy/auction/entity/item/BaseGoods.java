package com.happy.auction.entity.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.happy.auction.BR;

import java.io.Serializable;

/**
 * 商品item基类
 *
 * @author LiuCongshan
 * @date 17-9-12
 */

public class BaseGoods extends BaseObservable implements Serializable {
    /**
     * 晒单编号id
     */
    public int sid;
    /**
     * 商品的编号id
     */
    public int gid;
    /**
     * 期号
     */
    public int period;
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
    @Bindable
    public int current_price;

    public void setCurrentPrice(int price) {
        current_price = price;
        notifyPropertyChanged(BR.current_price);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemGoods) {
            ItemGoods item = (ItemGoods) obj;
            return item.sid == this.sid;
        }
        return super.equals(obj);
    }

    public BaseGoods getBaseGoods() {
        BaseGoods goods = new BaseGoods();
        goods.sid = sid;
        goods.gid = gid;
        goods.period = period;
        goods.title = title;
        goods.icon = icon;
        goods.market_price = market_price;
        goods.current_price = current_price;
        return goods;
    }
}
