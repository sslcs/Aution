package com.happy.auction.module.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivitySetPasswordBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.ChangePasswordParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;
import com.happy.auction.utils.Validation;

import java.lang.reflect.Type;

/**
 * 设置密码界面
 *
 * @author LiuCongshan
 */
public class SetPasswordActivity extends BaseBackActivity {
    private ActivitySetPasswordBinding mBinding;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), SetPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_password);
        mBinding.setActivity(this);
    }

    public void onClickConfirm(View view) {
        ChangePasswordParam param = new ChangePasswordParam();
        param.pwd = mBinding.etPassword.getText().toString();
        BaseRequest<ChangePasswordParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<LoginResponse>>() {}.getType();
                DataResponse<LoginResponse> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);

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

    public void afterTextChanged(Editable s) {
        mBinding.btnOK.setEnabled(checkValid());
    }

    private boolean validPassword(String password) {
        return Validation.password(password);
    }

    private boolean checkValid() {
        String password = mBinding.etPassword.getText().toString();
        if (validPassword(password)) {
            String confirm = mBinding.etPasswordConfirm.getText().toString();
            return password.equals(confirm);
        }
        return false;
    }

    public void onClickEye(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        if (selected) {
            mBinding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mBinding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onClickEyeConfirm(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        if (selected) {
            mBinding.etPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mBinding.etPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onClickSkip(View view) {
        finish();
    }
}
