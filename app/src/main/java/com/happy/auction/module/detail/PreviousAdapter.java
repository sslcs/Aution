package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemPreviousBinding;
import com.happy.auction.entity.item.ItemPrevious;

/**
 * 往期成交Adapter
 *
 * @author LiuCongshan
 * @date 17-9-26
 */
public class PreviousAdapter extends BaseCustomAdapter<ItemPrevious, ItemPreviousBinding> {
    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.ivImage.setImageResource(R.drawable.ic_empty_previous);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemPreviousBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemPreviousBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemPreviousBinding binding, ItemPrevious item, int position) {
        binding.setData(item);
    }
}
