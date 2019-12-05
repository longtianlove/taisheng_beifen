package com.taisheng.now.base;

import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.taisheng.now.view.chenjinshi.StatusBarUtil;


/**
 * Created by Administrator on 2015/6/17.
 */
public class BaseFragmentActivity extends FragmentActivity {
    private static String TAG = "BaseFragmentActivity";
    private ViewGroup m_titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setImageSystemBar();
    }

    public void setImageSystemBar(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);



        //沉浸式代码配置
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        //用来设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }



}
