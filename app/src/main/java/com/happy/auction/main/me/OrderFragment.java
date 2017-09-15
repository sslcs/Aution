package com.happy.auction.main.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.AdapterWrapper;
import com.happy.auction.adapter.CustomAdapter;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentListBinding;
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.OrderParam;
import com.happy.auction.entity.response.GoodsResponse;
import com.happy.auction.net.ResponseHandler;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 订单记录
 */
public class OrderFragment extends BaseFragment {
    public final static int TYPE_ALL = 0;
    public final static int TYPE_GOING = 1;
    public final static int TYPE_WIN = 2;
    public final static int TYPE_UNPAID = 3;
    private static final String KEY_TYPE = "type";
    private FragmentListBinding binding;

    private CustomAdapter<OrderAdapter> adapter;

    private int start;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(int type) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentListBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CustomAdapter<>(new OrderAdapter());
        adapter.setLoadMoreListener(new AdapterWrapper.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(start);
            }
        });
        binding.vList.setAdapter(adapter);

        if (getUserVisibleHint()) {
            loadData(1);
        }
    }

    private void loadData(int start) {
        this.start = start;
        OrderParam param = new OrderParam();
        param.records_status = getArguments().getInt(KEY_TYPE);
        BaseRequest<OrderParam> request = new BaseRequest<>(param);
        RequestEvent event = new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemOrder>>>() {}.getType();
                DataResponse<ArrayList<ItemOrder>> obj = GsonSingleton.get().fromJson(response, type);
                ArrayList<ItemOrder> data = obj.data;
                if (data != null) {
                    adapter.getInnerAdapter().addAll(data);
                    adapter.setHasMore(data.size() >= BaseParam.DEFAULT_LIMIT);
                } else {
                    adapter.setHasMore(false);
                }
            }
        });
        RxBus.getDefault().post(event);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser && adapter.getRealItemCount() == 0) {
            loadData(1);
        }
    }
}