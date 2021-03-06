package com.happy.auction.module.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.glide.ImageLoader;

import java.util.ArrayList;

/**
 * 首页商品Adapter
 *
 * @author LiuCongshan
 */
public class TabHomeAdapter extends BaseCustomAdapter<ItemGoods, ItemGoodsBinding> {
    private ArrayList<Integer> arrayChangedPosition;
    private int redColor;

    public TabHomeAdapter() {
        redColor = AppInstance.getInstance().getResources().getColor(R.color.main_red);
        arrayChangedPosition = new ArrayList<>();
    }

    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.tvAdd.setText(R.string.tip_empty_data);
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemGoodsBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemGoodsBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemGoodsBinding binding, ItemGoods item, int position) {
        binding.setGoods(item);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.getRoot().performClick();
            }
        });
        String tag = (String) binding.ivPic.getTag(binding.ivPic.getId());
        if (!item.icon.equals(tag)) {
            binding.ivPic.setTag(binding.ivPic.getId(), item.icon);
            ImageLoader.displayImage(binding.ivPic, item.icon);
        }

        setTime(binding, item, position);
    }

    private void setTime(final ItemGoodsBinding binding, ItemGoods item, int position) {
        binding.tvTime.cancel();
        if (item.status == 0) {
            binding.tvTime.finish();
            return;
        }

        binding.tvTime.setExpireTime(item.countdown);
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
