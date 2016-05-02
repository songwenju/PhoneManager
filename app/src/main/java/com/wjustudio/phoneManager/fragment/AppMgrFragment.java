package com.wjustudio.phoneManager.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.AppMgrAdapter;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.biz.AppMgrBizImpl;
import com.wjustudio.phoneManager.biz.IAppMgrBiz;
import com.wjustudio.phoneManager.javaBean.AppInfo;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

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
    private IAppMgrBiz mAppMgrBiz;
    private AppMgrAdapter mAppMgrAdapter;
    private Subscription mSubscription;

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
        createObservable();
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
                                displayTvShows(appInfoList);
                            }
                        });
    }

    private void displayTvShows(List<AppInfo> appInfoList) {
        mAppMgrAdapter.setList(appInfoList);
        mLinearLayout.setVisibility(View.GONE);
        mAppRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onIntListener() {

    }

    @Override
    protected void processClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        ButterKnife.unbind(this);
    }
}
