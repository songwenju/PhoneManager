package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.service.TheftProofService;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-4-28 : 14 : 46.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumSettingActivity extends BaseActivity {
    @Bind(R.id.sb_md)
    SwitchButton mSbMd;
    @Bind(R.id.ll_open_proof)
    LinearLayout mLlOpenProof;
    private boolean mIsOpenProof;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_black_num_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mIsOpenProof = CommonUtil.isRunningService(mContext, "com.wjustudio.phoneManager.service.BlackNumService");
        mSbMd.setChecked(mIsOpenProof);
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mLlOpenProof.setOnClickListener(this);
        mSbMd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSbMd.setChecked(isChecked);
                mIsOpenProof = isChecked;
                manageService();
            }
        });
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.ll_open_proof:
                mIsOpenProof = !mIsOpenProof;
                mSbMd.setChecked(mIsOpenProof);
                manageService();
                break;
        }
    }


    private void manageService() {
        Intent intent = new Intent(this, TheftProofService.class);
        if (mIsOpenProof) {
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
