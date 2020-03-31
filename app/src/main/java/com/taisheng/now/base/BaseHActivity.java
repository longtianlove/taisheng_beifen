package com.taisheng.now.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.taisheng.now.R;
import com.taisheng.now.util.Uiutils;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * @author 韩晓康
 * @date :2019/3/8  10:music
 * @description: 页面基类
 */
public abstract class BaseHActivity extends BaseActivity {
    public TextView tvLeft;
    public TextView tvTitle;
    public TextView tvRight;
    public ImageView ivRight;
    public ImageView ivTitle;
    public LinearLayout llTop;
    public FrameLayout baseFramelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uiutils.setSoftInput(this);
        initView(); //初始化控件
        initPhoto(savedInstanceState);
        setStatusBar();//改变状态栏颜色
        initData();//初始化数据
        addData(); //添加数据

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tvLeft != null && tvTitle != null && tvRight != null && ivRight != null && ivTitle != null) {
            setChangeTitle(tvLeft, tvTitle, tvRight, ivRight, ivTitle);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View title = LayoutInflater.from(this).inflate(R.layout.top_title, null);
        tvLeft = title.findViewById(R.id.tv_left);
        tvTitle = title.findViewById(R.id.tv_title);
        tvRight = title.findViewById(R.id.tv_right);
        ivRight = title.findViewById(R.id.iv_right);
        ivTitle = title.findViewById(R.id.iv_title);
        llTop = title.findViewById(R.id.ll_top);
        baseFramelayout = (FrameLayout) title.findViewById(R.id.base_framelayout);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 加载子类setContentView中传递进来的布局文件
        View childView = getLayoutInflater().inflate(layoutResID, null);
        // 将子类需要显示的内容放到flContent中进行显示
        baseFramelayout.addView(childView);
        super.setContentView(title);
    }

    /**
     * 改变状态蓝颜色
     */
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorWhite), 0);
        } else {
            llTop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            Drawable drawables = getResources().getDrawable(R.drawable.icon_back_new);
            drawables.setBounds(0, 0, drawables.getMinimumWidth(), drawables.getMinimumHeight());
            tvLeft.setCompoundDrawables(drawables, null, null, null);
            StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        }
//        StatusBarUtil.setTranslucentForImageView(this,0, null);//改变状态栏
        //设置状态栏上的字体为黑色-因为本页面是白色必须设置
//        UtilsStyle.statusBarLightMode(this);
//        StatusBarUtil.setLightMode(this);
//        StatusBarUtil.setDarkMode(this);
    }

    /**
     * 进入动画
     *
     * @param intent
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.out_to_left);
    }

    /**
     * 每次启动activity都会调用此方法
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode, options);

        }
    }

    private String mActivityJumpTag;        //activity跳转tag
    private long mClickTime;                //activity跳转时间

    /**
     * 检查是否重复跳转，不需要则重写方法并返回true
     */
    protected boolean checkDoubleClick(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mClickTime = SystemClock.uptimeMillis();
        return result;
    }

    /**
     * 退出动画
     */
    @Override
    public void finish() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        }
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
    }

    //初始化控件
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    //添加数据
    public abstract void addData();

    // 改变标题
    public abstract void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle);

    protected void initPhoto(Bundle savedInstanceState) {

    }

}
