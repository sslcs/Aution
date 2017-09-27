package com.happy.auction.module.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemCategoryBinding;
import com.happy.auction.entity.item.ItemCategory;

/**
 * 分类Adapter
 */
public class CategoryAdapter extends BaseAdapter<ItemCategory, ItemCategoryBinding> {
    private int selectedPosition = 0;

    @Override
    public ItemCategoryBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCategoryBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemCategoryBinding binding, ItemCategory item, int position) {
        binding.setData(item);
        binding.tvTitle.setSelected(position == selectedPosition);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        int lastPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(selectedPosition);
    }
}
