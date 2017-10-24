package com.happy.auction.module.login;

import android.app.Activity;
import android.content.Intent;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTabActivity;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.utils.RxBus;

import io.reactivex.functions.Consumer;

/**
 * 登录界面
 *
 * @author cs
 */
public class LoginActivity extends BaseTabActivity {
    private String phone;

    public static Intent newIntent() {
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

        RxBus.getDefault().subscribe(this, LoginResponse.class, new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) throws Exception {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
