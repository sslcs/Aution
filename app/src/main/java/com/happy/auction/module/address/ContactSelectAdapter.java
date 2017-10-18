package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemSelectContactBinding;
import com.happy.auction.entity.item.Contact;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 选择联系人Adapter
 */
public class ContactSelectAdapter extends BaseCustomAdapter<Contact, ItemSelectContactBinding> {
    private int mSelectedPosition = 0;

    @Override
    public ItemSelectContactBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemSelectContactBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemSelectContactBinding binding, final Contact item, final int position) {
        binding.setData(item);
        binding.ivCheck.setSelected(mSelectedPosition == position);
    }

    public int getSelectPosition() {
        return mSelectedPosition;
    }

    public void setSelectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(mSelectedPosition);
    }
}
