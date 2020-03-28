package com.taisheng.now.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author 韩晓康
 * @date :2019/3/8  10:music
 * @description: 页面基类
 */
public abstract class BaseIvActivity extends BaseActivity {
    public TextView tvLeft;
    public TextView tvTitle;
    public TextView tvRight;
    public ImageView ivRight;
    public ImageView ivTitle;
    public ImageView ivRightT;
    public LinearLayout llTop;
    public FrameLayout flRight;
    public FrameLayout baseFramelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uiutils.setSoftInput(this);
//        ARouter.getInstance().inject(this);
        initView(); //初始化控件
        initPhoto(savedInstanceState);
        initData();//初始化数据
        addData(); //添加数据
        setStatusBar();//改变状态栏颜色
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
        View title = LayoutInflater.from(this).inflate(R.layout.top_title_iv, null);
        tvLeft = title.findViewById(R.id.tv_left);
        flRight = title.findViewById(R.id.fl_right);
        tvTitle = title.findViewById(R.id.tv_title);
        tvRight = title.findViewById(R.id.tv_right);
        ivRight = title.findViewById(R.id.iv_right);
        ivRightT = title.findViewById(R.id.iv_right_t);
        ivTitle = title.findViewById(R.id.iv_title);
        llTop = title.findViewById(R.id.ll_top);
        baseFramelayout = (FrameLayout) title.findViewById(R.id.base_framelayout2);
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
        StatusBarUtil.setTranslucentForImageView(this, 0, (View)null);//改变状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            StatusBarUtil.setLightMode(this);
        }
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
//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        overridePendingTransition(R.anim.enter_from_right, R.anim.out_to_left);
//
//    }

    /**
     * 退出动画
     */
    @Override
    public void finish() {
//        if (getCurrentFocus() != null) {
//            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                    .hideSoftInputFromWindow(getCurrentFocus()
//                                    .getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        super.finish();
//        overridePendingTransition(R.anim.enter_from_left, R.anim.out_to_right);
    }

    //初始化控件
    public abstract void initView();

    //初始化数据
    public abstract void initData();

    //添加数据
    public abstract void addData();

    // 改变标题
    public abstract void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle);

//    @Override
//    protected void attachBaseContext(Context base) {//适配中英文8.0
//        super.attachBaseContext(LanguageUtils.attachBaseContext(base));
//    }

    protected void initPhoto(Bundle savedInstanceState) {

    }
}
