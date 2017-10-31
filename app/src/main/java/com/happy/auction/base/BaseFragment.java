package com.happy.auction.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author LiuCongshan
 */
public class BaseFragment extends Fragment {
    protected boolean hasCreatedView = false;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hasCreatedView = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }
}
