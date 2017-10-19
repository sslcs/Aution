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
import com.happy.auction.module.address.AddressActivity;
import com.happy.auction.module.address.ContactActivity;
import com.happy.auction.module.login.ChangePasswordActivity;

public class ManagerActivity extends BaseActivity {
    private static final int REQUEST_USER_INFO = 100;
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

    public void onClickPassword(View view) {startActivity(ChangePasswordActivity.newIntent());}

    public void onClickAddress(View view) {
        startActivity(AddressActivity.newIntent());
    }

    public void onClickContact(View view) {
        startActivity(ContactActivity.newIntent());
    }

    public void onClickUsername(View view) {
        startActivityForResult(UsernameActivity.newIntent(this), REQUEST_USER_INFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_USER_INFO == requestCode) {
            mBinding.setUser(AppInstance.getInstance().getUser());
        }
    }
}
