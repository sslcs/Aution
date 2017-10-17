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
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentAuctionCoinBinding;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CoinParam;
import com.happy.auction.entity.response.CoinResponse;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.UserInfo;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;

/**
 * 金币记录
 */
public class CoinFragment extends BaseFragment {
    public static final int TYPE_COIN = 1;
    public static final int TYPE_FREE = 2;
    private static final String KEY_TYPE = "TYPE";

    private FragmentAuctionCoinBinding binding;
    private CoinAdapter adapter;
    private int type;
    private int index;

    public CoinFragment() {
    }

    public static CoinFragment newInstance(int type) {
        CoinFragment fragment = new CoinFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentAuctionCoinBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        type = getArguments().getInt(KEY_TYPE, 1);
        binding.btnCharge.setVisibility(type == TYPE_COIN ? View.VISIBLE : View.GONE);

        binding.setFragment(this);
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.addItemDecoration(new DecorationSpace());
        adapter = new CoinAdapter();
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        binding.vList.setAdapter(adapter);

        loadData();
    }

    public void onClickCharge(View view) {
        DebugLog.e("onClickView");
    }

    public Spannable getBalance() {
        SpannableStringBuilder ss = new SpannableStringBuilder();
        if (type == TYPE_COIN) {
            ss.append(getString(R.string.balance_formatter, AppInstance.getInstance().getUser().auction_coin));
        } else {
            ss.append(getString(R.string.free_balance_formatter, AppInstance.getInstance().getUser().free_coin));
        }
        final int color = getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), 5, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void loadData() {
        CoinParam param = new CoinParam();
        param.start = index;
        param.coin_type = type;
        BaseRequest<CoinParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                DebugLog.e("onSuccess");
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<CoinResponse>>() {}.getType();
                DataResponse<CoinResponse> obj = GsonSingleton.get().fromJson(response, type);

                UserInfo info = AppInstance.getInstance().getUser();
                info.free_coin = obj.data.gift_coin;
                info.auction_coin = obj.data.coin;
                AppInstance.getInstance().setUser(info);

                int size = 0;
                if (index == 0) adapter.clear();
                if (obj.data != null && obj.data.records != null) {
                    adapter.addAll(obj.data.records);
                    size = obj.data.records.size();
                    index = adapter.getLast().id;
                }
                adapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }
}
