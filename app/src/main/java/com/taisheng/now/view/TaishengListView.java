package com.taisheng.now.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.taisheng.now.R;


/**
 * Created by long on 16/6/3.
 */
public class TaishengListView extends ListView implements AbsListView.OnScrollListener {

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnUpLoadListener mOnUpLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mLoadingFooter;
    /**
     * ListView的已经加载完毕的footer
     */
    private View mHasLoadAllFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    private TextView mLv_footer_loadall_text;

    /**
     * 标示是否有加载更多这个设置
     */
    private boolean ifHasLoadMore;

    private boolean mIfCanSide = true;//ListView是否可以滑动的标示

    /**
     * @param context
     */
    public TaishengListView(Context context) {
        this(context, null);
    }

    public TaishengListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mLoadingFooter = LayoutInflater.from(context).inflate(R.layout.taishenglistview_footer_loading, null,
                false);
        mHasLoadAllFooter = LayoutInflater.from(context).inflate(R.layout.taishenglistview_footer_loadall, null,
                false);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChinaHrListView);
        ifHasLoadMore = typedArray.getBoolean(R.styleable.ChinaHrListView_hasLoadMore, true);

//        LogUtil.i("lz", "ifHasLoadMore" + ifHasLoadMore);
        mLoadingFooter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mHasLoadAllFooter.setOnClickListener(null);
        mLv_footer_loadall_text = (TextView) mHasLoadAllFooter.findViewById(R.id.lv_footer_loadall_text);
        mListView = this;
        mListView.setOnScrollListener(this);

        this.addFooterView( LayoutInflater.from(context).inflate(R.layout.empty_headerview, null,
                false));

    }

    /*
         * (non-Javadoc)
         * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
         */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
//                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作,且有网络
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp() ;
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnUpLoadListener != null) {
            // 设置状态
            setLoading(true);
            //
            mOnUpLoadListener.onUpLoad();
        }
    }

    /**
     * 显示或者是隐藏正在加载的View
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.removeFooterView(mLoadingFooter);
            mListView.removeFooterView(mHasLoadAllFooter);
            mListView.addFooterView(mLoadingFooter);
        } else {
            if (mListView.getFooterViewsCount() > 0) {
                mListView.removeFooterView(mLoadingFooter);
            }
            mYDown = 0;
            mLastY = 0;
        }
//        LogUtil.i("lz", "setLoading count:" + mListView.getFooterViewsCount() + loading);
    }

    /**
     * 显示或者是隐藏正在加载的View
     * @param loading
     * @param ifSuccess 标示是否是加载成功，加载失败的话提示Toast；
     */
    public void setLoading(boolean loading,boolean ifSuccess) {

        if(!ifSuccess){
            //加载失败，Toast提示
//            ToastUtil.showShortToast("加载失败，请重试");
        }

        isLoading = loading;
        if (isLoading) {
            mListView.removeFooterView(mLoadingFooter);
            mListView.removeFooterView(mHasLoadAllFooter);
            mListView.addFooterView(mLoadingFooter);
        } else {
            if (mListView.getFooterViewsCount() > 0) {
                mListView.removeFooterView(mLoadingFooter);
            }
            mYDown = 0;
            mLastY = 0;
        }
//        LogUtil.i("lz", "setLoading count:" + mListView.getFooterViewsCount() + loading);
    }

    //设置ListView是否可以滑动
    public void setListViewSlide(boolean ifCanSide){
        mIfCanSide = ifCanSide;
    }

    //ListView与ScrollView连用问题，C端首页
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        if(mIfCanSide){
            //ListView可以滑动
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else{
            super.onMeasure(widthMeasureSpec, expandSpec);
        }
    }


    /**
     * 显示或者是隐藏加载完成的View
     *
     * @param loading
     */
    public void setLoadAllFooterVisible(boolean loading) {
        if (loading) {
            mListView.removeFooterView(mLoadingFooter);
            mListView.removeFooterView(mHasLoadAllFooter);
            mListView.addFooterView(mHasLoadAllFooter);
        } else {
            mListView.removeFooterView(mHasLoadAllFooter);
        }
//        LogUtil.i("lz", "setLoadAllFooterVisible count:" + mListView.getFooterViewsCount() + loading);
    }

    /**
     * 设置加载完成的View的点击事件
     *
     * @param onClickListener
     */
    public void setOnLoadAllViewClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            mHasLoadAllFooter.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置是否还有上拉加载；
     *
     * @param flag true 有上拉加载。
     */
    public void setHasLoadMore(boolean flag){
        ifHasLoadMore = flag;
    }

    /**
     * 设置加载完成的View的Text；
     *
     * @param text
     */
    public void setLoadAllViewText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mLv_footer_loadall_text.setText(text);
    }

    /**
     * 设置加载完成的view的textcolor和textsize
     */
    public void setLoadAllViewTextColorAndTextsize(int color,float size){
        mLv_footer_loadall_text.setTextColor(color);
        mLv_footer_loadall_text.setTextSize(size);
    }
    private OnScrollListener onScrollListener;
    public void setOnMScrollListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    /**
     * @param loadListener
     */
    public void setOnUpLoadListener(OnUpLoadListener loadListener) {
        mOnUpLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(onScrollListener!=null){
            onScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad() && ifHasLoadMore) {
//            if(Constants.Check_Net){
                // 有网才去加载
                loadData();
//            }else{
//                //没网提示
//                ToastUtil.showShortToast(getContext().getString(R.string.nonet));
//            }
//            LogUtil.i("lz","onScroll");
        }
        if(onScrollListener!=null){
            onScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnUpLoadListener {
        public void onUpLoad();
    }

}
