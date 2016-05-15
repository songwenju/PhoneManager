package com.wjustudio.phoneManager.activities;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： songwenju on 2016/5/14 21:45.
 * 邮箱： songwenju@outlook.com
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.wv_about_us)
    WebView mWvAboutUs;
    @Bind(R.id.pb_progress)
    ProgressBar mPbProgress;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about_us;
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
        mCommonTitleLayout.setTitle("关于");
        mCommonTitleLayout.setImgSettingVisible(false);

        //支持javascript
        mWvAboutUs.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWvAboutUs.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWvAboutUs.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mWvAboutUs.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        mWvAboutUs.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWvAboutUs.getSettings().setLoadWithOverviewMode(true);
        mWvAboutUs.loadUrl(AppConstants.ABOUT_US);
        mWvAboutUs.setWebViewClient(new MyAndroidWebViewClient ());

    }

    @Override
    protected void onInitListener() {

    }

    class MyAndroidWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mWvAboutUs.setVisibility(View.VISIBLE);
            mPbProgress.setVisibility(View.GONE);
        }

    }



    @Override
    protected void processClick(View v) {

    }

}
