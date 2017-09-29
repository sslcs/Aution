package com.happy.auction.module.detail;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


/**
 * Created by LiuCongshan on 17-9-29.
 * PlusMinusNumberView
 */

public class PlusMinusNumberView extends AppCompatEditText {
    private int number = 0;

    public PlusMinusNumberView(Context context) {
        super(context);
    }

    public PlusMinusNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlusMinusNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
