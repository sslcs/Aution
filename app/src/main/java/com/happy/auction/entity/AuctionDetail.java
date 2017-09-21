package com.happy.auction.entity;

import android.databinding.Bindable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.happy.auction.AppInstance;
import com.happy.auction.BR;
import com.happy.auction.R;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemGoods;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情
 */

public class AuctionDetail extends ItemGoods {
    /**
     * "start_time": 1504506671111, // 开始时间，单位：毫秒
     */

    /**
     * 最新出价人用户名
     */
    public String username;
    /**
     *
     */
    public int bid_times;
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
        super(goods);
    }

    @Bindable
    public int getBid_times() {
        return bid_times;
    }

    public void setBidTimes(int times) {
        this.bid_times = times;
        notifyPropertyChanged(BR.bid_times);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    public SpannableString getBidTimes(int times) {
        String formatted = AppInstance.getInstance().getString(R.string.my_bid_times, times, times);
        SpannableString ss = new SpannableString(formatted);
        String strTimes = String.valueOf(times);
        int start = formatted.indexOf(strTimes);
        int color = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), start, start + strTimes.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        start = formatted.lastIndexOf(strTimes);
        ss.setSpan(new ForegroundColorSpan(0xff179fe6), start, start + strTimes.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
