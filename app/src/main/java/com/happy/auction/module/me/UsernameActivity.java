package com.happy.auction.module.me;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityUsernameBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.UpdateUserInfoParam;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.ToastUtil;

/**
 * 修改用户名界面
 *
 * @author LiuCongshan
 */
public class UsernameActivity extends BaseBackActivity {
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

    public void onClickStore(View view) {
        final String username = mBinding.etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) return;
        UpdateUserInfoParam param = new UpdateUserInfoParam();
        param.username = username;
        BaseRequest<UpdateUserInfoParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                AppInstance.getInstance().setUsername(username);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }
}
