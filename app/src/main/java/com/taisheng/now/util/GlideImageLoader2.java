package com.taisheng.now.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by asus on 2019/5/22.
 */
public class GlideImageLoader2 extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load((Integer) path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
}
