package com.wjustudio.phoneManager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.wjustudio.phoneManager.biz.BlackNumBizImpl;
import com.wjustudio.phoneManager.javaBean.BlackNumInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.lang.reflect.Method;

public class BlackNumService extends Service {
    private Context mContext;
    private SMSReceiver mSmsReceiver;
    private TelephonyManager mTelephonyManager;
    private MyListener mListener;
    private BlackNumBizImpl mBlackNumBiz;

    public BlackNumService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.d(this, "开启服务");
        super.onCreate();
        mContext = this;
        mBlackNumBiz = new BlackNumBizImpl();


        aboardTel();
        registerSMSReceiver();
    }

    private void aboardTel() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mListener = new MyListener();
        mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                //挂断电话
                BlackNumInfo blackNumInfo = mBlackNumBiz.getBlackNumInfo(incomingNumber);

                if (blackNumInfo != null) {
                    int mode = blackNumInfo.getMode();
                    if (mode == BlackNumBizImpl.BLACK_NUM_ALL||mode == BlackNumBizImpl.BLACK_NUM_PHONE) {
                        endCall();
                        wrapCallInfo(incomingNumber);
                    }
                }
            }
        }
    }

    public void wrapCallInfo(final String num) {
        final ContentResolver resolver = getContentResolver();
        final Uri uri = Uri.parse("content://call_log/calls");
        resolver.registerContentObserver(uri, true, new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                resolver.delete(uri, "number = ?", new String[]{num});
                resolver.unregisterContentObserver(this);
            }
        });
    }

    public void endCall() {
        //通过反射使用ServiceManager,
        try {
            //1.通过类加载器获取class文件
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            //2.获取执行的方法
            //name : 方法名
            //parameterTypes : 参数类型
            Method method = serviceManager.getDeclaredMethod("getService", String.class);
            //3.执行方法
            //args : 实际参数
            //这里需要两个远程服务类.
            IBinder invoke = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            //aidl  .aidl
            //ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
            //挂断电话
            //iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void registerSMSReceiver() {
        mSmsReceiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(mSmsReceiver, filter);
    }


    /**
     * 短信到来的广播
     */
    public class SMSReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(this, "短信到来了");

            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            assert objects != null;
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();
                LogUtil.i(this, "拦截了一条短信,发送人：" + sender + "内容：" + body);
                BlackNumInfo blackNumInfo = mBlackNumBiz.getBlackNumInfo(sender);
                if (blackNumInfo != null) {
                    int mode = blackNumInfo.getMode();
                    if (mode == BlackNumBizImpl.BLACK_NUM_SMS || mode == BlackNumBizImpl.BLACK_NUM_ALL) {
                        //在黑名单中,拦截
                        abortBroadcast();
                    }
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.d(this, "停止服务");
        super.onDestroy();

        if (mSmsReceiver != null) {
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver = null;
        }
    }
}
