package com.happy.auction.module.message;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemMessageBinding;
import com.happy.auction.entity.item.ItemMessage;

/**
 * 消息记录Adapter
 *
 * @author LiuCongshan
 */
public class MessageAdapter extends BaseCustomAdapter<ItemMessage, ItemMessageBinding> {
    @Override
    public ItemMessageBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemMessageBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemMessageBinding binding, final ItemMessage item, int position) {
        binding.setData(item);
    }

}
