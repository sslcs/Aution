package com.happy.auction.tab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.databinding.ActivityMainBinding;
import com.happy.auction.entity.SendEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.SyncParam;
import com.happy.auction.entity.param.UserInfoParam;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.tab.category.TabCategoryFragment;
import com.happy.auction.tab.home.TabHomeFragment;
import com.happy.auction.tab.latest.TabLatestFragment;
import com.happy.auction.tab.me.TabMeFragment;
import com.happy.auction.ui.AuctionDetailActivity;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private WebSocket client = null;

    private MessagePresenter messagePresenter;
    private boolean isDestroyed = false;
    private long lastBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
        initWebSocket();
        initLayout();
    }

    private void init() {
        messagePresenter = new MessagePresenter();
        RxBus.getDefault().subscribe(this, SendEvent.class, new Consumer<SendEvent>() {
            @Override
            public void accept(SendEvent sendEvent) throws Exception {
                DebugLog.e("sendMessage: " + sendEvent.message);
                sendMessage(sendEvent.message);
            }
        });

        RxBus.getDefault().subscribe(this, LoginResponse.class, new Consumer<LoginResponse>() {
            @Override
            public void accept(LoginResponse response) throws Exception {
                AppInstance.getInstance().setLoginResponse(response);
                syncClient();
                getUserInfo();
            }
        });
    }

    private void initLayout() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(TabHomeFragment.newInstance());
        adapter.add(TabLatestFragment.newInstance());
        adapter.add(TabCategoryFragment.newInstance());
        adapter.add(TabMeFragment.newInstance());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        TabLayout.Tab tab = binding.tabLayout.getTabAt(0);
        if (tab != null) tab.setCustomView(R.layout.tab_home);
        tab = binding.tabLayout.getTabAt(1);
        if (tab != null) tab.setCustomView(R.layout.tab_latest);
        tab = binding.tabLayout.getTabAt(2);
        if (tab != null) tab.setCustomView(R.layout.tab_category);
        tab = binding.tabLayout.getTabAt(3);
        if (tab != null) tab.setCustomView(R.layout.tab_me);
    }

    public void onClickJoin(View view) {
        startActivity(AuctionDetailActivity.newIntent());
    }

    private void initWebSocket() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
//                .url("ws://192.168.1.64:9999/v1/ws")
                .url("ws://192.168.1.225:8888/v1/ws")
                .build();
        httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                DebugLog.e("onOpen");
                client = webSocket;
                syncClient();
                if (AppInstance.getInstance().isLogin()) {
                    getUserInfo();
                }
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                DebugLog.e("onClosed : " + reason);
                if (!isDestroyed) initWebSocket();
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                DebugLog.e("onClosing");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                DebugLog.e("onFailure");
                t.printStackTrace();
                if (response != null) {
                    DebugLog.e("onFailure : " + response.message());
                }
                if (isDestroyed) return;
                Observable.timer(3, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                initWebSocket();
                            }
                        });
            }

            @Override
            public void onMessage(WebSocket webSocket, final String message) {
                super.onMessage(webSocket, message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messagePresenter.handle(message);
                    }
                });
            }
        });
    }

    private void sendMessage(String message) {
        if (client == null) return;
        client.send(message);
    }

    private void syncClient() {
        SyncParam data = new SyncParam();
        BaseRequest<SyncParam> request = new BaseRequest<>(data);
        RxBus.getDefault().post(new SendEvent(request.toString()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
        RxBus.getDefault().unsubscribeAll();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressedTime > 1500) {
            ToastUtil.showShort(R.string.tip_exit);
            lastBackPressedTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void getUserInfo() {
        UserInfoParam data = new UserInfoParam();
        BaseRequest<UserInfoParam> request = new BaseRequest<>(data);
        RxBus.getDefault().post(new SendEvent(request.toString()));
    }
}
