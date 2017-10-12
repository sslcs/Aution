package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityUsernameBinding;

public class UsernameActivity extends BaseActivity {
    private ActivityUsernameBinding mBinding;

    public static Intent newIntent(Context context) {
        return new Intent(context, UsernameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_username);
        initLayout();
    }

    private void initLayout() {
        mBinding.setUser(AppInstance.getInstance().getUser());
    }
}
