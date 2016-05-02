package com.example.rxjavademo;

import android.graphics.drawable.Drawable;

/**
 * 作者： songwenju on 2016/5/1 09:46.
 * 邮箱： songwenju@outlook.com
 */
public class AppInfo {
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
     * 应用大小
     */
    public String apkSize;
    /**
     * 是否是用户程序
     */
    public boolean isUser;
    /**
     * 是否安装在内存卡
     */
    public boolean isSD;

    public AppInfo() {
    }

    public AppInfo(String packageName, String name, Drawable icon, String apkSize, boolean isUser, boolean isSD) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.apkSize = apkSize;
        this.isUser = isUser;
        this.isSD = isSD;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "packageName='" + packageName + '\'' +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                ", apkSize=" + apkSize +
                ", isUser=" + isUser +
                ", isSD=" + isSD +
                '}';
    }
}
