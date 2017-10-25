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
 * 图片加载类
 *
 * @author LiuCongshan
 * @date 17-8-16
 */
public class ImageLoader {
    public static void displayImage(String url, ImageView view, @DrawableRes int resId) {
        GlideApp.with(AppInstance.getInstance())
                .load(url)
                .error(resId)
                .into(view);
    }

    public static void displayImage(String url, ImageView view) {
        displayImage(url, view, R.drawable.pic_default);
    }

    public static void displayMenu(String url, ImageView view) {
        GlideApp.with(AppInstance.getInstance())
                .load(url)
                .into(view);
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

    @BindingAdapter("banner")
    public static void loadBanner(final ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        GlideApp.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    @BindingAdapter("image_url")
    public static void loadImage(ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.pic_default);
            return;
        }
        GlideApp.with(imageView.getContext())
                .load(url)
                .error(R.drawable.pic_default)
                .into(imageView);
    }

    @BindingAdapter(value = {"image_url", "image_width", "image_height"})
    public static void loadImageFix(final ImageView imageView, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(R.drawable.pic_default);
            return;
        }
        final int viewWidth = AppInstance.getInstance().dp2px(width);
        final int viewHeight = AppInstance.getInstance().dp2px(height);
        GlideApp.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .transform(new OriginalTransform(viewWidth, viewHeight))
                .error(R.drawable.pic_default)
                .into(imageView);
    }
}
