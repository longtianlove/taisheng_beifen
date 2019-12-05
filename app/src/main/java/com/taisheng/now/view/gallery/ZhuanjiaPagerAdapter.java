package com.taisheng.now.view.gallery;

import android.content.Context;
//import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.taisheng.now.R;

import java.util.List;

/**
 * Created by dragon on 2019/6/28.
 */

public class ZhuanjiaPagerAdapter extends PagerAdapter {
    //上下文
    private Context mContext;
    //数据
    private List<String> mData;

    /**
     * 构造函数
     * 初始化上下文和数据
     *
     * @param context
     * @param list
     */
    public ZhuanjiaPagerAdapter(Context context, List<String> list) {
        mContext = context;
        mData = list;
    }

    /**
     * 返回要滑动的VIew的个数
     *
     * @return
     */
    @Override
    public int getCount() {
        return mData.size();
    }

    /**
     * 1.将当前视图添加到container中
     * 2.返回当前View
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.item_zhuanjia, null);

        container.addView(view);
        return view;
    }

    /**
     * 从当前container中删除指定位置（position）的View
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}