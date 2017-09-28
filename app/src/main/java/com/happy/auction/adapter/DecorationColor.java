package com.happy.auction.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;

/**
 * Created by LiuCongshan on 17-9-28.
 * 颜色分割线
 */

public class DecorationColor extends DecorationSpace {
    private Paint dividerPaint;

    public DecorationColor() {
        super();
        init();
    }

    public DecorationColor(int dp) {
        super(dp);
        init();
    }

    private void init() {
        dividerPaint = new Paint();
        dividerPaint.setColor(AppInstance.getInstance().getResColor(R.color.divider));
    }

    public void setColor(int color) {
        dividerPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        if (childCount == 0) return;

        if (childCount == 1) {
            int type = parent.getAdapter().getItemViewType(0);
            if (type != 0) return;
        }

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        View view = parent.getChildAt(0);
        float top = view.getBottom();
        float bottom = view.getBottom() + dividerHeight;
        c.drawRect(left, top, right, bottom, dividerPaint);

        if (isEnableHeader()) {
            c.drawRect(left, 0, right, view.getTop(), dividerPaint);
        }

        if (!isEnableFooter()) {
            childCount = childCount - 1;
        }
        for (int i = 1; i < childCount; i++) {
            view = parent.getChildAt(i);
            top = view.getBottom();
            bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }
}
