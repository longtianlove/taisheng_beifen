package com.taisheng.now.util;

/**
 * 连续点击处理类
 * 
 * @author long
 * 
 */
public class DoubleClickUtil {

	private static long lastClickTime;

	/**
	 * 判断是否连续点击
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD <= 2000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 判断是否连续点击
	 *
	 * @return
	 */
	public static boolean isFastMiniDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD <= 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 判断是否连续点击--300毫秒
	 *
	 * @return
	 */
	public static boolean isDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD <= 300) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 判断是否连续点击
	 *
	 * @return
	 */
	public static boolean isFastLongDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD <= 3000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
