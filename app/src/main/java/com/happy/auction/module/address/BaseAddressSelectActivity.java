package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivitySelectAddressBinding;

/**
 * Created by LiuCongshan on 17-10-18.<br/>
 * 选择收货地址或联系人界面基类
 */
public abstract class BaseAddressSelectActivity extends BaseBackActivity {
    private final static int REQUEST_CODE = 100;
    protected ActivitySelectAddressBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        initLayout();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace(10));

        initChildLayout();
        loadData();
    }

    /**
     * 初始化子布局
     */
    protected abstract void initChildLayout();

    /**
     * 加载数据
     */
    protected abstract void loadData();

    public void onClickConfirm(View view) {
        Intent intent = new Intent();
        putIntentData(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            loadData();
        }
    }

    public void onClickManage(View view) {
        startActivityForResult(getManageIntent(), REQUEST_CODE);
    }

    /**
     * 获得管理地址Activity的intent
     *
     * @return intent for Manage activity
     */
    protected abstract Intent getManageIntent();

    /**
     * put收货地址或联系人
     *
     * @param intent 承载数据的intent
     */
    protected abstract void putIntentData(Intent intent);
}
