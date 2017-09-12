package com.happy.auction.main.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityForgetBinding;
import com.happy.auction.utils.Validation;

public class ForgetActivity extends BaseActivity {
    private ActivityForgetBinding binding;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
        binding.setActivity(this);
    }

    public void afterPhoneChanged(Editable s) {
        if (timer == null) {
            binding.btnCaptcha.setEnabled(validPhone());
        }
        binding.btnOK.setEnabled(checkValid());
    }

    public void afterTextChanged(Editable s) {
        binding.btnOK.setEnabled(checkValid());
    }

    private boolean validPhone() {
        return Validation.phone(binding.etPhone.getText());
    }

    private boolean validCaptcha() {
        return binding.etCaptcha.getText().length() > 3;
    }

    private boolean validPassword() {
        return Validation.password(binding.etPassword.getText());
    }

    private boolean validPasswordConfirm() {
        return Validation.password(binding.etPasswordConfirm.getText());
    }

    private boolean checkValid() {
        boolean valid = validPhone() && validCaptcha() && validPassword() && validPasswordConfirm();
        if (valid) {
            String password = binding.etPassword.getText().toString();
            String confirm = binding.etPasswordConfirm.getText().toString();
            return password.equals(confirm);
        }
        return false;
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

    public void onClickEye(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        if (selected) {
            binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onClickEyeConfirm(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        if (selected) {
            binding.etPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            binding.etPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onClickConfirm(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
