package com.happy.auction.module.address;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityContactEditBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ContactAddParam;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.ToastUtil;

/**
 * Created by LiuCongshan on 17-10-17.<br/>
 * 联系人编辑界面
 */

public class ContactEditActivity extends BaseActivity {
    private ActivityContactEditBinding mBinding;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ContactEditActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_edit);

        initLayout();
    }

    private void initLayout() {

    }

    public void onClickStore(View view) {
        ContactAddParam param = new ContactAddParam();
        param.remark = mBinding.etUsername.getText().toString().trim();
        param.phone = mBinding.etPhone.getText().toString().trim();
        BaseRequest<ContactAddParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
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
