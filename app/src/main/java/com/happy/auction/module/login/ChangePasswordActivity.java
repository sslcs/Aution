package com.happy.auction.module.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityChangePasswordBinding;
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

public class ChangePasswordActivity extends BaseActivity {
    private ActivityChangePasswordBinding mBinding;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), ChangePasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        mBinding.setActivity(this);
    }

    public void onClickConfirm(View view) {
        ChangePasswordParam param = new ChangePasswordParam();
        param.old_pwd = mBinding.etPasswordOld.getText().toString();
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

    private boolean validPassword() {
        return Validation.password(mBinding.etPassword.getText());
    }

    private boolean validPasswordOld() {
        return Validation.password(mBinding.etPasswordOld.getText());
    }

    private boolean checkValid() {
        String old = mBinding.etPasswordOld.getText().toString();
        if (TextUtils.isEmpty(old)) {
            String password = mBinding.etPassword.getText().toString();
            String confirm = mBinding.etPasswordConfirm.getText().toString();
            return password.equals(confirm);
        }

        boolean valid = validPasswordOld() && validPassword();
        if (valid) {
            String password = mBinding.etPassword.getText().toString();
            String confirm = mBinding.etPasswordConfirm.getText().toString();
            return password.equals(confirm) && !password.equals(old);
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

    public void onClickEyeOld(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        if (selected) {
            mBinding.etPasswordOld.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            mBinding.etPasswordOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }
}
