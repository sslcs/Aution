package com.happy.auction.module.home;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.happy.auction.R;

import java.util.Locale;

/**
 * Created by LiuCongshan on 17-9-20.
 * 自定义倒计时TextView
 */

public class CountDownHome extends android.support.v7.widget.AppCompatTextView {
    private final static String FORMATTER = "%02d:%02d:%02d";
    private CountDownTimer timer;
    private boolean repeat = false;

    private boolean reset = false;

    public CountDownHome(Context context) {
        super(context);
    }

    public CountDownHome(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownHome(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void cancel() {
        if (timer != null) timer.cancel();
    }

    public void finish() {
        reset = true;
        cancel();
        setTextColor(getResources().getColor(R.color.text_light));
        setText(getResources().getString(R.string.auction_finish));
    }

    public void setExpireTime(long expireTime) {
        cancel();
        long left = expireTime - System.currentTimeMillis();
        timer = new CountDownTimer(left, 1000) {
            @Override
            public void onTick(long l) {
                long mod1000 = l % 1000;
                long divide1000 = l / 1000;
                long minutes = divide1000 / 60;
                long seconds = divide1000 % 60;
                long millis = mod1000 / 10;
                setTime(String.format(Locale.CHINA, FORMATTER, 0, minutes, seconds));
            }

            @Override
            public void onFinish() {
                setTime("00:00:00");
                if (repeat) setExpireTime(System.currentTimeMillis() + 10000);
            }
        }.start();
    }

    private void setTime(String time) {
        if (reset) {
            reset = false;
            setTextColor(getResources().getColor(R.color.main_red));
        }
        setText(time);
    }
}
