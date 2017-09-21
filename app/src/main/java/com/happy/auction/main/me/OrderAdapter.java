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
        ItemOrder item = getItem(position);
        item.icon = "http://mobile-pic.cache.iciba.com/1486980953-8616_218-135-%E9%95%BF%E5%8F%91%E5%A4%96%E5%9B%BD%E5%A5%B3.jpg";
        ItemOrderBinding binding = (ItemOrderBinding) holder.getBinding();
        binding.setData(item);
    }
}
