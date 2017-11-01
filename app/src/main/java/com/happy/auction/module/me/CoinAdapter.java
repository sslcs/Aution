package com.happy.auction.module.me;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemCoinBinding;
import com.happy.auction.entity.item.ItemCoin;

/**
 * 金币记录Adapter
 *
 * @author LiuCognshan
 */
public class CoinAdapter extends BaseCustomAdapter<ItemCoin, ItemCoinBinding> {
    private int mType;

    public CoinAdapter(int type) {
        mType = type;
    }

    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.ivImage.setImageResource(mType == 1 ? R.drawable.ic_empty_coin : R.drawable.ic_empty_free);
        binding.tvAdd.setText(mType == 1 ? R.string.tip_empty_coin : R.string.tip_empty_free);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemCoinBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCoinBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemCoinBinding binding, ItemCoin item, int position) {
        binding.setData(item);
    }
}
