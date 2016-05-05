package com.wjustudio.phoneManager.activities;

import com.wjustudio.phoneManager.base.BaseSimpleSettingActivity;
import com.wjustudio.phoneManager.service.TheftProofService;

/**
 * songwenju on 16-4-28 : 14 : 46.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumSettingActivity extends BaseSimpleSettingActivity {

    @Override
    protected CharSequence getSettingOneText() {
        return null;
    }

    @Override
    protected boolean setLSettingOneVisibility() {
        return false;
    }

    @Override
    protected String getOpenServiceTitle() {
        return "开启黑名单服务";
    }

    @Override
    protected String getServiceFullName() {
        return "com.wjustudio.phoneManager.service.BlackNumService";
    }

    @Override
    protected String getSettingTitle() {
        return "通讯卫士设置";
    }

    @Override
    protected Class<?> getServiceClass() {
        return TheftProofService.class;
    }

    @Override
    protected void onSettingOneClick() {

    }
}
