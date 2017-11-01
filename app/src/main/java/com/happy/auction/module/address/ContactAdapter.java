package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.adapter.OnViewClickListener;
import com.happy.auction.databinding.EmptyAddressBinding;
import com.happy.auction.databinding.ItemContactBinding;
import com.happy.auction.entity.item.Contact;

/**
 * 联系人Adapter
 *
 * @author LiuCongshan
 * @date 17-10-17
 */
public class ContactAdapter extends BaseCustomAdapter<Contact, ItemContactBinding> {
    private OnViewClickListener<Contact> mListener;

    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyAddressBinding binding = EmptyAddressBinding.inflate(inflater, parent, false);
        binding.tvTitle.setText(R.string.tip_empty_contact);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemContactBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemContactBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemContactBinding binding, final Contact item, final int position) {
        binding.setData(item);

        if (mListener == null) {
            return;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewClick(view, item, position);
            }
        };
        binding.tvEdit.setOnClickListener(listener);
        binding.tvDelete.setOnClickListener(listener);
    }

    public void setOnViewClickListener(OnViewClickListener<Contact> listener) {
        mListener = listener;
    }
}
