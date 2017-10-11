package com.happy.auction.module.pay;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemPayTypeBinding;
import com.happy.auction.entity.item.ItemPayType;

/**
 * 支付方式Adapter
 */
public class PayTypeAdapter extends BaseAdapter<ItemPayType, ItemPayTypeBinding> {
    private int selectedPosition = 0;

    @Override
    public ItemPayTypeBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemPayTypeBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemPayTypeBinding binding, ItemPayType item, int position) {
        binding.setData(item);
        binding.ivCheck.setSelected(position == selectedPosition);
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
