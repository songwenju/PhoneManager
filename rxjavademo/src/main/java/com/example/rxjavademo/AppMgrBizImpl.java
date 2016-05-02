package com.example.rxjavademo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.format.Formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： songwenju on 2016/5/1 08:28.
 * 邮箱： songwenju@outlook.com
 */
public class AppMgrBizImpl implements IAppMgrBiz {
    public Context mContext;

    public AppMgrBizImpl(Context context) {
        mContext = context;
    }

    @Override
    public String getPhoneFreeMemory() {
        long freeSpace = Environment.getDataDirectory().getFreeSpace();

        return Formatter.formatFileSize(mContext,freeSpace);
    }

    @Override
    public String getSDFreeMemory() {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        return Formatter.formatFileSize(mContext,freeSpace);
    }

    @Override
    public List<AppInfo> getAppInfoList() {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo:packages) {
            //获取包名
            String packageName = packageInfo.packageName;
            //获取应用程序的信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //应用图标
            Drawable icon = applicationInfo.loadIcon(packageManager);
            //应用名称
            String appName = applicationInfo.loadLabel(packageManager).toString();
            //应用的大小,获取应用包所在文件夹下文件的大小
            File appFile = new File(applicationInfo.sourceDir);
            long apkSize = appFile.length();
            String apkSizeFormat = Formatter.formatFileSize(mContext,apkSize);
            //是否是用户程序
            boolean isUser = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM;
            //是否在SD卡上
            boolean isSD = (applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
            AppInfo appInfo = new AppInfo(packageName,appName,icon,apkSizeFormat,isUser,isSD);
            appInfoList.add(appInfo);
        }
        return appInfoList;
    }
}
