package com.happy.auction.detail;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.AuctionDetailBidAdapter;
import com.happy.auction.base.BaseActivity;
import com.happy.auction.databinding.ActivityAuctionDetailBinding;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.CountdownEvent;
import com.happy.auction.entity.item.BaseGoods;
import com.happy.auction.entity.item.BidRecord;
import com.happy.auction.entity.param.AuctionDetailParam;
import com.happy.auction.glide.ImageLoader;

import java.util.Locale;

/**
 * 竞拍详情
 */
public class AuctionDetailActivity extends BaseActivity {
    private ActivityAuctionDetailBinding binding;
    private AuctionDetailBidAdapter adapter;
    private AuctionDetail auctionDetail;

    private CountDownTimer timer;
    private AnimatorSet animator;

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
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        manager.setReverseLayout(true);//列表翻转
        binding.recyclerView.setLayoutManager(manager);
        adapter = new AuctionDetailBidAdapter();
        binding.recyclerView.setAdapter(adapter);

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

        float price = auctionDetail.market_price / 100.0f;
        String marketPrice = String.format(Locale.CHINA, "市场价：%.2f", price);
        binding.tvOriginalPrice.setText(marketPrice);
    }

    private void setCurrentPrice() {
        float price = auctionDetail.current_price / 100.0f;
        String currentPrice = String.format(Locale.CHINA, "当前价：%.2f", price);
        binding.tvCurrentPrice.setText(currentPrice);

        if (auctionDetail.bid_records != null && !auctionDetail.bid_records.isEmpty()) {
            binding.tvCurrentPerson.setText("当前出价人：" + auctionDetail.bid_records.get(0).username);
        } else {
            binding.tvCurrentPerson.setText("");
        }
    }

    private void startTimer(long time) {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(time, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000 / 60);
                int seconds = (int) (millisUntilFinished / 1000 % 60);
                int millis = (int) (millisUntilFinished % 1000 / 10);
                String time = String.format(Locale.CHINA, "%02d", minutes) +
                        ":" +
                        String.format(Locale.CHINA, "%02d", seconds) +
                        ":" +
                        String.format(Locale.CHINA, "%02d", millis);
                binding.tvAuctionStatus.setText(time);

                if (millisUntilFinished < 3050 && millisUntilFinished > 950 && millisUntilFinished % 1000 < 20) {
                    startAnimator();
                }
            }

            @Override
            public void onFinish() {
                binding.tvAuctionStatus.setText("00:00:00");
            }
        }.start();
    }

    private void startAnimator() {
        if (animator == null) {
            animator = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.tvAuctionStatus, "scaleX", 1, 2, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.tvAuctionStatus, "scaleY", 1, 2, 1);
            animator.playTogether(scaleX, scaleY);
        }

        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void setNormalView() {
        setCurrentPrice();
        binding.btnBottom.setText(R.string.detail_btn_bid);
        binding.tvBidTimes.setText(auctionDetail.bid_times + "次");
    }

    private void setFinishView() {
        auctionDetail.status = 1;
        binding.tvAuctionStatus.setText("竞拍结束");
        binding.tvCurrentPerson.setText("竞拍成功者：" + auctionDetail.username);
        float price = auctionDetail.current_price / 100.0f;
        String currentPrice = String.format(Locale.CHINA, "当前价：%.2f", price);
        binding.tvCurrentPrice.setText(currentPrice);
        binding.btnBottom.setText(R.string.detail_btn_go_latest);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void restart() {
        auctionDetail.current_price = 0;
        auctionDetail.bid_times = 0;
        auctionDetail.status = 0;
        auctionDetail.username = "";
        setNormalView();
        adapter.clear();
    }

    public void onClickBid(View view) {
        if (auctionDetail.status == 1) {
//            restart();
            return;
        }

        BidRecord item = new BidRecord();
        auctionDetail.current_price += 0.1;

        AuctionDetailParam data = new AuctionDetailParam();
        data.sid = "10086";

        auctionDetail.bid_times += 1;
        binding.tvBidTimes.setText(auctionDetail.bid_times + "次");
        setCurrentPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }


    private void onEvent(final CountdownEvent item) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BidRecord record = new BidRecord();
//                record.bid_price = item.current_price;
//                record.name = item.username;
                adapter.addItem(record);
                startTimer(item.expire - System.currentTimeMillis());
            }
        });
    }

    private void onEvent(AuctionDetail detail) {
        auctionDetail = detail;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initData();
                startTimer(auctionDetail.bid_expire_time - System.currentTimeMillis());
            }
        });
    }
}
