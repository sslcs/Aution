package com.happy.auction.module.me;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.DecorationSpace;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentListBinding;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.OrderParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 订单记录
 */
public class OrderFragment extends BaseFragment {
    public final static int TYPE_ALL = 0;
    public final static int TYPE_GOING = 1;
    public final static int TYPE_WIN = 2;
    public final static int TYPE_UNPAID = 3;

    private static final String KEY_TYPE = "type";
    private FragmentListBinding mBinding;

    private OrderAdapter adapter;

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
        mBinding = FragmentListBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        DecorationSpace decoration = new DecorationSpace(5);
        decoration.enableHeader();
        mBinding.vList.addItemDecoration(decoration);
        adapter = new OrderAdapter();
        adapter.setOnButtonClickListener(new OrderAdapter.OnButtonClickListener() {
            @Override
            public void go(View view, ItemOrder item) {
                Bundle bundle = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "v_info").toBundle();
                }
                startActivity(OrderDetailActivity.newIntent(item), bundle);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener<ItemOrder>() {
            @Override
            public void onItemClick(View view, ItemOrder item, int position) {
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                ItemOrder item = adapter.getLast();
                if (item != null) {
                    loadData(item.pid);
                }
            }
        });
        mBinding.vList.setAdapter(adapter);

        if (getUserVisibleHint()) {
            loadData(0);
        }
    }

    private void loadData(int start) {
        OrderParam param = new OrderParam();
        param.start = start;
        param.record_type = getArguments().getInt(KEY_TYPE);
        BaseRequest<OrderParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<ArrayList<ItemOrder>>>() {}.getType();
                DataResponse<ArrayList<ItemOrder>> obj = GsonSingleton.get().fromJson(response, type);
                ArrayList<ItemOrder> data = obj.data;
                if (data != null) {
                    adapter.addAll(data);
                    adapter.setHasMore(data.size() >= BaseParam.DEFAULT_LIMIT);
                } else {
                    adapter.setHasMore(false);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser && adapter.isEmpty()) {
            loadData(0);
        }
    }
}
