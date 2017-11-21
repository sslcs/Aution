package com.happy.auction.module.home;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import com.happy.auction.utils.DebugLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 实现RecycleView分页滚动的工具类
 *
 * @author LiuCongshan
 * @date 17-10-10
 */

public class PagerHelper extends PagerSnapHelper {
    private RecyclerView mRecyclerView = null;
    private CircleIndicator mIndicator;
    private Disposable mDisposable;
    private boolean enableAutoScroll = false;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new ScrollListener());
        mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
    }

    public void setIndicator(CircleIndicator indicator) {
        if (indicator == null) {
            throw new IllegalArgumentException("indicator must be not null");
        }
        mIndicator = indicator;
    }

    public void enableAutoScroll() {
        enableAutoScroll = true;
        if (mDisposable != null) {
            return;
        }
        mDisposable = Observable.interval(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        scroll();
                    }
                });
    }

    private void scroll() {
        if (mRecyclerView == null) {
            return;
        }

        int position = mLayoutManager.findLastVisibleItemPosition();
        if (position != mLayoutManager.findFirstVisibleItemPosition()) {
            return;
        }
        position++;
        if (position >= mRecyclerView.getAdapter().getItemCount()) {
            position = 0;
            mRecyclerView.scrollToPosition(position);
        } else {
            mRecyclerView.smoothScrollToPosition(position);
        }
        if (mIndicator != null) {
            mIndicator.onPageSelected(position);
        }
    }

    public void onPause() {
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    public void onResume() {
        if (enableAutoScroll) {
            enableAutoScroll();
        }
    }

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        int targetPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        DebugLog.e("targetPosition : " + targetPosition);
        if (mIndicator != null) {
            mIndicator.onPageSelected(targetPosition);
        }
        return targetPosition;
    }

    private class ScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dx != 0 || dy != 0) {
                onPause();
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                onResume();
            }
        }
    }
}
