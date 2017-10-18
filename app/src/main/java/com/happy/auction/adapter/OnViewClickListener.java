package com.happy.auction.adapter;

import android.view.View;

/**
 * Created by LiuCongshan on 17-10-18.<br/>
 * adapter子View点击事件回调接口
 */

public interface OnViewClickListener<T> {
    /**
     * adapter子View点击事件
     *
     * @param view     点击view
     * @param item     点击项data
     * @param position 点击项position
     */
    void onViewClick(View view, T item, int position);
}
