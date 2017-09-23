package com.happy.auction.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemBidRecordBinding;
import com.happy.auction.entity.item.BidRecord;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情参与记录
 */

public class AuctionDetailBidAdapter extends BaseAdapter<BidRecord> {
    @Override
    public CustomViewHolder<ItemBidRecordBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemBidRecordBinding binding = ItemBidRecordBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BidRecord item = getItem(position + 1);
        ItemBidRecordBinding binding = (ItemBidRecordBinding) holder.binding;
        binding.setRecord(item);
    }

    @Override
    public void addItem(BidRecord item) {
        if (super.getItemCount() >= 4) {
            removeItem(3);
        }
        addItem(0, item);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() - 1;
    }
}
