package com.happy.auction.module.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentPasswordLoginBinding;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.Validation;

/**
 * 密码登录
 * @author LiuCongshan
 */
public class PasswordLoginFragment extends BaseFragment {
    private FragmentPasswordLoginBinding binding;

    public static PasswordLoginFragment newInstance() {
        return new PasswordLoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        binding = FragmentPasswordLoginBinding.inflate(inflater);
        binding.setFragment(this);
        return binding.getRoot();
    }

    public void afterPhoneChanged(Editable s) {
        LoginActivity parent = (LoginActivity) getActivity();
        parent.setPhone(s.toString());

        binding.btnOK.setEnabled(validPhone() && validPassword());
    }

    public void afterPasswordChanged(Editable s) {
        binding.btnOK.setEnabled(validPhone() && validPassword());
    }

    private boolean validPhone() {
        return Validation.phone(binding.etPhone.getText());
    }

    private boolean validPassword() {
        return Validation.password(binding.etPassword.getText());
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

    public void onClickLogin(View view) {
        LoginParam param = new LoginParam();
        param.phone = binding.etPhone.getText().toString();
        param.pwd = binding.etPassword.getText().toString();
        param.login_type = LoginParam.TYPE_PASSWORD;
        ((LoginActivity)getActivity()).login(param);
    }

    public void onClickForget(View view) {
        Intent forget = new Intent(getActivity(), ForgetActivity.class);
        startActivityForResult(forget, 100);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!hasCreatedView || !isVisibleToUser) {
            return;
        }

        LoginActivity parent = (LoginActivity) getActivity();
        binding.etPhone.setText(parent.getPhone());
        Editable text = binding.etPhone.getText();
        binding.etPhone.setSelection(text.length());
        EventAgent.onEvent(R.string.login_password);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            onLoginSuccess();
        }
    }

    public void onLoginSuccess(){
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
