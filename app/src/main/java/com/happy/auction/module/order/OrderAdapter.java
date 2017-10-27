package com.happy.auction.module.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemOrderBinding;
import com.happy.auction.entity.item.ItemOrder;

/**
 * 订单记录Adapter
 *
 * @author LiuCongshan
 */
public class OrderAdapter extends BaseCustomAdapter<ItemOrder, ItemOrderBinding> {
    @Override
    public ItemOrderBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemOrderBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemOrderBinding binding, final ItemOrder item, int position) {
        binding.setData(item);
        binding.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().callOnClick();
            }
        });

    }
}
