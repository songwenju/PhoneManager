package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.service.TheftProofService;
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
                showReSetPwdDialog();

                break;
            case R.id.ll_open_proof:
                mIsOpenProof = !mIsOpenProof;
                mSbMd.setChecked(mIsOpenProof);
                SpUtil.putBoolean(AppConstants.IS_OPEN_PROOF, mIsOpenProof);
                manageService();
                break;
        }
    }

    /**
     * 显示设置密码的Dialog
     */
    private void showReSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_set_password, null);
        dialog.setView(dialogView, 0, 0, 0, 0);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        final EditText rePwd = (EditText) dialogView.findViewById(R.id.et_rePwd);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdStr = MD5Utils.decode(pwd.getText().toString().trim());
                String rePwdStr = MD5Utils.decode(rePwd.getText().toString().trim());
                if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空！");
                } else if (TextUtils.isEmpty(rePwdStr)) {
                    toast("再次输入的密码不能为空！");
                } else if (!pwdStr.equals(rePwdStr)) {
                    toast("两次密码不一致！");
                } else {
                    SpUtil.putString("enterPwd", pwdStr);
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
            startService(intent);
        } else {
            //关闭服务
            stopService(intent);
        }
    }

}
