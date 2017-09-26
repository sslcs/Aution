package com.happy.auction.module.latest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemLatestBinding;
import com.happy.auction.entity.item.ItemLatest;

/**
 * 最新成交Adapter
 */
public class TabLatestAdapter extends BaseAdapter<ItemLatest> {
    @Override
    public CustomViewHolder<ItemLatestBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLatestBinding binding = ItemLatestBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemLatest item = getItem(position);
        ItemLatestBinding binding = (ItemLatestBinding) holder.binding;
        binding.setData(item);
    }
}
