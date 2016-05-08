package com.wjustudio.phoneManager.base;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： songwenju on 2016/5/4 22:41.
 * 邮箱： songwenju@outlook.com
 */
public abstract class BaseSimpleSettingActivity extends BaseActivity {
    @Bind(R.id.sb_md)
    protected SwitchButton mSbMd;
    @Bind(R.id.ll_open_service)
    protected LinearLayout mLlOpenService;
    @Bind(R.id.ctl_common_title)
    protected CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tv_service_title)
    protected TextView mTvServiceTitle;
    @Bind(R.id.ll_setting_one)
    protected LinearLayout mLSettingOne;
    @Bind(R.id.tv_setting_one_text)
    TextView mTvSettingOne;
    protected boolean mIsOpenService;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_simple_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        mTvServiceTitle.setText(getOpenServiceTitle());
        if (setLSettingOneVisibility()) {
            mTvSettingOne.setText(getSettingOneText());
        }
    }

    protected abstract CharSequence getSettingOneText();

    protected abstract boolean setLSettingOneVisibility();

    @Override
    protected void onInitData() {
        mIsOpenService = CommonUtil.isRunningService(mContext, getServiceFullName());
        mSbMd.setChecked(mIsOpenService);
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle(getSettingTitle());
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    /**
     * 开启xx服务
     *
     * @return
     */
    protected abstract String getOpenServiceTitle();

    /**
     * 全类名
     *
     * @return
     */
    protected abstract String getServiceFullName();

    /**
     * 设置名称
     *
     * @return
     */
    protected abstract String getSettingTitle();


    /**
     * 服务所在class
     *
     * @return
     */
    protected abstract Class<?> getServiceClass();

    @Override
    protected void onInitListener() {
        mLlOpenService.setOnClickListener(this);
        mSbMd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSbMd.setChecked(isChecked);
                mIsOpenService = isChecked;
                manageService();
            }
        });
        mLSettingOne.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.ll_open_service:
                mIsOpenService = !mIsOpenService;
                mSbMd.setChecked(mIsOpenService);
                manageService();
                break;
            case R.id.ll_setting_one:
                onSettingOneClick();
                break;

        }
    }

    protected abstract void onSettingOneClick();


    protected void manageService() {
        Intent intent = new Intent(this, getServiceClass());
        if (mIsOpenService) {
            //开启服务去监听各种状态
            LogUtil.d(this, "开启服务去监听各种状态");
            startService(intent);
        } else {
            //关闭服务
            LogUtil.d(this, "关闭服务");
            stopService(intent);
        }
    }

}
