package com.happy.auction.module.me;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivitySettingBinding;
import com.happy.auction.entity.event.LogoutEvent;
import com.happy.auction.module.main.WebActivity;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.StringUtil;

/**
 * 设置页面
 *
 * @author LiuCongshan
 */
public class SettingActivity extends BaseBackActivity {
    private ActivitySettingBinding mBinding;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        initLayout();
    }

    private void initLayout() {
        mBinding.setUser(AppInstance.getInstance().getUser());
        if (BuildConfig.DEBUG) {
            mBinding.tvServer.setVisibility(View.VISIBLE);
        }
        mBinding.tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
    }

    public void onClickServiceProtocol(View view) {
        EventAgent.onEvent(R.string.setting_service_agreement);
        String title = getString(R.string.service_protocol);
        startActivity(WebActivity.newIntent(title, StringUtil.URL_SERVICE_PROTOCOL));
    }

    public void onClickAboutUs(View view) {
        EventAgent.onEvent(R.string.setting_about);
        startActivity(AboutActivity.newIntent());
    }

    public void onClickLogout(View view) {
        EventAgent.onEvent(R.string.setting_logout);
        new CustomDialog.Builder()
                .content(getString(R.string.tip_logout))
                .textLeft(getString(R.string.cancel))
                .textRight(getString(R.string.ok))
                .setOnClickRightListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        AppInstance.getInstance().logout();
                        RxBus.getDefault().post(new LogoutEvent());
                        finish();
                    }
                })
                .show(getSupportFragmentManager(), "Logout");
    }

    public void onClickServer(View view) {
        startActivity(new Intent("com.happy.DebugActivity"));
    }

    public void onClickCache(View view) {
        DataCleanManager.clearAllCache(this);
        mBinding.tvCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
    }
}
