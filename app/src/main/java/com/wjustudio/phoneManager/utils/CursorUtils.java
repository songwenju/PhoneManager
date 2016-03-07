package com.wjustudio.phoneManager.utils;

import android.database.Cursor;

/**
 * 作者：songwenju on 2016/3/7 07:39
 * 邮箱：songwenju01@163.com
 */
public class CursorUtils {
    public static final String TAG = "CursorUtils";
    public static void printCursor(Cursor cursor){
        LogUtil.e(TAG,"============");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            LogUtil.e(TAG,"name = "+cursor.getColumnName(i) + "; value ="+cursor.getString(i) );
        }
    }
}
