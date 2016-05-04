package com.wjustudio.phoneManager.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.AppMgrAdapter;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.biz.AppLockBizImpl;
import com.wjustudio.phoneManager.biz.AppMgrBizImpl;
import com.wjustudio.phoneManager.biz.IAppMgrBiz;
import com.wjustudio.phoneManager.javaBean.AppInfo;
import com.wjustudio.phoneManager.javaBean.AppLockInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 应用管理的fragment
 */
public class AppMgrFragment extends BaseFragment {
    @Bind(R.id.tv_phone_remain)
    TextView mTvPhoneRemain;
    @Bind(R.id.tv_sd_remain)
    TextView mTvSdRemain;
    @Bind(R.id.app_recyclerView)
    RecyclerView mAppRecyclerView;
    @Bind(R.id.ll_loading)
    LinearLayout mLinearLayout;
    @Bind(R.id.tv_app_list_title)
    TextView mAppListTitle;
    private PopupWindow mPopupWindow;
    private AppInfo mItemAppInfo;

    private IAppMgrBiz mAppMgrBiz;
    private AppMgrAdapter mAppMgrAdapter;
    private Subscription mSubscription;
    List<AppInfo> userAppList = new ArrayList<>();
    List<AppInfo> systemAppList = new ArrayList<>();

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_app_mgr;
    }

    @Override
    protected void onInitView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onInitData() {
        mAppMgrBiz = new AppMgrBizImpl(getContext());
    }

    @Override
    protected void onSetViewData() {
        mTvPhoneRemain.setText("手机剩余：" + mAppMgrBiz.getPhoneFreeMemory());
        mTvSdRemain.setText("SD卡剩余：" + mAppMgrBiz.getSDFreeMemory());
        mAppRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAppRecyclerView.addItemDecoration(new DividerItemDecoration(
                mContext, DividerItemDecoration.VERTICAL_LIST
        ));
        mAppMgrAdapter = new AppMgrAdapter(mContext);
        mAppRecyclerView.setAdapter(mAppMgrAdapter);
        setOnScrollListener();
        setOnItemClickListener();
        createObservable();
    }

    private void setOnItemClickListener() {
        mAppMgrAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                dismissPopWindow();
                if (getCurrentApp(position)) return;
                int[] location = new int[2];
                view.getLocationInWindow(location);
                int x = location[0];
                int y = location[1];
                View popView = View.inflate(mContext, R.layout.app_popup, null);
                popView.findViewById(R.id.tv_appPopup_uninstall).setOnClickListener(AppMgrFragment.this);
                popView.findViewById(R.id.tv_appPopup_start).setOnClickListener(AppMgrFragment.this);
                popView.findViewById(R.id.tv_appPopup_share).setOnClickListener(AppMgrFragment.this);
                popView.findViewById(R.id.tv_appPopup_detail).setOnClickListener(AppMgrFragment.this);
                mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //添加动画,要先有背景
                mPopupWindow.setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                mPopupWindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, x + CommonUtil.dip2px(mContext, 60)
                        , y - CommonUtil.dip2px(mContext, 0));
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1.0f);
                alphaAnimation.setDuration(200);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1.0f, 0, 1.0f,
                        Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                AnimationSet set = new AnimationSet(true);
                set.addAnimation(alphaAnimation);
                set.addAnimation(scaleAnimation);
                popView.startAnimation(set);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                dismissPopWindow();
                if (getCurrentApp(position)) return;
                AppLockBizImpl biz = new AppLockBizImpl();
                AppMgrAdapter.AppInfoHolder appInfoHolder = (AppMgrAdapter.AppInfoHolder)
                        view.getTag();
                AppLockInfo appLockInfo = new AppLockInfo(mItemAppInfo.packageName);
                if (biz.isLock(mItemAppInfo.packageName)) {
                    appInfoHolder.appLock.setImageResource(R.mipmap.unlock);
                    biz.unlockApp(appLockInfo);
                } else {
                    if (!mItemAppInfo.packageName.equals(mContext.getPackageName())) {
                        appInfoHolder.appLock.setImageResource(R.mipmap.lock);
                        biz.lockApp(appLockInfo);
                    } else {
                       toast("自己不能给自己加锁!");
                    }
                }
            }
        });
    }

    private boolean getCurrentApp(int position) {
        if (position == 0) {
            return true;
        } else if (position < userAppList.size() + 1) {
            mItemAppInfo = userAppList.get(position - 1);
        } else if (position == userAppList.size() + 1) {
            return true;
        } else if (position > userAppList.size() + 1) {
            mItemAppInfo = systemAppList.get(position - userAppList.size() - 2);
        }
        return false;
    }

    public void dismissPopWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    /**
     * RecyclerView的滚动监听
     */
    private void setOnScrollListener() {
        mAppRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dismissPopWindow();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();
                //这里要注意.获取数据在子线程.要判断是否为空
                if (userAppList != null && systemAppList != null) {
                    if (firstVisibleItem <= userAppList.size()) {
                        mAppListTitle.setText("用户程序(" + userAppList.size() + ")");
                    } else {
                        mAppListTitle.setText("系统程序(" + systemAppList.size() + ")");
                    }
                }

            }
        });
    }

    private void createObservable() {
        Observable<List<AppInfo>> appShowSubscription = Observable.fromCallable(new Callable<List<AppInfo>>() {
            @Override
            public List<AppInfo> call() {
                return mAppMgrBiz.getAppInfoList();
            }
        });

        mSubscription = appShowSubscription
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<List<AppInfo>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(List<AppInfo> appInfoList) {
                                displayAppShows(appInfoList);
                            }
                        });
    }

    //加载完毕后要显示的内容
    private void displayAppShows(List<AppInfo> appInfoList) {
        mAppMgrAdapter.setList(appInfoList);
        for (AppInfo appInfo : appInfoList) {
            if (appInfo.isUser) {
                userAppList.add(appInfo);
            } else {
                systemAppList.add(appInfo);
            }
        }
        mAppListTitle.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.GONE);
        mAppRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onIntListener() {

    }

    @Override
    protected void processClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_appPopup_uninstall:
                dismissPopWindow();
                if (mItemAppInfo.packageName.endsWith(mContext.getPackageName())) {
                    ToastUtil.showToast("不能卸载自己！");
                } else {
                    intent = new Intent();
                    intent.setAction("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + mItemAppInfo.packageName));
                    mContext.startActivity(intent);
                }
                break;
            case R.id.tv_appPopup_share:
                dismissPopWindow();
                intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "发现一款不错的应用:" + mItemAppInfo.name + ",去搜搜看");
                mContext.startActivity(intent);
                break;
            case R.id.tv_appPopup_start:
                dismissPopWindow();
                PackageManager pm = mContext.getPackageManager();
                intent = pm.getLaunchIntentForPackage(mItemAppInfo.packageName);
                if (intent != null) {
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.showToast("系统核心程序，已经在后台运行");
                }
                break;
            case R.id.tv_appPopup_detail:
                dismissPopWindow();
                intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.parse("package:" + mItemAppInfo.packageName));
                mContext.startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        dismissPopWindow();
        ButterKnife.unbind(this);
    }
}
