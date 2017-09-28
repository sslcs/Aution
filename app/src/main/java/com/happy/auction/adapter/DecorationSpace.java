package com.happy.auction.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.AppInstance;

/**
 * Created by LiuCongshan on 17-9-14.
 * 空白分割线
 */

public class DecorationSpace extends DecorationBase {
    int dividerHeight;

    public DecorationSpace() {
        this(1);
    }

    public DecorationSpace(int dp) {
        dividerHeight = (int) AppInstance.getInstance().getResources().getDisplayMetrics().density * dp;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        final int position = parent.getChildAdapterPosition(view);
        if (position != parent.getChildCount() - 1 || isEnableFooter()) {
            outRect.bottom = dividerHeight;
        }

        if (position == 0 && isEnableHeader()) {
            outRect.top = dividerHeight;
        }
    }
}
