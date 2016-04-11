package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.service.TheftProofService;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： songwenju on 2016/4/10 15:41.
 * 邮箱： songwenju@outlook.com
 */
public class TheftProofSettingActivity extends BaseActivity {
    @Bind(R.id.sb_md)
    SwitchButton mSbMd;
    @Bind(R.id.ll_open_proof)
    LinearLayout mLlOpenProof;
    @Bind(R.id.ll_edit_phone)
    LinearLayout mLlEditPhone;
    @Bind(R.id.ll_edit_pwd)
    LinearLayout mLlEditPwd;
    private boolean mIsOpenProof;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_proof_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mIsOpenProof = SpUtil.getBoolean(AppConstants.IS_OPEN_PROOF, true);
        mSbMd.setChecked(mIsOpenProof);
        manageService();
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mLlOpenProof.setOnClickListener(this);
        mLlEditPwd.setOnClickListener(this);
        mLlEditPhone.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.ll_edit_phone:

                break;
            case R.id.ll_edit_pwd:

                break;
            case R.id.ll_open_proof:
                mIsOpenProof = !mIsOpenProof;
                mSbMd.setChecked(mIsOpenProof);
                SpUtil.putBoolean(AppConstants.IS_OPEN_PROOF, mIsOpenProof);
                manageService();
                break;
        }
    }

    private void manageService() {
        Intent intent = new Intent(this, TheftProofService.class);
        if (mIsOpenProof) {
            //开启服务去监听各种状态
            startService(intent);
        } else {
            //关闭服务
            stopService(intent);
        }
    }

}
