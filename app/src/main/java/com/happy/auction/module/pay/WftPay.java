package com.happy.auction.module.pay;

import android.app.Activity;
import android.content.Context;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;

import java.lang.ref.WeakReference;

/**
 * 威富通
 * Created by zwy on 16-9-9.
 */
public class WftPay {
    private WeakReference<Context> context;
    private static WftPay wftPay;

    public WftPay(Context context) {
        this.context = new WeakReference<>(context);
    }

    public static WftPay getInstance(Context context) {
        if (wftPay == null) {
            wftPay = new WftPay(context);
        }
        return wftPay;
    }

    public void pay(String tokeId) {
        RequestMsg msg = new RequestMsg();
        msg.setTokenId(tokeId);
        msg.setTradeType(MainApplication.PAY_NEW_ZFB_WAP);

        if (context.get() == null) return;
        PayPlugin.unifiedH5Pay((Activity) context.get(), msg);
    }
}
