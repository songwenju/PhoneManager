package com.wjustudio.phoneManager.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CircleImageView;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-5-13 : 17 : 27.
 * 邮箱：songwenju@outlook.com
 */
public class UserInfoActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.user_name)
    TextView mUserName;
    @Bind(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @Bind(R.id.backup_recycler_view)
    RecyclerView mBackupRecyclerView;
    @Bind(R.id.login_out)
    Button mLoginOut;
    private Dialog mExistDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);

        initExistDialog();
    }

    /**
     * 初始化退出的dialog
     */
    private void initExistDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出登录");
        builder.setMessage("是否确认退出登录？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtil.putString(AppConstants.LOGIN_USER, "");
                UserInfoActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", null);
        mExistDialog = builder.create();
    }
    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("个人中心");
        mCommonTitleLayout.setImgSettingVisible(false);
        mUserName.setText(SpUtil.getString(AppConstants.LOGIN_USER,""));
    }

    @Override
    protected void onInitListener() {
        mLoginOut.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()){
            case R.id.login_out:
                mExistDialog.show();
                break;
            case R.id.iv_avatar:
                toast("修改头像");
                break;
        }
    }


}
