package com.happy.auction.adapter;

import android.view.View;

/**
 * 列表的条目点击事件接口<br/>
 * Created by LiuCongshan on 17-10-23.
 *
 * @author LiuCongshan
 */

public interface OnItemClickListener<T> {
    /**
     * adapter条目点击事件
     *
     * @param view     点击项view
     * @param item     点击项data
     * @param position 位置
     */
    void onItemClick(View view, T item, int position);
}
