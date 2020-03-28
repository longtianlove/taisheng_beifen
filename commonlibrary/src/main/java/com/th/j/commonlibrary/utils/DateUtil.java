package com.th.j.commonlibrary.utils;

import android.content.Context;


import com.th.j.commonlibrary.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 韩晓康
 * @date :2019/3/7 15:07
 * @description: 时间格式转换 获取当前时间
 */
public class DateUtil {
    public static String getTime() {
        long l = System.currentTimeMillis();
        return l + "";
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String dateToStampYMD(String s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long ts = date.getTime();
            res = String.valueOf(ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s);
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String stampToDate(long s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(s);
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String stampToDate2(long s) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(s);
            res = simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return SimpleDateFormat df = new SimpleDateFormat("HH:mm");// 设置日期格式
     * Date now = null;
     * Date beginTime = null;
     * Date beginTime1 = null;
     * Date endTime = null;
     * Date endTime1 = null;
     * try {
     * now = df.parse(df.format(new Date()));
     * beginTime = df.parse("09:00");
     * beginTime1 = df.parse("14:00");
     * endTime = df.parse("12:00");
     * endTime1 = df.parse("18:00");
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static String TimerFormat24H(Context context, long timer) {//可根据需要自行截取数据显示
        String timarFlow = "";
        Date date = new Date(timer);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeYMD = format.format(date);
        String[] split = timeYMD.split(" ");
        String timer24 = split[1];
        long timeStap = System.currentTimeMillis() - timer;
        if (timeStap >= 0 && timeStap <= 600000) {
            timarFlow = context.getString(R.string.just);
        } else if (timeStap >= 600000 && timeStap <= 630000) {
            timarFlow = context.getString(R.string.minutes_ago1);
        } else if (timeStap >= 630000 && timeStap <= 1800000) {
            timarFlow = context.getString(R.string.minutes_ago2);
        } else {
            return timer24;
        }

        return timarFlow;
    }

    public static String getChatTimeStr(Context context, long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        String timeStr = timeStamp + "";
        if (timeStr.length() == 10) {
            timeStamp = timeStamp * 1000;
        }
        inputTime.setTimeInMillis(timeStamp);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
//        if (calendar.before(inputTime)){
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + getString(R.string.prompt_69)+"MM"+getString(R.string.prompt_69)+"dd"+getString(R.string.day));
//            return sdf.format(currenTimeZone);
//        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return timeFormatStr(context, inputTime, sdf.format(currenTimeZone));
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
            return context.getString(R.string.yesterday) + " " + timeFormatStr(context, inputTime, sdf.format(currenTimeZone));
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "/" + "d" + " ");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(context, inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "M" + "/" + "d" + " ");
                String temp1 = sdf.format(currenTimeZone);
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
                String temp2 = timeFormatStr(context, inputTime, sdf1.format(currenTimeZone));
                return temp1 + temp2;
            }

        }
    }

    /**
     * 24小时制转化成12小时制
     *
     * @param strDay
     */
    public static String timeFormatStr(Context context, Calendar calendar, String strDay) {
        String tempStr = "";
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 11) {
            tempStr = context.getString(R.string.afternoon) + " " + strDay;
        } else {
            tempStr = context.getString(R.string.morning) + " " + strDay;
        }
        return tempStr;
    }

    public static boolean isWeekend(String date) {
        try {
            DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date bdate = format1.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(bdate);
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 倒计时
     *
     * @return
     */

    public String formatLongToTimeStr2(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue();
        if (second > 60) {
            minute = second / 60;   //取整
            second = second % 60;   //取余
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String strtime = "剩余：" + hour + "小时" + minute + "分" + second + "秒";
        return strtime;
    }

    /**
     * 倒计时补位
     *
     * @param date
     * @return
     */
    public String formatLongToTimeStr(Long date) {
        String strtime = "";
        String minStr = "";
        String sStr = "";
        long day = date / (60 * 60 * 24);
        long hour = (date / (60 * 60) - day * 24);
        long min = ((date / 60) - day * 24 * 60 - hour * 60);
        long s = (date - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

        if (min < 10 && s >= 10) {
            minStr = "0" + min;
            sStr = s + "";
        }
        if (min < 10 && s < 10) {
            minStr = "0" + min;
            sStr = "0" + s;
        }
        if (min >= 10 && s >= 10) {
            minStr = min + "";
            sStr = s + "";
        }
        if (min >= 10 && s < 10) {
            minStr = min + "";
            sStr = "0" + s;
        }
        strtime = hour + ":" + minStr + ":" + sStr;
        return strtime;
    }

    /**
     * 根据两个秒数 获取两个时间差
     */
    public static String getDatePoor(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        long diff = (endDate*1000 ) - (nowDate * 1000L);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        if (day > 2) {
            res = "";
        } else {
            if (day != 0) {
                res += day + "天";
            }
            if (hour != 0) {
                res += "  " + hour + ":" + min + ":" + sec;
            }
        }
        return res.replace("-","");
    }
    /**
     * 根据两个秒数 获取两个时间差
     */
    public static String getDatePoor2(long endDate, long nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        // long ns = 1000;
        // 获得两个时间的秒时间差异
        long diff = (endDate*1000 ) - (nowDate * 1000L);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        long sec = diff % nd % nh % nm / ns;
        String res = "";
        res=(day/365)+"";

        return res.replace("-","");
    }
}
