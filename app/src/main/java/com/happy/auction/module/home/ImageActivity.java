package com.happy.auction.module.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.happy.auction.AppInstance;
import com.happy.auction.R;
import com.happy.auction.adapter.OnItemClickListener;
import com.happy.auction.base.BasePageActivity;
import com.happy.auction.databinding.ActivityImageBinding;

import java.util.ArrayList;

/**
 * 图片展示页面
 *
 * @author LiuCongshan
 * @date 17-11-3
 */

public class ImageActivity extends BasePageActivity {
    private static final String KEY_DATA = "DATA";
    private static final String KEY_SELECTION = "SELECTION";
    private ActivityImageBinding mBinding;

    public static Intent newIntent(ArrayList<String> data, int selection) {
        Intent intent = new Intent(AppInstance.getInstance(), ImageActivity.class);
        intent.putStringArrayListExtra(KEY_DATA, data);
        intent.putExtra(KEY_SELECTION, selection);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image);
        initLayout();
    }

    private void initLayout() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.vList.setLayoutManager(llm);
        PagingScrollHelper helper = new PagingScrollHelper();
        helper.setRecycleView(mBinding.vList);

        ArrayList<String> data = getIntent().getStringArrayListExtra(KEY_DATA);
        ImageAdapter adapter = new ImageAdapter(data);
        adapter.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String item, int position) {
                finish();
            }
        });
        mBinding.vList.setAdapter(adapter);

        if (data.size() > 1) {
            int selection = getIntent().getIntExtra(KEY_SELECTION, 0);
            final ImageView[] dot = new ImageView[]{mBinding.ivDot0, mBinding.ivDot1};
            mBinding.ivDot0.setVisibility(View.VISIBLE);
            mBinding.ivDot1.setVisibility(View.VISIBLE);
            if (selection == 1) {
                mBinding.ivDot1.setSelected(true);
                mBinding.vList.scrollToPosition(selection);
            } else {
                mBinding.ivDot0.setSelected(true);
            }
            helper.setOnPageChangedListener(new PagingScrollHelper.OnPageChangedListener() {
                @Override
                public void onPageChanged(int index) {
                    dot[0].setSelected(index == 0);
                    dot[1].setSelected(index == 1);
                }
            });
        }
    }
}
