package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-5-5 : 15 : 45.
 * 邮箱：songwenju@outlook.com
 */
public class AppLockActivity extends BaseActivity {
    @Bind(R.id.tv_lock_app_icon)
    ImageView mTvLockAppIcon;
    @Bind(R.id.tv_lock_app_name)
    TextView mTvLockAppName;
    @Bind(R.id.et_applock_password)
    EditText mEtApplockPassword;
    @Bind(R.id.bt_applock_unlock)
    Button mBtApplockUnlock;
    private String packageName;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_applock;
    }

    @Override
    protected void onInitView() {
        Intent intent = getIntent();
        packageName = intent.getStringExtra("packageName");
        ButterKnife.bind(this);

    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable icon = applicationInfo.loadIcon(pm);
            String name = applicationInfo.loadLabel(pm).toString();
            icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
            mTvLockAppIcon.setImageDrawable(icon);
            mTvLockAppName.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInitListener() {
        mBtApplockUnlock.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        if(v.getId() == R.id.bt_applock_unlock){
            String pass = mEtApplockPassword.getText().toString().trim();
            if (TextUtils.isEmpty(pass)) {
                toast("密码不能为空.");
            }else {
                String password = SpUtil.getString(AppConstants.LOCK_PASSWORD, MD5Utils.decode("666"));
                if (password.equals(MD5Utils.decode(pass))) {
                    //发一个广播.告诉服务,已经解锁了,不用再加锁.
                    Intent intent = new Intent();
                    intent.setAction("com.songwenju.unlock");
                    intent.putExtra("unlockPackageName", packageName);
                    sendBroadcast(intent);
                    finish();
                }else {
                   toast("密码错误");
                }
            }
        }
    }


}
