package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityManagerBinding;

public class ManagerActivity extends BaseActivity {
    private ActivityManagerBinding mBinding;

    public static Intent newIntent(Context context) {
        return new Intent(context, ManagerActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_manager);
        initLayout();
    }

    private void initLayout() {
        mBinding.setUser(AppInstance.getInstance().getUser());
    }

    public void onClickAvatar(View view) {}

    public void onClickPassword(View view) {}

    public void onClickAddress(View view) {}

    public void onClickContact(View view) {}

    public void onClickUsername(View view) {
        startActivity(UsernameActivity.newIntent(this));
    }
}
