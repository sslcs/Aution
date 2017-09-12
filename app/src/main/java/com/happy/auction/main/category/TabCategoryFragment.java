package com.happy.auction.main.category;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.databinding.FragmentTabCategoryBinding;
import com.happy.auction.main.latest.DummyContent;
import com.happy.auction.main.latest.TabLatestAdapter;

/**
 * 商品分类
 */
public class TabCategoryFragment extends Fragment {
    private FragmentTabCategoryBinding binding;
    public TabCategoryFragment() {
    }

    public static TabCategoryFragment newInstance() {
        return new TabCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_tab_category, parent, false);
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
