package com.happy.auction.module.order;

import android.app.ActivityOptions;
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
import com.happy.auction.databinding.FragmentListBinding;
import com.happy.auction.entity.item.ItemOrder;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.OrderParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
import com.happy.auction.utils.GsonSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 订单记录
 *
 * @author LiuCongshan
 */
public class OrderFragment extends BaseFragment {
    public final static int TYPE_ALL = 0;
    public final static int TYPE_GOING = 1;
    public final static int TYPE_WIN = 2;
    public final static int TYPE_UNPAID = 3;

    private static final String KEY_TYPE = "type";

    private FragmentListBinding mBinding;
    private OrderAdapter mAdapter;
    private int mType;

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
        mType = getArguments().getInt(KEY_TYPE);
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(0);
            }
        });

        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        DecorationSpace decoration = new DecorationSpace(5);
        decoration.enableHeader();
        mBinding.vList.addItemDecoration(decoration);
        mAdapter = new OrderAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener<ItemOrder>() {
            @Override
            public void onItemClick(View view, ItemOrder item, int position) {
                if (item.status == 1 || item.status == 6) {
                    EventAgent.onEvent(R.string.orders_auction_detail);
                    startActivity(AuctionDetailActivity.newIntent(item));
                } else {
                    EventAgent.onEvent(R.string.orders_reward_detail);
                    Bundle bundle = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "v_info").toBundle();
                    }
                    startActivity(OrderDetailActivity.newIntent(item), bundle);
                }
            }
        });
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                ItemOrder item = mAdapter.getLast();
                if (item != null) {
                    loadData(item.pid);
                }
            }
        });
        mBinding.vList.setAdapter(mAdapter);

        if (getUserVisibleHint()) {
            loadData(0);
        }
    }

    private void loadData(final int start) {
        OrderParam param = new OrderParam();
        param.start = start;
        param.record_type = mType;
        BaseRequest<OrderParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mBinding.refreshView.setRefreshing(false);
                if (start == 0) {
                    mAdapter.clear();
                }
                Type type = new TypeToken<DataResponse<ArrayList<ItemOrder>>>() {}.getType();
                DataResponse<ArrayList<ItemOrder>> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && !obj.data.isEmpty()) {
                    mAdapter.addAll(obj.data);
                    size = obj.data.size();
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser) {
            if (mType == TYPE_ALL) {
                EventAgent.onEvent(R.string.orders_all);
            } else if (mType == TYPE_GOING) {
                EventAgent.onEvent(R.string.orders_ongoing);
            } else if (mType == TYPE_WIN) {
                EventAgent.onEvent(R.string.orders_deal);
            } else {
                EventAgent.onEvent(R.string.orders_due);
            }

            if (mAdapter.isEmpty()) {
                loadData(0);
            }
        }
    }
}
