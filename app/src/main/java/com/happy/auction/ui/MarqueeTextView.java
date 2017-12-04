package com.happy.auction.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.utils.DebugLog;
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

public class MarqueeTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
    final private ArrayList<ItemLatest> data = new ArrayList<>();
    private Disposable disposable;
    private int position = 0;
    private Context mContext;

    public MarqueeTextView(Context context) {
        super(context);
        init(context);
    }

    public MarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setFactory(this);
        int height = AppInstance.getInstance().dp2px(30);
        Animation in = new TranslateAnimation(0, 0, height, 0);
        in.setDuration(300);
        in.setInterpolator(new AccelerateInterpolator());
        Animation out = new TranslateAnimation(0, 0, 0, -height);
        out.setDuration(300);
        out.setInterpolator(new AccelerateInterpolator());
        setInAnimation(in);
        setOutAnimation(out);
    }

    public void addData(ArrayList<ItemLatest> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        for (ItemLatest item : data) {
            addData(item);
        }
        show();
        start();
    }

    public synchronized void addData(ItemLatest item) {
        if (item == null) {
            return;
        }
        if (data.size() >= 10) {
            data.remove(9);
        }
        data.add(0, item);
    }

    private void start() {
        if (disposable != null) {
            return;
        }
        disposable = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        show();
                    }
                });
    }

    public void stop(){
        if (disposable != null) {
            disposable.dispose();
        }
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

    @Override
    public View makeView() {
        TextView t = new android.support.v7.widget.AppCompatTextView(mContext){
            @Override
            public boolean isFocused() {
                return true;
            }
        };
        t.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setSingleLine();
        t.setCompoundDrawables(mContext.getResources().getDrawable(R.drawable.ic_announce),null,null,null);
        t.setCompoundDrawablePadding(10);
        t.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        t.setPadding(AppInstance.getInstance().dp2px(16), 0, 0, 0);
        t.setTextColor(AppInstance.getInstance().getResColor(R.color.text_title));
        t.setTextSize(14);
        return t;
    }
}
