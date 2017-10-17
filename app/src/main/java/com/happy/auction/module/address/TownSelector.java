package com.happy.auction.module.address;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aigestudio.wheelpicker.WheelPicker;
import com.happy.auction.databinding.FragmentTownSelectorBinding;

import java.util.ArrayList;

/**
 * Created by LiuCongshan on 17-10-17.
 * 乡镇地址选择器
 */

public class TownSelector extends BottomSheetDialogFragment {
    private AddressRecord mCurrentTown;

    private FragmentTownSelectorBinding mBinding;
    private OnSelectListener mListener;
    private ArrayList<AddressRecord> mData;

    public static TownSelector newInstance(ArrayList<AddressRecord> data, OnSelectListener listener) {
        TownSelector selector = new TownSelector();
        selector.mListener = listener;
        selector.mData = data;
        return selector;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentTownSelectorBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.setFragment(this);
        mBinding.townPicker.setData(mData);
        mBinding.townPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mCurrentTown = (AddressRecord) data;
            }
        });
    }

    public void onClickCancel(View view) {
        dismissAllowingStateLoss();
    }

    public void onClickSelect(View view) {
        dismissAllowingStateLoss();
        if (mListener != null) {
            mListener.onSelect(mCurrentTown.name);
        }
    }

    public interface OnSelectListener {
        /**
         * 选择完成
         *
         * @param town 乡镇
         */
        void onSelect(String town);
    }
}
