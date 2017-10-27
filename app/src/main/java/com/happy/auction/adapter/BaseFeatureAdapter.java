package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Display empty view while the data is empty;
 *
 * @author LiuCongshan
 * @date 17-9-4
 */

public abstract class BaseFeatureAdapter<T, B extends ViewDataBinding> extends BaseAdapter<T, B> {
    static final int ITEM_TYPE_HEADER_BASE = 0XFA000;
    static final int ITEM_TYPE_FOOTER_BASE = 0XFF000;
    private static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    private static final int ITEM_TYPE_LOADING = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 3;

    private boolean showEmpty = true;
    private boolean showLoading = true;
    private boolean showMore = true;
    private boolean isLoaded = false;
    private boolean hasMore = false;
    private LoadMoreListener mLoadMoreListener;

    public void disableEmpty() {
        showEmpty = false;
        notifyDataSetChanged();
    }

    public void disableLoading() {
        showLoading = false;
        notifyDataSetChanged();
    }

    public void disableMore() {
        showMore = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoading()) {
            return ITEM_TYPE_LOADING;
        }

        if (isShowEmpty()) {
            return ITEM_TYPE_EMPTY;
        }

        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_EMPTY) {
            return getBindingEmpty(parent, inflater);
        }

        if (viewType == ITEM_TYPE_LOADING) {
            return getBindingLoading(parent, inflater);
        }

        if (viewType == ITEM_TYPE_LOAD_MORE) {
            return getBindingMore(parent, inflater);
        }

        return new CustomViewHolder<>(getBinding(parent, inflater));
    }

    /**
     * 获取空数据ViewHolder
     *
     * @param parent   父布局
     * @param inflater 布局填充类
     * @return 空数据ViewHolder
     */
    public abstract CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater);

    /**
     * 获取加载更多ViewHolder
     *
     * @param parent   父布局
     * @param inflater 布局填充类
     * @return 加载更多ViewHolder
     */
    public abstract CustomViewHolder getBindingMore(ViewGroup parent, LayoutInflater inflater);

    /**
     * 获取加载中ViewHolder
     *
     * @param parent   父布局
     * @param inflater 布局填充类
     * @return 加载中ViewHolder
     */
    public abstract CustomViewHolder getBindingLoading(ViewGroup parent, LayoutInflater inflater);

    @Override
    public void onBindViewHolder(CustomViewHolder<B> holder, int position) {
        if (isShowEmpty() || isShowLoading()) {
            return;
        }

        if (isShowLoadMore(position)) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.loadMore();
            }
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return computeSpanSize(gridLayoutManager, spanSizeLookup, position);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    private int computeSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
        layoutManager.setAutoMeasureEnabled(false);
        if (isShowLoading() || isShowEmpty() || isShowLoadMore(position)) {
            return layoutManager.getSpanCount();
        }

        if (oldLookup != null) {
            return oldLookup.getSpanSize(position);
        }
        return 1;
    }

    @Override
    public void onViewAttachedToWindow(CustomViewHolder<B> holder) {
        super.onViewAttachedToWindow(holder);
        if (isShowLoading() || isShowEmpty() || isShowLoadMore(holder.getLayoutPosition())) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (isShowEmpty() || isShowLoading()) {
            return 1;
        }
        return getRealCount() + (hasLoadMore() ? 1 : 0);
    }

    private boolean isShowEmpty() {
        return isEmpty() && isLoaded && showEmpty;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    private boolean hasLoadMore() {
        return hasMore && showMore;
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position == getItemCount() - 1);
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    private boolean isShowLoading() {
        return isEmpty() && !isLoaded && showLoading;
    }

    public void setLoaded() {
        if (!isLoaded) {
            isLoaded = true;
            notifyDataSetChanged();
        }
    }
}
