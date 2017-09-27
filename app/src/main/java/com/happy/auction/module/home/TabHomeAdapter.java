package com.happy.auction.module.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.databinding.ItemGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.glide.ImageLoader;

import java.util.ArrayList;

/**
 * 首页商品Adapter
 */
public class TabHomeAdapter extends BaseCustomAdapter<ItemGoods, ItemGoodsBinding> {
    private ArrayList<Integer> arrayChangedPosition;
    private int redColor;

    public TabHomeAdapter() {
        redColor = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        arrayChangedPosition = new ArrayList<>();
    }

    @Override
    public ItemGoodsBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemGoodsBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(ItemGoodsBinding binding, ItemGoods item, int position) {
        binding.setGoods(item);
        String tag = (String) binding.ivPic.getTag(binding.ivPic.getId());
        if (!item.icon.equals(tag)) {
            binding.ivPic.setTag(binding.ivPic.getId(), item.icon);
            ImageLoader.loadImage(binding.ivPic, item.icon);
        }

        setTime(binding, item, position);
    }

    private void setTime(final ItemGoodsBinding binding, ItemGoods item, int position) {
        binding.tvTime.cancel();
        if (item.status == 0) {
            binding.tvTime.finish();
            return;
        }

        binding.tvTime.setExpireTime(item.bid_expire_time);
        binding.tvTime.setRepeat(item.current_price == 0);
        if (arrayChangedPosition.contains(position)) {
            arrayChangedPosition.remove(Integer.valueOf(position));
            ValueAnimator animator = ObjectAnimator.ofFloat(binding.bgPrice, "alpha", 0, 1, 0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float alpha = (float) valueAnimator.getAnimatedValue();
                    if (1 - alpha < 0.2) {
                        binding.tvPrice.setTextColor(0xffffffff);
                    } else {
                        binding.tvPrice.setTextColor(redColor);
                    }
                }
            });
            animator.setDuration(1000).start();
        }
    }

    public void addChangedPosition(int position) {
        if (!arrayChangedPosition.contains(position)) {
            arrayChangedPosition.add(position);
        }
    }
}
