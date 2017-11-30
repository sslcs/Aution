package com.happy.auction.module.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemBaskDetailBinding;
import com.happy.auction.entity.item.ItemBask;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.ui.LineTextView;

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
        binding.tvContent.initStatus(item.expand.get());
        binding.setData(item);

        displayImage(binding, item);
        showContent(binding, item);
    }

    private void showContent(final ItemBaskDetailBinding binding, final ItemBask item) {
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

    private ImageView getImageView(ItemBaskDetailBinding binding, int position, boolean isMask) {
        if (position == 1) {
            return isMask ? binding.ivMask1 : binding.ivImg1;
        }

        if (position == 2) {
            return isMask ? binding.ivMask2 : binding.ivImg2;
        }

        return isMask ? binding.ivMask3 : binding.ivImg3;
    }

    private void displayImage(final ItemBaskDetailBinding binding, final ItemBask item) {
        ImageLoader.displayImage(binding.ivImg, item.s_img.get(0));
        View.OnClickListener listener = null;
        if (mListener != null) {
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selection = (view.getId() == binding.ivImg1.getId() ? 1 : 0);
                    mListener.onClick(item.img, selection);
                }
            };
            binding.ivImg.setOnClickListener(listener);
        }

        int size = item.s_img.size();
        for (int i = 1; i < 4; i++) {
            ImageView iv = getImageView(binding, i, false);
            ImageView ivMask = getImageView(binding, i, true);
            if (i < size) {
                ivMask.setVisibility(View.VISIBLE);
                iv.setVisibility(View.VISIBLE);
                ImageLoader.displayImage(iv, item.s_img.get(i));
                iv.setOnClickListener(listener);
            } else {
                ivMask.setVisibility(View.GONE);
                iv.setVisibility(View.GONE);
            }
        }
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
