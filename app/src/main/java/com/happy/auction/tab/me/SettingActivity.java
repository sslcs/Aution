package com.happy.auction.tab.me;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivitySettingBinding;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.RxBus;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        binding.setUser(AppInstance.getInstance().getUser());
    }

    public void onClickServiceContract(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAboutUs(View view) {
        DebugLog.e("onClick");
    }

    public void onClickLogout(View view) {
        new CustomDialog.Builder()
                .content(getString(R.string.tip_logout))
                .textLeft(getString(R.string.cancel))
                .textRight(getString(R.string.ok))
                .setOnClickRightListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        AppInstance.getInstance().setUser(null);
                        RxBus.getDefault().post(new LogoutEvent());
                        onBackPressed();
                    }
                })
                .show(getSupportFragmentManager(), "Logout");
    }
}
