package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemPreviousBinding;
import com.happy.auction.entity.item.ItemPrevious;

/**
 * 往期成交Adapter
 */
public class PreviousAdapter extends BaseCustomAdapter<ItemPrevious, ItemPreviousBinding> {
    @Override
    public ItemPreviousBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemPreviousBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemPreviousBinding binding, ItemPrevious item, int position) {
        binding.setData(item);
    }
}
