package com.happy.auction.module.me;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentTabMeBinding;
import com.happy.auction.entity.param.BalanceParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.module.WebActivity;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;

import java.lang.reflect.Type;

import io.reactivex.functions.Consumer;

/**
 * 个人中心界面
 *
 * @author cs
 */
public class TabMeFragment extends BaseFragment {
    private FragmentTabMeBinding binding;

    public static TabMeFragment newInstance() {
        return new TabMeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentTabMeBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.setUser(AppInstance.getInstance().getUser());
        binding.setFragment(this);

        RxBus.getDefault().subscribe(this, LogoutEvent.class, new Consumer<LogoutEvent>() {
            @Override
            public void accept(LogoutEvent event) throws Exception {
                binding.setUser(null);
            }
        });

        RxBus.getDefault().subscribe(this, UserInfo.class, new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo user) throws Exception {
                binding.setUser(user);
            }
        });
    }

    private boolean isLogin() {
        if (AppInstance.getInstance().isLogin()) {
            return true;
        }
        startActivity(LoginActivity.newIntent());
        return false;
    }

    public void onClickSetting(View view) {
        startActivity(SettingActivity.newIntent());
    }

    public void onClickAvatar(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(ManagerActivity.newIntent(getActivity()));
    }

    public void onClickAuctionCoin(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(BalanceActivity.newIntent(getActivity(), 0));
    }

    public void onClickFreeCoin(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(BalanceActivity.newIntent(getActivity(), 1));
    }

    public void onClickPoint(View view) {
        DebugLog.e("onClickView");
    }

    public void onClickCharge(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(ChargePayActivity.newIntent());
    }

    public void onClickOrder(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(getActivity(), 0));
    }

    public void onClickAuctionGoing(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(getActivity(), 1));
    }

    public void onClickAuctionWin(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(getActivity(), 2));
    }

    public void onClickAuctionUnpaid(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(getActivity(), 3));
    }

    public void onClickMyCard(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(CardActivity.newIntent());
    }

    public void onClickMyPublish(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(BaskActivity.newIntent());
    }

    public void onClickMyService(View view) {
        String title = getString(R.string.my_service);
        String url = "http://106.75.177.248/service_center/index.html";
        startActivity(WebActivity.newIntent(title, url));
    }

    public void onClickMyMessage(View view) {
        DebugLog.e("onClickView");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppInstance.getInstance().isLogin()) {
            getBalance();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && AppInstance.getInstance().isLogin()) {
            getBalance();
        }
    }

    private void getBalance() {
        BalanceParam data = new BalanceParam();
        BaseRequest<BalanceParam> request = new BaseRequest<>(data);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<UserBalance>>() {}.getType();
                DataResponse<UserBalance> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null) {
                    return;
                }
                AppInstance.getInstance().setBalance(obj.data);
                if (binding != null) {
                    binding.setUser(AppInstance.getInstance().getUser());
                }
            }
        });
    }

    private SpannableString getSpannable(String text, int spanLength) {
        final SpannableString ss = new SpannableString(text);
        ss.setSpan(new RelativeSizeSpan(0.765f), text.length() - spanLength, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        final int color = getResources().getColor(R.color.text_normal);
        ss.setSpan(new ForegroundColorSpan(color), text.length() - spanLength, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public SpannableString getFreeCoin(UserInfo user) {
        int number = user == null ? 0 : user.free_coin;
        String text = getString(R.string.free_coin_formatter, number);
        return getSpannable(text, 2);
    }

    public SpannableString getAuctionCoin(UserInfo user) {
        int number = user == null ? 0 : user.auction_coin;
        String text = getString(R.string.auction_coin_formatter, number);
        return getSpannable(text, 2);
    }

    public SpannableString getPoint(UserInfo user) {
        int number = user == null ? 0 : user.points;
        String text = getString(R.string.point, number);
        return getSpannable(text, 2);
    }
}
