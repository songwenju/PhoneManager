package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.service.TheftProofService;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.MD5Utils;
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
        mIsOpenProof = CommonUtil.isRunningService(mContext, "com.wjustudio.phoneManager.service.TheftProofService");
        mSbMd.setChecked(mIsOpenProof);
    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mLlOpenProof.setOnClickListener(this);
        mLlEditPwd.setOnClickListener(this);
        mLlEditPhone.setOnClickListener(this);
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
            case R.id.ll_edit_phone:
                showReSetPhoneDialog();
                break;
            case R.id.ll_edit_pwd:
                showReSetPwdDialog();
                break;
            case R.id.ll_open_proof:
                mIsOpenProof = !mIsOpenProof;
                mSbMd.setChecked(mIsOpenProof);
                manageService();
                break;
        }
    }

    /**
     * 显示设置安全号码的Dialog
     */
    private void showReSetPhoneDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_reset_safe_num, null);
        dialog.setView(dialogView, 0, 0, 0, 0);
        final EditText newSafeNum = (EditText) dialogView.findViewById(R.id.et_new_safeNum);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSafeNumStr = newSafeNum.getText().toString().trim();
                if (TextUtils.isEmpty(newSafeNumStr)) {
                    toast("安全号码不能为空！");
                } else if (CommonUtil.isPhone(newSafeNumStr)) {
                    toast("安全号码输入有误！");
                } else {
                    SpUtil.putString(AppConstants.SAFE_NUM, newSafeNumStr);
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 显示设置密码的Dialog
     */
    private void showReSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_reset_password, null);
        dialog.setView(dialogView, 0, 0, 0, 0);
        final EditText originPwd = (EditText) dialogView.findViewById(R.id.et_origin_pwd);
        final EditText resetPwd = (EditText) dialogView.findViewById(R.id.et_reset_pwd);
        final EditText resetPwdAgain = (EditText) dialogView.findViewById(R.id.et_resetPwd_again);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String savePwd = SpUtil.getString(AppConstants.ENTER_PROOF_PWD, "");
                String originPwdStr = originPwd.getText().toString().trim();
                String originMD5PwdStr = MD5Utils.decode(originPwdStr);
                String rePwdStr = resetPwd.getText().toString().trim();
                String resetPwdAgainStr = resetPwdAgain.getText().toString().trim();
                String rePwdMD5Str = MD5Utils.decode(rePwdStr);
                LogUtil.d(this, "originPwdStr:" + originPwdStr);
                if (TextUtils.isEmpty(originPwdStr)) {
                    toast("原始密码不能为空！");
                } else if (!savePwd.equals(originMD5PwdStr)) {
                    toast("原始密码输入有误！");
                } else if (TextUtils.isEmpty(rePwdStr)) {
                    toast("输入的新密码不能为空！");
                } else if (TextUtils.isEmpty(resetPwdAgainStr)) {
                    toast("再次输入的新密码不能为空！");
                } else if (!rePwdStr.equals(resetPwdAgainStr)) {
                    toast("两次新密码不一致！");
                } else {
                    SpUtil.putString(AppConstants.ENTER_PROOF_PWD, rePwdMD5Str);
                    dialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
