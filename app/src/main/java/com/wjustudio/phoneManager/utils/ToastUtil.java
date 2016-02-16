package com.wjustudio.phoneManager.utils;

import android.widget.Toast;

import com.wjustudio.phoneManager.Common.PhoneManagerApplication;

/**
 * 作者：songwenju on 2016/1/31 21:12
 * 邮箱：songwenju01@163.com
 */
public class ToastUtil {
    private static Toast toast;

    /**
     * 保证是单例的toast.再上一个toast没有显示完的情况下下一个toast不会显示.
     * @param text
     */
    public static void showToast(String text){
        if (toast == null){
            toast = Toast.makeText(PhoneManagerApplication.mAppContext,"",Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

}
