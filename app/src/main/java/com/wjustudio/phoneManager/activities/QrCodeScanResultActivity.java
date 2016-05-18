package com.wjustudio.phoneManager.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QrCodeScanResultActivity extends BaseActivity {
    @Bind(R.id.result)
    TextView mResult;
    @Bind(R.id.qrcode_bitmap)
    ImageView mQrcodeBitmap;
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCtlCommonTitle;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_qr_code_scan;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        mCtlCommonTitle.setTitle("二维码扫描结果");
        mCtlCommonTitle.setImgSettingVisible(false);
        //扫描完了之后调到该界面
        Bundle bundle = this.getIntent().getExtras();
        //显示扫描到的内容
        mResult.setText(bundle.getString("result"));
        //显示
//		mImageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap"));
    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }

}
