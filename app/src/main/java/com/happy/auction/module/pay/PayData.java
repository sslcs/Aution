package com.happy.auction.module.pay;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.happy.auction.AppInstance;
import com.happy.auction.R;

/**
 * Created by LiuCongshan on 17-10-12.
 */

public class PayData {
    public int count;

    public PayData(int count) {
        this.count = count;
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

    public int getIntBalance() {
        return AppInstance.getInstance().getBalance();
    }

    public String getPay() {
        return "￥" + (count - getIntBalance());
    }
}
