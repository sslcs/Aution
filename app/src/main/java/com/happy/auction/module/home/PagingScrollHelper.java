package com.happy.auction.module.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;

/**
 * 实现RecycleView分页滚动的工具类
 *
 * @author LiuCongshan
 * @date 17-10-10
 * @see <a href="https://github.com/zhuguohui/HorizontalPage/blob/master/app/src/main/java/com/zhuguohui/horizontalpage/view/PagingScrollHelper.java">url</a>
 */

public class PagingScrollHelper extends RecyclerView.OnFlingListener {
    private final static int ORIENTATION_NULL = 0;
    private final static int ORIENTATION_VERTICAL = 1;
    private final static int ORIENTATION_HORIZONTAL = 2;

    private RecyclerView mRecyclerView = null;
    private int mOrientation;
    private ValueAnimator mAnimator = null;
    private OnPageChangedListener mListener;
    private LinearLayoutManager mLinearLayoutManager;
    private int offsetY = 0;
    private int offsetX = 0;

    public void setRecycleView(RecyclerView recycleView) {
        if (recycleView == null) {
            throw new IllegalArgumentException("recycleView must be not null");
        }
        mRecyclerView = recycleView;
        //处理滑动
        recycleView.setOnFlingListener(this);
        //设置滚动监听，记录滚动的状态，和总的偏移量
        recycleView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                handleScrollStateChanged(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滚动结束记录滚动的偏移量
                offsetY += dy;
                offsetX += dx;
            }
        });
        //获取滚动的方向
        updateLayoutManger();
    }

    private void updateLayoutManger() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            mLinearLayoutManager = (LinearLayoutManager) layoutManager;
            if (layoutManager.canScrollVertically()) {
                mOrientation = ORIENTATION_VERTICAL;
            } else if (layoutManager.canScrollHorizontally()) {
                mOrientation = ORIENTATION_HORIZONTAL;
            } else {
                mOrientation = ORIENTATION_NULL;
            }
            if (mAnimator != null) {
                mAnimator.cancel();
            }
        }
    }

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        if (mOrientation == ORIENTATION_NULL) {
            return false;
        }

        //记录滚动开始和结束的位置
        int endPoint = 0;
        int startPoint;

        //如果是垂直方向
        if (mOrientation == ORIENTATION_VERTICAL) {
            startPoint = offsetY;
            if (velocityY < 0) {
                endPoint = -mRecyclerView.getHeight();
            } else if (velocityY > 0) {
                endPoint = mRecyclerView.getHeight();
            }
        } else {
            startPoint = offsetX;
            if (velocityX < 0) {
                endPoint = -mRecyclerView.getWidth();
            } else if (velocityX > 0) {
                endPoint = mRecyclerView.getWidth();
            }
        }

        //使用动画处理滚动
        if (mAnimator == null) {
            mAnimator = ValueAnimator.ofInt(startPoint, endPoint);
            mAnimator.setDuration(300);
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator value) {
                    int nowPoint = (int) value.getAnimatedValue();
                    if (mOrientation == ORIENTATION_VERTICAL) {
                        int dy = nowPoint - offsetY;
                        //这里通过RecyclerView的scrollBy方法实现滚动。
                        mRecyclerView.scrollBy(0, dy);
                    } else {
                        int dx = nowPoint - offsetX;
                        mRecyclerView.scrollBy(dx, 0);
                    }
                }
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //回调监听
                    if (null != mListener) {
                        int position = mLinearLayoutManager.findFirstVisibleItemPosition();
                        mListener.onPageChanged(position);
                    }
                    offsetX = 0;
                    offsetY = 0;
                }
            });
        } else {
            mAnimator.cancel();
            mAnimator.setIntValues(startPoint, endPoint);
        }
        mAnimator.start();

        return false;
    }

    private void handleScrollStateChanged(int newState) {
        //newState==0表示滚动停止，此时需要处理回滚
        if (newState == 0 && mOrientation != ORIENTATION_NULL) {
            int vX = 0, vY = 0;
            if (mOrientation == ORIENTATION_VERTICAL) {
                //如果滑动的距离超过屏幕的一半表示需要滑动到下一页
                if (Math.abs(offsetY) > mRecyclerView.getHeight() / 2) {
                    vY = offsetY;
                }
            } else {
                if (Math.abs(offsetX) > mRecyclerView.getWidth() / 2) {
                    vX = offsetX;
                }
            }
            onFling(vX, vY);
        }
    }

    public void setOnPageChangedListener(OnPageChangedListener listener) {
        mListener = listener;
    }

    public interface OnPageChangedListener {
        /**
         * 页面切换事件
         *
         * @param index 当前页面index
         */
        void onPageChanged(int index);
    }
}
