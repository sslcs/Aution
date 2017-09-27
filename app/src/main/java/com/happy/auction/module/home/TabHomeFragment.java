package com.happy.auction.module.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.databinding.FragmentTabHomeBinding;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.event.BidEvent;
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
 * 首页
 */
public class TabHomeFragment extends Fragment {
    private FragmentTabHomeBinding binding;
    private TabHomeAdapter adapter;
    private int start;
    private String goods_type = GoodsParam.TYPE_HOT;
    private Disposable disposableRefresh;

    public TabHomeFragment() {
    }

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentTabHomeBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        binding.vList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.vList.addItemDecoration(new HomeDecoration());
        binding.vList.setItemAnimator(null);
        adapter = new TabHomeAdapter();
        adapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadGoods(start + GoodsParam.DEFAULT_LIMIT);
                binding.vList.stopScroll();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemGoods goods = adapter.getItem(position);
                startActivity(AuctionDetailActivity.newIntent(goods));
            }
        });
        binding.vList.setAdapter(adapter);
        binding.tvAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag() == null) return;
                ItemLatest item = (ItemLatest) view.getTag();
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    if (!GoodsParam.TYPE_NEWBIE.equals(goods_type)) {
                        adapter.clear();
                        goods_type = GoodsParam.TYPE_NEWBIE;
                        loadGoods(1);
                    }
                } else {
                    if (!GoodsParam.TYPE_HOT.equals(goods_type)) {
                        adapter.clear();
                        goods_type = GoodsParam.TYPE_HOT;
                        loadGoods(1);
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

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = adapter.getPosition(item);
                if (position == -1) return;
                item = adapter.getItem(position);
                item.current_price = event.current_price;
                item.bid_expire_time = event.bid_expire_time;
                adapter.addChangedPosition(position);
                adapter.notifyItemChanged(position);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = adapter.getPosition(item);
                if (position == -1) return;
                item = adapter.getItem(position);
                item.setStatus(0);
                adapter.notifyItemChanged(position);

                if (disposableRefresh == null || disposableRefresh.isDisposed()) {
                    disposableRefresh = Observable.timer(10, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    loadGoods(0);
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

        loadGoods(0);
    }

    private void loadAnnounce() {
        AnnounceParam param = new AnnounceParam();
        BaseRequest<AnnounceParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemLatest>>>() {}.getType();
                DataResponse<ArrayList<ItemLatest>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) return;
                binding.tvAnnounce.addData(obj.data);
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
                if (obj.data == null || obj.data.isEmpty()) return;
                int length = obj.data.size();
                ImageView[] ivMenu = new ImageView[]{binding.menu.ivMenu0, binding.menu.ivMenu1, binding.menu.ivMenu2, binding.menu.ivMenu3};
                TextView[] tvMenu = new TextView[]{binding.menu.tvMenu0, binding.menu.tvMenu1, binding.menu.tvMenu2, binding.menu.tvMenu3};
                for (int i = 0; i < length && i < 4; i++) {
                    ItemMenu menu = obj.data.get(i);
                    tvMenu[i].setText(menu.title);
                    ImageLoader.displayImage(menu.icon, ivMenu[i]);
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

            }
        });
    }

    private void loadGoods(final int start) {
        this.start = start;
        GoodsParam param = new GoodsParam();
        param.type = goods_type;
        param.start = start;
        BaseRequest<GoodsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (start == 0) adapter.clear();
                adapter.addAll(obj.data.goods);
                adapter.setHasMore(obj.data.goods != null && obj.data.goods.size() >= BaseParam.DEFAULT_LIMIT);
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
}
