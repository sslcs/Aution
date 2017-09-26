package com.happy.auction.module.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.adapter.AdapterWrapper;
import com.happy.auction.adapter.SpaceDecoration;
import com.happy.auction.base.BaseAdapter;
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
    private AdapterWrapper<CategoryGoodsAdapter> adapter;
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
        adapterCategory.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
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

        final CategoryGoodsAdapter inner = new CategoryGoodsAdapter();
        inner.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ItemGoods item = inner.getItem(position);
                startActivity(AuctionDetailActivity.newIntent(item));
            }
        });
        adapter = new AdapterWrapper<>(inner);
        adapter.setLoadMoreListener(new AdapterWrapper.LoadMoreListener() {
            @Override
            public void loadMore() {
                loadData(currentIndex);
            }
        });
        binding.vList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.vList.setAdapter(adapter);
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
                adapter.setLoaded();
                Type type = new TypeToken<DataResponse<GoodsResponse>>() {}.getType();
                DataResponse<GoodsResponse> obj = GsonSingleton.get().fromJson(response, type);
                if (currentIndex == 0) adapter.getInnerAdapter().clear();

                int size = 0;
                if (obj.data.goods != null) {
                    size = obj.data.goods.size();
                    adapter.getInnerAdapter().addAll(obj.data.goods);
                }
                adapter.setHasMore(size >= BaseParam.DEFAULT_LIMIT);
                currentIndex += size;
            }
        });
    }
}
