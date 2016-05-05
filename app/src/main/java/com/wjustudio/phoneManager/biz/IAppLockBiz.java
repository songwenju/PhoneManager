package com.wjustudio.phoneManager.biz;

import com.wjustudio.phoneManager.javaBean.AppLockInfo;

import java.util.List;

/**
 * songwenju on 16-5-4 : 16 : 04.
 * 邮箱：songwenju@outlook.com
 */
public interface IAppLockBiz {

    /**
     * 应用是否加锁
     * @param packageName 包名
     * @return
     */
    boolean isLock(String packageName);

    /**
     * 加锁应用
     * @param appLockInfo 加锁的应用
     */
    void lockApp(AppLockInfo appLockInfo);

    /**
     * 解锁应用
     * @param appLockInfo 加锁的应用
     */
    void unlockApp(AppLockInfo appLockInfo);


    /**
     * 获得所有加锁的应用
     * @return
     */
    List<AppLockInfo> getAllLockApp();

}
