package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemBottomBinding;
import com.happy.auction.databinding.ItemLoadMoreBinding;

/**
 * 为adapter设置默认EmptyView,LoadMoreView,LoadingView,BottomView.
 *
 * @author LiuCongshan
 * @date 17-9-12
 */

public abstract class BaseCustomAdapter<T, B extends ViewDataBinding> extends BaseFeatureAdapter<T, B> {
    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public CustomViewHolder getBindingMore(ViewGroup parent, LayoutInflater inflater) {
        ItemLoadMoreBinding binding = ItemLoadMoreBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public CustomViewHolder getBindingLoading(ViewGroup parent, LayoutInflater inflater) {
        ItemLoadMoreBinding binding = ItemLoadMoreBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public CustomViewHolder getBindingBottom(ViewGroup parent, LayoutInflater inflater) {
        ItemBottomBinding binding = ItemBottomBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }
}
