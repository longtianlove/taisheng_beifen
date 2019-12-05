package com.taisheng.now.view.refresh;

import android.content.Context;
import android.util.AttributeSet;


import com.taisheng.now.R;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by long on 2017/4/25.
 */

public class MaterialDesignPtrFrameLayout extends PtrFrameLayout {
    private PtrFrameLayout mPtrFrameLayout;

    /**
     * @param context
     */
    public MaterialDesignPtrFrameLayout(Context context) {
        this(context, null);
    }

    public MaterialDesignPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPtrFrameLayout = this;

        // header
        final TaiShengMaterialHeader header = new TaiShengMaterialHeader(getContext());
        int[] colors = getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new LayoutParams(-1, -2));
        header.setPadding(0, 20, 0, 20);
        header.setPtrFrameLayout(mPtrFrameLayout);

        mPtrFrameLayout.setLoadingMinTime(700);
        mPtrFrameLayout.setDurationToCloseHeader(300);
        mPtrFrameLayout.setHeaderView(header);
        mPtrFrameLayout.addPtrUIHandler(header);
        mPtrFrameLayout.setPinContent(true);//设置内容不动。

        //页面加载时候自动刷新
//        mPtrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mPtrFrameLayout.autoRefresh(true);
//            }
//        }, 100);
        mPtrFrameLayout.setResistance(1.5f);//阻尼系数 默认: 1.7f，越大，感觉下拉时越吃力。
        disableWhenHorizontalMove(true);
    }
}
