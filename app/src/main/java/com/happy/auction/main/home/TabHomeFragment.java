package com.happy.auction.main.home;

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
import com.happy.auction.adapter.AdapterWrapper;
import com.happy.auction.adapter.CustomAdapter;
import com.happy.auction.base.BaseAdapter;
import com.happy.auction.databinding.FragmentTabHomeBinding;
import com.happy.auction.detail.AuctionDetailActivity;
import com.happy.auction.entity.event.BidEvent;
import com.happy.auction.entity.response.DataResponse;
import com.happy.auction.entity.event.AuctionEndEvent;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemMenu;
import com.happy.auction.entity.param.AnnounceParam;
import com.happy.auction.entity.param.BannerParam;
import com.happy.auction.entity.param.BaseParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.GoodsParam;
import com.happy.auction.entity.param.MenuParam;
import com.happy.auction.entity.response.GoodsResponse;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * 首页
 */
public class TabHomeFragment extends Fragment {
    private FragmentTabHomeBinding binding;
    private CustomAdapter<TabHomeAdapter> adapter;
    private int start;
    private String goods_type = GoodsParam.TYPE_HOT;

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
        adapter = new CustomAdapter<>(new TabHomeAdapter());
        adapter.setLoadMoreListener(new AdapterWrapper.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadGoods(start + GoodsParam.DEFAULT_LIMIT);
                binding.vList.stopScroll();
            }
        });
        adapter.getInnerAdapter().setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemGoods goods = adapter.getInnerAdapter().getItem(position);
                startActivity(AuctionDetailActivity.newIntent(goods));
            }
        });
        binding.vList.setAdapter(adapter);
        binding.tvAnnounce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvAnnounce.setText("124sdflsjlfdjslajdflasldfjsaldfjlsldflsdfldfjsaldfjlsldflsdfldfjsaldfjlsldflsdf");
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    if (!GoodsParam.TYPE_NEWBIE.equals(goods_type)) {
                        adapter.getInnerAdapter().clear();
                        goods_type = GoodsParam.TYPE_NEWBIE;
                        loadGoods(1);
                    }
                } else {
                    if (!GoodsParam.TYPE_HOT.equals(goods_type)) {
                        adapter.getInnerAdapter().clear();
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
                int position = adapter.getInnerAdapter().getPosition(item);
                if (position == -1) return;
                item = adapter.getInnerAdapter().getItem(position);
                item.current_price = event.current_price;
                item.bid_expire_time = event.bid_expire_time;
                adapter.getInnerAdapter().addChangedPosition(position);
                adapter.notifyItemChanged(position);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                ItemGoods item = new ItemGoods();
                item.sid = event.sid;
                int position = adapter.getInnerAdapter().getPosition(item);
                if (position == -1) return;
                item = adapter.getInnerAdapter().getItem(position);
                item.setStatus(0);
                adapter.notifyItemChanged(position);
            }
        });
    }

    private void loadData() {
        loadBanner();

        loadMenu();

        loadAnnounce();

        loadGoods(1);
    }

    private void loadAnnounce() {
        AnnounceParam announce = new AnnounceParam();
        BaseRequest<AnnounceParam> request = new BaseRequest<>(announce);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {

            }
        });
    }

    private void loadMenu() {
        MenuParam menu = new MenuParam();
        BaseRequest<MenuParam> request = new BaseRequest<>(menu);
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
        BannerParam banner = new BannerParam();
        BaseRequest<BannerParam> request = new BaseRequest<>(banner);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {

            }
        });
    }

    private void loadGoods(int start) {
        this.start = start;
        GoodsParam goods = new GoodsParam();
        goods.type = goods_type;
        goods.start = start;
        BaseRequest<GoodsParam> request = new BaseRequest<>(goods);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                adapter.getInnerAdapter().addAll(obj.data.goods);
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
