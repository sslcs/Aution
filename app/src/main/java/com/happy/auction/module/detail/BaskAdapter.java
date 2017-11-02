package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemBaskDetailBinding;
import com.happy.auction.entity.item.ItemBask;
import com.happy.auction.glide.ImageLoader;

/**
 * 详情晒单分享Adapter
 *
 * @author LiuCongshan
 */
public class BaskAdapter extends BaseCustomAdapter<ItemBask, ItemBaskDetailBinding> {
    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.ivImage.setImageResource(R.drawable.ic_empty_bask);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemBaskDetailBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemBaskDetailBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemBaskDetailBinding binding, ItemBask item, int position) {
        binding.setData(item);
        ImageLoader.displayImage(binding.ivImg, item.s_img.get(0));
        if (item.s_img.size() > 1) {
            ImageLoader.displayImage(binding.ivImg1, item.s_img.get(1));
        }
    }
}
