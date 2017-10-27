package com.happy.auction.entity.response;

import android.content.res.Resources;
import android.databinding.Bindable;

import com.happy.auction.AppInstance;
import com.happy.auction.BR;
import com.happy.auction.R;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情
 */

public class AuctionDetail extends ItemGoods {
    /**
     * 最新出价人用户名
     */
    public String username;
    /**
     * 最新出价人头像
     */
    @Bindable
    public String avatar;
    /**
     * 开奖时间，单位：毫秒
     */
    public long prize_time;
    /**
     * 出价记录
     */
    public ArrayList<BidRecord> bid_records;
    /**
     * 起拍价，单位：分
     */
    public int bid_start_price;
    /**
     * 加价幅度，单位：分
     */
    public int bid_increment;
    /**
     * 手续费，虚拟币的数量
     */
    public int bid_fee;
    /**
     * 竞拍倒计时，单位：毫秒
     */
    public int bid_countdown;
    /**
     * 退币比例
     */
    public String bid_refund;

    public AuctionDetail(ItemGoods goods) {
        this((BaseGoods) goods);
        if (goods == null) {
            return;
        }
        this.bid_expire_time = goods.bid_expire_time;
        this.status = goods.status;
    }

    public AuctionDetail(BaseGoods goods) {
        if (goods == null) {
            return;
        }

        this.sid = goods.sid;
        this.gid = goods.gid;
        this.period = goods.period;
        this.title = goods.title;
        this.description = goods.description;
        this.icon = goods.icon;
        this.market_price = goods.market_price;
        this.current_price = goods.current_price;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);
    }

    public String getTitlePrice(int status, int price) {
        StringBuilder sb = new StringBuilder();
        Resources res = AppInstance.getInstance().getResources();
        sb.append(res.getString(status == 0 ? R.string.price_deal : R.string.price_current));
        sb.append("：").append(StringUtil.formatSignMoney(price));
        return sb.toString();
    }

    public ItemGoods getItemGoods() {
        ItemGoods item = new ItemGoods();
        item.sid = sid;
        item.title = title;
        item.period = period;
        return item;
    }
}
