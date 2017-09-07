package com.happy.auction.tab.me;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happy.auction.R;
import com.happy.auction.databinding.FragmentTabMeBinding;
import com.happy.auction.utils.DebugLog;

/**
 * 个人中心
 */
public class TabMeFragment extends Fragment {
    private FragmentTabMeBinding binding;

    public TabMeFragment() {}

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
        UserDataVM user = new UserDataVM();
        user.username = "不是本人";
        user.avatar = "https://avatars1.githubusercontent.com/u/66577?v=4&s=60";
        user.auction_coin = 90000;
        user.free_coin = 888888;
        user.points = 6666;
        binding.setUser(user);
        binding.setFragment(this);
    }

    @BindingAdapter("spanLength")
    public static void setSpannable(View v, int spanLength) {
        final String itemText = ((TextView) v).getText().toString();
        final SpannableString sString = new SpannableString(itemText);

        sString.setSpan(new RelativeSizeSpan(0.765f), itemText.length() - spanLength, itemText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        final int color = v.getContext().getResources().getColor(R.color.text_normal);
        sString.setSpan(new ForegroundColorSpan(color), itemText.length() - spanLength, itemText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) v).setText(sString);
    }

    public void onClickSetting(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAvatar(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAuctionCoin(View view) {
        DebugLog.e("onClick");
    }

    public void onClickFreeCoin(View view) {
        DebugLog.e("onClick");
    }

    public void onClickPoint(View view) {
        DebugLog.e("onClick");
    }

    public void onClickCharge(View view) {
        DebugLog.e("onClick");
    }

    public void onClickOrder(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAuctionGoing(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAuctionWin(View view) {
        DebugLog.e("onClick");
    }

    public void onClickAuctionUnpaid(View view) {
        DebugLog.e("onClick");
    }

    public void onClickMyCard(View view) {
        DebugLog.e("onClick");
    }

    public void onClickMyPublish(View view) {
        DebugLog.e("onClick");
    }

    public void onClickMyService(View view) {
        DebugLog.e("onClick");
    }

    public void onClickMyMessage(View view) {
        DebugLog.e("onClick");
    }
}
