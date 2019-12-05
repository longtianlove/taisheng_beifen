package com.taisheng.now.view.banner;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
//import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.R;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.ScreenUtil;


import java.util.ArrayList;

//import android.util.Log;

/**
 * Created by long on 2016/3/21.
 */
public class BannerViewPager {
    private static final float SCALEH = 312f / 750f;
    Context mcontext;
    View mview;
    public ViewPagerAdapter madapter;
   public ImageHandler mimageHandler;
    ViewPager.OnPageChangeListener mlistener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (msize > 1) {
//                Log.e("longlonglong++position", position + "*****");
                setTipImageBackground(position % msize);
                mimageHandler.sendMessage(Message.obtain(mimageHandler, ImageHandler.MSG_PAGE_CHANGED, position - 1, 0));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (msize > 1) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING://手动
                        mimageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);//暂停轮播
                        setmScrollSpeed(100);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE://自动轮播
                        mimageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        setmScrollSpeed(500);
                        break;
                    default:
                        break;
                }
            }
        }
    };
    ViewPager mviewPager;
    ViewGroup mgroup;
    ImageView[] mtips;
    SimpleDraweeView[] mbannerItems;
    ViewPagerItemListener mviewPagerItemListener;
    ArrayList<String> mpictureUrls;
    int msize = 2;

    public BannerViewPager(Context context) {
        mcontext = context;
        mview = View.inflate(mcontext, R.layout.header_banner_recommend, null);
        mgroup = (ViewGroup) mview.findViewById(R.id.viewGroup);
        mviewPager = (ViewPager) mview.findViewById(R.id.viewPager);

    }

    public void setPictureUrls(ArrayList<String> pictureUrls) {
        mpictureUrls = pictureUrls;
        msize = pictureUrls.size();
        mgroup.removeAllViews();
        if(mimageHandler!=null) {
            mimageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);//暂停轮播
        }
        init();
    }


    public void setLocalPictureIds(){
        msize = 3;
        mgroup.removeAllViews();
        if(mimageHandler!=null) {
            mimageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);//暂停轮播
        }
        initLocal();
    }

    void init() {

        if (msize > 1) {
            mtips = new ImageView[msize];
            for (int i = 0; i < msize; i++) {
                ImageView imageView = new ImageView(mcontext);
                mtips[i] = imageView;
                if (i == 0) {
                    mtips[i].setBackgroundResource(R.drawable.banner_indicator_focused);
                } else {
                    mtips[i].setBackgroundResource(R.drawable.banner_indicator_unfocused);
                }
                final float scale = mcontext.getResources().getDisplayMetrics().density;
                int width = (int) (8 * scale + 0.5f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width, width));
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                layoutParams.bottomMargin = 10;
                mgroup.addView(imageView, layoutParams);
            }
        }

        mbannerItems = new SimpleDraweeView[msize];
        for (int i = 0; i < msize; i++) {
            mbannerItems[i] = new SimpleDraweeView(mcontext);
            mbannerItems[i].setScaleType(ImageView.ScaleType.FIT_XY);
            Uri uri = Uri.parse(mpictureUrls.get(i));
            mbannerItems[i].setImageURI(uri);
        }
        madapter = new ViewPagerAdapter(mbannerItems, mpictureUrls, mcontext, mviewPager, mviewPagerItemListener);
        mviewPager.setAdapter(madapter);
        madapter.setmScrollSpeed(500);
        mviewPager.setOnPageChangeListener(mlistener);
        mimageHandler = new ImageHandler(this, 0);
        if (msize > 1)
            mimageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_FIRST, ImageHandler.MSG_DELAY);

        int height = (int) (SCALEH * ScreenUtil.getScreenW(SampleAppLike.mcontext));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        mviewPager.setLayoutParams(layoutParams);
    }

    void initLocal() {

        if (msize > 1) {
            mtips = new ImageView[msize];
            for (int i = 0; i < msize; i++) {
                ImageView imageView = new ImageView(mcontext);
                mtips[i] = imageView;
                if (i == 0) {
                    mtips[i].setBackgroundResource(R.drawable.banner_indicator_focused);
                } else {
                    mtips[i].setBackgroundResource(R.drawable.banner_indicator_unfocused);
                }
                final float scale = mcontext.getResources().getDisplayMetrics().density;
//                int width = (int) (8 * scale + 0.5f);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(mcontext,4), DensityUtil.dip2px(mcontext,3)));
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                layoutParams.bottomMargin = 10;
                mgroup.addView(imageView, layoutParams);
            }
        }

        mbannerItems = new SimpleDraweeView[msize];
        int[] ids=new int[]{R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
        for (int i = 0; i < msize; i++) {
            mbannerItems[i] = new SimpleDraweeView(mcontext);
            mbannerItems[i].setScaleType(ImageView.ScaleType.FIT_XY);
            ViewPager.LayoutParams lp=new ViewPager.LayoutParams();
            lp.width= ViewGroup.LayoutParams.MATCH_PARENT;
            lp.height=ViewGroup.LayoutParams.MATCH_PARENT;
            mbannerItems[i].setLayoutParams(lp);
//            Uri uri = Uri.parse(mpictureUrls.get(i));
            mbannerItems[i].setBackground(mcontext.getResources().getDrawable(ids[i]));
        }
        madapter = new ViewPagerAdapter(mbannerItems, mpictureUrls, mcontext, mviewPager, mviewPagerItemListener);
        mviewPager.setAdapter(madapter);
        madapter.setmScrollSpeed(500);
        mviewPager.setOnPageChangeListener(mlistener);
        mimageHandler = new ImageHandler(this, 0);
        if (msize > 1)
            mimageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_FIRST, ImageHandler.MSG_DELAY);

        int height = (int) (SCALEH * ScreenUtil.getScreenW(SampleAppLike.mcontext));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        mviewPager.setLayoutParams(layoutParams);
    }


    public View getContentView() {//获取当前页面用来设置listview的header
        return mview;
    }

    public void setOnItemClickListener(ViewPagerItemListener listener) {//设置轮播item点击事件
        madapter.mViewPagerItemListener = listener;
    }

    public void setmScrollSpeed(int duration) {
        madapter.setmScrollSpeed(duration);
    }

    private void setTipImageBackground(int selectionitems) {//设置下面的点点
        for (int i = 0; i < mtips.length; i++) {
            if (i == selectionitems) {
                mtips[i].setBackgroundResource(R.drawable.banner_indicator_focused);
            } else {
                mtips[i].setBackgroundResource(R.drawable.banner_indicator_unfocused);
            }
        }
    }
    //    public abstract  void getBannerItems();

    public interface ViewPagerItemListener {
        void onViewPagerItemClick(int position);
    }
}
