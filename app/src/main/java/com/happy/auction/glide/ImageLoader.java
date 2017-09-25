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
import com.happy.auction.utils.DebugLog;

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
//                .placeholder(R.drawable.pic_default)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        DebugLog.e("width: " + resource.getWidth() + " height: " + resource.getHeight());
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
                .error(R.drawable.pic_default)
                .into(imageView);
    }

    @BindingAdapter(value = {"image_url", "image_width", "image_height"})
    public static void loadImage2(final ImageView imageView, String url, int width, int height) {
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
