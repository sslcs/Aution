package com.happy.auction.module.me;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.base.BaseBackActivity;
import com.happy.auction.databinding.ActivityListBinding;
import com.happy.auction.entity.item.ItemBask;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BaskMyParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.module.detail.BaskAdapter;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 晒单记录界面<br/>
 * Created by LiuCongshan on 17-10-23.
 *
 * @author LiuCongshan
 */

public class BaskListActivity extends BaseBackActivity {
    private ActivityListBinding mBinding;

    private BaskAdapter mAdapter;

    public static Intent newIntent() {
        return new Intent(AppInstance.getInstance(), BaskListActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_list);
        initLayout();
    }

    private void initLayout() {
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                loadData(0);
            }
        });
        mBinding.tvToolbarTitle.setText(R.string.my_bask);
        mBinding.vList.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vList.addItemDecoration(new DecorationSpace());
        mAdapter = new BaskAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(mAdapter.getLast().bid);
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        loadData(0);
    }

    private void loadData(int start) {
        BaskMyParam param = new BaskMyParam();
        param.start = start;
        BaseRequest<BaskMyParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                Type type = new TypeToken<DataResponse<ArrayList<ItemBask>>>() {}.getType();
                DataResponse<ArrayList<ItemBask>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    size = obj.data.size();
                    mAdapter.addAll(obj.data);
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }
        });
    }
}
