package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemLoadMoreBinding;

/**
 * Created by LiuCongshan on 17-9-12.
 * 为adapter设置默认EmptyView,LoadMoreView.
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
}
