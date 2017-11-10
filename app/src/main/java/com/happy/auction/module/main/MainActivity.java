package com.happy.auction.module.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.BuildConfig;
import com.happy.auction.R;
import com.happy.auction.adapter.ViewPagerAdapter;
import com.happy.auction.base.BaseTimeActivity;
import com.happy.auction.databinding.ActivityMainBinding;
import com.happy.auction.entity.event.BidNowEvent;
import com.happy.auction.entity.event.RequestEvent;
import com.happy.auction.entity.event.WinEvent;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.MessageCountParam;
import com.happy.auction.entity.param.SyncParam;
import com.happy.auction.entity.param.UserInfoParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.LoginResponse;
import com.happy.auction.entity.response.MessageCount;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.module.category.TabCategoryFragment;
import com.happy.auction.module.home.TabHomeFragment;
import com.happy.auction.module.latest.TabLatestFragment;
import com.happy.auction.module.login.SetPasswordActivity;
import com.happy.auction.module.me.TabMeFragment;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.PreferenceUtil;
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
 *
 * @author LiuCongshan
 */
public class MainActivity extends BaseTimeActivity {
    private final MessageHandler mMessageHandler = new MessageHandler();
    private ActivityMainBinding mBinding;
    private WebSocket mSocket = null;
    private boolean isDestroyed = false;
    private long mLastBackPressedTime;

    public static Intent newInstance() {
        Intent intent = new Intent(AppInstance.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        listenEvents();
        initWebSocket();
    }

    private void listenEvents() {
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
            }
        });

        RxBus.getDefault().subscribe(this, BidNowEvent.class, new Consumer<BidNowEvent>() {
            @Override
            public void accept(BidNowEvent bidNowEvent) throws Exception {
                mBinding.viewPager.setCurrentItem(2, true);
                startActivity(MainActivity.newInstance());
            }
        });

        RxBus.getDefault().subscribe(this, WinEvent.class, new Consumer<WinEvent>() {
            @Override
            public void accept(WinEvent event) throws Exception {
                startActivity(WinActivity.newIntent(event));
            }
        });
    }

    private void initLayout() {
        if (mBinding.viewPager.getAdapter() != null) {
            return;
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.add(TabHomeFragment.newInstance());
        adapter.add(TabLatestFragment.newInstance());
        adapter.add(TabCategoryFragment.newInstance());
        adapter.add(TabMeFragment.newInstance());
        mBinding.viewPager.setOffscreenPageLimit(4);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        TabLayout.Tab tab = mBinding.tabLayout.getTabAt(0);
        if (tab != null) {
            tab.setCustomView(R.layout.tab_home);
        }
        tab = mBinding.tabLayout.getTabAt(1);
        if (tab != null) {
            tab.setCustomView(R.layout.tab_latest);
        }
        tab = mBinding.tabLayout.getTabAt(2);
        if (tab != null) {
            tab.setCustomView(R.layout.tab_category);
        }
        tab = mBinding.tabLayout.getTabAt(3);
        if (tab != null) {
            tab.setCustomView(R.layout.tab_me);
        }
    }

    private void initWebSocket() {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        String host = BuildConfig.HOST;
        if (BuildConfig.DEBUG) {
            host = PreferenceUtil.getHost();
        }
        Request request = new Request.Builder()
                .url("ws://" + host + "/ws")
                .build();
        httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                DebugLog.e("onOpen");
                mSocket = webSocket;
                syncClient();
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                DebugLog.e("onClosed : " + reason);
                if (!isDestroyed) {
                    initWebSocket();
                }
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
                mMessageHandler.clear();
                mSocket = null;
                if (isDestroyed) {
                    return;
                }
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
                        mMessageHandler.handle(message);
                    }
                });
            }
        });
    }

    private void sendMessage(RequestEvent event) {
        if (mSocket == null) {
            return;
        }
        DebugLog.e(event.message);
        if (event.callback != null) {
            mMessageHandler.addHandler(event.callback);
        }
        mSocket.send(event.message);
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
                    getMessageCount();
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
        if (mSocket != null) {
            mSocket.cancel();
            mSocket = null;
        }
        RxBus.getDefault().unsubscribeAll();
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastBackPressedTime > 1500) {
            ToastUtil.show(R.string.tip_exit);
            mLastBackPressedTime = System.currentTimeMillis();
        } else {
            setResult(911);
            finish();
        }
    }

    private void getUserInfo() {
        UserInfoParam param = new UserInfoParam();
        BaseRequest<UserInfoParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<UserInfo>>() {}.getType();
                DataResponse<UserInfo> obj = GsonSingleton.get().fromJson(response, type);
                AppInstance.getInstance().setUser(obj.data);
                RxBus.getDefault().post(obj.data);

                showSetPassword(obj.data);
            }
        });
    }

    private void showSetPassword(UserInfo info) {
        if (info.noPassword() && PreferenceUtil.showSetPassword(info.phone)) {
            startActivity(SetPasswordActivity.newIntent());
        }
    }

    private void getMessageCount() {
        MessageCountParam param = new MessageCountParam();
        BaseRequest<MessageCountParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<MessageCount>>() {}.getType();
                DataResponse<MessageCount> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data != null) {
                    AppInstance.getInstance().mMessageCount.set(obj.data.count);
                }
            }
        });
    }
}
