package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemBidRecordBinding;
import com.happy.auction.entity.item.BidRecord;

/**
 * Created by LiuCongshan on 17-8-18.<br/>
 * 竞拍记录Adapter
 */

public class BidRecordAdapter extends BaseCustomAdapter<BidRecord, ItemBidRecordBinding> {
    private int status;

    /**
     * 商品状态
     *
     * @param status 0:已结束，1：未开始，2：正在进行
     */
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public ItemBidRecordBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemBidRecordBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemBidRecordBinding binding, BidRecord item, int position) {
        binding.setData(item);
        if (position == 0) {
            binding.tvStatus.setText(status == 0 ? R.string.bid_deal : R.string.bid_first);
            binding.tvStatus.setTextColor(R.color.main_red);
        } else {
            binding.tvStatus.setText(R.string.bid_out);
            binding.tvStatus.setTextColor(R.color.text_normal);
        }
    }
}
