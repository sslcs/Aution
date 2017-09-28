package com.happy.auction.module;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.databinding.ActivityMainBinding;
import com.happy.auction.entity.event.RequestEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.SyncParam;
import com.happy.auction.entity.param.UserInfoParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.module.category.TabCategoryFragment;
import com.happy.auction.module.home.TabHomeFragment;
import com.happy.auction.module.latest.TabLatestFragment;
import com.happy.auction.module.me.TabMeFragment;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
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
    private final MessageHandler messageHandler = new MessageHandler();
    private ActivityMainBinding binding;
    private WebSocket client = null;
    private boolean isDestroyed = false;
    private long lastBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
        initWebSocket();
    }

    private void init() {
        RxBus.getDefault().subscribe(this, RequestEvent.class, new Consumer<RequestEvent>() {
            @Override
            public void accept(RequestEvent requestEvent) throws Exception {
                sendMessage(requestEvent);
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

        RxBus.getDefault().subscribe(this, UserInfo.class, new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo user) throws Exception {
                user.avatar = "http://mobile-pic.cache.iciba.com/1486980953-8616_218-135-%E9%95%BF%E5%8F%91%E5%A4%96%E5%9B%BD%E5%A5%B3.jpg";
                AppInstance.getInstance().setUser(user);
            }
        });
    }

    private void initLayout() {
        if (binding.viewPager.getAdapter() != null) return;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(TabHomeFragment.newInstance());
        adapter.add(TabLatestFragment.newInstance());
        adapter.add(TabCategoryFragment.newInstance());
        adapter.add(TabMeFragment.newInstance());
        binding.viewPager.setOffscreenPageLimit(4);
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

    private void initWebSocket() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
//                .url("ws://192.168.1.64:9999/v1/ws")
//                .url("ws://192.168.1.225:8888/v1/ws")
                .url("ws://192.168.1.250:80/v1/ws")
                .build();
        httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                DebugLog.e("onOpen");
                client = webSocket;
                syncClient();
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
                messageHandler.clear();
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
                        messageHandler.handle(message);
                    }
                });
            }
        });
    }

    private void sendMessage(RequestEvent event) {
        if (client == null) return;
        DebugLog.e("sendMessage: " + event.message);
        messageHandler.addHandler(event.callback);
        client.send(event.message);
    }

    private void syncClient() {
        SyncParam data = new SyncParam();
        BaseRequest<SyncParam> request = new BaseRequest<>(data);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                initLayout();
                if (AppInstance.getInstance().isLogin()) {
                    getUserInfo();
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                // token invalid
                if (code == -1005) {
                    AppInstance.getInstance().logout();
                    syncClient();
                }
            }
        });
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
            ToastUtil.show(R.string.tip_exit);
            lastBackPressedTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void getUserInfo() {
        UserInfoParam data = new UserInfoParam();
        BaseRequest<UserInfoParam> request = new BaseRequest<>(data);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<UserInfo>>() {}.getType();
                DataResponse<UserInfo> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);
            }
        });
    }
}
