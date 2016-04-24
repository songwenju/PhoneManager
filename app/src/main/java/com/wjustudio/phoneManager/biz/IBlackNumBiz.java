package com.wjustudio.phoneManager.biz;

import com.wjustudio.phoneManager.javaBean.BlackNumInfo;

import java.util.List;

/**
 * songwenju on 16-4-20 : 12 : 27.
 * 邮箱：songwenju@outlook.com
 */
public interface IBlackNumBiz {
    /**
     * 获得所有的黑名单
     * @return
     */
    List<BlackNumInfo> getAllBlackNum();

    /**
     * 添加一个黑名单
     * @param blackNumInfo
     */
    void insertBlackNum(BlackNumInfo blackNumInfo);

    /**
     * 删除黑名单信息
     * @param blackNumInfo
     */
    void deleteBlackNum(BlackNumInfo blackNumInfo);

    /**
     * 升级黑名单信息
     * @param blackNumInfo
     */
    void updateBlackNum(BlackNumInfo blackNumInfo);

    /**
     * 黑名单信息是否存在
     * @param blackNum
     * @return
     */
    boolean isExist(String blackNum);

}
