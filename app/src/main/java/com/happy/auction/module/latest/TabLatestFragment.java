package com.happy.auction.module.latest;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentTabLatestBinding;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.LatestParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 最新成交
 *
 * @author LiuCongshan
 */
public class TabLatestFragment extends BaseFragment {
    private FragmentTabLatestBinding mBinding;
    private TabLatestAdapter mAdapter;
    private int mStart = 0;

    public TabLatestFragment() {
    }

    public static TabLatestFragment newInstance() {
        return new TabLatestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentTabLatestBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vList.addItemDecoration(new DecorationSpace());
        mAdapter = new TabLatestAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<ItemLatest>() {
            @Override
            public void onItemClick(View view, ItemLatest item, int position) {
                if (position < 5) {
                    EventAgent.onEvent(getString(R.string.announce_) + (position + 1));
                }
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setAdapter(this.mAdapter);
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        loadData();
    }

    private void loadData() {
        LatestParam param = new LatestParam();
        param.start = mStart;
        BaseRequest<LatestParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                if (mStart == 0) {
                    mAdapter.clear();
                }
                Type type = new TypeToken<DataResponse<ArrayList<ItemLatest>>>() {}.getType();
                DataResponse<ArrayList<ItemLatest>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    mAdapter.addAll(obj.data);
                    size = obj.data.size();
                    mStart += size;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }
        });
    }

    private void refresh() {
        mStart = 0;
        loadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser) {
            EventAgent.onEvent(R.string.tab_2);
            refresh();
        }
    }
}
