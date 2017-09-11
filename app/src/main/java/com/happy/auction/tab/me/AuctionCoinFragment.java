package com.happy.auction.tab.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.databinding.FragmentListBinding;
import com.happy.auction.tab.latest.DummyContent;
import com.happy.auction.tab.latest.TabLatestAdapter;

/**
 * 拍币记录
 */
public class AuctionCoinFragment extends Fragment {
    private FragmentListBinding binding;

    public AuctionCoinFragment() {
    }

    public static AuctionCoinFragment newInstance() {
        return new AuctionCoinFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentListBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.setAdapter(new TabLatestAdapter(DummyContent.ITEMS));
    }
}
