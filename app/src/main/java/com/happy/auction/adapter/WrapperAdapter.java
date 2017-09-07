package com.happy.auction.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by LiuCongshan on 17-9-4.
 */

abstract class WrapperAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    static final int ITEM_TYPE_REFRESH = Integer.MAX_VALUE - 3;
    static final int ITEM_TYPE_HEADER_BASE = 0XFA000;
    static final int ITEM_TYPE_FOOTER_BASE = 0XFF000;

    RecyclerView.Adapter<RecyclerView.ViewHolder> mInnerAdapter;

    WrapperAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }
}
