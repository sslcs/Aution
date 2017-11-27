package com.happy.auction.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.happy.auction.AppInstance;
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
    private Rect mBounds;
    private int mColorLine, mColorPoint, mColorText;
    private int mRadius;

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
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(AppInstance.getInstance().dp2px(12));
        mPaint.setStrokeWidth(2);
        mBounds = new Rect();
        mColorLine = getResources().getColor(R.color.line_chart);
        mColorText = getResources().getColor(R.color.text_title);
        mColorPoint = getResources().getColor(R.color.main_red);
        mRadius = AppInstance.getInstance().dp2px(4);
    }

    public void setData(ItemTrend middle, int max, ItemTrend left, ItemTrend right) {
        mMiddle = middle;
        mLeft = left;
        mRight = right;
        mMax = max;
    }

    public void setColor(int color) {
        mColorPoint = color;
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
        int axisBottom = getHeight() - AppInstance.getInstance().dp2px(60);
        int height = axisBottom - AppInstance.getInstance().dp2px(10);
        int width = getWidth();
        int halfWidth = width / 2;

        // draw axis X
        mPaint.setColor(mColorLine);
        canvas.drawLine(halfWidth, axisBottom - 10, halfWidth, axisBottom, mPaint);
        canvas.drawLine(0, axisBottom, width, axisBottom, mPaint);

        // draw line, 如果成交价大于市场价，取市场价
        int price = mMiddle.deal_price > mMax ? mMax : mMiddle.deal_price;
        int middleY = axisBottom - height * price / mMax;
        if (mLeft != null) {
            // 绘制左边的连线
            price = mLeft.deal_price > mMax ? mMax : mLeft.deal_price;
            int leftY = axisBottom - height * price / mMax;
            canvas.drawLine(halfWidth, middleY, -halfWidth, leftY, mPaint);
        }
        if (mRight != null) {
            // 绘制右边的连线
            price = mRight.deal_price > mMax ? mMax : mRight.deal_price;
            int rightY = axisBottom - height * price / mMax;
            canvas.drawLine(halfWidth, middleY, halfWidth * 3, rightY, mPaint);
        }

        // draw period
        mPaint.setColor(mColorText);
        mPaint.getTextBounds(mMiddle.period, 0, mMiddle.period.length(), mBounds);
        int periodY = axisBottom + 10 + mBounds.height();
        int periodX = halfWidth - mBounds.width() / 4;
        canvas.save();
        canvas.rotate(-45, halfWidth + mBounds.width() / 4, axisBottom);
        canvas.drawText(mMiddle.period, periodX, periodY, mPaint);
        canvas.restore();

        // draw point and price
        mPaint.setColor(mColorPoint);
        String text = mMiddle.getPrice();
        int textY = middleY - mRadius * 2;
        // 在圆点下面绘制数值
        if (mMiddle.deal_price >= mMax) {
            mPaint.getTextBounds(text, 0, text.length(), mBounds);
            textY = middleY + mRadius * 2 + mBounds.height();
        }
        canvas.drawText(text, halfWidth, textY, mPaint);
        canvas.drawCircle(halfWidth, middleY, mRadius, mPaint);
    }
}
