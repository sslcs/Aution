package com.happy.auction.main.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentAuctionCoinBinding;
import com.happy.auction.main.latest.DummyContent;
import com.happy.auction.main.latest.TabLatestAdapter;
import com.happy.auction.utils.DebugLog;

/**
 * 拍币记录
 */
public class AuctionCoinFragment extends BaseFragment {
    private FragmentAuctionCoinBinding binding;

    public AuctionCoinFragment() {
    }

    public static AuctionCoinFragment newInstance() {
        return new AuctionCoinFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentAuctionCoinBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.setFragment(this);
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.setAdapter(new TabLatestAdapter(DummyContent.ITEMS));
    }

    public void onClickCharge(View view) {
        DebugLog.e("onClick");
    }

    public Spannable getBalance() {
        String balance = getString(R.string.balance_formatter, AppInstance.getInstance().getUser().auction_coin);
        SpannableString ss = new SpannableString(balance);
        final int color = getResources().getColor(R.color.main_red);
        ss.setSpan(new ForegroundColorSpan(color), 5, balance.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
