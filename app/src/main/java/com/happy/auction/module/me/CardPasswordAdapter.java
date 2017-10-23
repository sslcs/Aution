package com.happy.auction.module.me;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.adapter.BaseCustomAdapter;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.ItemCardPasswordBinding;
import com.happy.auction.entity.item.ItemCardPassword;

/**
 * 我的卡密adapter  <br/>
 * Created by LiuCongshan on 17-10-23.
 *
 * @author LiuCongshan
 */

public class CardPasswordAdapter extends BaseCustomAdapter<ItemCardPassword, ItemCardPasswordBinding> {
    private OnSelectionChangeListener mListener;

    @Override
    public ItemCardPasswordBinding getBinding(ViewGroup parent, LayoutInflater inflater) {
        return ItemCardPasswordBinding.inflate(inflater, parent, false);
    }

    @Override
    public void bindItem(final ItemCardPasswordBinding binding, final ItemCardPassword item, final int position) {
        binding.setData(item);
        binding.ivCheck.setSelected(item.isSelected);
        binding.ivEye.setSelected(!item.isHidden);
        if (item.isHidden) {
            binding.tvPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            binding.tvPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }

        binding.ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.isHidden = !item.isHidden;
                notifyItemChanged(position);
            }
        });

        if (mListener == null) {
            return;
        }

        setOnItemClickListener(new OnItemClickListener<ItemCardPassword>() {
            @Override
            public void onItemClick(View view, ItemCardPassword item, int position) {
                item.isSelected = !item.isSelected;
                notifyItemChanged(position);
                mListener.onSelectionChanged(item, item.isSelected);
            }
        });
    }

    public void setOnSelectionChangedListener(OnSelectionChangeListener listener) {
        mListener = listener;
    }

    public interface OnSelectionChangeListener {
        /**
         * 选中事件改变
         *
         * @param item     卡密
         * @param selected 是否选中
         */
        void onSelectionChanged(ItemCardPassword item, boolean selected);
    }
}
