package com.happy.auction.module.me;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemCoinBinding;
import com.happy.auction.entity.item.ItemCoin;

/**
 * 金币记录Adapter
 */
public class CoinAdapter extends BaseCustomAdapter<ItemCoin, ItemCoinBinding> {
    @Override
    public ItemCoinBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCoinBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemCoinBinding binding, ItemCoin item, int position) {
        binding.setData(item);
    }
}
