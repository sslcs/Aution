package com.happy.auction.main;

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
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.SyncParam;
import com.happy.auction.entity.param.UserInfoParam;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.main.category.TabCategoryFragment;
import com.happy.auction.main.home.TabHomeFragment;
import com.happy.auction.main.latest.TabLatestFragment;
import com.happy.auction.main.me.TabMeFragment;
import com.happy.auction.net.ResponseHandler;
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
    }

    private void init() {
        messagePresenter = new MessagePresenter();
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
                .url("ws://192.168.1.225:8888/v1/ws")
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

    private void sendMessage(RequestEvent event) {
        if (client == null) return;
        DebugLog.e("sendMessage: " + event.message);
        messagePresenter.addHandler(event.handler);
        client.send(event.message);
    }

    private void syncClient() {
        SyncParam data = new SyncParam();
        BaseRequest<SyncParam> request = new BaseRequest<>(data);
        RequestEvent event = new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {
                initLayout();
                if (AppInstance.getInstance().isLogin()) {
                    getUserInfo();
                }
            }
        });
        RxBus.getDefault().post(event);
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
        RequestEvent event = new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<UserInfo>>() {}.getType();
                DataResponse<UserInfo> obj = GsonSingleton.get().fromJson(response, type);
                RxBus.getDefault().post(obj.data);
            }
        });
        RxBus.getDefault().post(event);
    }
}
