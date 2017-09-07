package com.happy.auction.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuCongshan on 17-9-4.<br/>
 * Display footer view by {@link #addFootView(View)}
 */

public class FooterWrapper<T> extends WrapperAdapter<T> {
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    public FooterWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(adapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFootViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(parent.getContext(), mFootViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooterViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return getFootersCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                int viewType = getItemViewType(position);
                if (mFootViews.get(viewType) != null) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null)
                    return oldLookup.getSpanSize(position);
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder);
        }
    }

    private boolean isFooterViewPos(int position) {
        return position >= getRealItemCount();
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + ITEM_TYPE_FOOTER_BASE, view);
    }

    public int getFootersCount() {
        return mFootViews.size();
    }
}
