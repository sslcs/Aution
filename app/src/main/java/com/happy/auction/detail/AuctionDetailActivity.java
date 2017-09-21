package com.happy.auction.detail;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.AuctionDetailBidAdapter;
import com.happy.auction.adapter.SpaceDecoration;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityAuctionDetailBinding;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.CountdownEvent;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.item.ItemGoods;
import com.happy.auction.glide.ImageLoader;

/**
 * 竞拍详情
 */
public class AuctionDetailActivity extends BaseActivity {
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

        ItemGoods goods = (ItemGoods) getIntent().getSerializableExtra("goods");
        auctionDetail = new AuctionDetail(goods);
        auctionDetail.bid_times = 10;
        binding.setData(auctionDetail);
        auctionDetail.bid_expire_time = System.currentTimeMillis() + 7000;
        binding.tvAuctionStatus.setExpireTime(auctionDetail.bid_expire_time);
        binding.tvAuctionStatus.setSyncView(binding.tvToolbarTime);
        binding.tvAuctionStatus.setRepeat(true);

        adapter = new AuctionDetailBidAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(new SpaceDecoration());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(manager);

        binding.appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < -binding.bgPrice.getTop()) {
                    binding.tvToolbarPrice.setVisibility(View.VISIBLE);
                    binding.tvToolbarTime.setVisibility(View.VISIBLE);
                    int height = -binding.bgPrice.getTop() - verticalOffset;
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.tvToolbarPrice.getLayoutParams();
                    ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams) binding.tvToolbarTime.getLayoutParams();
                    if (height < binding.tvToolbarTitle.getHeight()) {
                        params.height = height;
                        params2.height = height;
                    } else {
                        params.height = binding.tvToolbarTitle.getHeight();
                        params2.height = binding.tvToolbarTitle.getHeight();
                    }
                    binding.tvToolbarPrice.setLayoutParams(params);
                    binding.tvToolbarTime.setLayoutParams(params2);
                } else {
                    binding.tvToolbarPrice.setVisibility(View.GONE);
                    binding.tvToolbarTime.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        if (auctionDetail.icon != null) {
            ImageLoader.displayOriginal(auctionDetail.icon, binding.ivGoodsImage);
        }
        binding.tvGoodsName.setText(auctionDetail.title);
    }

    private void setCurrentPrice() {
        if (auctionDetail.bid_records != null && !auctionDetail.bid_records.isEmpty()) {
            binding.tvCurrentPerson.setText("当前出价人：" + auctionDetail.bid_records.get(0).username);
        } else {
            binding.tvCurrentPerson.setText("");
        }
    }


    public void onClickBid(View view) {
        if (auctionDetail.status == 0) {
            return;
        }

        BidRecord item = new BidRecord();
        item.bid_price = auctionDetail.current_price + 10;
        item.username = "我出" + item.bid_price;
        binding.tvAuctionStatus.setExpireTime(System.currentTimeMillis() + 10000);
        addBidRecord(item);

        auctionDetail.setCurrentPrice(auctionDetail.current_price + 10);
        binding.tvAuctionStatus.setRepeat(false);

        auctionDetail.setBidTimes(auctionDetail.bid_times + 1);
        setCurrentPrice();

        if (auctionDetail.bid_times % 5 == 0) {
            binding.tvAuctionStatus.finish();
        }
    }

    private void addBidRecord(BidRecord item) {
        auctionDetail.setUsername(item.username);
        adapter.addItem(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (auctionDetail.status != 0) {
            binding.tvAuctionStatus.finish();
        }
    }


    private void onEvent(final CountdownEvent item) {
        binding.tvAuctionStatus.setRepeat(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BidRecord record = new BidRecord();
//                record.bid_price = item.current_price;
//                record.name = item.username;
                adapter.addItem(record);
            }
        });
    }

    private void onEvent(AuctionDetail detail) {
        auctionDetail = detail;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }
}
