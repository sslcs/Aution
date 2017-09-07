package com.happy.auction.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LiuCongshan on 17-9-4.<br/>
 * Display empty view while the data is empty;<br/>
 */

public class AdapterWrapper<T extends RecyclerView.Adapter>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final int ITEM_TYPE_HEADER_BASE = 0XFA000;
    static final int ITEM_TYPE_FOOTER_BASE = 0XFF000;
    private static final int ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 1;
    private static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_REFRESH = Integer.MAX_VALUE - 3;

    private T mInnerAdapter;

    private View mEmptyView;
    private int mEmptyLayoutId;

    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    private LoadMoreListener mLoadMoreListener;
    private boolean hasMore = false;

    private View mRefreshView;
    private int mRefreshLayoutId;
    private OnRefreshListener mOnRefreshListener;
    private boolean isRefreshing = false;
    private RecyclerView mRecyclerView;

    public AdapterWrapper(T adapter) {
        mInnerAdapter = adapter;
    }

    public T getInnerAdapter() {
        return mInnerAdapter;
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowRefresh(position)) {
            return ITEM_TYPE_REFRESH;
        }
        if (isEmpty()) {
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

        if (viewType == ITEM_TYPE_LOAD_MORE) {
            if (mLoadMoreView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                mLoadMoreView = inflater.inflate(mLoadMoreLayoutId, parent, false);
            }
            return ViewHolder.createViewHolder(parent.getContext(), mLoadMoreView);
        }

        if (viewType == ITEM_TYPE_REFRESH) {
            if (mRefreshView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                mRefreshView = inflater.inflate(mRefreshLayoutId, parent, false);
            }
            return ViewHolder.createViewHolder(parent.getContext(), mRefreshView);
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isEmpty() || isShowRefresh(position)) {
            return;
        }

        if (isShowLoadMore(position)) {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.loadMore();
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - (hasRefresh() ? 1 : 0));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isEmpty() || isShowLoadMore(position) || isShowRefresh(position)) {
                    return layoutManager.getSpanCount();
                }

                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });

        if (hasRefresh()) {
            mRecyclerView = recyclerView;
            recyclerView.scrollToPosition(1);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    // Stop at first item while flying up.
                    if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                        if (dy < 0) {
                            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                            if (manager instanceof LinearLayoutManager) {
                                LinearLayoutManager llm = (LinearLayoutManager) manager;
                                if (llm.findFirstVisibleItemPosition() == 0) {
                                    recyclerView.stopScroll();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager llm = (LinearLayoutManager) manager;
                        if (llm.findFirstCompletelyVisibleItemPosition() == 0) {
                            if (mOnRefreshListener != null && !isRefreshing) {
                                isRefreshing = true;
                                mOnRefreshListener.onRefresh();
                            }
                        } else if (llm.findFirstVisibleItemPosition() == 0) {
                            scrollTop();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        if (isEmpty()) {
            WrapperUtils.setFullSpan(holder);
        } else if (isShowLoadMore(holder.getLayoutPosition()) ||
                isShowRefresh(holder.getLayoutPosition())) {
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
        if (isEmpty()) return 1 + (hasRefresh() ? 1 : 0);
        return mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0) + (hasRefresh() ? 1 : 0);
    }

    // Empty adapter begin-----------------------------------------
    private boolean isEmpty() {
        return (mEmptyView != null || mEmptyLayoutId != 0) && getRealItemCount() == 0;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setEmptyView(int layoutId) {
        mEmptyLayoutId = layoutId;
    }
    //Empty adapter end-----------------------------------------

    // LoadMore adapter begin-----------------------------------------
    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        notifyDataSetChanged();
    }

    private boolean hasLoadMore() {
        return hasMore && (mLoadMoreView != null || mLoadMoreLayoutId != 0);
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && (position >= getItemCount() - 1);
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

    // Refresh adapter begin-----------------------------------------
    private boolean hasRefresh() {
        return mRefreshView != null || mRefreshLayoutId != 0;
    }

    private boolean isShowRefresh(int position) {
        return hasRefresh() && position == 0;
    }

    public void setRefreshing(boolean isRefreshing) {
        if (!isRefreshing) {
            scrollTop();
        }
        this.isRefreshing = isRefreshing;
    }

    private void scrollTop() {
        if (getItemCount() > 1) {
            int top = mRecyclerView.getChildAt(1).getTop();
            mRecyclerView.scrollBy(0, top);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        if (listener != null) {
            mOnRefreshListener = listener;
        }
    }

    public void setRefreshView(View view) {
        mRefreshView = view;
    }

    public void setRefreshView(int layoutId) {
        mRefreshLayoutId = layoutId;
    }
    // Refresh adapter end-----------------------------------------

    public interface LoadMoreListener {
        void loadMore();
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
