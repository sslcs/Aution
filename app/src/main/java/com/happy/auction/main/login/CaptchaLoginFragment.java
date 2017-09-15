package com.happy.auction.main.login;


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
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.param.CaptchaParam;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.net.ResponseHandler;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;
import com.happy.auction.utils.Validation;

import java.lang.reflect.Type;

/**
 * 免密登录
 */
public class CaptchaLoginFragment extends BaseFragment {
    private FragmentCaptchaLoginBinding binding;
    private CountDownTimer timer;

    public CaptchaLoginFragment() {
        // Required empty public constructor
    }

    public static CaptchaLoginFragment newInstance() {
        return new CaptchaLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        binding = FragmentCaptchaLoginBinding.inflate(inflater);
        binding.setFragment(this);
        return binding.getRoot();
    }

    public void afterPhoneChanged(Editable s) {
        LoginActivity parent = (LoginActivity) getActivity();
        parent.setPhone(s.toString());

        if (validPhone()) {
            if (timer == null) {
                binding.btnCaptcha.setEnabled(true);
            }

            if (validCaptcha()) {
                binding.btnOK.setEnabled(true);
            }
        } else {
            binding.btnCaptcha.setEnabled(false);
            binding.btnOK.setEnabled(false);
        }
    }

    public void afterCaptchaChanged(Editable s) {
        binding.btnOK.setEnabled(validCaptcha() && validPhone());
    }

    private boolean validPhone() {
        return Validation.phone(binding.etPhone.getText());
    }

    private boolean validCaptcha() {
        return binding.etCaptcha.getText().length() > 3;
    }

    public void onClickCaptcha(View view) {
        binding.btnCaptcha.setEnabled(false);
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                binding.btnCaptcha.setText((int) (l / 1000) + "s");
            }

            @Override
            public void onFinish() {
                binding.btnCaptcha.setText(R.string.get_captcha);
                if (validPhone()) {
                    binding.btnCaptcha.setEnabled(true);
                }
                timer = null;
            }
        }.start();

        CaptchaParam param = new CaptchaParam();
        param.forgetPwd = 3;
        param.phone = binding.etPhone.getText().toString();

        BaseRequest<CaptchaParam> request = new BaseRequest<>(param);
        RequestEvent event = new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
            }

            @Override
            public void onSuccess(String response, String message) {
                ToastUtil.show(message);
            }
        });
        RxBus.getDefault().post(event);
    }

    public void onClickLogin(View view) {
        LoginParam param = new LoginParam();
        param.phone = binding.etPhone.getText().toString();
        param.code = binding.etCaptcha.getText().toString();
        param.login_type = LoginParam.TYPE_CAPTCHA;

        BaseRequest<LoginParam> request = new BaseRequest<>(param);
        RequestEvent event = new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<LoginResponse>>() {}.getType();
                DataResponse<LoginResponse> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);
            }
        });
        RxBus.getDefault().post(event);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!hasCreatedView || !isVisibleToUser) return;

        LoginActivity parent = (LoginActivity) getActivity();
        binding.etPhone.setText(parent.getPhone());
        Editable text = binding.etPhone.getText();
        binding.etPhone.setSelection(text.length());
    }
}
