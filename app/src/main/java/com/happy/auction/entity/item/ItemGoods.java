package com.happy.auction.entity.item;

import android.databinding.Bindable;

import com.happy.auction.BR;

/**
 * Created by LiuCongshan on 17-9-12.
 * 商品item
 */

public class ItemGoods extends BaseGoods {
    /**
     * 竞拍过期时间戳，单位：毫秒
     */
    public long bid_expire_time;
    /**
     * 状态:<br/>
     * 0:已结束，1：未开始，2：正在进行
     */
    public int status;

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = 0;
        notifyPropertyChanged(BR.status);
    }
}
