package com.happy.auction.main.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.AppInstance;

/**
 * Created by LiuCongshan on 17-9-14.
 * 空白分割线
 */

public class Decoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;

    public Decoration() {
        dividerHeight = (int) AppInstance.getInstance().getResources().getDisplayMetrics().density;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;//类似加了一个bottom padding
        int position = parent.getChildAdapterPosition(view);
        if (position % 2 == 0) {
            outRect.right = 1;
        } else {
            outRect.left = 1;
        }
    }
}
