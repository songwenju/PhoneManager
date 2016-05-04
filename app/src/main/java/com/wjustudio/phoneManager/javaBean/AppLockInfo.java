package com.wjustudio.phoneManager.javaBean;

import org.litepal.crud.DataSupport;

/**
 * songwenju on 16-5-4 : 15 : 59.
 * 邮箱：songwenju@outlook.com
 */
public class AppLockInfo extends DataSupport{
    private String packageName;

    public AppLockInfo(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppLockInfo{" +
                "packageName='" + packageName + '\'' +
                '}';
    }
}
