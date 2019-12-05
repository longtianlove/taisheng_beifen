package com.taisheng.now.view.banner;

import android.content.Context;
import android.net.Uri;
//import android.support.v4.view.PagerAdapter;
//import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 16/3/17.
 * 头部bannerViewPagerAdapter
 */
public class ViewPagerAdapter extends PagerAdapter {
    List<ImageView> mImageViewsArray;
//    ImageView[] mImageViews;
    ArrayList<String> mpictureUrls;
    Context mcontext;
    ViewPager mviewPager;
    int msize=1;
    public BannerViewPager.ViewPagerItemListener mViewPagerItemListener;
    ViewPagerAdapter(ImageView[] imageViews, ArrayList<String> pictureUrls, Context context, ViewPager viewPager, BannerViewPager.ViewPagerItemListener Listener) {
        mImageViewsArray=new ArrayList<ImageView>();
        for(int i=0;i<imageViews.length;i++){
            mImageViewsArray.add(imageViews[i]);
        }
        mpictureUrls=pictureUrls;
        mcontext = context;
        mviewPager=viewPager;
        msize=mImageViewsArray.size();
        if(msize==2){
            doSizeIs2();
        }
//        mImageViews= (ImageView[]) mImageViewsArray.toArray();
        mViewPagerItemListener=Listener;
    }
    //对两张单独处理
    public void doSizeIs2(){
        SimpleDraweeView imageView3=  new SimpleDraweeView(mcontext);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);
        SimpleDraweeView imageView4=new SimpleDraweeView(mcontext);
        imageView4.setScaleType(ImageView.ScaleType.FIT_XY);
        Uri uri1 = Uri.parse(mpictureUrls.get(0));
        imageView3.setImageURI(uri1);
        Uri uri2 = Uri.parse(mpictureUrls.get(1));
        imageView4.setImageURI(uri2);
        mImageViewsArray.add(imageView3);
        mImageViewsArray.add(imageView4);
    }

    @Override
    public int getCount() {
        return msize==1?msize:Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        if (mImageViews.length > 3)
//            ((ViewPager) container).removeView(mImageViews[position % mImageViews.length]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        if (position<0){
            position = msize+position;
        }
        final int mposition = position;
        if (mImageViewsArray.get(mposition % mImageViewsArray.size()).getParent() != null) {
            ((ViewPager) mImageViewsArray.get(mposition % mImageViewsArray.size()).getParent()).removeView(mImageViewsArray.get(mposition % mImageViewsArray.size()));
        }
        ((ViewPager) container).addView(mImageViewsArray.get(mposition % mImageViewsArray.size()));
        mImageViewsArray.get(mposition % mImageViewsArray.size()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPagerItemListener.onViewPagerItemClick(mposition % msize);
                /** by    yangwenxin
                 * 上线将此点击事件的提示去掉
                 */
//                ToastUtil.showShortToast(mcontext, (mposition % msize) + "");
            }
        });
        return mImageViewsArray.get(mposition % mImageViewsArray.size());
    }
    public void setmScrollSpeed(int duration){
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mcontext,
                    new AccelerateInterpolator());
            field.set(mviewPager, scroller);
            scroller.setmDuration(duration);
        }catch (Exception e){

        }
    }
}