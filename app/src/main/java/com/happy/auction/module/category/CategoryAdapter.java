package com.happy.auction.module.category;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemCategoryBinding;
import com.happy.auction.entity.item.ItemCategory;

/**
 * 分类Adapter
 */
public class CategoryAdapter extends BaseAdapter<ItemCategory> {
    private int selectedPosition = 0;

    @Override
    public CustomViewHolder<ItemCategoryBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(inflater);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemCategory item = getItem(position);
        ItemCategoryBinding binding = (ItemCategoryBinding) holder.binding;
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
