package com.happy.auction.main.me;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemOrderBinding;
import com.happy.auction.entity.item.ItemOrder;

/**
 * 订单记录Adapter
 */
public class OrderAdapter extends BaseAdapter<ItemOrder> {
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemOrder item = getItem(position);
        ItemOrderBinding binding = (ItemOrderBinding) holder.getBinding();
        binding.setData(item);
    }
}
