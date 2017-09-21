package com.happy.auction.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.AppInstance;

/**
 * Created by LiuCongshan on 17-9-14.
 * 空白分割线
 */

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;
    private boolean enableHeader = false;

    public SpaceDecoration() {
        dividerHeight = (int) AppInstance.getInstance().getResources().getDisplayMetrics().density;
    }

    public SpaceDecoration(int dp) {
        dividerHeight = (int) AppInstance.getInstance().getResources().getDisplayMetrics().density * dp;
    }

    public void enableHeader() {
        enableHeader = true;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;//类似加了一个bottom padding

        if (enableHeader && parent.getChildAdapterPosition(view) == 0) {
            outRect.top = dividerHeight;
        }
    }
}
