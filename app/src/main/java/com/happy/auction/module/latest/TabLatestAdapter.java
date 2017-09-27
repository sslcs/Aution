package com.happy.auction.module.latest;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.ItemLatestBinding;
import com.happy.auction.entity.item.ItemLatest;

/**
 * 最新成交Adapter
 */
public class TabLatestAdapter extends BaseCustomAdapter<ItemLatest, ItemLatestBinding> {
    @Override
    public ItemLatestBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemLatestBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemLatestBinding binding, ItemLatest item, int position) {
        binding.setData(item);
    }
}
