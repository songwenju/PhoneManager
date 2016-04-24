package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
 * 手机防盗的设置界面
 */
public class TheftProofSelectTwoActivity extends BaseActivity {
    @Bind(R.id.et_safe_number)
    EditText mEtSafeNumber;
    @Bind(R.id.btn_select_number)
    ImageButton mBtnSelectNumber;
    @Bind(R.id.btn_complete)
    Button mBtnComplete;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_proof_setup_two;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        logd("mEtSafeNumber.toString():" + mEtSafeNumber.toString());
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mBtnComplete.setOnClickListener(this);
        mBtnSelectNumber.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_number:
                toast("选择联系人");
                Intent intent = new Intent(this, SelectContactActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.btn_complete:
                String safeNum = mEtSafeNumber.getText().toString().trim();
                if (TextUtils.isEmpty(safeNum)) {
                    ToastUtil.showToast("手机号码不能为空!");
                } else if (!CommonUtil.isMobile(safeNum)) {
                    ToastUtil.showToast("手机号码的格式不正确！");
                } else {
                    SpUtil.putString(AppConstants.SAFE_NUM, MD5Utils.decode(safeNum));
                    SpUtil.putBoolean(AppConstants.IS_OPEN_PROTECT, true);
                    intent = new Intent(this, TheftProofActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loge("data" + data);
        if (data != null) {
            String contactNum = data.getStringExtra(AppConstants.CONTACT_NUM);
            //mEtSafeNumber = (EditText) findViewById(R.id.et_safe_number);
            logd("mEtSafeNumber.toString():" + mEtSafeNumber.toString());
            if (!TextUtils.isEmpty(contactNum)) {
                mEtSafeNumber.setText(contactNum);
            }
        }
    }
}
