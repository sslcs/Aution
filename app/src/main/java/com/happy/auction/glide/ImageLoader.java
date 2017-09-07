package com.happy.auction.glide;


import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
        if(TextUtils.isEmpty(url)){
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

//    public static void displayImage(String url, final ImageView view, final OnSuccessListener listener) {
//        Glide.with(AppInstance.getInstance().getApplicationContext())
//                .asBitmap()
//                .load(url)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        view.setImageBitmap(resource);
//                        listener.onSuccess();
//                        LogUtils.v(this,"onSuccess");
//                    }
//
//                    @Override
//                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                        super.onLoadFailed(e, errorDrawable);
//                        LogUtils.v(this,e);
//                    }
//                });
//    }
//
//    public interface OnSuccessListener{
//        void onSuccess();
//    }
//
//    public void loadRoundImage(String url, ImageView view) {
//        Glide.with(AppInstance.getInstance().getApplicationContext())
//                .load(url)
//                .bitmapTransform(new CropCircleTransformation(AppInstance.getInstance().getApplicationContext()))
//                .placeholder(R.drawable.ic_default_avatar)
//                .error(R.drawable.ic_default_avatar)
//                .dontAnimate()
//                .into(view);
//    }
//
//    public void loadImageDownloadOnly(final List<String> urlList, final OnResultListener onResultListener) {
//        if (urlList == null || urlList.size() == 0) return;
//        if (drawables == null) {
//            drawables = new ArrayList<>();
//        } else {
//            drawables.clear();
//        }
//
//        for (String url : urlList) {
//            Glide.with(AppInstance.getInstance().getApplicationContext())
//                    .load(url)
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                            BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
//                            drawables.addAll(bitmapDrawable);
//                            if (drawables.size() == urlList.size() && onResultListener != null) {
//                                onResultListener.onLoadingComplete(drawables);
//                            }
//                            Timber.d("onResourceReady -------------------- " + bitmapDrawable);
//                        }
//
//                        @Override
//                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                            super.onLoadFailed(e, errorDrawable);
//                            if (onResultListener != null) {
//                                onResultListener.onLoadFailed(e, errorDrawable);
//                            }
//                        }
//                    });
//        }
//    }
//
//    public void loadImageDownloadOnly(String url) {
//        Timber.d("load image " + url);
//        Glide.with(AppInstance.getInstance().getApplicationContext())
//                .load(url)
//                .downloadOnly(58, 58);
//    }
//
//    public interface OnResultListener {
//        void onLoadingComplete(List<Drawable> drawableList);
//
//        void onLoadFailed(Exception e, Drawable drawable);
//    }
}
