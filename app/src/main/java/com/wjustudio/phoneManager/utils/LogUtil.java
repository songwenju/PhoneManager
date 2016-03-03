package com.wjustudio.phoneManager.utils;

import android.util.Log;

/**
 * 日志工具类
 * @author songwenju
 *
 */
public class LogUtil {
	private static boolean isDebug = true;
	
	public static void d(String tag, String msg){
		if (isDebug) {
			Log.d("swj_"+tag, msg);
		}
	}
	
	/**
	 * 强大的打日志的方法,可以直接通过传入类,并打印类名.比如在mainActivity中传入log.i(this,"msg");
	 * @param object
	 * @param msg
	 */
	public static void d(Object object, String msg){
		if (isDebug) {
			Log.d("swj_"+object.getClass().getSimpleName(), msg);
		}
	}
	
	public static void i(String tag, String msg){
		if (isDebug) {
			Log.i("swj_"+tag, msg);
		}
	}
	
	public static void i(Object object, String msg){
		if (isDebug) {
			Log.i("swj_"+object.getClass().getSimpleName(), msg);
		}
	}
	public static void e(String tag, String msg){
		if (isDebug) {
			Log.e("swj_"+tag, msg);
		}
	}
	public static void e(Object object, String msg){
		if (isDebug) {
			Log.e("swj_"+object.getClass().getSimpleName(), msg);
		}
	}
}
