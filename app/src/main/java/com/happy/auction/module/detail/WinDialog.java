package com.happy.auction.module.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.base.BaseDialog;
import com.happy.auction.databinding.DialogWinBinding;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.module.order.OrderActivity;

/**
 * 中奖弹框
 *
 * @author LiuCongshan
 * @date 17-11-1
 */

public class WinDialog extends BaseDialog {
    private DialogWinBinding mBinding;

    private BaseGoods mData;

    public void setData(BaseGoods data) {
        mData = data;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DialogWinBinding.inflate(inflater, container, false);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.setData(mData);
        mBinding.setFragment(this);
    }

    public void onClickClose(View view) {
        dismissAllowingStateLoss();
    }

    public void onClickDetail(View view) {
        dismissAllowingStateLoss();
        startActivity(OrderActivity.newIntent(2));
    }
}
