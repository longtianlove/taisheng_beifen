package com.taisheng.now.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by dragon on 2019/7/9.
 */

public class WithScrolleViewListView extends ListView {


    public WithScrolleViewListView(Context context) {
        super(context);
    }

    public WithScrolleViewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithScrolleViewListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
