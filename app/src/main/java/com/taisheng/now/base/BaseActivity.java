package com.taisheng.now.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.taisheng.now.view.chenjinshi.StatusBarUtil;

import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by dragon on 2019/6/27.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    protected void onDestroy() {
//        NotificationCenter.INSTANCE.removeObserver(this);
        isDestoryed = true;
        super.onDestroy();
    }
    private boolean isDestoryed;

    /**
     * 是否销毁了
     *
     * @return 页面是否销毁掉  bug fixxed with umeng at 5.0.1 by yangwenxin
     */
    public boolean isDestroy() {
        return isDestoryed;
    }
}
