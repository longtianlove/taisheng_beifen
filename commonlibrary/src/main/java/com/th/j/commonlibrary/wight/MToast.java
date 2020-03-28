package com.th.j.commonlibrary.wight;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.th.j.commonlibrary.R;

public class MToast {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private static MToast result;//当前类的对象
    //动画时间
    private final int ANIMATION_DURATION = 600;
    private static TextView mTextView;
    private ViewGroup container;
    private View v;
    //默认展示时间
    private int HIDE_DELAY = 2000;
    private LinearLayout mContainer;
    private AlphaAnimation mFadeOutAnimation;
    private AlphaAnimation mFadeInAnimation;
    private static boolean isShow = false;//用于标识是否显示
    private static Context mContext;
    private Handler mHandler = new Handler();

    private MToast(Context context) {
        mContext = context;

        container = (ViewGroup) ((Activity) context)
                .findViewById(android.R.id.content);

        v = ((Activity) context).getLayoutInflater().inflate(
                R.layout.etoast, container);
        mContainer = (LinearLayout) v.findViewById(R.id.mbContainer);//获取显示的布局
        mContainer.setVisibility(View.GONE);//大的布局隐藏
        mTextView = (TextView) v.findViewById(R.id.mbMessage);//获取显示的文本
    }

    public static MToast makeText(Context context, String message, int HIDE_DELAY) {
        if (result == null) {
            result = new MToast(context);
        } else {
            //这边主要是当切换Activity后我们应该更新当前持有的context，不然无法显示的
            if (!mContext.getClass().getName().equals(context.getClass().getName())) {
                result = new MToast(context);
            }else {
                isShow=false;
            }
        }
        //设置显示的时间
        if (HIDE_DELAY == LENGTH_LONG) {
            result.HIDE_DELAY = 2500;
        } else {
            result.HIDE_DELAY = 1500;
        }
        mTextView.setText(message);//设置显示的信息
        return result;
    }

    public static MToast makeText(Context context, int resId, int HIDE_DELAY) {
        String mes = "";
        try {
            mes = context.getResources().getString(resId);//根据id显示android中内置的内容
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return makeText(context, mes, HIDE_DELAY);
    }

    public void show() {
        if (isShow) {

            //如果已经显示，则再次显示不生效(就是不显示了)
            return;
        }
        isShow = true;//标识为显示
        //显示动画
        mFadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        //消失动画
        mFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnimation.setDuration(ANIMATION_DURATION);//设置消失动画的显示时间
        //监听消失动画的事件
        mFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShow = false;//动画开始后将动画状态改为false,消失动画执行的时候就将动画的状态改为false
            }

            //动画结束
            @Override
            public void onAnimationEnd(Animation animation) {
                //隐藏布局（没有remove主要是为了防止一个页面创建多次布局）
                mContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mContainer.setVisibility(View.VISIBLE);//显示
        mFadeInAnimation.setDuration(ANIMATION_DURATION);//设置显示动画的时间

        mContainer.startAnimation(mFadeInAnimation);//为布局设置开始的动画并开始

        mHandler.postDelayed(mHideRunnable, HIDE_DELAY);//在指定时间后开启线程,执行消失的动画
    }


    //线程
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mContainer.startAnimation(mFadeOutAnimation);//为布局设置结束的动画并开始
        }
    };

    /**
     * 销毁当前显示的类
     */
    public void cancel() {
        //如果当前为显示状态的话
        if (isShow) {
            isShow = false;//设置动画的状态为false
            mContainer.setVisibility(View.GONE);//隐藏显示的布局
            mHandler.removeCallbacks(mHideRunnable);//移除开启的线程
        }
    }

    //这个方法主要是为了解决用户在重启页面后单例还会持有上一个context，
//并且上面的mContext.getClass().getName()其实是一样的
//所以使用上还需在你们的BaseActivity的onDestroy()方法中调用该方法
    public static void reset() {
        result = null;
        isShow=false;
    }

    public void setText(CharSequence s) {
        if (result == null) return;
        TextView mTextView = (TextView) v.findViewById(R.id.mbMessage);
        if (mTextView == null)
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        mTextView.setText(s);
    }

    public void setText(int resId) {
        setText(mContext.getText(resId));
    }
}
