package com.happy.auction.module.latest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.databinding.FragmentTabLatestBinding;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.LatestParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 最新成交
 */
public class TabLatestFragment extends Fragment {
    private FragmentTabLatestBinding binding;
    private TabLatestAdapter adapter;
    private int start;

    public TabLatestFragment() {
    }

    public static TabLatestFragment newInstance() {
        return new TabLatestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentTabLatestBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.addItemDecoration(new DecorationSpace());
        adapter = new TabLatestAdapter();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemLatest item = adapter.getItem(position);
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(start);
            }
        });
        binding.vList.setAdapter(this.adapter);

        loadData(0);
    }

    private void loadData(int start) {
        this.start = start;
        LatestParam param = new LatestParam();
        param.start = start;
        BaseRequest<LatestParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemLatest>>>() {}.getType();
                DataResponse<ArrayList<ItemLatest>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) return;
                adapter.addAll(obj.data);
                int size = obj.data.size();
                adapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                TabLatestFragment.this.start += size;
            }
        });
    }
}
