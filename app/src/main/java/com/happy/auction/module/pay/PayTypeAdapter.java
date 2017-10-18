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
    private int mSelectedPosition = 0;

    @Override
    public ItemPayTypeBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemPayTypeBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemPayTypeBinding binding, ItemPayType item, int position) {
        binding.setData(item);
        binding.ivCheck.setSelected(position == mSelectedPosition);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public void setSelectedPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(mSelectedPosition);
    }
}
