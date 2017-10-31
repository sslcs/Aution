package com.happy.auction.module.me;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityAboutBinding;

/**
 * 关于我们页面
 *
 * @author LiuCongshan
 */
public class AboutActivity extends BaseBackActivity {
    private ActivityAboutBinding mBinding;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        initLayout();
    }

    private void initLayout() {
        String data = getString(R.string.version, BuildConfig.VERSION_NAME);
        mBinding.setData(data);
    }
}
