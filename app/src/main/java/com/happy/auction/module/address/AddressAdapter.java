package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemAddressBinding;
import com.happy.auction.entity.item.Address;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 收货地址Adapter
 */
public class AddressAdapter extends BaseCustomAdapter<Address, ItemAddressBinding> {
    private OnViewClickListener mListener;
    private int mPositionDefault = -1;

    @Override
    public ItemAddressBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemAddressBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemAddressBinding binding, final Address item, int position) {
        binding.setData(item);
        if (item.is_default == 1) {
            mPositionDefault = position;
            binding.tvDefault.setSelected(true);
        } else {
            binding.tvDefault.setSelected(false);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onClickView(view, item);
                }
            }
        };

        binding.tvDefault.setOnClickListener(listener);
        binding.tvEdit.setOnClickListener(listener);
        binding.tvDelete.setOnClickListener(listener);
    }

    public void setOnViewClickListener(OnViewClickListener listener) {
        mListener = listener;
    }

    public int getPositionDefault() {
        return mPositionDefault;
    }

    public interface OnViewClickListener {
        /**
         * adapter子View点击事件
         *
         * @param view 点击view
         * @param item 点击项data
         */
        void onClickView(View view, Address item);
    }
}
