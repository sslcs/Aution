package com.happy.auction.main.home;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.ItemGoodsBinding;
import com.happy.auction.entity.item.ItemGoods;

import java.util.Locale;

/**
 * 首页商品Adapter
 */
public class TabHomeAdapter extends BaseAdapter<ItemGoods> {
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
            if (left > 0) {
                binding.getRoot().setTag(new CustomTimer(left, binding).start());
                left = left / 1000;
                binding.tvTime.setText(String.format(Locale.CHINA, "00:00:%02d", left));
            } else {
                binding.tvTime.setText("00:00:00");
            }
        }


    }

    class CustomTimer extends CountDownTimer {
        private ItemGoodsBinding binding;

        public CustomTimer(long millisInFuture, ItemGoodsBinding binding) {
            super(millisInFuture, 1000);
            this.binding = binding;
        }

        @Override
        public void onTick(long l) {
            binding.tvTime.setText(String.format(Locale.CHINA, "00:00:%02d", l / 1000));
        }

        @Override
        public void onFinish() {
            binding.tvTime.setText("00:00:00");
        }
    }
}
