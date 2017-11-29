package com.happy.auction.module.pay;

import android.databinding.BaseObservable;
import android.databinding.ObservableInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.happy.auction.AppInstance;
import com.happy.auction.R;

/**
 * @author LiuCongshan
 * @date 17-10-12
 */

public class PayData extends BaseObservable {
    public int count;

    public final ObservableInt minus = new ObservableInt();

    public PayData(int count) {
        this.count = count;
        minus.set(AppInstance.getInstance().getBalance());
    }

    public SpannableString getStringBalance() {
        int coinAuction = AppInstance.getInstance().getUser().auction_coin;
        int coinFree = AppInstance.getInstance().getUser().free_coin;
        String balance = AppInstance.getInstance().getString(R.string.pay_balance, coinAuction, coinFree);
        SpannableString ss = new SpannableString(balance);
        int color = AppInstance.getInstance().getResColor(R.color.text_normal);
        ss.setSpan(new ForegroundColorSpan(color), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return ss;
    }

    public String getPay(int minus) {
        return "￥" + (count - minus);
    }
}
