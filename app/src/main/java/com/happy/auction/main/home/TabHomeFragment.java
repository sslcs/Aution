package com.happy.auction.main.home;

import android.os.Bundle;
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
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.RequestEvent;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.item.ItemMenu;
import com.happy.auction.entity.param.AnnounceParam;
import com.happy.auction.entity.param.BannerParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.GoodsParam;
import com.happy.auction.entity.param.MenuParam;
import com.happy.auction.glide.ImageLoader;
import com.happy.auction.net.ResponseHandler;
import com.happy.auction.detail.AuctionDetailActivity;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 首页
 */
public class TabHomeFragment extends Fragment {
    private FragmentTabHomeBinding binding;
    private CustomAdapter<TabHomeAdapter> adapter;
    private int start;

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
        loadData();

        binding.vList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.vList.addItemDecoration(new Decoration());
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
            public void onItemClick(View view, int position, Object item) {
                ItemGoods goods = (ItemGoods) item;
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

        Observable.interval(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ItemGoods goods = adapter.getInnerAdapter().getItem(5);
                        goods.current_price += 10;
                        int position = adapter.getInnerAdapter().getPosition(goods);
                        if (position == -1) return;
                        adapter.getInnerAdapter().setAnimatePosition(position);
                        adapter.getInnerAdapter().refresh(position, goods);
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
        RxBus.getDefault().post(new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {

            }
        }));
    }

    private void loadMenu() {
        MenuParam menu = new MenuParam();
        BaseRequest<MenuParam> request = new BaseRequest<>(menu);
        RxBus.getDefault().post(new RequestEvent<>(request, new ResponseHandler() {
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
        }));
    }

    private void loadBanner() {
        BannerParam banner = new BannerParam();
        BaseRequest<BannerParam> request = new BaseRequest<>(banner);
        RxBus.getDefault().post(new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {

            }
        }));
    }

    private void loadGoods(int start) {
        this.start = start;
        GoodsParam goods = new GoodsParam();
        goods.type = GoodsParam.TYPE_HOT;
        goods.start = start;
        BaseRequest<GoodsParam> request = new BaseRequest<>(goods);
        RxBus.getDefault().post(new RequestEvent<>(request, new ResponseHandler() {
            @Override
            public void onSuccess(String response, String message) {
                ArrayList<ItemGoods> data = new ArrayList<>(3);
                for (int i = 0; i < 30; i++) {
                    ItemGoods item = new ItemGoods();
                    item.title = i + "佳能Canon EOS 800D高配牛逼哄哄带闪电";
                    item.status = i;
//                    item.current_price = 100 + i;
                    item.gid = String.valueOf(i);
                    item.bid_expire_time = System.currentTimeMillis() + i * 1000;
                    data.add(item);
                }
                adapter.getInnerAdapter().addAll(data);
                adapter.notifyDataSetChanged();

//                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
//                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
//                adapter.getInnerAdapter().addAll(obj.data.goods);
//                adapter.setHasMore(obj.data.goods != null && obj.data.goods.size() >= BaseParam.DEFAULT_LIMIT);
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RxBus.getDefault().unsubscribe(this);
    }
}
