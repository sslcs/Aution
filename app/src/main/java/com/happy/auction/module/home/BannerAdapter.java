package com.happy.auction.module.home;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.happy.auction.AppInstance;
import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemBannerBinding;
import com.happy.auction.entity.item.ItemBanner;
import com.happy.auction.glide.GlideApp;

import java.util.List;

/**
 * Created by LiuCongshan on 17-10-10.
 * 首页banner adapter
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
