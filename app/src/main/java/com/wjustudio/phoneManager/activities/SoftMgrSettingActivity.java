package com.wjustudio.phoneManager.activities;

import com.wjustudio.phoneManager.base.BaseSimpleSettingActivity;
import com.wjustudio.phoneManager.service.LockAppService;

/**
 * 作者： songwenju on 2016/5/4 22:12.
 * 邮箱： songwenju@outlook.com
 */
public class SoftMgrSettingActivity extends BaseSimpleSettingActivity{

    @Override
    protected String getOpenServiceTitle() {
        return "开启程序锁服务";
    }

    @Override
    protected String getServiceFullName() {
        return "com.wjustudio.phoneManager.service.LockAppService";
    }

    @Override
    protected String getSettingTitle() {
        return "软件管家设置";
    }

    @Override
    protected Class<?> getServiceClass() {
        return LockAppService.class;
    }
}
