package com.happy.auction.module.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemBannerBinding;
import com.happy.auction.entity.item.ItemBanner;

import java.util.List;

/**
 * 首页banner adapter
 *
 * @author LiuCongshan
 * @date 17-10-10
 */

public class BannerAdapter extends BaseAdapter<ItemBanner, ItemBannerBinding> {
    public BannerAdapter(List<ItemBanner> items) {
        super(items);
    }

    @Override
    public ItemBannerBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemBannerBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemBannerBinding binding, ItemBanner item, int position) {
        binding.setData(item);
    }
}
