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
import com.happy.auction.entity.event.LogoutEvent;
import com.happy.auction.entity.param.BalanceParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.module.WebActivity;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.message.MessageActivity;
import com.happy.auction.module.order.OrderActivity;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.ui.CustomDialog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.StringUtil;

import java.lang.reflect.Type;

import io.reactivex.functions.Consumer;

/**
 * 个人中心界面
 *
 * @author LiuCongshan
 */
public class TabMeFragment extends BaseFragment {
    private FragmentTabMeBinding mBinding;

    public static TabMeFragment newInstance() {
        return new TabMeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentTabMeBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.setUser(AppInstance.getInstance().getUser());
        mBinding.setFragment(this);

        RxBus.getDefault().subscribe(this, LogoutEvent.class, new Consumer<LogoutEvent>() {
            @Override
            public void accept(LogoutEvent event) throws Exception {
                mBinding.setUser(null);
            }
        });

        RxBus.getDefault().subscribe(this, UserInfo.class, new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo user) throws Exception {
                mBinding.setUser(user);
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
        startActivity(BalanceActivity.newIntent(0));
    }

    public void onClickFreeCoin(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(BalanceActivity.newIntent(1));
    }

    public void onClickPoint(View view) {
        new CustomDialog.Builder()
                .content(getString(R.string.tip_coming))
                .textRight(getString(R.string.ok))
                .show(getChildFragmentManager(), "dialog_coming");
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
        startActivity(OrderActivity.newIntent(0));
    }

    public void onClickAuctionGoing(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(1));
    }

    public void onClickAuctionWin(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(2));
    }

    public void onClickAuctionUnpaid(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(OrderActivity.newIntent(3));
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
        startActivity(BaskListActivity.newIntent());
    }

    public void onClickMyService(View view) {
        String title = getString(R.string.my_service);
        startActivity(WebActivity.newIntent(title, StringUtil.URL_SERVICE_CENTER));
    }

    public void onClickMyMessage(View view) {
        if (!isLogin()) {
            return;
        }
        startActivity(MessageActivity.newIntent());
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
                if (mBinding != null) {
                    mBinding.setUser(AppInstance.getInstance().getUser());
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
