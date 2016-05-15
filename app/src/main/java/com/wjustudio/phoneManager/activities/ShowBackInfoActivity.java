package com.wjustudio.phoneManager.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.BackupInfoAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.ContactJson;
import com.wjustudio.phoneManager.javaBean.UserContactInfo;
import com.wjustudio.phoneManager.utils.ContactUtils;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.OkHttpUtil;
import com.wjustudio.phoneManager.utils.RequestServerUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者： songwenju on 2016/5/15 19:52.
 * 邮箱： songwenju@outlook.com
 */
public class ShowBackInfoActivity extends BaseActivity{
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.rv_my_back)
    RecyclerView mRvMyBack;
    private ProgressDialog mProgressDialog;
    private List<UserContactInfo.UserContactBean> mUserContacts;
    private BackupInfoAdapter mAdapter;
    private Gson mGson;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_backup_information;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }


    @Override
    protected void onInitData() {
        mProgressDialog = ProgressDialog.show(mContext, null, "正在加载备份信息，请稍候...");
        mGson = new Gson();
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
        mRvMyBack.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new BackupInfoAdapter(mContext, mUserContacts);
        mRvMyBack.setAdapter(mAdapter);
        mAdapter.setOnReloadClickListener(new BackupInfoAdapter.onReloadClickListener() {
            @Override
            public void onReloadClick(int position) {
                final String backupJson = mUserContacts.get(position).getJsonUrl();
                LogUtil.i(this,"json："+backupJson);
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
                                    public void onCompleted() {}

                                    @Override
                                    public void onError(Throwable e) {}
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
                for (ContactJson.ContactsBean contact :contacts) {
                    ContactUtils.writeToPhone(mContext,contact);
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("我的备份");
        mCommonTitleLayout.setImgSettingVisible(false);
        createObservable();
    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }
}
