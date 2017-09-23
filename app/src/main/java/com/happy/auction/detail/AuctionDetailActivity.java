package com.happy.auction.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.AuctionDetailBidAdapter;
import com.happy.auction.adapter.SpaceDecoration;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityAuctionDetailBinding;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.BidEvent;
import com.happy.auction.entity.DataResponse;
import com.happy.auction.entity.item.AuctionEndEvent;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.entity.param.AuctionDetailParam;
import com.happy.auction.entity.param.BaseRequest;
import com.happy.auction.entity.param.BidParam;
import com.happy.auction.main.login.LoginActivity;
import com.happy.auction.net.NetCallback;
import com.happy.auction.net.NetClient;
import com.happy.auction.utils.DebugLog;
import com.happy.auction.utils.GsonSingleton;
import com.happy.auction.utils.RxBus;
import com.happy.auction.utils.ToastUtil;

import java.lang.reflect.Type;

import io.reactivex.functions.Consumer;

/**
 * 竞拍详情
 */
public class AuctionDetailActivity extends BaseActivity {
    private static final int REQUEST_CODE_LOGIN = 100;

    private ActivityAuctionDetailBinding binding;
    private AuctionDetailBidAdapter adapter;
    private AuctionDetail auctionDetail;

    public static Intent newIntent(BaseGoods goods) {
        Intent intent = new Intent(AppInstance.getInstance(), AuctionDetailActivity.class);
        intent.putExtra("goods", goods);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auction_detail);
        initLayout();
    }

    private void initLayout() {
        binding.tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int offset = -binding.tvAuctionStatus.getTop();
                if (verticalOffset < offset) {
                    binding.tvToolbarPrice.setVisibility(View.VISIBLE);
                    binding.tvToolbarTime.setVisibility(View.VISIBLE);
                    int height = offset - verticalOffset;
                    ConstraintLayout.LayoutParams paramsPrice = (ConstraintLayout.LayoutParams) binding.tvToolbarPrice.getLayoutParams();
                    ConstraintLayout.LayoutParams paramsTime = (ConstraintLayout.LayoutParams) binding.tvToolbarTime.getLayoutParams();
                    int heightTitle = binding.tvToolbarTitle.getHeight();
                    if (height < heightTitle) {
                        paramsPrice.height = height;
                        paramsTime.height = height;
                    } else {
                        paramsPrice.height = heightTitle;
                        paramsTime.height = heightTitle;
                    }
                    binding.tvToolbarPrice.setLayoutParams(paramsPrice);
                    binding.tvToolbarTime.setLayoutParams(paramsTime);
                } else {
                    binding.tvToolbarPrice.setVisibility(View.GONE);
                    binding.tvToolbarTime.setVisibility(View.GONE);
                }
            }
        });

        adapter = new AuctionDetailBidAdapter();
        binding.recyclerView.setAdapter(adapter);
        SpaceDecoration decoration = new SpaceDecoration();
        decoration.enableHeader();
        binding.recyclerView.addItemDecoration(decoration);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);

        BaseGoods goods = (BaseGoods) getIntent().getSerializableExtra("goods");
        if (goods instanceof ItemGoods) {
            auctionDetail = new AuctionDetail((ItemGoods) goods);
        } else {
            auctionDetail = new AuctionDetail(goods);
        }

        initData();
        listenEvents();
        loadData();
    }

    private void initData() {
        binding.setData(auctionDetail);
        binding.tvAuctionStatus.setSyncView(binding.tvToolbarTime);
        if (auctionDetail.status == 0) {
            binding.tvAuctionStatus.finish();
        } else {
            binding.tvAuctionStatus.setExpireTime(auctionDetail.bid_expire_time);
            binding.tvAuctionStatus.setRepeat(auctionDetail.current_price == auctionDetail.bid_start_price);
        }

        if (auctionDetail.bid_records != null && !auctionDetail.bid_records.isEmpty()) {
            adapter.addAll(auctionDetail.bid_records);
            binding.setItem(auctionDetail.bid_records.get(0));
        }
    }

    private void listenEvents() {
        RxBus.getDefault().subscribe(this, BidEvent.class, new Consumer<BidEvent>() {
            @Override
            public void accept(BidEvent event) throws Exception {
                if (event.sid != auctionDetail.sid) return;
                DebugLog.e("bid : " + GsonSingleton.get().toJson(event));
                auctionDetail.setCurrentPrice(event.current_price);
                binding.tvAuctionStatus.setExpireTime(event.bid_expire_time);
                binding.tvAuctionStatus.setRepeat(false);

                BidRecord record = new BidRecord(event);
                record.avatar = "http://mobile-pic.cache.iciba.com/1486980953-8616_218-135-%E9%95%BF%E5%8F%91%E5%A4%96%E5%9B%BD%E5%A5%B3.jpg";
                binding.setItem(record);
                adapter.addItem(record);
            }
        });

        RxBus.getDefault().subscribe(this, AuctionEndEvent.class, new Consumer<AuctionEndEvent>() {
            @Override
            public void accept(AuctionEndEvent event) throws Exception {
                if (event.sid != auctionDetail.sid) return;
                auctionDetail.setStatus(0);
                binding.tvAuctionStatus.finish();
            }
        });
    }

    private void loadData() {
        AuctionDetailParam param = new AuctionDetailParam();
        param.sid = auctionDetail.sid;
        BaseRequest<AuctionDetailParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
                Type type = new TypeToken<DataResponse<AuctionDetail>>() {}.getType();
                DataResponse<AuctionDetail> obj = GsonSingleton.get().fromJson(response, type);
                auctionDetail = obj.data;
                initData();
            }
        });
    }

    public void onClickBid(View view) {
        if (auctionDetail.status == 0) {
            return;
        }

        if (!AppInstance.getInstance().isLogin()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, REQUEST_CODE_LOGIN);
            return;
        }

        BidParam param = new BidParam();
        param.sid = auctionDetail.sid;
        param.take_coin = param.buy;
        BaseRequest<BidParam> request = new BaseRequest<>(param);
        NetClient.query(request, new NetCallback() {
            @Override
            public void onSuccess(String response, String message) {
            }

            @Override
            public void onError(int code, String message) {
                super.onError(code, message);
                ToastUtil.show(message);
            }
        });
    }

    public void onClickNext(View view) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auctionDetail.status != 0) {
            binding.tvAuctionStatus.cancel();
        }
        RxBus.getDefault().unsubscribe(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_CODE_LOGIN) {
            onClickBid(null);
        }
    }
}
