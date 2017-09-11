package com.happy.auction.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.AuctionDetailJoinAdapter;
import com.happy.auction.databinding.ActivityAuctionDetailBinding;
import com.happy.auction.entity.AuctionDetail;
import com.happy.auction.entity.AuctionDetailParam;
import com.happy.auction.entity.CountdownEvent;
import com.happy.auction.entity.JoinRecord;
import com.happy.auction.glide.ImageLoader;

import java.util.Locale;

/**
 * 竞拍详情
 */
public class AuctionDetailActivity extends BaseActivity {
    private ActivityAuctionDetailBinding binding;
    private AuctionDetailJoinAdapter adapter;
    private AuctionDetail auctionDetail;

    private CountDownTimer timer;
    private AnimatorSet animator;

    public static Intent newIntent() {
        Intent intent = new Intent(AppInstance.getInstance(), AuctionDetailActivity.class);
        return intent;
    }

    public static Intent newIntent(String sid) {
        Intent intent = newIntent();
        intent.putExtra("sid", sid);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auction_detail);
        new ToolbarBuilder(binding.toolbar).setTitle("竞拍详情");

        initLayout();
    }

    private void initLayout() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        manager.setReverseLayout(true);//列表翻转
        binding.recyclerView.setLayoutManager(manager);
        adapter = new AuctionDetailJoinAdapter();
        binding.recyclerView.setAdapter(adapter);
    }

    private void initData() {
        if (auctionDetail.img != null && !auctionDetail.img.isEmpty()) {
            ImageLoader.displayOriginal(auctionDetail.img.get(0), binding.ivGoodsImage);
        }
        binding.tvGoodsName.setText(auctionDetail.good_name);

        String originalPrice = String.format(Locale.CHINA, "市场价：%s", auctionDetail.original_price);
        binding.tvOriginalPrice.setText(originalPrice);
    }

    private void setCurrentPrice() {
        String price = String.format(Locale.CHINA, "当前价：%.1f", auctionDetail.current_price);
        binding.tvCurrentPrice.setText(price);

        if (TextUtils.isEmpty(auctionDetail.current_bidder)) {
            binding.tvCurrentPerson.setText("");
        } else {
            binding.tvCurrentPerson.setText("当前出价人：" + auctionDetail.current_bidder);
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
        binding.tvJoinTimes.setText(auctionDetail.bid_times + "次");
    }

    private void setFinishView() {
        auctionDetail.status = 1;
        binding.tvAuctionStatus.setText("竞拍结束");
        binding.tvCurrentPerson.setText("竞拍成功者：" + auctionDetail.current_bidder);
        String price = String.format(Locale.CHINA, "成交价：%.1f", auctionDetail.current_price);
        binding.tvCurrentPrice.setText(price);
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
        auctionDetail.current_bidder = "";
        setNormalView();
        adapter.clear();
    }

    public void onClickJoin(View view) {
        if (auctionDetail.status == 1) {
//            restart();
            return;
        }

        JoinRecord item = new JoinRecord();
        auctionDetail.current_price += 0.1;

        AuctionDetailParam data = new AuctionDetailParam();
        data.uid = "不是本人";
        data.sid = "10086";

        auctionDetail.current_bidder = data.uid;
        auctionDetail.bid_times += 1;
        binding.tvJoinTimes.setText(auctionDetail.bid_times + "次");
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
                JoinRecord record = new JoinRecord();
                record.price = item.current_price;
                record.name = item.current_bidder;
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
                startTimer(auctionDetail.expire - System.currentTimeMillis());
            }
        });
    }
}
