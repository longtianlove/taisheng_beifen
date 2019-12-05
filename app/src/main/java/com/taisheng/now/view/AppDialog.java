package com.taisheng.now.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.taisheng.now.R;
import com.taisheng.now.util.DensityUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Dialog 工具类
 * @author user
 *
 */
public class AppDialog extends Dialog {

	public AppDialog(Context context, int layout, int width, int height,
                     int animId, int gravity) {
		this(context,R.style.MyDialogStyleTop, layout, width, height,
		animId, gravity);
	}
	public AppDialog(Context context, int style, int layout, int width, int height,
                     int animId, int gravity) {
		super(context, R.style.MyDialogStyleTop);
		setContentView(layout);
		Window window = getWindow();
		window.setWindowAnimations(animId);
		window.setGravity(gravity);
		WindowManager.LayoutParams params = window.getAttributes();
        //设置宽度
        int margin=getContext().getResources().getDimensionPixelSize(R.dimen.dialog_service_margin)*2;
		params.width = DensityUtil.getScreenWidth(getContext())-margin;
		params.height = height;
		window.setAttributes(params);
	}
    public AppDialog(Context context, int style, int layout, int width, int height,
                     int animId, int gravity, boolean self) {
        super(context,R.style.MyDialogStyleTop);
        setContentView(layout);
        Window window = getWindow();
        window.setWindowAnimations(animId);
        window.setGravity(gravity);
        WindowManager.LayoutParams params = window.getAttributes();
        //设置宽度
        int margin=getContext().getResources().getDimensionPixelSize(R.dimen.self_dialog_service_margin)*2;
        params.width = DensityUtil.getScreenWidth(getContext())-margin;
        params.height = height;
        window.setAttributes(params);
    }
    public AppDialog(Context context, int layout, int width, int height,
                     int animId, int gravity, boolean self) {
//        super(context,R.style.MyDialogStyleTop);
//        setContentView(layout);
//        Window window = getWindow();
//        window.setWindowAnimations(animId);
//        window.setGravity(gravity);
//        WindowManager.LayoutParams params = window.getAttributes();
//        //设置宽度
//        int margin=getContext().getResources().getDimensionPixelSize(R.dimen.self_dialog_service_margin)*2;
//        params.width = DensityUtil.getScreenWidth(getContext())-margin;
//        params.height = height;
//        window.setAttributes(params);

        this(context,R.style.MyDialogStyleTop, layout, width, height,
                animId, gravity,self);
    }

	public AppDialog(Context context) {
		super(context, R.style.MyDialogStyleTop);
	}

    @SuppressLint("WrongConstant")
    public static class DateUtil {
        /**
         * 获取过去任意天内的日期数组
         * @param intervals      intervals天内
         * @return              日期数组
         */
        public static ArrayList<String> test(int intervals ) {
            ArrayList<String> pastDaysList = new ArrayList<>();
            for (int i = 0; i <intervals; i++) {
                pastDaysList.add(getPastDateString(i));
            }
            return pastDaysList;
        }
        /**
         * 获取过去第几天的日期
         *
         * @param past
         * @return
         */
        public static String getPastDateString(int past) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
            Date today = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String result = format.format(today);
            Log.e(null, result);
            return result;
        }

        /**
         * 获取过去第几天的日期
         *
         * @param past
         * @return
         */
        public static Date getPastDate(int past) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
            return calendar.getTime();
        }

        public static int getPastDay(int past){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * 获取过去任意天内的日期数组
         * @param intervals      intervals天内
         * @return              日期数组
         */
        public static ArrayList<Date> getPastDates(int intervals ) {
            ArrayList<Date> pastDaysList = new ArrayList<>();
            for (int i = intervals; i >0; i--) {
                pastDaysList.add(getPastDate(i));
            }
            return pastDaysList;
        }

        /**
         * 获取过去任意天内的日期数组
         * @param intervals      intervals天内
         * @return              日期数组
         */
        public static ArrayList<Integer> getPastDays(int intervals ) {
            ArrayList<Integer> pastDaysList = new ArrayList<>();
            for (int i = intervals; i >0; i--) {
                pastDaysList.add(getPastDay(i));
            }
            return pastDaysList;
        }


        public static String deviceInfoTime(long timemills) {
            if(timemills==0){
                timemills=System.currentTimeMillis();
            }
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            c.setTimeInMillis(timemills);
           int curYear = c.get(Calendar.YEAR);
            int curMon = c.get(Calendar.MONTH) + 1;
            int curDay = c.get(Calendar.DATE);
            int curHour=c.get(Calendar.HOUR_OF_DAY);
            int curMinute = c.get(Calendar.MINUTE);
//            return String.format("%s年%s月%s日%s:%s", curYear, curMon, curDay,curHour,curMinute);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date=new Date(timemills);
            return simpleDateFormat.format(date);
        }

        public static Date getFirstDataOfCurMonth(){
            Date date=new Date();
            date.setMonth(date.getMonth()+1);
            date.setDate(date.getDate()-1);
            return date;
        }

        public static Date getCurDate(){
            Date date=new Date();
            date.setMonth(date.getMonth()+1);
            return date;
        }

        //获取当天日期
        public static Date getCurrentDate(){
            Date date=new Date();
            return date;
        }
    }
}
