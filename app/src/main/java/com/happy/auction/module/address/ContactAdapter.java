package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.OnViewClickListener;
import com.happy.auction.databinding.ItemContactBinding;
import com.happy.auction.entity.item.Contact;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 联系人Adapter
 */
public class ContactAdapter extends BaseCustomAdapter<Contact, ItemContactBinding> {
    private OnViewClickListener<Contact> mListener;

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
