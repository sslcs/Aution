package com.happy.auction.debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.module.main.SplashActivity;
import com.happy.auction.utils.PreferenceUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 测试环境服务器选择界面
 *
 * @author LiuCongshan
 * @date 17-11-9
 */

public class DebugActivity extends BaseBackActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }

    public void onClickDebug(View view) {
        PreferenceUtil.setHost(BuildConfig.HOST);
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        restart();
                    }
                });
    }

    public void onClickRelease(View view) {
        PreferenceUtil.setHost(BuildConfig.HOST_RELEASE);
        Observable.timer(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        restart();
                    }
                });
    }

    private void restart() {
        Intent intent = new Intent(DebugActivity.this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
