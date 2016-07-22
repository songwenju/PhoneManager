package com.example.rxjavademo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public Context mContext;
    private RecyclerView mRecyclerView;
    private AppMgrAdapter mAppMgrAdapter;
    private IAppMgrBiz mAppMgrBiz;
    private ProgressBar mProgressBar;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mAppMgrBiz = new AppMgrBizImpl(mContext);
        initUI();
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
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void initUI() {
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAppMgrAdapter = new AppMgrAdapter(this);
        mRecyclerView.setAdapter(mAppMgrAdapter);
    }


    public void click(View view){
        startActivity(new Intent(this,ButtonActivity.class));
    }
    @Override
    protected void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
