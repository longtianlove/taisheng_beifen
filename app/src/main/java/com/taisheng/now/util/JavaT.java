package com.taisheng.now.util;

import com.th.j.commonlibrary.utils.LogUtilH;

import java.util.Date;

public class JavaT {
    public static void main(String[] args) {

        String datePoor = getDatePoor(1585188082, 1567067986);

        System.out.print(datePoor);
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
        long diff = (endDate * 1000L) - (nowDate * 1000L);
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
            res = "2020";
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
}
