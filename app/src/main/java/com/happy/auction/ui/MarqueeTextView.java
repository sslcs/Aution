package com.happy.auction.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.happy.auction.R;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.utils.StringUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 揭晓广播
 *
 * @author LiuCongshan
 * @date 17-9-13
 */

public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    final private ArrayList<ItemLatest> data = new ArrayList<>();
    private Disposable disposable;
    private int position = 0;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public void addData(ArrayList<ItemLatest> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        for (ItemLatest item : data) {
            addData(item);
        }
    }

    public synchronized void addData(ItemLatest item) {
        if (item == null) {
            return;
        }
        if (data.size() >= 10) {
            data.remove(9);
        }
        data.add(0, item);
        start();
    }

    private void start() {
        if (disposable != null) {
            return;
        }
        disposable = Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        show();
                    }
                });
    }

    private void show() {
        synchronized (data) {
            if (data.isEmpty()) {
                return;
            }
            if (position < 0 || position >= data.size()) {
                position = 0;
            }

            ItemLatest item = data.get(position++);
            setText(getText(item));
            setTag(item);
        }
    }

    private SpannableString getText(ItemLatest item) {
        String price = StringUtil.formatSignMoney(item.current_price);
        String text = getContext().getString(R.string.format_announce, item.username, price, item.title);
        SpannableString ss = new SpannableString(text);
        int color = getContext().getResources().getColor(R.color.main_red);
        int start = item.username.length() + 1;
        ss.setSpan(new ForegroundColorSpan(color), start, start + price.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        start += price.length() + 2;
        ss.setSpan(new ForegroundColorSpan(0xff1b86ff), start, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
