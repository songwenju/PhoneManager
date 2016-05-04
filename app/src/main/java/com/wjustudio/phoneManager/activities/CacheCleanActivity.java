package com.wjustudio.phoneManager.activities;

import android.view.View;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-4-14 : 12 : 17.
 * 邮箱：songwenju@outlook.com
 */
public class CacheCleanActivity extends BaseActivity{
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_cache_mgr;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        mCommonTitleLayout.setTitle("缓存清理");
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mCommonTitleLayout.setOnSettingImgClickListener(new CommonTitleLayout.OnSettingImgClickListener() {
            @Override
            public void onSettingImgClick() {
                toast("setting");
//                Intent intent = new Intent(mContext, BlackNumSettingActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void processClick(View v) {

    }
}
