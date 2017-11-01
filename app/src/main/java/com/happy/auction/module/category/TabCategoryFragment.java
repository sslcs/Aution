package com.happy.auction.module.category;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.DecorationColor;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentTabCategoryBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.item.ItemCategory;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CategoryParam;
import com.happy.auction.entity.param.GoodsParam;
import com.happy.auction.entity.param.SubscribeParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.GoodsResponse;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 商品分类
 *
 * @author LiuCongshan
 */
public class TabCategoryFragment extends BaseFragment {
    private FragmentTabCategoryBinding mBinding;
    private CategoryAdapter mAdapterCategory;
    private CategoryGoodsAdapter mAdapterGoods;
    private int mStart;
    private ItemCategory mCurrentCategory;
    private Disposable mDisposableRefresh;

    public TabCategoryFragment() {
    }

    public static TabCategoryFragment newInstance() {
        return new TabCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentTabCategoryBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        mAdapterCategory = new CategoryAdapter();
        mAdapterCategory.setOnItemClickListener(new OnItemClickListener<ItemCategory>() {
            @Override
            public void onItemClick(View view, ItemCategory item, int position) {
                if (position == mAdapterCategory.getSelectedPosition()) {
                    return;
                }
                mAdapterCategory.setSelectedPosition(position);
                mCurrentCategory = item;
                refresh();
            }
        });
        mBinding.vCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vCategory.setAdapter(mAdapterCategory);
        mBinding.vCategory.addItemDecoration(new DecorationColor());

        mAdapterGoods = new CategoryGoodsAdapter();
        mAdapterGoods.setOnItemClickListener(new OnItemClickListener<ItemGoods>() {
            @Override
            public void onItemClick(View view, ItemGoods item, int position) {
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        mAdapterGoods.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vList.setAdapter(mAdapterGoods);
        mBinding.vList.addItemDecoration(new DecorationColor());

        loadCategory();
        listenEvents();
    }

    private void refresh() {
        mStart = 0;
        loadData();
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = mAdapterGoods.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = mAdapterGoods.getItem(position);
                item.current_price = event.current_price;
                item.bid_expire_time = event.bid_expire_time;
                mAdapterGoods.notifyItemChanged(position);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = mAdapterGoods.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = mAdapterGoods.getItem(position);
                item.setStatus(0);
                mAdapterGoods.notifyItemChanged(position);

                if (mDisposableRefresh == null || mDisposableRefresh.isDisposed()) {
                    mDisposableRefresh = Observable.timer(10, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    refresh();
                                }
                            });
                }
            }
        });
    }

    private void loadCategory() {
        CategoryParam param = new CategoryParam();
        BaseRequest<CategoryParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemCategory>>>() {}.getType();
                DataResponse<ArrayList<ItemCategory>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) {
                    return;
                }
                mAdapterCategory.addAll(obj.data);
                mCurrentCategory = mAdapterCategory.getItem(0);
                refresh();
            }
        });
    }

    private void loadData() {
        if (mStart == 0) {
            mAdapterGoods.clear();
        }
        GoodsParam param = new GoodsParam();
        param.tid = mCurrentCategory.tid;
        param.start = mStart;
        BaseRequest<GoodsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapterGoods.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data.goods != null && !obj.data.goods.isEmpty()) {
                    mAdapterGoods.addAll(obj.data.goods);
                    size = obj.data.goods.size();
                    mStart += size;
                }
                mAdapterGoods.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapterGoods == null || !getUserVisibleHint()) {
            return;
        }
        SubscribeParam param = new SubscribeParam();
        param.addAll(mAdapterGoods.getData());
        NetClient.query(new BaseRequest<>(param), null);
    }
}
