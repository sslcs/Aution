package com.happy.auction.module;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityWebBinding;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.me.BaskListActivity;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.ToastUtil;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;

/**
 * WebView页面
 *
 * @author LiuCongshan
 */

public class WebActivity extends BaseBackActivity {
    private static final int REQUEST_CODE_LOGIN = 100;

    private static final String PAGE_RECHARGE = "recharge";
    private static final String PAGE_BASK = "bask";
    private static final String PAGE_LOGIN = "login";
    private static final String PAGE_GOODS = "allGoods";
    private static final String PAGE_SHARE = "share_frame";

    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String SCHEME_LOCAL = "hpjp://native/openLocal?";
    private static final String SCHEME_QQ = "mqqwpa://";

    private ActivityWebBinding mBinding;
    private String mTitle, mUrl;

    public static Intent newIntent(String title, String url) {
        DebugLog.e("title: " + title + " url: " + url);
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        Intent intent = new Intent(AppInstance.getInstance(), WebActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_URL, url);
        return intent;
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
        mUrl = getIntent().getStringExtra(KEY_URL);
        if (handleUrl(mUrl)) {
            return;
        }

        mTitle = getIntent().getStringExtra(KEY_TITLE);
        if (getString(R.string.strategy).equals(mTitle)) {
            if (AppInstance.getInstance().isLogin()) {
                mUrl += "?state=1&headimg=" + AppInstance.getInstance().getUser().avatar;
                mUrl += "&name=" + Uri.parse(AppInstance.getInstance().getUser().username);
                mUrl += "&local=gdgz";
            } else {
                mUrl += "?state=0";
            }
        }

        initWebView();
        mBinding.tvToolbarTitle.setText(mTitle);
        mBinding.webView.loadUrl(mUrl);
    }

    private boolean handleUrl(String url) {
        if (!url.startsWith(SCHEME_LOCAL)) {
            return false;
        }

        String param = url.substring(24);
        HashMap<String, String> map = parseStringMap(param);
        String page = map.get("page");
        if (PAGE_RECHARGE.equals(page)) {
            startActivity(ChargePayActivity.newIntent());
            finish();
        } else if (PAGE_BASK.equalsIgnoreCase(page)) {
            startActivity(BaskListActivity.newIntent());
            finish();
        } else if (PAGE_GOODS.equalsIgnoreCase(page)) {
            finish();
        } else if (PAGE_LOGIN.equalsIgnoreCase(page)) {
            startActivityForResult(LoginActivity.newIntent(), REQUEST_CODE_LOGIN);
        } else if (PAGE_SHARE.equalsIgnoreCase(page)) {
            startActivityForResult(LoginActivity.newIntent(), REQUEST_CODE_LOGIN);
        } else {
            return false;
        }
        return true;
    }

    private void initWebView() {
        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (handleUrl(url)) {
                    view.stopLoading();
                    return true;
                }
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                        || error.getPrimaryError() == SslError.SSL_EXPIRED
                        || error.getPrimaryError() == SslError.SSL_INVALID
                        || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
                    handler.proceed();
                } else {
                    super.onReceivedSslError(view, handler, error);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String url) {
                super.onReceivedError(view, errorCode, description, url);
                if (url.startsWith(SCHEME_QQ)) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        ToastUtil.show(R.string.error_qq);
                    }
                }
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_LOGIN == requestCode) {
            if (getString(R.string.strategy).equals(mTitle) && mUrl.contains("state=0")) {
                mUrl = mUrl.replace("state=0", "state=2");
                mBinding.webView.loadUrl(mUrl);
            }
        }
    }
}
