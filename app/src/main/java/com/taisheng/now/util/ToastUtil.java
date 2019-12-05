package com.taisheng.now.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.taisheng.now.SampleAppLike;


/**
 * Created by long on 2017/4/11.
 */
@SuppressLint("WrongConstant")
public class ToastUtil {

    public static Handler handler;
    public static void showTost(final String msg){
        handler=new Handler(SampleAppLike.mcontext.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast= Toast.makeText(SampleAppLike.mcontext,
                        msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

    }
    public static void showNetError(){
        Toast.makeText(SampleAppLike.mcontext,"网络错误，请稍后重试",Toast.LENGTH_SHORT).show();
    }

    public static void showAtCenter(String msg){
        Toast toast= Toast.makeText(SampleAppLike.mcontext,
                msg, Toast.LENGTH_SHORT);
      toast.setGravity(Gravity.CENTER, 0, 0);
       toast.show();
    }
}
