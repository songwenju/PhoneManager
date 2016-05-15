package com.wjustudio.phoneManager.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.ContactInfo;
import com.wjustudio.phoneManager.utils.ContactUtils;
import com.wjustudio.phoneManager.utils.FileUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.NetUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： songwenju on 2016/5/15 13:38.
 * 邮箱： songwenju@outlook.com
 */
public class BackUpActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.backup_contact)
    Button mBackupContact;
    @Bind(R.id.reload_contact)
    Button mReloadContact;
    private String mLocalJsonUrl;
    private String mResultStr;
    private ProgressDialog mProgressDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_backup_request;
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
        mCommonTitleLayout.setTitle("信息备份");
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    @Override
    protected void onInitListener() {
        mBackupContact.setOnClickListener(this);
        mReloadContact.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.backup_contact:
                //1.获得通讯录信息
                List<ContactInfo> mContactList = ContactUtils.getContact(mContext);
                //2.拼接成json串写入文件，文件名带日期
                writeToJsonFile(mContactList);
                //3.将文件上传到服务器，并将用户信息和文件信息写入服务器数据库（和user数据库一对多）
                // 新线程后台上传服务端
                mProgressDialog = ProgressDialog.show(mContext, null, "正在上传，请稍候...");
                new Thread(uploadJsonRunnable).start();
                break;
            case R.id.reload_contact:
                Intent intent = new Intent(mContext,ShowBackInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void writeToJsonFile(List<ContactInfo> mContactList) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ 'contacts': [");
        for (ContactInfo contactInfo : mContactList) {
            sb.append("{")
                    .append("'contact_name':'").append(contactInfo.contact_name).append("',")
                    .append("'contact_phoneNum':'").append(contactInfo.contact_phoneNum).append("',")
                    .append("'email':'").append(contactInfo.email).append("'")
                    .append("},");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("] }");
        LogUtil.i(this, sb.toString());
        mLocalJsonUrl = FileUtil.saveJsonFile(SpUtil.getString(AppConstants.LOGIN_USER, ""), sb.toString());
        LogUtil.i(this, "mLocalJsonUrl:" + mLocalJsonUrl);
    }

    /**
     * 使用HttpUrlConnection post上传文件
     * 原理是： 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
     */
    Runnable uploadJsonRunnable = new Runnable() {
        @Override
        public void run() {
            String jsonUrl = AppConstants.JSON_URL_PATH;
            if (TextUtils.isEmpty(jsonUrl)) {
                BackUpActivity.this.runOnUiThread(new Runnable() {
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
                    URL url = new URL(jsonUrl);
                    textParams = new HashMap<>();
                    fileParams = new HashMap<>();
                    // 要上传的文件
                    File file = new File(mLocalJsonUrl);
                    fileParams.put("json", file);
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
                    mProgressDialog.dismiss();

                    try {
                        // 返回数据示例，根据需求和后台数据灵活处理
                        // {"status":"1","statusMessage":"上传成功","imageUrl":"http://120.24.219.49/726287_temphead.jpg"}
                        if (mResultStr != null) {
                            JSONObject jsonObject = new JSONObject(mResultStr);
                            // 服务端以字符串“1”作为操作成功标记
                            if (jsonObject.optString("status").equals("1")) {
                                toast("备份成功");
                            } else {
                                toast(jsonObject.optString("statusMessage"));
                            }
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
