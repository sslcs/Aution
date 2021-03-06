package com.happy.auction.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.base.BaseDialog;
import com.happy.auction.databinding.DialogLoadingBinding;

/**
 * 加载中弹框
 *
 * @author LiuCongshan
 * @date 17-10-31
 */

public class LoadingDialog extends BaseDialog {
    private DialogLoadingBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setCancelable(false);
        mBinding = DialogLoadingBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }
}
