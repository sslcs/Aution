package com.happy.auction.module.home;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.happy.auction.adapter.DecorationSpace;

/**
 * Created by LiuCongshan on 17-9-14.
 * 首页Grid分割线
 */

public class HomeDecoration extends DecorationSpace {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (position % 2 == 0) {
            outRect.right = 1;
        } else {
            outRect.left = 1;
        }
    }
}
