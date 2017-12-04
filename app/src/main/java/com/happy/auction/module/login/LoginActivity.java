package com.happy.auction.module.login;

import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTabActivity;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.LoginParam;
import com.happy.auction.entity.param.UserInfoParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.LoadingDialog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.PreferenceUtil;
import com.happy.auction.utils.RxBus;

import java.lang.reflect.Type;

/**
 * 登录界面
 *
 * @author LiuCongshan
 */
public class LoginActivity extends BaseTabActivity {
    private static OnLoginListener mListener;
    private String phone;
    private LoadingDialog mLoadingDialog;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), LoginActivity.class);
    }

    public static Intent newIntent(OnLoginListener listener) {
        mListener = listener;
        return new Intent(AppInstance.getInstance(), LoginActivity.class);
    }

    @Override
    protected void initLayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(CaptchaLoginFragment.newInstance());
        adapter.add(PasswordLoginFragment.newInstance());
        adapter.setTitles(new String[]{getString(R.string.captcha_login), getString(R.string.password_login)});
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        if (!PreferenceUtil.loginCaptcha()) {
            mBinding.viewPager.setCurrentItem(1);
        }
    }

    private void getUserInfo() {
        UserInfoParam param = new UserInfoParam();
        BaseRequest<UserInfoParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mLoadingDialog.dismiss();
                Type type = new TypeToken<DataResponse<UserInfo>>() {}.getType();
                DataResponse<UserInfo> obj = GsonSingleton.get().fromJson(response, type);
                AppInstance.getInstance().setUser(obj.data);
                RxBus.getDefault().post(obj.data);

                if (obj.data == null) {
                    return;
                }
                setResult(RESULT_OK);
                finish();
                if (obj.data.noPassword() && PreferenceUtil.showSetPassword(obj.data.phone)) {
                    startActivity(SetPasswordActivity.newIntent());
                } else if (mListener != null) {
                    mListener.onLogin();
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                mLoadingDialog.dismiss();
            }
        });
    }

    public void login(LoginParam param) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
            mLoadingDialog.show(getSupportFragmentManager(), "loading");
        }
        PreferenceUtil.setLoginCaptcha(LoginParam.TYPE_CAPTCHA.equals(param.login_type));
        BaseRequest<LoginParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<LoginResponse>>() {}.getType();
                DataResponse<LoginResponse> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);
                getUserInfo();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                mLoadingDialog.dismiss();
            }
        });
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mListener = null;
    }

    public interface OnLoginListener {
        /**
         * 回调登录成功事件
         */
        void onLogin();
    }
}
