package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseSimpleSettingActivity;
import com.wjustudio.phoneManager.service.LockAppService;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.RunningActivityUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

/**
 * 作者： songwenju on 2016/5/4 22:12.
 * 邮箱： songwenju@outlook.com
 */
public class SoftMgrSettingActivity extends BaseSimpleSettingActivity{

    @Override
    protected CharSequence getSettingOneText() {
        return "修改程序锁密码";
    }

    @Override
    protected boolean setLSettingOneVisibility() {
        mLSettingOne.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    protected String getOpenServiceTitle() {
        return "开启程序锁服务";
    }

    @Override
    protected String getServiceFullName() {
        return "com.wjustudio.phoneManager.service.LockAppService";
    }

    @Override
    protected String getSettingTitle() {
        return "软件管家设置";
    }

    @Override
    protected Class<?> getServiceClass() {
        return LockAppService.class;
    }

    @Override
    protected void onSettingOneClick() {
        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(mContext, R.layout.dialog_reset_lock_pwd,null);
        //设置自定义的view
        alertDialog.setView(view, 0, 0, 0, 0);

        //初始化控件
        final EditText appLockPwd = (EditText) view.findViewById(R.id.et_reset_lock_pwd);
        Button cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) view.findViewById(R.id.btn_confirm);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = appLockPwd.getText().toString().trim();
                SpUtil.putString(AppConstants.LOCK_PASSWORD, MD5Utils.decode(pwd));
                alertDialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
       alertDialog.show();
    }

    @Override
    protected void onInitListener() {
        mLlOpenService.setOnClickListener(this);
        mSbMd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (RunningActivityUtil.isNoOption(mContext)) {
                        if (!RunningActivityUtil.isNoSwitch(mContext)) {
                            showAlertDialog();
                        }else {
                            mSbMd.setChecked(isChecked);
                            mIsOpenService = isChecked;
                            manageService();
                        }
                    }else {
                        ToastUtil.showToast("您的手机不支持程序锁");
                    }

                } else {
                    mSbMd.setChecked(isChecked);
                    mIsOpenService = isChecked;
                    manageService();
                }

            }
        });
        mLSettingOne.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.ll_open_service:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (RunningActivityUtil.isNoOption(mContext)) {
                        if (!RunningActivityUtil.isNoSwitch(mContext)) {
                            showAlertDialog();
                        }else {
                            setCheckAndService();
                        }
                    }else {
                        ToastUtil.showToast("您的手机不支持程序锁");
                    }

                } else {
                    setCheckAndService();
                }

                break;
            case R.id.ll_setting_one:
                onSettingOneClick();
                break;

        }
    }

    private void setCheckAndService() {
        mIsOpenService = !mIsOpenService;
        mSbMd.setChecked(mIsOpenService);
        manageService();
    }

    private void showAlertDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示：");
        builder.setMessage("检测到您的系统没有程序锁权限，点击确认将跳转到开启界面，选择程序并打开权限");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent,0);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSbMd.setChecked(false);
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (RunningActivityUtil.isNoOption(mContext) && !RunningActivityUtil.isNoSwitch(mContext)) {
                    if (!RunningActivityUtil.isNoSwitch(mContext)) {
                        mSbMd.setChecked(false);
                    } else {
                        manageService();
                    }
                }
            }
        }

    }
}
