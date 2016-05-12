package com.wjustudio.phoneManager.biz;

/**
 * songwenju on 16-5-12 : 12 : 14.
 * 邮箱：songwenju@outlook.com
 */
public interface IPhoneLocationBiz {
    /**
     * 查询号码归属地
     * @param phoneNum
     * @return
     */
    String query(String phoneNum);
}
