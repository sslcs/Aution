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
    private int[] colors = new int[]{0xffe53ca7, 0xffe53c3f, 0xff52ba54, 0xff5f78e1, 0xff9320ff, 0xffff4e00};

    @Override
    public ItemTrendBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemTrendBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemTrendBinding binding, ItemTrend item, int position) {
        ItemTrend itemLeft = getItem(position - 1);
        ItemTrend itemRight = getItem(position + 1);
        binding.itemView.setData(item, mMax, itemLeft, itemRight);
        binding.itemView.setColor(colors[position % 6]);
    }

    public void setMax(int max) {
        this.mMax = max;
    }
}
