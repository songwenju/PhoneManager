package com.example.rxjavademo;

import java.util.List;

/**
 * 作者： songwenju on 2016/4/30 18:34.
 * 邮箱： songwenju@outlook.com
 */
public interface IAppMgrBiz {
    /**
     * 获得手机内部可用空间
     * @return
     */
    String getPhoneFreeMemory();

    /**
     * 获得SD卡可用空间
     * @return
     */
    String getSDFreeMemory();

    /**
     * 获得app信息的列表
     * @return
     */
    List<AppInfo> getAppInfoList();
}
