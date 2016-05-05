package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseSimpleSettingActivity;
import com.wjustudio.phoneManager.service.LockAppService;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.SpUtil;

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
}
