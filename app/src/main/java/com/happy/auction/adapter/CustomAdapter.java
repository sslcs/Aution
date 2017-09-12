package com.happy.auction.adapter;

import android.support.v7.widget.RecyclerView;

import com.happy.auction.R;

/**
 * Created by LiuCongshan on 17-9-12.
 * 未adapter设置默认EmptyView,LoadMoreView,RefreshView.
 */

public class CustomAdapter<T extends RecyclerView.Adapter> extends AdapterWrapper<T> {
    public CustomAdapter(T adapter) {
        super(adapter);
        setEmptyView(R.layout.empty_view);
        setLoadMoreView(R.layout.item_load_more);
    }
}
