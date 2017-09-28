package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemBaskDetailBinding;
import com.happy.auction.entity.item.ItemBask;

/**
 * 详情晒单分享Adapter
 */
public class BaskAdapter extends BaseCustomAdapter<ItemBask, ItemBaskDetailBinding> {
    @Override
    public ItemBaskDetailBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemBaskDetailBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemBaskDetailBinding binding, ItemBask item, int position) {
        binding.setData(item);
    }
}
