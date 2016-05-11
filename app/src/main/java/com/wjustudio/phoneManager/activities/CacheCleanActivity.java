package com.wjustudio.phoneManager.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.CacheInfo;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-4-14 : 12 : 17.
 * 邮箱：songwenju@outlook.com
 */
public class CacheCleanActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tv_cache_result)
    TextView mTvCacheResult;
    @Bind(R.id.bt_cache_clear)
    Button mBtCacheClear;
    @Bind(R.id.lv_cache_app)
    ListView mLvCacheApp;
    @Bind(R.id.ll_cache_data)
    LinearLayout mLlCacheData;
    @Bind(R.id.iv_cache_img)
    ImageView mIvCacheImg;
    @Bind(R.id.tv_cache_scanApp)
    TextView mTvCacheScanApp;
    @Bind(R.id.ll_cache_anim)
    LinearLayout mLlCacheAnim;

    private List<CacheInfo> cacheInfos;
    private RotateAnimation ra;
    private PackageManager pm;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_cache_mgr;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        mCommonTitleLayout.setTitle("缓存清理");
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    @Override
    protected void onInitListener() {
    }

    @Override
    protected void processClick(View v) {

    }


    /**
     * 扫描垃圾
     */
    private void scan() {
        cacheInfos = new ArrayList<>();
        cacheInfos.clear();
        //为图片设置选择动画
        ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(RotateAnimation.INFINITE);
        LinearInterpolator interpolator = new LinearInterpolator();
        ra.setInterpolator(interpolator);

        mIvCacheImg.startAnimation(ra);

        //扫描软件
        pm = getPackageManager();
        new Thread(new Runnable() {

            @Override
            public void run() {
                List<PackageInfo> packages = pm.getInstalledPackages(0);

                for (PackageInfo packageInfo : packages) {
                    SystemClock.sleep(100);
                    String packageName = packageInfo.packageName;
                    //反射获取缓存的大小
                    //反射获取方法,这里用反射因为方法存在,但被google隐藏了.
                    try {
//                        Class<?> pmClass = Class.forName("android.content.pm.PackageManager");
//                        Method getPackageSizeInfo = pmClass.getDeclaredMethod("getPackageSizeInfo",
//                                String.class, IPackageStatsObserver.class);
//
//                        getPackageSizeInfo.invoke(pm, packageName, mStatsObserver);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    final String name = applicationInfo.loadLabel(pm).toString();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mTvCacheScanApp.setText("正在扫描:" + name);

                        }
                    });

                }
//                //扫描完成,停止动画
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                            /*for (CacheInfo cacheInfo : cacheInfos) {
//								LogUtils.i(TAG, cacheInfo.toString());
//							}*/
//                        ra.cancel();
//                        mLlCacheAnim.setVisibility(View.GONE);
//                        mLlCacheData.setVisibility(View.VISIBLE);
//                        if (cacheInfos.size() > 0) {
//                            mTvCacheResult.setVisibility(View.GONE);
//                            mBtCacheClear.setVisibility(View.VISIBLE);
//
//                            adapter = new MyAdapter();
//                            lv_cache_app.setAdapter(adapter);
//                            bt_cache_clear.setOnClickListener(new OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    // 反射获取方法
//                                    // 清理缓存
//                                    try {
//                                        Class<?> loadClass = getActivity()
//                                                .getClass()
//                                                .getClassLoader()
//                                                .loadClass(
//                                                        "android.content.pm.PackageManager");
//                                        // 获取方法
//                                        Method method = loadClass
//                                                .getDeclaredMethod(
//                                                        "freeStorageAndNotify",
//                                                        Long.TYPE,
//                                                        IPackageDataObserver.class);
//                                        // 执行方法
//                                        method.invoke(
//                                                pm,
//                                                Long.MAX_VALUE,
//                                                new MyIPackageDataObserver());
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    // 清理缓存
//                                    cacheInfos.clear();
//                                    // 更新界面
//                                    adapter.notifyDataSetChanged();
//                                    bt_cache_clear.setVisibility(View.GONE);
//                                    tv_cache_result.setVisibility(View.VISIBLE);
//                                }
//                            });
//                        } else {
//                            bt_cache_clear.setVisibility(View.GONE);
//                            tv_cache_result.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });


            }
        }).start();


    }


//    //创建内部类对象,使用远程服务的操作,远程服务的内部类,获取app的缓存大小
//    IPackageStatsObserver.Stub mStatsObserver = new Stub() {
//
//        @Override
//        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
//                throws RemoteException {
//            long cachesize = pStats.cacheSize;
//            if (cachesize > 0) {
//                String cacheSizeStr = Formatter.formatFileSize(getActivity(), cachesize);
//                LogUtils.i(TAG, pStats.packageName + "的缓存大小" + cacheSizeStr);
//                cacheInfos.add(new CacheInfo(pStats.packageName, cacheSizeStr));
//            }
//
//        }
//    };


//    /**
//     * 缓存清理
//     */
//    private class MyIPackageDataObserver extends IPackageDataObserver.Stub {
//
//        @Override
//        public void onRemoveCompleted(String packageName, boolean succeeded)
//                throws RemoteException {
//            // 清理缓存完成会调用的方法
//        }
//
//    }

}
