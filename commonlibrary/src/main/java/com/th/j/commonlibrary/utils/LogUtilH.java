package com.th.j.commonlibrary.utils;

import android.util.Log;


/**
 * 日志工具，用于日志打印
 */
public class LogUtilH {

    static String TAG = "hpig";

    public enum LogLevel {
        DEBUG_LEVEL, // 调试级别 日志
        RELEASE_LEVEL // 发布级别 日志
    }

    // 日志级别
    private static LogLevel logLevel = LogLevel.DEBUG_LEVEL;
//    private static LogLevel logLevel = LogLevel.RELEASE_LEVEL;

    public static void setLogLevel(LogLevel logLevel) {
        LogUtilH.logLevel = logLevel;
    }

    public static void i(String msg) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.i(TAG+"i", msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.i(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.e(TAG+"e", msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.e(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.d(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (logLevel == LogLevel.DEBUG_LEVEL) {
            Log.w(tag, msg, tr);
        }
    }
}
