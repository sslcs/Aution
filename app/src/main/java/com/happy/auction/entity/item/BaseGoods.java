package com.happy.auction.entity.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.happy.auction.BR;

import java.io.Serializable;

/**
 * Created by LiuCongshan on 17-9-12.
 * 商品item基类
 */

public class BaseGoods extends BaseObservable implements Serializable {
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
    public int current_price;

    public BaseGoods() {}

    public BaseGoods(@NonNull BaseGoods goods) {
        this.sid = goods.sid;
        this.gid = goods.gid;
        this.period = goods.period;
        this.title = goods.title;
        this.description = goods.description;
        this.icon = goods.icon;
        this.market_price = goods.market_price;
        setCurrentPrice(goods.current_price);
    }

    @Bindable
    public int getCurrent_price() {
        return current_price;
    }

    public void setCurrentPrice(int price) {
        current_price = price;
        notifyPropertyChanged(BR.current_price);
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
