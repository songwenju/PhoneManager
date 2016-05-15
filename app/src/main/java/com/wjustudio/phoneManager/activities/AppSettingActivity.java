package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.lib.switchButton.SwitchButton;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-5-13 : 20 : 04.
 * 邮箱：songwenju@outlook.com
 */
public class AppSettingActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.iv_avatar)
    SimpleDraweeView mIvAvatar;
    @Bind(R.id.user_info)
    LinearLayout mUserInfo;
    @Bind(R.id.sb_md)
    SwitchButton mSbMd;
    @Bind(R.id.rl_switch_day_mode)
    RelativeLayout mRlSwitchDayMode;
    @Bind(R.id.tv_about)
    TextView mTvAbout;
    @Bind(R.id.tv_version)
    TextView mTvVersion;
    @Bind(R.id.tv_share)
    TextView mTvShare;
    private PackageManager mPackageManager;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_app_setting;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);

    }

    @Override
    protected void onInitData() {
        mPackageManager = getPackageManager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String spUrl = SpUtil.getString(AppConstants.AVATAR_SERVER_PATH, "");
        if (spUrl.equals("")) {
            mIvAvatar.setImageResource(R.mipmap.login_default_face);
        } else {
            Uri uri = Uri.parse(spUrl);
            mIvAvatar.setImageURI(uri);
        }
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("设置");
        mCommonTitleLayout.setImgSettingVisible(false);
        PackageInfo packageInfo = null;
        try {
            packageInfo = mPackageManager.getPackageInfo("com.wjustudio.phoneManager", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String versionName = packageInfo.versionName;

            mTvVersion.setText("版本号：" +versionName);
        }
    }

    @Override
    protected void onInitListener() {
        mUserInfo.setOnClickListener(this);
        mTvAbout.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        Intent intent ;
        switch (v.getId()) {
            case R.id.user_info:
                 intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_about:
                 intent = new Intent(mContext,AboutActivity.class);
                startActivity(intent);
                break;
        }
    }

}
