package com.happy.auction.module.main;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;

import java.io.File;

/**
 * @author LiuCongshan
 * @date 17-11-14
 */

public class CustomWebView extends WebView {
    public CustomWebView(Context context) {
        super(context);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // init settings
        WebSettings settings = getSettings();

        // build in zoom bar
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);

        // auto scale to fit in
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // enabled flash plugin or other
        settings.setPluginState(WebSettings.PluginState.ON);

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        final String ua = settings.getUserAgentString() + getBaseUserAgent();
        settings.setUserAgentString(ua);
    }

    private String getBaseUserAgent() {
        return " UA/plat=1&ver=" + BuildConfig.VERSION_NAME + "&channel=" + AppInstance.getInstance().getChannel();
    }
}
