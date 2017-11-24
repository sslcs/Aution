package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemTrendBinding;
import com.happy.auction.entity.item.ItemTrend;

/**
 * 趋势图Adapter
 *
 * @author LiuCongshan
 * @date 17-11-24
 */

public class TrendAdapter extends BaseCustomAdapter<ItemTrend, ItemTrendBinding> {
    private int mMax;

    @Override
    public ItemTrendBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemTrendBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemTrendBinding binding, ItemTrend item, int position) {
        ItemTrend itemLeft = getItem(position + 1);
        ItemTrend itemRight = getItem(position - 1);
        binding.itemView.setData(item, mMax, itemLeft, itemRight);
    }

    public void setMax(int max) {
        this.mMax = max;
    }
}
