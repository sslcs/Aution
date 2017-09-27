package com.happy.auction.module.me;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemOrderBinding;
import com.happy.auction.entity.item.ItemOrder;

/**
 * 订单记录Adapter
 */
public class OrderAdapter extends BaseCustomAdapter<ItemOrder, ItemOrderBinding> {
    @Override
    public ItemOrderBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemOrderBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemOrderBinding binding, ItemOrder item, int position) {
        binding.setData(item);
    }
}
