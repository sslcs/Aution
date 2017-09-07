package com.happy.auction.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BaseActivity extends AppCompatActivity {
    public void onClickBack(View view) {
        onBackPressed();
    }
}
