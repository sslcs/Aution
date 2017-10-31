package com.happy.auction.module.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityForgetBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CaptchaParam;
import com.happy.auction.entity.param.FindPasswordParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.TimerButton;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;
import com.happy.auction.utils.Validation;

import java.lang.reflect.Type;

/**
 * 忘记密码页面
 *
 * @author LiuCongshan
 */
public class ForgetActivity extends BaseBackActivity {
    private ActivityForgetBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        initLayout();
    }

    private void initLayout() {
        mBinding.setActivity(this);
        mBinding.btnCaptcha.setOnFinishListener(new TimerButton.OnFinishListener() {
            @Override
            public void onFinish() {
                if (validPhone()) {
                    mBinding.btnCaptcha.setEnabled(true);
                }
            }
        });
    }

    public void afterPhoneChanged(Editable s) {
        mBinding.btnCaptcha.setEnabled(validPhone());
        mBinding.btnOK.setEnabled(checkValid());
    }

    public void afterTextChanged(Editable s) {
        mBinding.btnOK.setEnabled(checkValid());
    }

    private boolean validPhone() {
        return Validation.phone(mBinding.etPhone.getText());
    }

    private boolean validCaptcha() {
        return mBinding.etCaptcha.getText().length() > 3;
    }

    private boolean validPassword() {
        return Validation.password(mBinding.etPassword.getText());
    }

    private boolean checkValid() {
        boolean valid = validPhone() && validCaptcha() && validPassword();
        if (valid) {
            String password = mBinding.etPassword.getText().toString();
            String confirm = mBinding.etPasswordConfirm.getText().toString();
            return password.equals(confirm);
        }
        return false;
    }

    public void onClickCaptcha(View view) {
        mBinding.btnCaptcha.setEnabled(false);
        mBinding.btnCaptcha.start();

        CaptchaParam param = new CaptchaParam();
        param.forgetPwd = 1;
        param.phone = mBinding.etPhone.getText().toString();
        BaseRequest<CaptchaParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                ToastUtil.show(message);
            }
        });
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

    public void onClickConfirm(View view) {
        FindPasswordParam param = new FindPasswordParam();
        param.phone = mBinding.etPhone.getText().toString();
        param.code = mBinding.etCaptcha.getText().toString();
        param.pwd = mBinding.etPassword.getText().toString();
        BaseRequest<FindPasswordParam> request = new BaseRequest<>(param);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.btnCaptcha.cancel();
    }
}
