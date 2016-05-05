package com.wjustudio.phoneManager.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.wjustudio.phoneManager.activities.AppLockActivity;
import com.wjustudio.phoneManager.biz.AppLockBizImpl;
import com.wjustudio.phoneManager.biz.IAppLockBiz;
import com.wjustudio.phoneManager.javaBean.AppLockInfo;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.RunningActivityUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

import java.util.List;

public class LockAppService extends Service {
    private Context mContext;
    private IAppLockBiz mBiz;
    private UnlockReceiver unlockReceiver;
    private String unlockPackageName;


    /**
     * 解锁完成的广播接收者
     */
    public class UnlockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            unlockPackageName = intent.getStringExtra("unlockPackageName");
            LogUtil.i(this, unlockPackageName + "解锁了");
        }
    }

    public LockAppService() {
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
        mBiz = new AppLockBizImpl(mContext);

        unlockReceiver = new UnlockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.songwenju.unlock");
        registerReceiver(unlockReceiver, filter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (RunningActivityUtil.isNoOption(mContext)) {
                if (!RunningActivityUtil.isNoSwitch(mContext)) {
                    Intent intent = new Intent(
                            Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    lockApk();
                }
            }else {
                ToastUtil.showToast("您的手机不支持程序锁");
            }

        } else {
            lockApk();
        }
    }

    /**
     * 锁住Apk
     */
    private void lockApk() {
        LogUtil.i(this,"lockApk");
        new Thread(new Runnable() {
            private List<AppLockInfo> appLockInfoList;

            @Override
            public void run() {
                //每次查询数据库是一个耗时的工作,先把加锁的应用信息放在一个集合中,每次从集合中判断,当数据库内容变化时再使用内容观察者通知集合重新加载数据
                appLockInfoList = mBiz.getAllLockApp();
                Uri uri = Uri.parse("content://appLock/change");
                mContext.getContentResolver().registerContentObserver(uri, true
                        , new ContentObserver(null) {
                            @Override
                            public void onChange(boolean selfChange) {
                                super.onChange(selfChange);
                                appLockInfoList = mBiz.getAllLockApp();
                            }
                        });

                while (true) {
                    SystemClock.sleep(1000);
                    String packageName = RunningActivityUtil.getTaskPackNameForMi(mContext);
                    LogUtil.i(this, "当前任务栈顶是" + packageName);
                    for (AppLockInfo appLockInfo : appLockInfoList) {
                        String appPackageName = appLockInfo.getPackageName();
                        if (packageName.equals(appPackageName) && !packageName.equals(getPackageName())) {
                            if (!packageName.equals(unlockPackageName)) {
                                Intent intent = new Intent(mContext, AppLockActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("packageName", packageName);
                                startActivity(intent);
                            }
                        }
                    }
                }
            }
        }).start();
    }


    @Override
    public void onDestroy() {
        LogUtil.d(this, "停止服务");
        super.onDestroy();
    }
}
