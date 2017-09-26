package com.happy.auction.module.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemCategoryGoodsBinding;
import com.happy.auction.databinding.ItemLatestBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemLatest;

/**
 * 分类商品Adapter
 */
public class CategoryGoodsAdapter extends BaseAdapter<ItemGoods> {
    @Override
    public CustomViewHolder<ItemCategoryGoodsBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryGoodsBinding binding = ItemCategoryGoodsBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemGoods item = getItem(position);
        ItemCategoryGoodsBinding binding = (ItemCategoryGoodsBinding) holder.binding;
        binding.setData(item);
    }
}
