package com.wjustudio.phoneManager.biz;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.utils.SpUtil;

/**
 * 作者：songwenju on 2016/3/20 16:13
 * 邮箱：songwenju01@163.com
 */
public class TheftProofBizImpl implements ITheftProofServiceBiz {
    private Context mContext;
    private LocationManager mLocationManager;
    private MyLocalListener mLocalListener;
    private final DevicePolicyManager mDpm;

    public TheftProofBizImpl(Context context) {
        this.mContext = context;
        mDpm = (DevicePolicyManager) mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    public String getSIM() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }

    @Override
    public void bindSIM() {
        SpUtil.putString(AppConstants.SIM_SERIAL_NUM, getSIM());
    }

    @Override
    public void sendMsgSIMChange() {
        boolean isOpenProtect = SpUtil.getBoolean(AppConstants.IS_OPEN_PROTECT, false);
        if (isOpenProtect) {
            String safeNum = SpUtil.getString(AppConstants.SAFE_NUM, null);
            if (!TextUtils.isEmpty(safeNum)) {
                //发送报警短信
                String simSerialNumSp = SpUtil.getString(AppConstants.SIM_SERIAL_NUM, null);
                String simSerialNum = getSIM();
                //如果两个sim卡信息有一个为空,则不进行下面的操作
                if (TextUtils.isEmpty(simSerialNumSp) || TextUtils.isEmpty(simSerialNum)) {
                    return;
                }
                //比较两个sim卡信息,如果不相同,重启时则给设置的安全号码发送短信
                if (!simSerialNum.equals(simSerialNumSp)) {
                    SmsManager sm = SmsManager.getDefault();
                    sm.sendTextMessage(safeNum, null, "手机电话卡换了", null, null);
                    lockScreen();
                }
            }
        }
    }

    @Override
    public void playSecurityMusic() {
        //播放res目录下播放音频文件
        AudioManager audioManager = (AudioManager) mContext.getSystemService(mContext.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        MediaPlayer player = MediaPlayer.create(mContext, R.raw.ylzs);
        player.setVolume(1.0f, 1.0f);
        player.setLooping(true);
        player.start();
    }

    @Override
    public void getLocation() {
        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(true);
        String bestProvider = mLocationManager.getBestProvider(criteria, true);
        mLocalListener = new MyLocalListener();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(bestProvider, 0, 0, mLocalListener);
    }

    public class MyLocalListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            float accuracy = location.getAccuracy();//精确度
            double latitude = location.getLatitude();//经度
            double longitude = location.getLongitude();//纬度
            double altitude = location.getAltitude();  //海拔
            float speed = location.getSpeed();//移动速度

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                    .append("accuracy:" + accuracy + "\n")
                    .append("latitude:" + latitude + "\n")
                    .append("longitude:" + longitude + "\n")
                    .append("altitude:" + altitude + "\n")
                    .append("speed:" + speed);
            String safeNum = SpUtil.getString(AppConstants.SAFE_NUM, "");
            if (!TextUtils.isEmpty(safeNum)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safeNum, null, stringBuilder.toString(), null, null);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 停止监听
     */
    public void stopLocationListener() {
        if (mLocationManager != null) {
            if (mLocalListener != null) {
                if (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.removeUpdates(mLocalListener);
            }
            mLocationManager = null;
        }
    }

    @Override
    public void lockScreen() {
        mDpm.lockNow();
    }

    @Override
    public void resetPhonePwd(String newNum) {
        mDpm.resetPassword(newNum,0);
    }

    @Override
    public void cleanData() {
        //清除sd卡数据
       mDpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
    }
}
