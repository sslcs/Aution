package com.happy.auction.module;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityWebBinding;
import com.happy.auction.module.me.BaskActivity;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.utils.DebugLog;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * WebView页面<br/>
 * Created by LiuCongshan on 17-10-23.
 *
 * @author LiuCongshan
 */

public class WebActivity extends BaseActivity {
    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String SCHEME_LOCAL = "hpjp://native/openLocal?";

    private ActivityWebBinding mBinding;

    public static Intent newIntent(String title, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        if (url.startsWith(SCHEME_LOCAL)) {
            return openLocal(url);
        } else {
            Intent intent = new Intent(AppInstance.getInstance(), WebActivity.class);
            intent.putExtra(KEY_TITLE, title);
            intent.putExtra(KEY_URL, url);
            return intent;
        }
    }

    private static Intent openLocal(String url) {
        String param = url.substring(24);
        HashMap<String, String> map = parseStringMap(param);
        String pageLink = map.get("page");
        if ("recharge".equalsIgnoreCase(pageLink)) {
            return ChargePayActivity.newIntent();
        } else if ("bask".equalsIgnoreCase(pageLink)) {
            return BaskActivity.newIntent();
        } else {
            return null;
        }
    }

    /**
     * 解析String参数串，输出参数数组
     *
     * @param param 参数串
     * @return 参数数组
     */
    private static HashMap<String, String> parseStringMap(String param) {
        HashMap<String, String> map = new HashMap<>(1);
        try {
            String[] arrayEntry = param.split("&");
            for (String entry : arrayEntry) {
                String[] parts = entry.split("=");
                map.put(parts[0], URLDecoder.decode(String.valueOf(parts[1]), "UTF-8"));
            }
        } catch (Exception e) {
            DebugLog.e(e.getMessage());
        }
        return map;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        initLayout();
    }

    private void initLayout() {
        initWebView();
        String title = getIntent().getStringExtra(KEY_TITLE);
        mBinding.tvToolbarTitle.setText(title);

        String url = getIntent().getStringExtra(KEY_URL);
        mBinding.webView.loadUrl(url);
    }

    private void initWebView() {
        // init settings
        final WebSettings settings = mBinding.webView.getSettings();

        // build in zoom bar
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);

        // auto scale to fit in
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // enabled flash plugin or other
        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        try {
            File cacheDir = getApplicationContext().getExternalCacheDir();
            if (cacheDir == null) {
                getCacheDir();
            }
            if (cacheDir != null) {
                settings.setCacheMode(WebSettings.LOAD_DEFAULT);
                settings.setAppCachePath(cacheDir.getAbsolutePath());
                settings.setAppCacheEnabled(true);
                settings.setAllowFileAccess(true);
                settings.setDomStorageEnabled(true);
                settings.setLoadsImagesAutomatically(true);
            }
        } catch (Exception ignored) {
        }

        final String ua = settings.getUserAgentString() + getBaseUserAgent();
        settings.setUserAgentString(ua);
    }

    private String getBaseUserAgent() {
        return " UA/plat=1&ver=" + BuildConfig.VERSION_NAME + "&channel=" + AppInstance.getInstance().getChannel() + "&appid=3 ";
    }
}
