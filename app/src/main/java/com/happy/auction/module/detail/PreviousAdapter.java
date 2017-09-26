package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemLatestBinding;
import com.happy.auction.databinding.ItemPreviousBinding;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.entity.item.ItemPrevious;

/**
 * 往期成交Adapter
 */
public class PreviousAdapter extends BaseAdapter<ItemPrevious> {
    @Override
    public CustomViewHolder<ItemPreviousBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPreviousBinding binding = ItemPreviousBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemPrevious item = getItem(position);
        ItemPreviousBinding binding = (ItemPreviousBinding) holder.binding;
        binding.setData(item);
    }
}
