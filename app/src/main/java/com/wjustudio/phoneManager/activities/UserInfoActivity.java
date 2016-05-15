package com.wjustudio.phoneManager.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.BackupInfoAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.ContactJson;
import com.wjustudio.phoneManager.javaBean.UserContactInfo;
import com.wjustudio.phoneManager.utils.ContactUtils;
import com.wjustudio.phoneManager.utils.FileUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.NetUtil;
import com.wjustudio.phoneManager.utils.OkHttpUtil;
import com.wjustudio.phoneManager.utils.RequestServerUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;
import com.wjustudio.phoneManager.widgt.SelectPicPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    SimpleDraweeView mIvAvatar;
    @Bind(R.id.backup_recycler_view)
    RecyclerView mRvMyBack;
    @Bind(R.id.login_out)
    Button mLoginOut;
    private Dialog mExistDialog;
    private String mLocalUrlPath;    // 图片本地路径
    private String mResultStr = "";  // 服务端返回结果集
    private static ProgressDialog pd;// 上传等待进度圈
    private ProgressDialog mProgressDialog;
    private List<UserContactInfo.UserContactBean> mUserContacts;
    private BackupInfoAdapter mAdapter;
    private Gson mGson;

    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        initExistDialog();
        if (!TextUtils.isEmpty(SpUtil.getString(AppConstants.LOGIN_USER, ""))) {
            mProgressDialog = ProgressDialog.show(mContext, null, "正在加载备份信息，请稍候...");
        }
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
                SpUtil.putString(AppConstants.AVATAR_SERVER_PATH, "");
                UserInfoActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", null);
        mExistDialog = builder.create();
    }

    @Override
    protected void onInitData() {
        mGson = new Gson();
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("个人中心");
        mCommonTitleLayout.setImgSettingVisible(false);
        mUserName.setText(SpUtil.getString(AppConstants.LOGIN_USER, ""));
    }

    @Override
    protected void onInitListener() {
        mLoginOut.setOnClickListener(this);
        mIvAvatar.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String spUrl = SpUtil.getString(AppConstants.AVATAR_SERVER_PATH, "");
        Uri uri = Uri.parse(spUrl);
        mIvAvatar.setImageURI(uri);

        createObservable();
    }


    /**
     * 创建RxJava观察者和被观察者
     */
    private void createObservable() {
        //观察者
        Observable<String> progressSubscription = Observable.fromCallable(
                new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return RequestServerUtil.getBackupInfo(SpUtil.getString(AppConstants.LOGIN_USER, ""));
                    }
                }
        );

        //观察者和被观察者建立关系
        progressSubscription
                .subscribeOn(Schedulers.io())
                //在主线程
                .observeOn(AndroidSchedulers.mainThread())
                //订阅被观察者
                .subscribe(
                        //被观察者
                        new Observer<String>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String backUpInfoJson) {
                                displayResult(backUpInfoJson);
                                LogUtil.i(this, "backUpInfoJson:" + backUpInfoJson);
                            }
                        }
                );
    }

    private void displayResult(final String backUpInfoJson) {
        LogUtil.i(this, "displayResult");
        mProgressDialog.dismiss();
        UserContactInfo userContact;
        userContact = mGson.fromJson(backUpInfoJson, UserContactInfo.class);
        mUserContacts = userContact.getUserContact();
        for (UserContactInfo.UserContactBean userContactInfo : mUserContacts) {
            LogUtil.i(this, "userContactInfo:" + userContactInfo.getJsonUrl());
        }
        //2.选择对应的备份，下载下来，解析到集合中
        mRvMyBack.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMyBack.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new BackupInfoAdapter(mContext, mUserContacts);
        mRvMyBack.setAdapter(mAdapter);
        mAdapter.setOnReloadClickListener(new BackupInfoAdapter.onReloadClickListener() {
            @Override
            public void onReloadClick(int position) {
                final String backupJson = mUserContacts.get(position).getJsonUrl();
                LogUtil.i(this, "json：" + backupJson);
                Observable<String> progressSubscription = Observable.fromCallable(
                        new Callable<String>() {
                            @Override
                            public String call() throws Exception {
                                return OkHttpUtil.getStringFromServer(backupJson);
                            }
                        }
                );

                //观察者和被观察者建立关系
                progressSubscription
                        .subscribeOn(Schedulers.io())
                        //在主线程
                        .observeOn(AndroidSchedulers.mainThread())
                        //订阅被观察者
                        .subscribe(
                                //被观察者
                                new Observer<String>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }

                                    @Override
                                    public void onNext(String stringFromServer) {
                                        showResetDialog(stringFromServer);
                                    }
                                }
                        );
            }
        });
    }

    private void showResetDialog(final String stringFromServer) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("还原通讯录");
        builder.setMessage("是否确认还原？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContactJson contactJson = mGson.fromJson(stringFromServer, ContactJson.class);
                List<ContactJson.ContactsBean> contacts = contactJson.getContacts();
                for (ContactJson.ContactsBean contact : contacts) {
                    ContactUtils.writeToPhone(mContext, contact);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.login_out:
                mExistDialog.show();
                break;
            case R.id.iv_avatar:
                menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                    AppConstants.IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, AppConstants.REQUEST_CODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, AppConstants.REQUEST_CODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case AppConstants.REQUEST_CODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case AppConstants.REQUEST_CODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/"
                        + AppConstants.IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case AppConstants.REQUEST_CODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, AppConstants.REQUEST_CODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picData
     */
    private void setPicToView(Intent picData) {
        Bundle extras = picData.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            mLocalUrlPath = FileUtil.saveBitmapFile(SpUtil.getString(AppConstants.LOGIN_USER, ""), photo);
            LogUtil.i(this, "mLocalUrlPath:" + mLocalUrlPath);


            // 新线程后台上传服务端
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            new Thread(uploadImageRunnable).start();
        }
    }


    /**
     * 使用HttpUrlConnection post上传文件
     * 原理是： 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
     */
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            String imgUrl = AppConstants.PIC_URL_PATH;
            if (TextUtils.isEmpty(imgUrl)) {
                UserInfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Map<String, String> textParams;
                Map<String, File> fileParams;

                try {
                    // 创建一个URL对象
                    URL url = new URL(imgUrl);
                    textParams = new HashMap<>();
                    fileParams = new HashMap<>();
                    // 要上传的图片文件
                    File file = new File(mLocalUrlPath);
                    fileParams.put("image", file);
                    // 利用HttpURLConnection对象从网络中获取网页数据
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
                    conn.setConnectTimeout(5000);
                    // 设置允许输出（发送POST请求必须设置允许输出）
                    conn.setDoOutput(true);
                    // 设置使用POST的方式发送
                    conn.setRequestMethod("POST");
                    // 设置不使用缓存（容易出现问题）
                    conn.setUseCaches(false);
                    conn.setRequestProperty("Charset", "UTF-8");//设置编码
                    // 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
                    conn.setRequestProperty("ser-Agent", "Fiddler");
                    // 设置contentType
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                    OutputStream os = conn.getOutputStream();
                    DataOutputStream ds = new DataOutputStream(os);
                    NetUtil.writeStringParams(textParams, ds);
                    NetUtil.writeFileParams(fileParams, ds);
                    NetUtil.paramsEnd(ds);
                    // 对文件流操作完,要记得及时关闭
                    os.close();
                    // 服务器返回的响应吗
                    int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                    LogUtil.i(this, "code:" + code);
                    // 对响应码进行判断
                    if (code == 200) {// 返回的响应码200,是成功
                        // 得到网络返回的输入流
                        InputStream is = conn.getInputStream();
                        mResultStr = NetUtil.readString(is);
                        LogUtil.i(this, "mResultStr" + mResultStr);

                    } else {
                        toast("请求URL失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
            }
        }
    };

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();

                    try {
                        // 返回数据示例，根据需求和后台数据灵活处理
                        // {"status":"1","statusMessage":"上传成功","imageUrl":"http://120.24.219.49/726287_temphead.jpg"}
                        JSONObject jsonObject = new JSONObject(mResultStr);

                        // 服务端以字符串“1”作为操作成功标记
                        if (jsonObject.optString("status").equals("1")) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图，3为三分之一
                            option.inSampleSize = 1;

                            // 服务端返回的JsonObject对象中提取到图片的网络URL路径
                            String imageUrl = jsonObject.optString("imageUrl");
                            LogUtil.i(this, "imgUrl:" + imageUrl);
//                            String serverImageUrl = AppConstants.BASE_URL + StringUtils.spliteLast(imageUrl, "\\");
                            SpUtil.putString(AppConstants.AVATAR_SERVER_PATH, imageUrl);
                            LogUtil.i(this, "serverImageUrl:" + imageUrl);
                            mIvAvatar.setImageURI(Uri.parse(imageUrl));
                            toast("头像修改成功");
                        } else {
                            toast(jsonObject.optString("statusMessage"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
            return false;
        }
    });

}
