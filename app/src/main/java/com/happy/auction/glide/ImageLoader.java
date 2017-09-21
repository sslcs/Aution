package com.happy.auction.glide;


import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.happy.auction.AppInstance;
import com.happy.auction.R;

/**
 * Created by LiuCongshan on 17-8-16.
 * 图片加载类
 */
public class ImageLoader {
    public static void displayImage(String url, ImageView view, @DrawableRes int resId) {
        GlideApp.with(AppInstance.getInstance())
                .load(url)
                .placeholder(resId)
                .error(resId)
                .into(view);
    }

    public static void displayImage(String requestUrl, ImageView view) {
        displayImage(requestUrl, view, R.drawable.pic_default);
    }

    public static void displayOriginal(String url, final ImageView view) {
        GlideApp.with(AppInstance.getInstance())
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.pic_default)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        view.setImageBitmap(resource);
                    }
                });
    }

    @BindingAdapter("avatar")
    public static void loadAvatar(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.pic_default);
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.pic_default)
                .error(R.drawable.pic_default)
                .into(imageView);
    }

    @BindingAdapter("image_url")
    public static void loadImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.pic_default);
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.pic_default)
                .error(R.drawable.pic_default)
                .into(imageView);
    }
}
