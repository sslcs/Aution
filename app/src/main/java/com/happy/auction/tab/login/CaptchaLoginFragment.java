package com.happy.auction.tab.login;


import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentCaptchaLoginBinding;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.Validation;

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
    }

    public void onClickLogin(View view) {
        DebugLog.e("onClick");
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

    public void onLoginSuccess() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
