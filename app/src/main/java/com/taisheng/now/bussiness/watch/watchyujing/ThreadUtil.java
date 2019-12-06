package com.taisheng.now.bussiness.watch.watchyujing;



import com.taisheng.now.EventManage;

import org.greenrobot.eventbus.EventBus;

/**
 * 主要处理项目当中线程停止的
 * Created by 龙 on 2017/8/21.
 */

public class ThreadUtil {
    //刚开启紧急搜寻模式的前5分钟不做判断   1000 60 5
    public static Thread open_gps_donot_check_Thread;



    public static void open_gps_donot_check_Thread(final long time) {
        if (open_gps_donot_check_Thread == null) {
            try {
                open_gps_donot_check_Thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
             while (true) {
                 try {
                     Thread.sleep(time);
//                            Thread.sleep(1000);
                 } catch (Exception e) {

                 }
                 EventBus.getDefault().postSticky(new EventManage.getYujingxinxi());


             }
                    }
                });
            } catch (Exception e) {

            }
            open_gps_donot_check_Thread.start();
        }

    }


}
