package com.happy.auction.module.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BasePageActivity;
import com.happy.auction.databinding.ActivityWinBinding;
import com.happy.auction.entity.event.WinEvent;
import com.happy.auction.module.order.OrderActivity;

/**
 * 中奖弹框
 *
 * @author LiuCongshan
 * @date 17-11-8
 */

public class WinActivity extends BasePageActivity {
    private static final String KEY_DATA = "DATA";
    private ActivityWinBinding mBinding;

    public static Intent newIntent(WinEvent event) {
        Intent intent = new Intent(AppInstance.getInstance(), WinActivity.class);
        intent.putExtra(KEY_DATA, event);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_win);
        initLayout();
    }

    private void initLayout() {
        WinEvent event = (WinEvent) getIntent().getSerializableExtra(KEY_DATA);
        mBinding.setData(event);
    }

    public void onClickClose(View view) {
        finish();
    }

    public void onClickDetail(View view) {
        startActivity(OrderActivity.newIntent(2));
        finish();
    }
}
