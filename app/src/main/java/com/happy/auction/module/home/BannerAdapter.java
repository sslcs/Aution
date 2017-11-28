package com.happy.auction.module.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.adapter.CustomViewHolder;
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

    @Override
    public int getItemCount() {
        if (getRealCount() > 1) {
            return Integer.MAX_VALUE;
        }
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder<ItemBannerBinding> holder, int position) {
        ItemBanner item = getItem(position % getRealCount());
        bindItem(holder.mBinding, item, position);
        setClickListener(holder, item);
    }
}
