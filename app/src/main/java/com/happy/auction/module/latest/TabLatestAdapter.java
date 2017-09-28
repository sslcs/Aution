package com.happy.auction.module.latest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
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
    public void bindItem(final ItemLatestBinding binding, ItemLatest item, int position) {
        binding.setData(item);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().performClick();
            }
        });
    }
}
