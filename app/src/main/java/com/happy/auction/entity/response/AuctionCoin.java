package com.happy.auction.entity.response;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.happy.auction.AppInstance;
import com.happy.auction.BR;
import com.happy.auction.R;

/**
 * 商品详情出价币数
 *
 * @author LiuCongshan
 * @date 17-9-25
 */

public class AuctionCoin extends BaseObservable {
    /**
     * 已出价的总拍币数
     */
    @Bindable
    public int bid_coin;
    /**
     * 已出价的总赠币数
     */
    @Bindable
    public int bid_gift_coin;
    /**
     * 当前出价总币数
     */
    @Bindable
    public int current_bid_coins;
    /**
     * 当前已出价总币数
     */
    @Bindable
    public int current_bidden_coins;

    public void setBidCoin(int coin) {
        bid_coin = coin;
        notifyPropertyChanged(BR.bid_coin);
    }

    public void setBidGiftCoin(int coin) {
        bid_gift_coin = coin;
        notifyPropertyChanged(BR.bid_gift_coin);
    }

    public void setCurrentCoin(int coin) {
        current_bid_coins = coin;
        notifyPropertyChanged(BR.current_bid_coins);
    }

    public void setCurrentProgress(int coin) {
        current_bidden_coins = coin;
        notifyPropertyChanged(BR.current_bidden_coins);
    }

    public SpannableString getBidTimes(int coins, int freeCoins) {
        String formatted = AppInstance.getInstance().getString(R.string.my_bid_times, coins, freeCoins);
        SpannableString ss = new SpannableString(formatted);
        String strCoin = String.valueOf(coins);
        int start = formatted.indexOf(strCoin);
        int color = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), start, start + strCoin.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        String strFree = String.valueOf(freeCoins);
        start = formatted.lastIndexOf(strFree);
        ss.setSpan(new ForegroundColorSpan(0xff179fe6), start, start + strFree.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public SpannableString getProgress(int progress, int total, int fee) {
        if (fee > 1) {
            progress /= fee;
            total /= fee;
        }
        String formatted = AppInstance.getInstance().getString(R.string.auto_bid_progress, progress, total);
        SpannableString ss = new SpannableString(formatted);
        String strTimes = String.valueOf(progress);
        int start = formatted.indexOf(strTimes);
        int color = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), start, start + strTimes.length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
