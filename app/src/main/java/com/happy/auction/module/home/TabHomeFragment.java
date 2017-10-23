package com.happy.auction.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BaseFragment;
import com.happy.auction.databinding.FragmentTabHomeBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.item.ItemBanner;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemLatest;
import com.happy.auction.entity.item.ItemMenu;
import com.happy.auction.entity.param.AnnounceParam;
import com.happy.auction.entity.param.BannerParam;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.GoodsParam;
import com.happy.auction.entity.param.MenuParam;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.response.GoodsResponse;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.module.WebActivity;
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 首页<br/>
 *
 * @author LiuCongshan
 */
public class TabHomeFragment extends BaseFragment {
    private FragmentTabHomeBinding mBinding;
    private TabHomeAdapter mAdapter;
    private int mStart = 0;
    private String mType = GoodsParam.TYPE_HOT;
    private Disposable mDisposableRefresh;

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        mBinding = FragmentTabHomeBinding.inflate(inflater);
        initLayout();
        return mBinding.getRoot();
    }

    private void initLayout() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvBanner.setLayoutManager(llm);

        mBinding.vList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mBinding.vList.addItemDecoration(new HomeDecoration());
        mBinding.vList.setItemAnimator(null);
        mAdapter = new TabHomeAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadGoods();
                mBinding.vList.stopScroll();
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener<ItemGoods>() {
            @Override
            public void onItemClick(View view, ItemGoods item, int position) {
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        mBinding.vList.setAdapter(mAdapter);
        mBinding.tvAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == null) {
                    return;
                }
                ItemLatest item = (ItemLatest) view.getTag();
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    if (!GoodsParam.TYPE_NEWBIE.equals(mType)) {
                        mAdapter.clear();
                        mType = GoodsParam.TYPE_NEWBIE;
                        refresh();
                    }
                } else {
                    if (!GoodsParam.TYPE_HOT.equals(mType)) {
                        mAdapter.clear();
                        mType = GoodsParam.TYPE_HOT;
                        refresh();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        loadData();
        listenEvents();
    }

    private void refresh() {
        mStart = 0;
        loadGoods();
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = mAdapter.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = mAdapter.getItem(position);
                item.current_price = event.current_price;
                item.bid_expire_time = event.bid_expire_time;
                mAdapter.addChangedPosition(position);
                mAdapter.notifyItemChanged(position);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = mAdapter.getPosition(item);
                if (position == -1) {
                    return;
                }
                item = mAdapter.getItem(position);
                item.setStatus(0);
                mAdapter.notifyItemChanged(position);

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

    private void loadData() {
        loadBanner();

        loadMenu();

        loadAnnounce();

        refresh();
    }

    private void loadAnnounce() {
        AnnounceParam param = new AnnounceParam();
        BaseRequest<AnnounceParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemLatest>>>() {}.getType();
                DataResponse<ArrayList<ItemLatest>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) {
                    return;
                }
                mBinding.tvAnnounce.addData(obj.data);
            }
        });
    }

    private void loadMenu() {
        MenuParam param = new MenuParam();
        BaseRequest<MenuParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemMenu>>>() {}.getType();
                DataResponse<ArrayList<ItemMenu>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) {
                    return;
                }
                int length = obj.data.size();
                ImageView[] ivMenu = new ImageView[]{mBinding.menu.ivMenu0, mBinding.menu.ivMenu1, mBinding.menu.ivMenu2, mBinding.menu.ivMenu3};
                TextView[] tvMenu = new TextView[]{mBinding.menu.tvMenu0, mBinding.menu.tvMenu1, mBinding.menu.tvMenu2, mBinding.menu.tvMenu3};
                for (int i = 0; i < length && i < 4; i++) {
                    final ItemMenu menu = obj.data.get(i);
                    tvMenu[i].setText(menu.title);
                    ImageLoader.displayMenu(menu.icon, ivMenu[i]);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = WebActivity.newIntent(menu.title, menu.url);
                            if (intent != null) {
                                startActivity(intent);
                            }
                        }
                    };
                    tvMenu[i].setOnClickListener(listener);
                    ivMenu[i].setOnClickListener(listener);
                }
            }
        });
    }

    private void loadBanner() {
        BannerParam param = new BannerParam();
        BaseRequest<BannerParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemBanner>>>() {}.getType();
                DataResponse<ArrayList<ItemBanner>> obj = GsonSingleton.get().fromJson(response, type);
                BannerAdapter adapter = new BannerAdapter(obj.data);
                adapter.setOnItemClickListener(new OnItemClickListener<ItemBanner>() {
                    @Override
                    public void onItemClick(View view, ItemBanner item, int position) {
                        Intent intent = WebActivity.newIntent(item.title, item.url);
                        if (intent != null) {
                            startActivity(intent);
                        }
                    }
                });
                mBinding.rvBanner.setAdapter(adapter);
                PagingScrollHelper helper = new PagingScrollHelper();
                helper.setUpRecycleView(mBinding.rvBanner);

            }
        });
    }

    private void loadGoods() {
        GoodsParam param = new GoodsParam();
        param.type = mType;
        param.start = mStart;
        BaseRequest<GoodsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                mAdapter.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (mStart == 0) {
                    mAdapter.clear();
                }
                if (obj.data.goods != null) {
                    int size = obj.data.goods.size();
                    mStart += size;
                    mAdapter.addAll(obj.data.goods);
                    mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                } else {
                    mAdapter.setHasMore(false);
                }
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (hasCreatedView && isVisibleToUser) {
            refresh();
        }
    }
}
