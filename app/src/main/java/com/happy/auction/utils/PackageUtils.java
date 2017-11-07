package com.happy.auction.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.happy.auction.AppInstance;

import java.io.File;
import java.util.List;

/**
 * PackageUtils
 * <ul>
 * <strong>Uninstall package</strong>
 * <li>{@link PackageUtils#uninstallNormal(Context, String)}</li>
 * </ul>
 * <ul>
 * <strong>Others</strong>
 * <li>{@link PackageUtils#isTopActivity(Context, String)} whether the app whost package's name is packageName is on the
 * top of the stack</li>
 * </ul>
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-15
 */
public class PackageUtils {
    public static boolean isInstalledWXClient(Context context) {
        return PackageUtils.isInstalled(context, Uri.parse("weixin://"));
    }

    public static boolean isInstalledAlipay(Context context) {
        return PackageUtils.isInstalled(context, Uri.parse("alipays://platformapi/startApp"));
    }

    public static boolean isInstalledQQ(Context context) {
        return PackageUtils.isInstalled(context, Uri.parse("mqqwpa://"));
    }

    public static boolean isInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static boolean isInstalled(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    /**
     * install package normal by system intent
     *
     * @param filePath file path of package
     * @return whether apk exist
     */
    public static boolean installNormal(String filePath) {
        if (TextUtils.isEmpty(filePath)) return false;

        File file = new File(filePath);
        if (!file.exists() || !file.isFile() || file.length() <= 0) {
            return false;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppInstance.getInstance().startActivity(i);
        return true;
    }

    /**
     * uninstall package normal by system intent
     *
     * @param context
     * @param packageName package name of app
     * @return whether package name is empty
     */
    public static boolean uninstallNormal(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }

        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse(new StringBuilder(32).append("package:")
                .append(packageName).toString()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }

    /**
     * whether the app whose package's name is packageName is on the top of the stack
     * <ul>
     * <strong>Attentions:</strong>
     * <li>You should addFragment <strong>android.permission.GET_TASKS</strong> in manifest</li>
     * </ul>
     *
     * @param context
     * @param packageName
     * @return if params error or task stack is null, return null, otherwise retun whether the app is on the top of
     * stack
     */
    public static Boolean isTopActivity(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo == null || tasksInfo.size() == 0) {
            return null;
        }
        try {
            return packageName.equals(tasksInfo.get(0).topActivity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get app version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * get app version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context, String pkgName) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(pkgName, 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }
}