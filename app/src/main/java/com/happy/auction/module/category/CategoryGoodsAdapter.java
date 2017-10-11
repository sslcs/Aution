package com.happy.auction.module.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemCategoryGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.module.home.CountDownHome;

/**
 * 分类商品Adapter
 */
public class CategoryGoodsAdapter extends BaseCustomAdapter<ItemGoods, ItemCategoryGoodsBinding> {

    @Override
    public ItemCategoryGoodsBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCategoryGoodsBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemCategoryGoodsBinding binding, ItemGoods item, int position) {
        binding.setData(item);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().performClick();
            }
        });
        String tag = (String) binding.ivIcon.getTag(binding.ivIcon.getId());
        if (!item.icon.equals(tag)) {
            binding.ivIcon.setTag(binding.ivIcon.getId(), item.icon);
            ImageLoader.loadImage(binding.ivIcon, item.icon);
        }

        setTime(binding.tvTime, item);
    }

    private void setTime(CountDownHome timer, ItemGoods item) {
        timer.cancel();
        if (item.status == 0) {
            timer.finish();
            return;
        }

        timer.setExpireTime(item.bid_expire_time);
        timer.setRepeat(item.current_price == 0);
    }
}
