package com.happy.auction.entity.item;

import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.happy.auction.AppInstance;
import com.happy.auction.BR;
import com.happy.auction.R;
import com.happy.auction.utils.StringUtil;

/**
 * 商品item
 *
 * @author LiuCongshan
 * @date 17-9-12
 */
public class ItemGoods extends BaseGoods {
    /**
     * 竞拍过期时间戳，单位：毫秒
     */
    @SerializedName("bid_expire_time")
    public long countdown;
    /**
     * 状态:<br/>
     * 0:已结束，1：未开始，2：正在进行, 3:停拍中
     */
    public int status;

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemGoods) {
            ItemGoods item = (ItemGoods) obj;
            if (gid != 0 && item.gid != 0) {
                return item.gid == this.gid;
            }
        }
        return super.equals(obj);
    }

    public ItemGoods getItemGoods() {
        ItemGoods goods = new ItemGoods();
        goods.sid = sid;
        goods.gid = gid;
        goods.period = period;
        goods.title = title;
        goods.icon = icon;
        goods.market_price = market_price;
        goods.current_price = current_price;
        goods.status = status;
        return goods;
    }

    public String getPrice(int status, int price) {
        String strPrice = StringUtil.formatMoney(price);
        if (status == 0) {
            return AppInstance.getInstance().getString(R.string.format_deal_price, strPrice);
        }
        return AppInstance.getInstance().getString(R.string.format_current_price, strPrice);
    }
}
