package com.happy.auction.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuCongshan on 17-9-4.<br/>
 * Display empty view while the data is empty;<br/>
 */

public class AdapterWrapper<A extends RecyclerView.Adapter>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int ITEM_TYPE_HEADER_BASE = 0XFA000;
    static final int ITEM_TYPE_FOOTER_BASE = 0XFF000;
    private static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    private static final int ITEM_TYPE_LOADING = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 3;

    private A mInnerAdapter;

    private View mEmptyView;
    private int mEmptyLayoutId;

    private View mLoadMoreView;
    private int mLoadMoreLayoutId;

    private View mLoadingView;
    private int mLoadingLayoutId;
    private boolean isLoaded = false;

    private LoadMoreListener mLoadMoreListener;
    private boolean hasMore = false;

    public AdapterWrapper(A adapter) {
        mInnerAdapter = adapter;
    }

    public A getInnerAdapter() {
        return mInnerAdapter;
    }

    public int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (!isLoaded && isShowLoading()) {
            return ITEM_TYPE_LOADING;
        }

        if (isShowEmpty()) {
            return ITEM_TYPE_EMPTY;
        }

        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_EMPTY) {
            if (mEmptyView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                mEmptyView = inflater.inflate(mEmptyLayoutId, parent, false);
            }
            return ViewHolder.createViewHolder(parent.getContext(), mEmptyView);
        }

        if (viewType == ITEM_TYPE_LOADING) {
            if (mLoadingView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                mLoadingView = inflater.inflate(mLoadingLayoutId, parent, false);
            }
            return ViewHolder.createViewHolder(parent.getContext(), mLoadingView);
        }

        if (viewType == ITEM_TYPE_LOAD_MORE) {
            if (mLoadMoreView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                mLoadMoreView = inflater.inflate(mLoadMoreLayoutId, parent, false);
            }
            return ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowEmpty()) {
            return;
        }

        if (isShowLoadMore(position)) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.loadMore();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                layoutManager.setAutoMeasureEnabled(false);
                if (isShowEmpty() || isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }

                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isShowEmpty()) {
            WrapperUtils.setFullSpan(holder);
        } else if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        if (isShowEmpty() || isShowLoading()) return 1;
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    // Empty adapter begin-----------------------------------------
    private boolean isShowEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && getRealItemCount() == 0;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
    }
    // Empty adapter end-----------------------------------------

    // LoadMore adapter begin-----------------------------------------
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    private boolean hasLoadMore() {
        return hasMore && (mLoadMoreView != null || mLoadMoreLayoutId != 0);
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position == getItemCount() - 1);
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        if (listener != null) {
            mLoadMoreListener = listener;
        }
    }

    public void setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    public void setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
    }
    // LoadMore adapter end-----------------------------------------

    // Loading adapter begin-----------------------------------------
    private boolean isShowLoading() {
        return (mLoadingView != null || mLoadingLayoutId != 0) && getRealItemCount() == 0;
    }

    public void setLoadingView(View emptyView) {
        mLoadingView = emptyView;
    }

    public void setLoadingView(int layoutId) {
        mLoadingLayoutId = layoutId;
    }

    public void setLoaded() {
        isLoaded = true;
    }
    // Loading adapter end-----------------------------------------

    public interface LoadMoreListener {
        void loadMore();
    }
}
