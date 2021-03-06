package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.adapter.OnViewClickListener;
import com.happy.auction.databinding.EmptyAddressBinding;
import com.happy.auction.databinding.ItemAddressBinding;
import com.happy.auction.entity.item.Address;

/**
 * 收货地址Adapter
 *
 * @author LiuCongshan
 * @date 17-10-17
 */
public class AddressAdapter extends BaseCustomAdapter<Address, ItemAddressBinding> {
    private OnViewClickListener<Address> mListener;
    private int mPositionDefault = -1;

    public AddressAdapter() {
        showBottom(false);
    }

    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyAddressBinding binding = EmptyAddressBinding.inflate(inflater, parent, false);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemAddressBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemAddressBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemAddressBinding binding, final Address item, final int position) {
        binding.setData(item);

        if (mPositionDefault != -1) {
            binding.tvDefault.setSelected(mPositionDefault == position);
        } else if (item.is_default == 1) {
            mPositionDefault = position;
            binding.tvDefault.setSelected(true);
        } else {
            binding.tvDefault.setSelected(false);
        }

        if (mListener == null) {
            return;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewClick(view, item, position);
            }
        };
        binding.tvDefault.setOnClickListener(listener);
        binding.tvEdit.setOnClickListener(listener);
        binding.tvDelete.setOnClickListener(listener);
    }

    public void setOnViewClickListener(OnViewClickListener<Address> listener) {
        mListener = listener;
    }

    public void setPositionDefault(int position) {
        mPositionDefault = position;
        notifyDataSetChanged();
    }
}
