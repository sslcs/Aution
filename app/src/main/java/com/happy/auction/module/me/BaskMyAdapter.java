package com.happy.auction.module.me;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.entity.event.BidNowEvent;
import com.happy.auction.module.detail.BaskAdapter;
import com.happy.auction.utils.RxBus;

/**
 * @author LiuCongshan
 * @date 17-11-2
 */

public class BaskMyAdapter extends BaskAdapter {
    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.ivImage.setImageResource(R.drawable.ic_empty_bask);
        binding.btnBid.setVisibility(View.VISIBLE);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().post(new BidNowEvent());
            }
        });
        return new CustomViewHolder<>(binding);
    }
}
