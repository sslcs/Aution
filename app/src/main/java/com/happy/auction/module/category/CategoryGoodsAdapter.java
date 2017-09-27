package com.happy.auction.module.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.ItemCategoryGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;

/**
 * 分类商品Adapter
 */
public class CategoryGoodsAdapter extends BaseCustomAdapter<ItemGoods, ItemCategoryGoodsBinding> {

    @Override
    public ItemCategoryGoodsBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCategoryGoodsBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemCategoryGoodsBinding binding, ItemGoods item, int position) {
        binding.setData(item);
    }
}
