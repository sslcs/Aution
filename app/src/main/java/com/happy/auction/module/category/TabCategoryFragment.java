package com.happy.auction.module.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.DecorationColor;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.FragmentTabCategoryBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.item.ItemCategory;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.CategoryParam;
import com.happy.auction.entity.param.GoodsParam;
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
 */
public class TabCategoryFragment extends Fragment {
    private FragmentTabCategoryBinding mBinding;
    private CategoryAdapter adapterCategory;
    private CategoryGoodsAdapter adapterGoods;
    private int currentIndex;
    private ItemCategory currentCategory;
    private Disposable disposableRefresh;

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
        adapterCategory = new CategoryAdapter();
        adapterCategory.setOnItemClickListener(new OnItemClickListener<ItemCategory>() {
            @Override
            public void onItemClick(View view, ItemCategory item, int position) {
                if (position == adapterCategory.getSelectedPosition()) {
                    return;
                }
                adapterCategory.setSelectedPosition(position);
                currentCategory = item;
                refresh();
            }
        });
        mBinding.vCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vCategory.setAdapter(adapterCategory);
        mBinding.vCategory.addItemDecoration(new DecorationColor());

        adapterGoods = new CategoryGoodsAdapter();
        adapterGoods.setOnItemClickListener(new OnItemClickListener<ItemGoods>() {
            @Override
            public void onItemClick(View view, ItemGoods item, int position) {
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        adapterGoods.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData();
            }
        });
        mBinding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.vList.setAdapter(adapterGoods);
        mBinding.vList.addItemDecoration(new DecorationColor());

        loadCategory();
        listenEvents();
    }

    private void refresh() {
        currentIndex = 0;
        loadData();
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = adapterGoods.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = adapterGoods.getItem(position);
                item.current_price = event.current_price;
                item.bid_expire_time = event.bid_expire_time;
                adapterGoods.notifyItemChanged(position);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = adapterGoods.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = adapterGoods.getItem(position);
                item.setStatus(0);
                adapterGoods.notifyItemChanged(position);

                if (disposableRefresh == null || disposableRefresh.isDisposed()) {
                    disposableRefresh = Observable.timer(10, TimeUnit.SECONDS)
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
                adapterCategory.addAll(obj.data);
                currentCategory = adapterCategory.getItem(0);
                refresh();
            }
        });
    }

    private void loadData() {
        GoodsParam param = new GoodsParam();
        param.tid = currentCategory.tid;
        param.start = currentIndex;
        BaseRequest<GoodsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapterGoods.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (currentIndex == 0) {
                    adapterGoods.clear();
                }

                int size = 0;
                if (obj.data.goods != null && !obj.data.goods.isEmpty()) {
                    adapterGoods.addAll(obj.data.goods);
                    size = obj.data.goods.size();
                    currentIndex += size;
                }
                adapterGoods.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
            }
        });
    }
}
