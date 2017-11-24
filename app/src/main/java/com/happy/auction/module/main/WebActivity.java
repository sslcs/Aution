package com.happy.auction.module.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityWebBinding;
import com.happy.auction.entity.param.ActivityCompleteParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.module.home.BaskAllActivity;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.StringUtil;
import com.happy.auction.utils.ToastUtil;

import java.net.URLDecoder;
import java.util.HashMap;

import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.functions.Consumer;

/**
 * WebView页面
 *
 * @author LiuCongshan
 */

public class WebActivity extends BaseBackActivity {
    public static final String PAGE_CHARGE = "recharge";

    private static final int REQUEST_CODE_LOGIN = 100;

    private static final String PAGE_BASK = "bask";
    private static final String PAGE_LOGIN = "login";
    private static final String PAGE_GOODS = "allGoods";
    private static final String PAGE_SHARE = "share_frame";

    private static final String KEY_TITLE = "title";
    private static final String KEY_URL = "url";
    private static final String SCHEME_LOCAL = "hpjp://native/openLocal?";
    private static final String SCHEME_QQ = "mqqwpa://";

    private static final String NAME_STRATEGY = "auction_valuable";
    private static final String NAME_NEWBIE = "new_pay";


    private ActivityWebBinding mBinding;
    private String mUrl;

    public static Intent newIntent(String title, String url) {
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

        String mTitle = getIntent().getStringExtra(KEY_TITLE);
        if (mUrl.contains(NAME_STRATEGY)) {
            if (AppInstance.getInstance().isLogin()) {
                mUrl += "?state=1&headimg=" + AppInstance.getInstance().getUser().avatar;
                mUrl += "&name=" + Uri.parse(AppInstance.getInstance().getUser().username);
                mUrl += "&local=gdgz";
            } else {
                mUrl += "?state=0";
            }
        } else if (mUrl.contains(NAME_NEWBIE)) {
            if (AppInstance.getInstance().isLogin()) {
                mUrl += "?uid=" + AppInstance.getInstance().uid;
                mUrl += "&token=" + AppInstance.getInstance().token;
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
        if (PAGE_CHARGE.equals(page)) {
            startActivity(ChargePayActivity.newIntent());
            finish();
        } else if (PAGE_BASK.equalsIgnoreCase(page)) {
            startActivity(BaskAllActivity.newIntent());
            finish();
        } else if (PAGE_GOODS.equalsIgnoreCase(page)) {
            onActivityComplete();
        } else if (PAGE_LOGIN.equalsIgnoreCase(page)) {
            if (mUrl.contains(NAME_STRATEGY)) {
                RxBus.getDefault().subscribe(this, UserInfo.class, new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo user) throws Exception {
                        mUrl += "&headimg=" + AppInstance.getInstance().getUser().avatar;
                        mUrl += "&name=" + Uri.parse(AppInstance.getInstance().getUser().username);
                        mUrl += "&local=gdgz";
                        mBinding.webView.loadUrl(mUrl);
                    }
                });
            }
            startActivityForResult(LoginActivity.newIntent(), REQUEST_CODE_LOGIN);
        } else if (PAGE_SHARE.equalsIgnoreCase(page)) {
            String title = map.get("wxtitle");
            String content = map.get("wxcontent");
            share(title, content);
        } else {
            return false;
        }
        return true;
    }

    private void onActivityComplete() {
        ActivityCompleteParam param = new ActivityCompleteParam();
        BaseRequest<ActivityCompleteParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                finish();
            }

            @Override
            public void onError(int code, String message) {
                finish();
            }
        });
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
                if (url.startsWith(SCHEME_QQ)) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        ToastUtil.show(R.string.error_qq);
                    }
                } else {
                    super.onReceivedError(view, errorCode, description, url);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (REQUEST_CODE_LOGIN == requestCode) {
            if (mUrl.contains(NAME_STRATEGY) && mUrl.contains("state=0")) {
                mUrl = mUrl.replace("state=0", "state=2");
                mBinding.webView.loadUrl(mUrl);
            } else if (mUrl.contains(NAME_NEWBIE)) {
                mUrl += "?uid=" + AppInstance.getInstance().uid;
                mUrl += "&token=" + AppInstance.getInstance().token;
                mBinding.webView.loadUrl(mUrl);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().unsubscribe(this);
    }


    private void share(String title, String content) {
        OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();
        oks.setTitle(title);
        oks.setText(content);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        oks.setImageData(logo);
        String url = mUrl.replace("client_index", "login");
        url += "?name=" + StringUtil.formatPhone(AppInstance.getInstance().getUser().phone);
        url += "&uid=" + AppInstance.getInstance().uid;
        url += "&state=3";
        oks.setUrl(url);
        oks.setSilent(true);
        oks.show(this);
    }
}
