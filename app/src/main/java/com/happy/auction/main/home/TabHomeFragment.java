package com.happy.auction.main.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.databinding.FragmentTabHomeBinding;
import com.happy.auction.main.latest.DummyContent;
import com.happy.auction.main.latest.TabLatestAdapter;

/**
 * 首页
 */
public class TabHomeFragment extends Fragment {
    private FragmentTabHomeBinding binding;

    public TabHomeFragment() {
    }

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_tab_home, parent, false);
        binding = DataBindingUtil.bind(view);
        initLayout(view);
        return view;
    }

    private void initLayout(View view) {
        Context context = view.getContext();
        binding.vList.setLayoutManager(new LinearLayoutManager(context));
        binding.vList.setAdapter(new TabLatestAdapter(DummyContent.ITEMS));
    }
}
