package com.taisheng.now.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    /**
     * 时间戳转换成日期格式字符串
     * @param milliseconds 精确到微秒的字符串
     * @return
     */
    public static String timeStamp2Date(String milliseconds,String format) {
        if(milliseconds == null || milliseconds.isEmpty() || milliseconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(milliseconds)));
    }
}
