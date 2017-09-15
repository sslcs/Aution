package com.happy.auction.main.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentPasswordLoginBinding;
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.net.ResponseHandler;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.Validation;

import java.lang.reflect.Type;

/**
 * 密码登录
 */
public class PasswordLoginFragment extends BaseFragment {
    private FragmentPasswordLoginBinding binding;

    public PasswordLoginFragment() {
        // Required empty public constructor
    }

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

    public void onClickForget(View view) {
        Intent forget = new Intent(getActivity(), ForgetActivity.class);
        startActivityForResult(forget, 100);
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
