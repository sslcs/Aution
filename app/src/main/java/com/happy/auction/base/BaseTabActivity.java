package com.happy.auction.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.databinding.ActivityTabPagerBinding;

/**
 * TabActivity基类
 *
 * @author LiuCongshan
 */
public abstract class BaseTabActivity extends BaseTimeActivity {
    protected static final String KEY_SELECTION = "selection";
    protected ActivityTabPagerBinding mBinding;

    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tab_pager);

        initLayout();
    }

    /**
     * 初始化界面
     */
    protected abstract void initLayout();
}
