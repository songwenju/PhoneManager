package com.wjustudio.phoneManager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.presenter.TheftProofBizImpl;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.SpUtil;

public class TheftProofService extends Service {
    private Context mContext;
    private TheftProofBizImpl mTheftProofBiz;
    private SIMChangeReceiver mSimChangeReceiver;
    private SMSReceiver mSmsReceiver;

    public TheftProofService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.d(this,"开启服务");
        super.onCreate();
        mContext = this;
        mTheftProofBiz = new TheftProofBizImpl(mContext);

        registerSimChangeReceiver();
        registerSMSReceiver();
    }

    private void registerSMSReceiver() {
        mSmsReceiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1001);
        registerReceiver(mSmsReceiver,filter);
    }

    private void registerSimChangeReceiver() {
        mSimChangeReceiver = new SIMChangeReceiver();
        CommonUtil.registerReceiver(mContext,mSimChangeReceiver, "android.intent.action.BOOT_COMPLETED");
    }


    //各种广播接收者
    //接收到开机广播
    public class SIMChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //锁屏
            mTheftProofBiz.lockScreen();
            mTheftProofBiz.sendMsgSIMChange();
        }
    }

    /**
     * 短信到来的广播
     */
    public class SMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(this, "短信到来了");
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            String safeNum = SpUtil.getString(AppConstants.SAFE_NUM, "");
            assert objects != null;
            for (Object object : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                String sender = smsMessage.getOriginatingAddress();
                String body = smsMessage.getMessageBody();
                if (("#W"+safeNum).equals(body)) {
                    LogUtil.i(this, "播放报警音乐");
                    //拦截了不让它在通知栏显示
                    abortBroadcast();
                    //播放音乐
                    mTheftProofBiz.playSecurityMusic();

                } else if (("#L"+safeNum).equals(body)) {
                    LogUtil.i(this, "返回位置信息");
                    //将这个放在服务中,手机获取位置信息要花很长时间
                    abortBroadcast();
                    mTheftProofBiz.getLocation();
                } else if (("#P"+safeNum).equals(body)) {
                    LogUtil.i(this, "远程清除数据");
                    mTheftProofBiz.cleanData();
                    abortBroadcast();
                } else if (("#S"+safeNum).equals(body)) {
                    LogUtil.i(this, "远程锁屏");
                    //锁屏
                    mTheftProofBiz.lockScreen();
                    //重置开机密码
                    mTheftProofBiz.resetPhonePwd("1234");
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        LogUtil.d(this,"停止服务");
        super.onDestroy();
        if (mSimChangeReceiver != null) {
            unregisterReceiver(mSimChangeReceiver);
            mSimChangeReceiver = null;
        }
        if (mSmsReceiver != null) {
            unregisterReceiver(mSmsReceiver);
            mSmsReceiver = null;
        }
        mTheftProofBiz.stopLocationListener();
    }
}
