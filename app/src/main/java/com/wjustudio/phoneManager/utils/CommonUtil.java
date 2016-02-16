package com.wjustudio.phoneManager.utils;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.wjustudio.phoneManager.Common.PhoneManagerApplication;

import java.security.MessageDigest;

/**
 * 作者：songwenju on 2016/1/24 20:29
 * 邮箱：songwenju01@163.com
 */
public class CommonUtil {
    /**
     * 在主线程执行任务,该方法在fragment中也可以使用.
     * @param r
     */
    public static void runOnUiThread(Runnable r){
        PhoneManagerApplication.mAppHandler.post(r);
    }

    /**
     * 在主线程中执行延迟任务
     * @param r
     * @param delayMillis
     */
    public static void runOnUiThread(Runnable r,long delayMillis){
        PhoneManagerApplication.mAppHandler.postDelayed(r,delayMillis);
    }

    /**
     * 获得Resources对象
     * @return
     */
    public static Resources getResources(){
        return PhoneManagerApplication.mAppContext.getResources();
    }

    /**
     * 获得String
     * @param id
     * @return
     */
    public static String getString(int id){
        return getResources().getString(id);
    }

    /**
     * 获取字符数组
     * @param id
     * @return
     */
    public static String[] getStringArray(int id){
        return getResources().getStringArray(id);
    }
    /**
     * 获得颜色值
     * @param id
     * @return
     */
    public static int getColor(int id){
        return getResources().getColor(id);
    }

    /**
     * 获得Dimens值
     * @param id
     * @return
     */
    public static int getDimens(int id){
        return (int) getResources().getDimension(id);
    }

    /**
     * 获得Drawable对象
     * @param id
     * @return
     */
    public static Drawable getDrawable(int id){
        return getResources().getDrawable(id);
    }

    /**
     * 显示dialog弹框
     * @param context
     * @param message
     */
    public static void showInfoDialog(Context context, String message){
        showInfoDialog(context,message,"提示","确定","",null,null);
    }
    /**
     * 显示dialog弹框
     * @param context
     * @param message
     * @param titleStr
     * @param positiveStr
     * @param positiveListener
     */
    public static void showInfoDialog(Context context, String message,
                                      String titleStr , String positiveStr,String negativeStr,
                                      DialogInterface.OnClickListener positiveListener,
                                      DialogInterface.OnClickListener negativeListener){
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(titleStr);
        localBuilder.setMessage(message);
        if (positiveListener == null){
            positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        }
        localBuilder.setPositiveButton(positiveStr,positiveListener);
        localBuilder.setNegativeButton(negativeStr,negativeListener);
        localBuilder.show();

    }

    /**
     * md5加密
     * @param paramString
     * @return
     */
    public static String md5(String paramString){
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        }catch (Exception e){
            return paramString;
        }
    }

    /**
     * 将制定的byte数组转换为16进制的字符串
     * @param digest
     * @return
     */
    private static String byteToHexString(byte[] digest) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0;i < digest.length; i++){
            String hex = Integer.toHexString(digest[i] & 0xFF);
            if (hex.length() == 1){
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return  hexString.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
