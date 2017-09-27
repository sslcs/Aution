package com.happy.auction.module.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.LoadMoreListener;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.adapter.SpaceDecoration;
import com.happy.auction.databinding.FragmentTabCategoryBinding;
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

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 商品分类
 */
public class TabCategoryFragment extends Fragment {
    private FragmentTabCategoryBinding binding;
    private CategoryAdapter adapterCategory;
    private CategoryGoodsAdapter adapterGoods;
    private int currentIndex;
    private ItemCategory currentCategory;

    public TabCategoryFragment() {
    }

    public static TabCategoryFragment newInstance() {
        return new TabCategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        binding = FragmentTabCategoryBinding.inflate(inflater);
        initLayout();
        return binding.getRoot();
    }

    private void initLayout() {
        adapterCategory = new CategoryAdapter();
        adapterCategory.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == adapterCategory.getSelectedPosition()) return;

                adapterCategory.setSelectedPosition(position);
                currentCategory = adapterCategory.getItem(position);
                loadData(0);
            }
        });
        binding.vCategory.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vCategory.setAdapter(adapterCategory);
        binding.vCategory.addItemDecoration(new SpaceDecoration());

        adapterGoods = new CategoryGoodsAdapter();
        adapterGoods.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemGoods item = adapterGoods.getItem(position);
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        adapterGoods.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(currentIndex);
            }
        });
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.setAdapter(adapterGoods);
        binding.vList.addItemDecoration(new SpaceDecoration());

        loadCategory();
    }

    private void loadCategory() {
        CategoryParam param = new CategoryParam();
        BaseRequest<CategoryParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<ArrayList<ItemCategory>>>() {}.getType();
                DataResponse<ArrayList<ItemCategory>> obj = GsonSingleton.get().fromJson(response, type);
                if (obj.data == null || obj.data.isEmpty()) return;
                adapterCategory.addAll(obj.data);
                currentCategory = adapterCategory.getItem(0);
                loadData(0);
            }
        });
    }

    private void loadData(int index) {
        currentIndex = index;
        GoodsParam param = new GoodsParam();
        param.tid = currentCategory.tid;
        param.start = index;
        BaseRequest<GoodsParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                adapterGoods.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (currentIndex == 0) adapterGoods.clear();

                int size = 0;
                if (obj.data.goods != null) {
                    size = obj.data.goods.size();
                    adapterGoods.addAll(obj.data.goods);
                }
                adapterGoods.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                currentIndex += size;
            }
        });
    }
}
