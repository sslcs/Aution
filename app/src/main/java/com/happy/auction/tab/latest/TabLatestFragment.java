package com.happy.auction.tab.latest;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happy.auction.R;
import com.happy.auction.adapter.AdapterWrapper;
import com.happy.auction.databinding.FragmentTabLatestBinding;
import com.happy.auction.utils.DebugLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 最新成交
 */
public class TabLatestFragment extends Fragment {
    private FragmentTabLatestBinding binding;

    public TabLatestFragment() {
    }

    public static TabLatestFragment newInstance() {
        return new TabLatestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_tab_latest, parent, false);
        binding = DataBindingUtil.bind(view);
        initLayout(view);
        return view;
    }

    private void initLayout(View view) {
        Context context = view.getContext();
        binding.vList.setLayoutManager(new LinearLayoutManager(context));
        TabLatestAdapter adapter = new TabLatestAdapter(DummyContent.ITEMS);
        final AdapterWrapper<TabLatestAdapter> wrapper = new AdapterWrapper<>(adapter);
        wrapper.setEmptyView(R.layout.empty_view);
        wrapper.setLoadMoreView(R.layout.item_load_more);
        wrapper.setRefreshView(R.layout.item_load_more);

        wrapper.setLoadMoreListener(new AdapterWrapper.LoadMoreListener() {
            @Override
            public void loadMore() {
                Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                DebugLog.e("loadMore");
                                wrapper.getInnerAdapter().addAll(DummyContent.ITEMS);
                                wrapper.setHasMore(wrapper.getInnerAdapter().getItemCount() < 200);
                            }
                        });
            }
        });
        wrapper.setOnRefreshListener(new AdapterWrapper.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Observable.timer(1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(@NonNull Long aLong) throws Exception {
                                DebugLog.e("onRefresh");
                                wrapper.getInnerAdapter().clear();
                                wrapper.getInnerAdapter().addAll(DummyContent.ITEMS);
                                wrapper.setHasMore(true);
                                wrapper.setRefreshing(false);
                            }
                        });
            }
        });

        binding.vList.setAdapter(wrapper);
    }
}
