package com.happy.auction.module.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemOrderBinding;
import com.happy.auction.entity.event.BidNowEvent;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.utils.RxBus;

/**
 * 订单记录Adapter
 *
 * @author LiuCongshan
 */
public class OrderAdapter extends BaseCustomAdapter<ItemOrder, ItemOrderBinding> {
    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.tvAdd.setText(R.string.tip_empty_order);
        binding.ivImage.setImageResource(R.drawable.ic_empty_order);
        binding.btnBid.setVisibility(View.VISIBLE);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().post(new BidNowEvent());
            }
        });
        return new CustomViewHolder<>(binding);
    }

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
