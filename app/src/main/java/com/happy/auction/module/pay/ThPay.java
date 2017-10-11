package com.happy.auction.module.pay;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.happy.auction.AppInstance;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.FileUtils;
import com.happy.auction.utils.PackageUtils;
import com.happy.auction.utils.ToastUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by LiuCongshan on 17-10-11.
 * 天宏支付
 */

public class ThPay {
    private final static String pkgName = "com.sankuai.meituan.app.pay";
    private final static String activityName = pkgName + ".PayActivity";
    private static ThPay thPay;
    private WeakReference<Context> contextWeakReference;
    private WeakReference<FragmentManager> fragmentManagerWeakReference;

    private ThPay(FragmentManager fragmentManager, Context context) {
        this.contextWeakReference = new WeakReference<>(context);
        fragmentManagerWeakReference = new WeakReference<>(fragmentManager);
    }

    public static ThPay getInstance(FragmentManager fragmentManager, Context context) {
        synchronized (ThPay.class) {
            if (thPay == null) {
                thPay = new ThPay(fragmentManager, context);
            }
            return thPay;
        }
    }

    private void showInstallPayPluginDialog(String content) {
        if (fragmentManagerWeakReference.get() == null) return;
        new CustomDialog.Builder()
                .textLeft("取消")
                .textRight("确定")
                .content(content)
                .setOnClickRightListener(new CustomDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogFragment dialog) {
                        dialog.dismiss();
                        try {
                            FileUtils.installAllAssetsApk(contextWeakReference.get());
                        } catch (IOException e) {
                            ToastUtil.show("安装失败！");
                        }
                    }
                })
                .show(fragmentManagerWeakReference.get(), "install");
    }

    public void pay(String tokeId, String appId) {
        if (checkInstall()) {
            try {
                Intent launchIntent = new Intent();
                launchIntent.setClassName(pkgName, activityName);
                launchIntent.putExtra("tokeId", tokeId);
                launchIntent.putExtra("appId", appId);
                launchIntent.putExtra("channel", AppInstance.getInstance().getChannel());
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                contextWeakReference.get().startActivity(launchIntent);
            } catch (ActivityNotFoundException ignored) {
            }
        }
    }

    private boolean checkInstall() {
        if (contextWeakReference.get() == null) return false;
        if (!PackageUtils.isInstalled(contextWeakReference.get(), pkgName)) {
            showInstallPayPluginDialog("需安装微信支付组件，安装后将能使用微信支付");
            return false;
        } else if (PackageUtils.getAppVersionCode(contextWeakReference.get(), pkgName) < 4) {
            showInstallPayPluginDialog("需要更新支付组件，更新后将能使用微信支付");
            return false;
        }
        return true;
    }
}
