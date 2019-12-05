package com.taisheng.now.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by dragon on 2019/7/10.
 */

public class WithListViewScrollView extends ScrollView {
    public WithListViewScrollView(Context context) {
        super(context);
    }

    public WithListViewScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithListViewScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //解决ScrollView内嵌套的ListView加载完数据后，自动滚动ListView所在位置的问题。
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    private float mLastXIntercept = 0f;
    private float mLastYIntercept = 0f;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        float x = ev.getX();
        float y = ev.getY();
        int action = ev.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                intercepted = false;
                //初始化mActivePointerId
                super.onInterceptTouchEvent(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                //横坐标位移增量
                float deltaX = x - mLastXIntercept;
                //纵坐标位移增量
                float deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) < Math.abs(deltaY)) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
        }
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }



}
