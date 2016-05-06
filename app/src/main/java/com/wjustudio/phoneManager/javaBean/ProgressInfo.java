package com.wjustudio.phoneManager.javaBean;

import android.graphics.drawable.Drawable;

/**
 * songwenju on 16-5-6 : 13 : 21.
 * 邮箱：songwenju@outlook.com
 */
public class ProgressInfo {
    /**
     * 包名
     */
    public String packageName;
    /**
     * 应用名称
     */
    public String name;
    /**
     * 应用图标
     */
    public Drawable icon;
    /**
     * 应用运行时占用内存的大小
     */
    public long ramSize;
    /**
     * 是否是用户程序
     */
    public boolean isUser;
    /**
     * 是否被选中
     */
    public boolean isChecked;

    public ProgressInfo() {
        super();
    }

    @Override
    public String toString() {
        return "TaskInfo [packageName=" + packageName + ", name=" + name
                + ", icon=" + icon + ", ramSize=" + ramSize + ", isUser="
                + isUser + ", isChecked=" + isChecked + "]";
    }
}
