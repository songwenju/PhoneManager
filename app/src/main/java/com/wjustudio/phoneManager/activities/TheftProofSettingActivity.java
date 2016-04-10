package com.wjustudio.phoneManager.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;

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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_proof_setting;
    }

    @Override
    protected void onInitView() {

    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        mSbMd.setChecked(true);

    }

    @Override
    protected void onInitListener() {
        mLlOpenProof.setOnClickListener(this);
        mLlEditPwd.setOnClickListener(this);
        mLlEditPhone.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()){
            case R.id.ll_edit_phone:
                break;
            case R.id.ll_edit_pwd:
                break;
            case R.id.ll_open_proof:
                break;
        }
    }

}
