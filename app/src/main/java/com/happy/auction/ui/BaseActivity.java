package com.happy.auction.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.happy.auction.R;
import com.happy.auction.databinding.IncludeToolbarBinding;

/**
 * Created by LiuCongshan on 17-8-16.
 * Activity基础类
 */

public class BaseActivity extends AppCompatActivity {


    class ToolbarBuilder {
        private IncludeToolbarBinding binding;

        /**
         * 初始化toolbar
         * @param binding IncludeToolbarBinding
         */
        ToolbarBuilder(IncludeToolbarBinding binding) {
            this.binding = binding;
            binding.ivToolbarLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        ToolbarBuilder setTitle(String title) {
            binding.tvToolbarTitle.setText(title);
            return this;
        }
    }


}
