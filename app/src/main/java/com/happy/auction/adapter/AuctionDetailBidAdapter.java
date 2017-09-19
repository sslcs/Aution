package com.happy.auction.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.databinding.ItemJoinRecordBinding;
import com.happy.auction.entity.item.BidRecord;

import java.util.Locale;

/**
 * Created by LiuCongshan on 17-8-18.
 * 竞拍详情参与记录
 */

public class AuctionDetailBidAdapter extends BaseAdapter<BidRecord> {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_join_record, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        BidRecord item = getItem(position);
        ViewHolder vh = (ViewHolder) holder;
        vh.binding.tvUsername.setText(item.username);
        float price = item.bid_price / 100.0f;
        String bidPrice = String.format(Locale.CHINA, "￥%.2f", price);
        vh.binding.tvPrice.setText(bidPrice);
    }

    @Override
    public void addItem(BidRecord item) {
        super.addItem(item);
        if (getItemCount() > 3) {
            removeItem(0);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() - 1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        ItemJoinRecordBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
