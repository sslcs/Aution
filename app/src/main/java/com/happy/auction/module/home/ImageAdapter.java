package com.happy.auction.module.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseAdapter;
import com.happy.auction.databinding.ItemImageBinding;
import com.happy.auction.glide.ImageLoader;

import java.util.List;

/**
 * 图片adapter
 *
 * @author LiuCongshan
 * @date 17-11-3
 */

public class ImageAdapter extends BaseAdapter<String, ItemImageBinding> {
    public ImageAdapter(List<String> items) {
        super(items);
    }

    @Override
    public ItemImageBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemImageBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemImageBinding binding, String item, int position) {
        ImageLoader.displayImage(binding.ivImage, item);
    }
}
