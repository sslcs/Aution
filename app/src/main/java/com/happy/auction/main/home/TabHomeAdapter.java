package com.happy.auction.main.home;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.utils.DebugLog;

import java.util.Locale;

/**
 * 首页商品Adapter
 */
public class TabHomeAdapter extends BaseAdapter<ItemGoods> {
    private int animatePosition = -1;
    private int redColor;

    public TabHomeAdapter() {
        redColor = AppInstance.getInstance().getResources().getColor(R.color.main_red);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodsBinding binding = ItemGoodsBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new CustomViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.CustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ItemGoods goods = getItem(position);
        final ItemGoodsBinding binding = (ItemGoodsBinding) holder.getBinding();

        if (binding.getRoot().getTag() != null) {
            CustomTimer timer = (CustomTimer) binding.getRoot().getTag();
            timer.cancel();
        }

        binding.setGoods(goods);
        if (goods.status == 0) {
            binding.tvTime.setText(R.string.auction_finish);
        } else {
            long left = goods.bid_expire_time - System.currentTimeMillis();
            if (left > 0 && holder.getAdapterPosition() == 1) {
                binding.getRoot().setTag(new CustomTimer(left, binding).start());
                setTimeLeft(binding, left / 1000);
            } else {
                binding.tvTime.setText("00:00:00");
            }
        }

        if (position == animatePosition) {
            animatePosition = -1;
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

    public void setAnimatePosition(int position) {
        animatePosition = position;
    }

    private void setTimeLeft(ItemGoodsBinding binding, long time) {
        binding.tvTime.setText(String.format(Locale.CHINA, "00:00:%02d", time));
    }

    class CustomTimer extends CountDownTimer {
        private ItemGoodsBinding binding;

        private CustomTimer(ItemGoodsBinding binding) {
            this(10100, binding);
        }

        public CustomTimer(long millisInFuture, ItemGoodsBinding binding) {
            super(millisInFuture + 100, 1000);
            this.binding = binding;
        }

        @Override
        public void onTick(long l) {
            setTimeLeft(binding, l / 1000);
        }

        @Override
        public void onFinish() {
            if (binding.getGoods().current_price == 0) {
                binding.tvTime.setText("00:00:10");
                binding.getGoods().bid_expire_time = System.currentTimeMillis()+10000;
                binding.getRoot().setTag(new CustomTimer(binding).start());
            } else {
                binding.tvTime.setText("00:00:00");
            }
        }
    }
}
