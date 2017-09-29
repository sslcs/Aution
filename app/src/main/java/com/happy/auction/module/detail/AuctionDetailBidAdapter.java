package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemBidRecordBinding;
import com.happy.auction.entity.item.BidRecord;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情竞拍记录
 */

public class AuctionDetailBidAdapter extends BaseAdapter<BidRecord, ItemBidRecordBinding> {
    @Override
    public ItemBidRecordBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemBidRecordBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemBidRecordBinding binding, BidRecord item, int position) {
        item = getItem(position + 1);
        binding.setData(item);
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
