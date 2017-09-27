package com.happy.auction.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by LiuCongshan on 17-9-27.
 */

public class CustomViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final B binding;

    public CustomViewHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public B getBinding() {
        return binding;
    }
}