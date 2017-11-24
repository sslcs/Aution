package com.happy.auction.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.entity.item.ItemTrend;

/**
 * @author LiuCongshan
 * @date 17-11-24
 */

public class ChartItemView extends View {
    private ItemTrend mLeft, mRight, mMiddle;
    private int mMax;
    private Paint mPaint;
    private int mColorLine,mColorPoint;

    public ChartItemView(Context context) {
        super(context);
        init();
    }

    public ChartItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorLine = getResources().getColor(R.color.text_light);
        mColorPoint = getResources().getColor(R.color.main_red);
    }

    public void setData(ItemTrend middle, int max, ItemTrend left, ItemTrend right) {
        mMiddle = middle;
        mLeft = left;
        mRight = right;
        mMax = max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight() - 100;
        int width = getWidth();

        mPaint.setColor(mColorLine);
        canvas.drawLine(width / 2, height - 2, width / 2, height, mPaint);
        canvas.drawLine(0, height, width, height, mPaint);

        int middleY = height * mMiddle.deal_price / mMax;
        if (mLeft != null) {
            int leftY = height * mLeft.deal_price / mMax;
            canvas.drawLine(width / 2, middleY, -width / 2, leftY, mPaint);
        }
        if (mRight != null) {
            int rightY = height * mRight.deal_price / mMax;
            canvas.drawLine(width / 2, middleY, width * 3 / 2, rightY, mPaint);
        }

        mPaint.setColor(mColorPoint);
        canvas.drawCircle(width / 2, middleY, 10, mPaint);
    }
}
