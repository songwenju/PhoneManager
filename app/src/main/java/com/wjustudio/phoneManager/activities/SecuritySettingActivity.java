package com.wjustudio.phoneManager.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手机防盗的相关设置界面
 */
public class SecuritySettingActivity extends BaseActivity {
    @Bind(R.id.btn_next)
    Button mBtnNext;
    @Bind(R.id.et_safe_number)
    EditText mEtSafeNumber;
    @Bind(R.id.btn_select_number)
    ImageButton mBtnSelectNumber;
    @Bind(R.id.btn_complete)
    Button mBtnComplete;
    @Bind(R.id.setting_one)
    LinearLayout mSettingOne;
    @Bind(R.id.setting_two)
    LinearLayout mSettingTwo;
    private String mSafeNum;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_security_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mSafeNum = mEtSafeNumber.getText().toString().trim();
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mBtnNext.setOnClickListener(this);
        mBtnComplete.setOnClickListener(this);
        mBtnSelectNumber.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                mSettingOne.setVisibility(View.GONE);
                mSettingTwo.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_select_number:
                toast("选择联系人");
                break;
            case R.id.btn_complete:
                if (TextUtils.isEmpty(mSafeNum)) {
                    ToastUtil.showToast("手机号码不能为空!");
                } else if (!CommonUtil.isMobile(mSafeNum)) {
                    ToastUtil.showToast("手机号码的格式不正确！");
                } else {
                    SpUtil.putString(AppConstants.SAFE_NUM, MD5Utils.decode(mSafeNum));
                    SpUtil.putBoolean(AppConstants.IS_OPEN_PROTECT,true);
                    finish();
                }
                break;
        }
    }

}
