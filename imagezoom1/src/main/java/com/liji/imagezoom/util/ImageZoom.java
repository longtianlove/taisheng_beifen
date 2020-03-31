package com.liji.imagezoom.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.liji.imagezoom.activity.ImagePagerActivity;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：liji on 2016/7/18 11:03
 * 邮箱：lijiwork@sina.com
 */
public class ImageZoom {
    
    /**
     * 跳转到图片预览页面
     *
     * @param context
     * @param positon 图片显示的页码
     * @param list    图片URL
     */
    public static void show(Context context, int positon, List<String> list) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, positon);
        context.startActivity(intent);
    }
    
    /**
     * 跳转到图片预览页面
     *
     * @param context
     * @param url     当前图片url
     * @param list    图片URL
     */
    public static void show(Context context, String url, List<String> list) {
        try {
            int positon = list.indexOf(url);
            Intent intent = new Intent(context, ImagePagerActivity.class);
            // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
            intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) list);
            intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, positon);
            context.startActivity(intent);
        }
        catch (Exception e) {
            Log.e("imagezoom", e.getMessage());
        }
    }
    
    /**
     * 跳转到大图预览，只有一张图
     * @param context
     * @param url
     */
    public static void show(Context context, String url) {
        try {
            if (!TextUtils.isEmpty(url)) {
                List<String> mImageUrls = new ArrayList<>();
                mImageUrls.add(url);
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mImageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                context.startActivity(intent);
            }
            else {
                Log.e("ImageZoom", "url is null");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到大图预览，只有一张图
     * @param context
     * @param url
     * @param type
     */
    public static void show(Context context, String url, int type) {
        try {
            if (!TextUtils.isEmpty(url)) {
                List<String> mImageUrls = new ArrayList<>();
                
                //本地相册图片
                if (type == ImageUrlType.LOCAL) {
                    if (url.contains("storage")) {
                        mImageUrls.add(ImageDownloader.Scheme.FILE.wrap(url));
                    }
                    else {
                        mImageUrls.add(url);
                    }
                }
                //drawable目录下面的图
                else if (type == ImageUrlType.DRAWABLE) {
                    mImageUrls.add("drawable://" + url);
                }
                else {
                    //网络图
                    mImageUrls.add(url);
                }
                
                Intent intent = new Intent(context, ImagePagerActivity.class);
                intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mImageUrls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                context.startActivity(intent);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
