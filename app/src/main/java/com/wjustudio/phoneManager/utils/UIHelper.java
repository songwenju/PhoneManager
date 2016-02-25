package com.wjustudio.phoneManager.utils;

import android.content.Context;
import android.content.Intent;

import com.wjustudio.phoneManager.activities.LoginActivity;

/**
 * 界面帮助类
 *
 */
public class UIHelper {

    /** 全局web样式 */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page.js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window.location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common.css\">";
    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

    /**
     * 显示登录界面
     * 
     * @param context
     */
    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }



//    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
//    public static void initWebView(WebView webView) {
//        WebSettings settings = webView.getSettings();
//        settings.setDefaultFontSize(15);
//        settings.setJavaScriptEnabled(true);
//        settings.setSupportZoom(true);
//        settings.setBuiltInZoomControls(true);
//        int sysVersion = Build.VERSION.SDK_INT;
//        if (sysVersion >= 11) {
//            settings.setDisplayZoomControls(false);
//        } else {
//            ZoomButtonsController zbc = new ZoomButtonsController(webView);
//            zbc.getZoomControls().setVisibility(View.GONE);
//        }
//        webView.setWebViewClient(UIHelper.getWebViewClient());
//    }

//    /**
//     * 添加网页的点击图片展示支持
//     */
//    @SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
//    @JavascriptInterface
//    public static void addWebImageShow(final Context cxt, WebView wv) {
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.addJavascriptInterface(new OnWebViewImageListener() {
//            @Override
//            @JavascriptInterface
//            public void showImagePreview(String bigImageUrl) {
//                if (bigImageUrl != null && !StringUtils.isEmpty(bigImageUrl)) {
//                    UIHelper.showImagePreview(cxt, new String[] { bigImageUrl });
//                }
//            }
//        }, "mWebViewImageListener");
//    }

//    /**
//     * 获取webviewClient对象
//     *
//     * @return
//     */
//    public static WebViewClient getWebViewClient() {
//
//        return new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                showUrlRedirect(view.getContext(), url);
//                return true;
//            }
//        };
//    }
//
//    public static String setHtmlCotentSupportImagePreview(String body) {
//        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
//        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
//                || TDevice.isWifiOpen()) {
//            // 过滤掉 img标签的width,height属性
//            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
//            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
//            // 添加点击图片放大支持
//            // 添加点击图片放大支持
//            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
//                    "$1$2\" onClick=\"showImagePreview('$2')\"");
//        } else {
//            // 过滤掉 img标签
//            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
//        }
//        return body;
//    }
//
//    /**
//     * 发送通知广播
//     *
//     * @param context
//     * @param notice
//     */
//    public static void sendBroadCast(Context context, Notice notice) {
//        if (!((AppContext) context.getApplicationContext()).isLogin()
//                || notice == null)
//            return;
//        Intent intent = new Intent(Constants.INTENT_ACTION_NOTICE);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("notice_bean", notice);
//        intent.putExtras(bundle);
//        context.sendBroadcast(intent);
//    }
//
//    /**
//     * 发送通知广播
//     *
//     * @param context
//     */
//    public static void sendBroadcastForNotice(Context context) {
//        Intent intent = new Intent(NoticeService.INTENT_ACTION_BROADCAST);
//        context.sendBroadcast(intent);
//    }
//
//    /**
//     * 显示用户头像大图
//     *
//     * @param context
//     * @param avatarUrl
//     */
//    public static void showUserAvatar(Context context, String avatarUrl) {
//        if (StringUtils.isEmpty(avatarUrl)) {
//            return;
//        }
//        String url = AvatarView.getLargeAvatar(avatarUrl);
//        ImagePreviewActivity.showImagePrivew(context, 0, new String[] { url });
//    }

//    /**
//     * 显示登陆用户的个人中心页面
//     *
//     * @param context
//     */
//    public static void showMyInformation(Context context) {
//        showSimpleBack(context, SimpleBackPage.MY_INFORMATION);
//    }
//
//    /**
//     * 显示扫一扫界面
//     *
//     * @param context
//     */
//    public static void showScanActivity(Context context) {
//        Intent intent = new Intent(context, CaptureActivity.class);
//        context.startActivity(intent);
//    }
//
//    /**
//     * 显示设置界面
//     *
//     * @param context
//     */
//    public static void showSetting(Context context) {
//        showSimpleBack(context, SimpleBackPage.SETTING);
//    }

//    /**
//     * 显示关于界面
//     *
//     * @param context
//     */
//    public static void showAboutOSC(Context context) {
//        showSimpleBack(context, SimpleBackPage.ABOUT_OSC);
//    }

//    /**
//     * 清除app缓存
//     *
//     * @param activity
//     */
//    public static void clearAppCache(Activity activity) {
//        final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 1) {
//                    AppContext.showToastShort("缓存清除成功");
//                } else {
//                    AppContext.showToastShort("缓存清除失败");
//                }
//            }
//        };
//        new Thread() {
//            @Override
//            public void run() {
//                Message msg = new Message();
//                try {
//                    AppContext.getInstance().clearAppCache();
//                    msg.what = 1;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    msg.what = -1;
//                }
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }


}
