package com.happy.auction.adapter;

import android.view.View;

/**
 * Created by LiuCongshan on 17-9-27.
 */

public interface OnItemClickListener {
    /**
     * adapter条目点击事件
     * @param view 点击项view
     * @param position 位置
     */
    void onItemClick(View view, int position);
}
