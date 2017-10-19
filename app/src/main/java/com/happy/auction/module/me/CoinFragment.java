package com.happy.auction.module.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentAuctionCoinBinding;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CoinParam;
import com.happy.auction.entity.response.CoinResponse;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UserBalance;
import com.happy.auction.module.pay.ChargePayActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;

/**
 * 金币记录
 */
public class CoinFragment extends BaseFragment {
    public static final int TYPE_COIN = 1;
    public static final int TYPE_FREE = 2;
    private static final String KEY_TYPE = "TYPE";

    private FragmentAuctionCoinBinding mBinding;
    private CoinAdapter mAdapter;
    private int mType;
    private int mIndex;

    public static CoinFragment newInstance(int type) {
        CoinFragment fragment = new CoinFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentAuctionCoinBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mType = getArguments().getInt(KEY_TYPE, 1);
        mBinding.btnCharge.setVisibility(mType == TYPE_COIN ? View.VISIBLE : View.GONE);

        mBinding.setFragment(this);
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vList.addItemDecoration(new DecorationSpace());
        mAdapter = new CoinAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData();
    }

    public void onClickCharge(View view) {
        startActivity(ChargePayActivity.newIntent());
    }

    public Spannable getBalance() {
        SpannableStringBuilder ss = new SpannableStringBuilder();
        if (mType == TYPE_COIN) {
            ss.append(getString(R.string.balance_formatter, AppInstance.getInstance().getUser().auction_coin));
        } else {
            ss.append(getString(R.string.free_balance_formatter, AppInstance.getInstance().getUser().free_coin));
        }
        final int color = getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), 5, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void setBalance(CoinResponse data) {
        if (data == null) {
            return;
        }
        UserBalance balance = new UserBalance();
        balance.free_coin = data.gift_coin;
        balance.auction_coin = data.coin;
        AppInstance.getInstance().setBalance(balance);
        mBinding.tvBalance.setText(getBalance());
    }

    private void loadData() {
        CoinParam param = new CoinParam();
        param.start = mIndex;
        param.coin_type = mType;
        BaseRequest<CoinParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapter.setLoaded();
                Type type = new TypeToken<DataResponse<CoinResponse>>() {}.getType();
                DataResponse<CoinResponse> obj = GsonSingleton.get().fromJson(response, type);
                setBalance(obj.data);

                int size = 0;
                if (mIndex == 0) {
                    mAdapter.clear();
                }
                if (obj.data != null && obj.data.records != null) {
                    mAdapter.addAll(obj.data.records);
                    size = obj.data.records.size();
                    mIndex = mAdapter.getLast().id;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mType == TYPE_COIN) {
            mIndex = 0;
            loadData();
        }
    }
}
