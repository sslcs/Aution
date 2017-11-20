package com.happy.auction.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
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
import com.happy.auction.module.detail.AuctionDetailActivity;
import com.happy.auction.module.login.LoginActivity;
import com.happy.auction.module.main.WebActivity;
import com.happy.auction.module.message.MessageActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.EventAgent;
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
 * 首页
 *
 * @author LiuCongshan
 */
public class TabHomeFragment extends BaseFragment {
    private FragmentTabHomeBinding mBinding;
    private TabHomeAdapter mAdapter;
    private int mStart = 0;
    private String mType = GoodsParam.TYPE_HOT;
    private Disposable mDisposableRefresh;
    private PagingScrollHelper mPageHelper;

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
        mBinding.setCount(AppInstance.getInstance().mMessageCount);
        mBinding.setFragment(this);

        mBinding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.refreshView.setEnabled(verticalOffset >= 0);
            }
        });
        mBinding.refreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

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
                EventAgent.onEvent(R.string.home_detail);
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        mBinding.vList.setAdapter(mAdapter);
        mBinding.tvAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventAgent.onEvent(R.string.home_marquee);
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
                    EventAgent.onEvent(R.string.home_sticky_2);
                    if (!GoodsParam.TYPE_NEWBIE.equals(mType)) {
                        mType = GoodsParam.TYPE_NEWBIE;
                        refresh();
                    }
                } else {
                    EventAgent.onEvent(R.string.home_sticky_1);
                    if (!GoodsParam.TYPE_HOT.equals(mType)) {
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

        loadAnnounce();
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
                item.countdown = event.countdown;
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

        loadGoods();
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
                    ImageLoader.displayMenu(ivMenu[i], menu.icon);
                    final int finalI = i;
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openMenu(menu.title, menu.url);
                            EventAgent.onEvent(getString(R.string.home_menu_) + (finalI + 1));
                        }
                    };
                    tvMenu[i].setOnClickListener(listener);
                    ivMenu[i].setOnClickListener(listener);
                }
            }
        });
    }

    private void openMenu(final String title, final String url) {
        if (url.contains(WebActivity.PAGE_CHARGE) && !AppInstance.getInstance().isLogin()) {
            startActivity(LoginActivity.newIntent(new LoginActivity.OnLoginListener() {
                @Override
                public void onLogin() {
                    openMenu(title, url);
                }
            }));
            return;
        }

        Intent intent = WebActivity.newIntent(title, url);
        if (intent != null) {
            startActivity(intent);
        }
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
                        openMenu(item.title, item.url);
                        EventAgent.onEvent(getString(R.string.banner_) + (position + 1));
                    }
                });
                mBinding.rvBanner.setAdapter(adapter);
                if (obj.data != null && obj.data.size() > 0) {
                    mPageHelper = new PagingScrollHelper();
                    mPageHelper.setRecycleView(mBinding.rvBanner);
                    if (obj.data.size() > 1) {
                        mPageHelper.setIndicator(mBinding.circleIndicator);
                        mPageHelper.enableAutoScroll();
                        mBinding.circleIndicator.setVisibility(View.VISIBLE);
                        mBinding.circleIndicator.setCount(obj.data.size());
                    }
                }
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
                mBinding.refreshView.setRefreshing(false);
                if (mStart == 0) {
                    mAdapter.clear();
                }
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                int size = 0;
                if (obj.data != null && obj.data.goods != null && !obj.data.goods.isEmpty()) {
                    size = obj.data.goods.size();
                    mAdapter.addAll(obj.data.goods);
                    mStart = mAdapter.getLast().sid;
                }
                mAdapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                mAdapter.setLoaded();
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
                mBinding.refreshView.setRefreshing(false);
            }
        });
    }

    public void onClickMessage(View view) {
        EventAgent.onEvent(R.string.home_message);
        if (AppInstance.getInstance().isLogin()) {
            startActivity(MessageActivity.newIntent());
        } else {
            startActivity(LoginActivity.newIntent(new LoginActivity.OnLoginListener() {
                @Override
                public void onLogin() {
                    startActivity(MessageActivity.newIntent());
                }
            }));
        }
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
            EventAgent.onEvent(R.string.tab_1);
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            refresh();
            if (mPageHelper != null) {
                mPageHelper.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPageHelper != null) {
            mPageHelper.onPause();
        }
    }
}
