package com.happy.auction.module.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.happy.auction.BuildConfig;
import com.happy.auction.base.BasePageActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 启动页
 *
 * @author LiuCongshan
 */

public class SplashActivity extends BasePageActivity {
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = Manifest.permission.READ_PHONE_STATE;
            if (!shouldShowRequestPermissionRationale(permission)) {
                return true;
            }

            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, 100);
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUM();

        if (checkPermission()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initUM() {
        MobclickAgent.openActivityDurationTrack(false);
        String key = "59f67ae5734be42684000065";
        String channel = "0";
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this, key, channel));
        MobclickAgent.enableEncrypt(true);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
