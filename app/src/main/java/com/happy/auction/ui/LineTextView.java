package com.happy.auction.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * @author LiuCongshan
 * @date 17-11-17
 */

public class LineTextView extends AppCompatTextView {
    private final static int MAX_LINES = 2;
    private int mRealCount;
    private boolean bExpand = false;
    private ShowExpandListener mListener;

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (TextUtils.isEmpty(getText())) {
            return;
        }

        if (mRealCount == 0) {
            mRealCount = -1;
            return;
        }

        if (mRealCount != -1) {
            return;
        }

        mRealCount = getLineCount();
        if (mListener != null) {
            mListener.showExpand(mRealCount > MAX_LINES);
        }
        setMaxLines(bExpand ? Integer.MAX_VALUE : MAX_LINES);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void initStatus(boolean expand) {
        setText("");
        bExpand = expand;
        mRealCount = 0;
        setMaxLines(Integer.MAX_VALUE);
    }

    public void expand(boolean expand) {
        bExpand = expand;
        setMaxLines(expand ? Integer.MAX_VALUE : MAX_LINES);
    }

    public void setShowExpandListener(ShowExpandListener listener) {
        this.mListener = listener;
    }

    public interface ShowExpandListener {
        /**
         * 显示或隐藏按钮
         *
         * @param show 是否显示
         */
        void showExpand(boolean show);
    }
}
