package com.happy.auction.module.login;


import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentCaptchaLoginBinding;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CaptchaParam;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.TimerButton;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.PreferenceUtil;
import com.happy.auction.utils.ToastUtil;
import com.happy.auction.utils.Validation;

/**
 * 免密登录
 *
 * @author LiuCongshan
 */
public class CaptchaLoginFragment extends BaseFragment {
    private FragmentCaptchaLoginBinding mBinding;

    public static CaptchaLoginFragment newInstance() {
        return new CaptchaLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mBinding = FragmentCaptchaLoginBinding.inflate(inflater, container, false);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        if (PreferenceUtil.showTipCaptcha()) {
            mBinding.tvTip.setVisibility(View.VISIBLE);
            mBinding.tvClose.setVisibility(View.VISIBLE);
        }

        mBinding.btnCaptcha.setOnFinishListener(new TimerButton.OnFinishListener() {
            @Override
            public void onFinish() {
                if (validPhone()) {
                    mBinding.btnCaptcha.setEnabled(true);
                }
            }
        });
        mBinding.setFragment(this);
    }

    public void afterPhoneChanged(Editable s) {
        LoginActivity parent = (LoginActivity) getActivity();
        parent.setPhone(s.toString());

        if (validPhone()) {
            mBinding.btnCaptcha.setEnabled(true);
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
        mBinding.btnCaptcha.start();

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
        ((LoginActivity)getActivity()).login(param);
    }

    public void onClickClose(View view) {
        PreferenceUtil.closeTipCaptcha();
        mBinding.tvClose.setVisibility(View.GONE);
        mBinding.tvTip.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!hasCreatedView || !isVisibleToUser) {
            return;
        }

        LoginActivity parent = (LoginActivity) getActivity();
        mBinding.etPhone.setText(parent.getPhone());
        Editable text = mBinding.etPhone.getText();
        mBinding.etPhone.setSelection(text.length());
        EventAgent.onEvent(R.string.login_quickpass);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.btnCaptcha.cancel();
    }
}
