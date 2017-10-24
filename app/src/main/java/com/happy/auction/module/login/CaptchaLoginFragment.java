package com.happy.auction.module.login;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentCaptchaLoginBinding;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CaptchaParam;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;
import com.happy.auction.utils.Validation;

import java.lang.reflect.Type;

/**
 * 免密登录
 */
public class CaptchaLoginFragment extends BaseFragment {
    private FragmentCaptchaLoginBinding mBinding;
    private CountDownTimer mTimer;

    public CaptchaLoginFragment() {
        // Required empty public constructor
    }

    public static CaptchaLoginFragment newInstance() {
        return new CaptchaLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mBinding = FragmentCaptchaLoginBinding.inflate(inflater, container, false);
        mBinding.btnCaptcha.setTransformationMethod(null);
        mBinding.setFragment(this);
        return mBinding.getRoot();
    }

    public void afterPhoneChanged(Editable s) {
        LoginActivity parent = (LoginActivity) getActivity();
        parent.setPhone(s.toString());

        if (validPhone()) {
            if (mTimer == null) {
                mBinding.btnCaptcha.setEnabled(true);
            }

            if (validCaptcha()) {
                mBinding.btnOK.setEnabled(true);
            }
        } else {
            mBinding.btnCaptcha.setEnabled(false);
            mBinding.btnOK.setEnabled(false);
        }
    }

    public void afterCaptchaChanged(Editable s) {
        mBinding.btnOK.setEnabled(validCaptcha() && validPhone());
    }

    private boolean validPhone() {
        return Validation.phone(mBinding.etPhone.getText());
    }

    private boolean validCaptcha() {
        return mBinding.etCaptcha.getText().length() > 3;
    }

    public void onClickCaptcha(View view) {
        mBinding.btnCaptcha.setEnabled(false);
        mTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                mBinding.btnCaptcha.setText((int) (l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                mBinding.btnCaptcha.setText(R.string.get_captcha);
                if (validPhone()) {
                    mBinding.btnCaptcha.setEnabled(true);
                }
                mTimer = null;
            }
        }.start();

        CaptchaParam param = new CaptchaParam();
        param.forgetPwd = 3;
        param.phone = mBinding.etPhone.getText().toString();
        BaseRequest<CaptchaParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                ToastUtil.show(message);
            }
        });
    }

    public void onClickLogin(View view) {
        LoginParam param = new LoginParam();
        param.phone = mBinding.etPhone.getText().toString();
        param.code = mBinding.etCaptcha.getText().toString();
        param.login_type = LoginParam.TYPE_CAPTCHA;

        BaseRequest<LoginParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<LoginResponse>>() {}.getType();
                DataResponse<LoginResponse> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!hasCreatedView || !isVisibleToUser) return;

        LoginActivity parent = (LoginActivity) getActivity();
        mBinding.etPhone.setText(parent.getPhone());
        Editable text = mBinding.etPhone.getText();
        mBinding.etPhone.setSelection(text.length());
    }
}
