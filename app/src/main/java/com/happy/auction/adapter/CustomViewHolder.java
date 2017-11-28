package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * 包装ViewDataBinding成ViewHolder
 *
 * @author LiuCongshan
 * @date 17-9-27
 */

public class CustomViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final B mBinding;

    public CustomViewHolder(B binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }
}