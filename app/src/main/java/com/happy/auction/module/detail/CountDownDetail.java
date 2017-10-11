package com.happy.auction.module.detail;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happy.auction.R;

import java.util.Locale;

/**
 * Created by LiuCongshan on 17-9-20.
 * 自定义倒计时TextView
 */

public class CountDownDetail extends android.support.v7.widget.AppCompatTextView {
    private final static String FORMATTER = "%02d:%02d:%02d";
    private ObjectAnimator animator;
    private CountDownTimer timer;
    private boolean repeat = false;
    private TextView tvSync;

    private int widthNormal;
    private boolean reset = false;
    private boolean disableAnimator = false;

    public CountDownDetail(Context context) {
        super(context);
        init();
    }

    public CountDownDetail(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountDownDetail(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        widthNormal = (int) getPaint().measureText("00:00:00");
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
        setTime(getResources().getString(R.string.auction_finish));
    }

    public void setSyncView(TextView tvSync) {
        this.tvSync = tvSync;
    }

    public void setExpireTime(long expireTime) {
        cancel();
        long left = expireTime - System.currentTimeMillis();
        timer = new CountDownTimer(left, 10) {
            @Override
            public void onTick(long l) {
                long mod1000 = l % 1000;
                long divide1000 = l / 1000;
                long minutes = divide1000 / 60;
                long seconds = divide1000 % 60;
                long millis = mod1000 / 10;
                setTime(String.format(Locale.CHINA, FORMATTER, minutes, seconds, millis));

                if (disableAnimator) return;
                if (l < 3300 && l > 1200 && mod1000 < 300 && mod1000 > 200) {
                    startAnimator();
                }
            }

            @Override
            public void onFinish() {
                setTime("00:00:00");
                if (repeat) setExpireTime(System.currentTimeMillis() + 10000);
            }
        }.start();
    }

    private void setTime(String time) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (reset) {
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            setTextColor(getResources().getColor(R.color.text_light));
            reset = false;
        } else if (params.width != widthNormal) {
            params.width = widthNormal;
            setTextColor(getResources().getColor(R.color.main_red));
        }

        setText(time);
        if (tvSync != null) {
            tvSync.setText(time);
        }
    }

    private void startAnimator() {
        if (animator == null) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1, 2, 1);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1, 2, 1);
            animator = ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY);
        }

        if (!animator.isRunning()) {
            animator.start();
        }
    }

    public void setDisableAnimator() {
        disableAnimator = true;
    }
}
