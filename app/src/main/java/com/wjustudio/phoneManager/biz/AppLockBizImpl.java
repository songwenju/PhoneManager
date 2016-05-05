package com.wjustudio.phoneManager.biz;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.wjustudio.phoneManager.javaBean.AppLockInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * songwenju on 16-5-4 : 16 : 05.
 * 邮箱：songwenju@outlook.com
 */
public class AppLockBizImpl implements IAppLockBiz {
    private Context mContext;

    public AppLockBizImpl(Context context) {
        mContext = context;
    }

    @Override
    public boolean isLock(String packageName) {
        List<AppLockInfo> appLockInfoList = DataSupport
                .where("packageName = ?",packageName).find(AppLockInfo.class);
        return appLockInfoList != null && appLockInfoList.size() > 0;
    }

    @Override
    public void lockApp(AppLockInfo appLockInfo) {
        appLockInfo.saveThrows();
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = Uri.parse("content://appLock/change");
        resolver.notifyChange(uri, null);
    }

    @Override
    public void unlockApp(AppLockInfo appLockInfo) {
        if (!appLockInfo.isSaved()){
            appLockInfo.save();
        }
        appLockInfo.delete();
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri = Uri.parse("content://appLock/change");
        resolver.notifyChange(uri, null);
    }

    @Override
    public List<AppLockInfo> getAllLockApp() {
        return DataSupport.findAll(AppLockInfo.class);
    }
}
