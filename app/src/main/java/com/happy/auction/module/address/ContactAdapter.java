package com.happy.auction.module.address;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemContactBinding;
import com.happy.auction.entity.item.Contact;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 联系人Adapter
 */
public class ContactAdapter extends BaseCustomAdapter<Contact, ItemContactBinding> {
    @Override
    public ItemContactBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemContactBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemContactBinding binding, Contact item, int position) {
        binding.setData(item);
    }
}
