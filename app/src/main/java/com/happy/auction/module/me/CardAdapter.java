package com.happy.auction.module.me;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.CustomViewHolder;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.EmptyViewBinding;
import com.happy.auction.databinding.ItemCardBinding;
import com.happy.auction.entity.event.BidNowEvent;
import com.happy.auction.entity.item.ItemCard;
import com.happy.auction.entity.item.ItemCardPassword;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.module.MainActivity;
import com.happy.auction.utils.RxBus;

/**
 * 我的卡密adapter
 *
 * @author LiuCongshan
 * @date 17-10-20
 */

public class CardAdapter extends BaseCustomAdapter<ItemCard, ItemCardBinding> {
    private OnSelectionChangeListener mListener;

    @Override
    public CustomViewHolder getBindingEmpty(ViewGroup parent, LayoutInflater inflater) {
        EmptyViewBinding binding = EmptyViewBinding.inflate(inflater, parent, false);
        binding.ivImage.setImageResource(R.drawable.ic_empty_card);
        binding.btnBid.setVisibility(View.VISIBLE);
        binding.btnBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBus.getDefault().post(new BidNowEvent());
            }
        });
        return new CustomViewHolder<>(binding);
    }

    @Override
    public ItemCardBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCardBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemCardBinding binding, final ItemCard item, final int position) {
        binding.setData(item);
        binding.ivCheck.setSelected(item.isSelected);

        String tag = (String) binding.ivIcon.getTag(binding.ivIcon.getId());
        if (!item.icon.equals(tag)) {
            binding.ivIcon.setTag(binding.ivIcon.getId(), item.icon);
            ImageLoader.displayImage(binding.ivIcon, item.icon);
        }

        binding.vList.setLayoutManager(new LinearLayoutManager(AppInstance.getInstance()));
        final CardPasswordAdapter mAdapter = new CardPasswordAdapter();
        for (ItemCardPassword p : item.card) {
            p.isSelected = item.isSelected;
        }
        mAdapter.addAll(item.card);
        mAdapter.setOnSelectionChangedListener(new CardPasswordAdapter.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(ItemCardPassword child, boolean selected) {
                boolean checkParent = true;
                for (ItemCardPassword itemChild : item.card) {
                    if (!itemChild.isSelected) {
                        checkParent = false;
                        break;
                    }
                }
                item.isSelected = checkParent;
                binding.ivCheck.setSelected(item.isSelected);
                if (mListener == null) {
                    return;
                }
                mListener.onChildSelectionChanged(child, selected);
            }
        });
        binding.vList.setAdapter(mAdapter);

        binding.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.isSelected = !item.isSelected;
                onClickCheck(item, item.isSelected);
                view.setSelected(item.isSelected);
                mAdapter.notifyDataSetChanged();
            }
        });

        setOnItemClickListener(new OnItemClickListener<ItemCard>() {
            @Override
            public void onItemClick(View view, ItemCard item, int position) {
                item.isExpand = !item.isExpand;
                notifyItemChanged(position);
            }
        });
    }

    private void onClickCheck(ItemCard parent, boolean selected) {
        if (mListener == null) {
            return;
        }

        mListener.onSelectionChanged(parent, selected);
        for (ItemCardPassword item : parent.card) {
            if (item.isSelected != selected) {
                item.isSelected = selected;
                mListener.onChildSelectionChanged(item, selected);
            }
        }
    }

    public void setOnSelectionChangedListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    public interface OnSelectionChangeListener {
        /**
         * 选中事件改变
         *
         * @param item     卡条目
         * @param selected 是否选中
         */
        void onSelectionChanged(ItemCard item, boolean selected);

        /**
         * 选中事件改变
         *
         * @param item     卡密条目
         * @param selected 是否选中
         */
        void onChildSelectionChanged(ItemCardPassword item, boolean selected);
    }
}
