package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemBaskDetailBinding;
import com.happy.auction.entity.item.ItemBask;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.ui.LineTextView;
import com.happy.auction.utils.DebugLog;

import java.util.ArrayList;

/**
 * 详情晒单分享Adapter
 *
 * @author LiuCongshan
 */
public class BaskAdapter extends BaseCustomAdapter<ItemBask, ItemBaskDetailBinding> {
    private OnClickImageListener mListener;

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
    public void bindItem(final ItemBaskDetailBinding binding, final ItemBask item, final int position) {
        binding.tvContent.setTag(position);
        binding.tvContent.initStatus(item.expand.get());
        binding.setData(item);
        ImageLoader.displayImage(binding.ivImg, item.s_img.get(0));
        if (item.s_img.size() > 1) {
            binding.ivImg1.setVisibility(View.VISIBLE);
            ImageLoader.displayImage(binding.ivImg1, item.s_img.get(1));
        } else {
            binding.ivImg1.setVisibility(View.GONE);
        }

        if (mListener != null) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selection = (view.getId() == binding.ivImg1.getId() ? 1 : 0);
                    mListener.onClick(item.img, selection);
                }
            };
            binding.ivImg.setOnClickListener(listener);
            binding.ivImg1.setOnClickListener(listener);
        }

        binding.tvContent.setShowExpandListener(new LineTextView.ShowExpandListener() {
            @Override
            public void showExpand(boolean show) {
                item.show.set(show);
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.show.get()) {
                    item.expand.set(!item.expand.get());
                    binding.tvContent.expand(item.expand.get());
                    float rotation = item.expand.get() ? -90 : 90;
                    binding.ivExpand.animate().rotation(rotation);
                }
            }
        };
        binding.tvContent.setOnClickListener(listener);
        binding.tvExpand.setOnClickListener(listener);
        binding.ivExpand.setOnClickListener(listener);
    }

    public void setOnClickImageListener(OnClickImageListener listener) {
        mListener = listener;
    }

    public interface OnClickImageListener {
        /**
         * 点击图片事件
         *
         * @param img       图片地址
         * @param selection 选中位置
         */
        void onClick(ArrayList<String> img, int selection);
    }
}
