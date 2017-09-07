package com.happy.auction.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.utils.DebugLog;

/**
 * Created by LiuCongshan on 17-9-4.
 * Display refresh view.
 *
 * @see #setRefreshView(int)
 * @see #setRefreshView(View)
 * @see #setOnRefreshListener(OnRefreshListener)
 */

public class RefreshWrapper<T> extends WrapperAdapter<T> {
    private View mRefreshView;
    private int mRefreshLayoutId;
    private OnRefreshListener mOnRefreshListener;
    private boolean isRefreshing = false;
    private RecyclerView mRecyclerView;

    public RefreshWrapper(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        super(adapter);
    }

    private boolean hasRefresh() {
        return mRefreshView != null || mRefreshLayoutId != 0;
    }

    private boolean isShowRefresh(int position) {
        return hasRefresh() && position == 0;
    }

    public boolean isRefresh() {
        return isRefreshing;
    }

    public void setRefreshing(boolean isRefreshing) {
        if (!isRefreshing) {
            scrollTop();
        }
        this.isRefreshing = isRefreshing;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowRefresh(position)) {
            return ITEM_TYPE_REFRESH;
        }
        return mInnerAdapter.getItemViewType(position - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_REFRESH) {
            ViewHolder holder;
            if (mRefreshView != null) {
                holder = ViewHolder.createViewHolder(parent.getContext(), mRefreshView);
            } else {
                holder = ViewHolder.createViewHolder(parent.getContext(), parent, mRefreshLayoutId);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowRefresh(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (hasRefresh()) {
            recyclerView.scrollToPosition(1);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    DebugLog.e("newState : " + newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                        if (manager instanceof LinearLayoutManager) {
                            LinearLayoutManager llm = (LinearLayoutManager) manager;
                            if (llm.findFirstCompletelyVisibleItemPosition() == 0) {
                                if (mOnRefreshListener != null) {
                                    mOnRefreshListener.onRefresh();
                                }
                            } else if (llm.findFirstVisibleItemPosition() == 0) {
                                scrollTop();
                            }
                        }
                    }
                }
            });
        }
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowRefresh(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }

    private void scrollTop() {
        int top = mRecyclerView.getChildAt(1).getTop();
        mRecyclerView.scrollBy(0, top);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowRefresh(holder.getLayoutPosition())) {
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
        return mInnerAdapter.getItemCount() + 1;
    }

    public RefreshWrapper setOnRefreshListener(OnRefreshListener listener) {
        if (listener != null) {
            mOnRefreshListener = listener;
        }
        return this;
    }

    public RefreshWrapper setRefreshView(View view) {
        mRefreshView = view;
        return this;
    }

    public RefreshWrapper setRefreshView(int layoutId) {
        mRefreshLayoutId = layoutId;
        return this;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
