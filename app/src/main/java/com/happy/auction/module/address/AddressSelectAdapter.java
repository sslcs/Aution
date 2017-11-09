package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemSelectAddressBinding;
import com.happy.auction.entity.item.Address;

/**
 * 选择收货地址Adapter
 *
 * @author LiuCongshan
 * @date 17-10-19
 */
public class AddressSelectAdapter extends BaseCustomAdapter<Address, ItemSelectAddressBinding> {
    private int mSelectedPosition = -1;
    private int mId;

    public AddressSelectAdapter(int id) {
        mId = id;
        showBottom(false);
    }

    @Override
    public ItemSelectAddressBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemSelectAddressBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemSelectAddressBinding binding, Address item, int position) {
        binding.setData(item);
        if (mSelectedPosition != -1) {
            binding.ivCheck.setSelected(mSelectedPosition == position);
        } else if (item.aid == mId) {
            mSelectedPosition = position;
            binding.ivCheck.setSelected(true);
        } else {
            binding.ivCheck.setSelected(false);
        }
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

    public void setId(int id) {
        mId = id;
    }
}
