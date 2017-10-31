package com.happy.auction.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.happy.auction.R;

/**
 * 验证码倒计时控件
 *
 * @author LiuCongshan
 * @date 17-10-31
 */

public class TimerButton extends android.support.v7.widget.AppCompatButton {
    private CountDownTimer mTimer;
    private OnFinishListener mListener;

    public TimerButton(Context context) {
        super(context);
    }

    public TimerButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void start() {
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                setText((int) (l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                mTimer = null;
                setText(R.string.get_captcha);
                if (mListener != null) {
                    mListener.onFinish();
                }
            }
        }.start();
    }

    public void cancel() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mTimer == null) {
            super.setEnabled(enabled);
        }
    }

    public void setOnFinishListener(OnFinishListener listener) {
        mListener = listener;
    }

    public interface OnFinishListener {
        /**
         * 倒计时结束事件
         */
        void onFinish();
    }
}
