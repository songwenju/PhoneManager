package com.wjustudio.phoneManager.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.biz.IProgressBiz;
import com.wjustudio.phoneManager.biz.ProgressBizImpl;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * songwenju on 16-4-14 : 12 : 16.
 * 邮箱：songwenju@outlook.com
 */
public class ProcessMgrActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tv_progress_num)
    TextView mTvProgressNum;
    @Bind(R.id.tv_progress_rate)
    TextView mTvProgressRate;
    @Bind(R.id.ll_loading)
    LinearLayout mLlLoading;
    @Bind(R.id.progress_recyclerView)
    RecyclerView mProgressRecyclerView;
    @Bind(R.id.tv_progress_list_title)
    TextView mTvProgressListTitle;

    @Bind(R.id.btn_select_all)
    Button mBtnSelectAll;
    @Bind(R.id.btn_select_reverse)
    Button mBtnSelectReverse;
    @Bind(R.id.btn_clear)
    Button mBtnClear;
    @Bind(R.id.btn_hide_progress)
    Button mBtnHideProgress;
    private IProgressBiz mBiz;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_process_mgr;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        mCommonTitleLayout.setTitle("进程管理");
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    @Override
    protected void onInitData() {
        mBiz = new ProgressBizImpl(mContext);
    }

    @Override
    protected void onSetViewData() {
        mProgressRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mProgressRecyclerView.addItemDecoration(new DividerItemDecoration(
                mContext,DividerItemDecoration.VERTICAL_LIST
        ));

        //mProgressRecyclerView.setAdapter();
        createObservable();
    }

    /**
     * 创建RxJava观察者和被观察者
     */
    private void createObservable() {
        //观察者
        Observable<List<ProgressInfo>> progressSubscription = Observable.fromCallable(
                new Callable<List<ProgressInfo>>() {
                    @Override
                    public List<ProgressInfo> call() throws Exception {
                        return mBiz.getProgressInfoList();
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
                        new Observer<List<ProgressInfo>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(List<ProgressInfo> progressInfos) {
                                displayProgress(progressInfos);
                            }
                        }
                );
    }

    private void displayProgress(List<ProgressInfo> progressInfos) {
        mTvProgressNum.setText("进程数:"+ progressInfos.size());
        try {
            mTvProgressRate.setText("可用内存/总内存:"+ mBiz.getAvailMem()+"/"+mBiz.getTotalMem());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mTvProgressListTitle.setVisibility(View.VISIBLE);
        mLlLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onInitListener() {
        mCommonTitleLayout.setOnSettingImgClickListener(new CommonTitleLayout.OnSettingImgClickListener() {
            @Override
            public void onSettingImgClick() {
                toast("setting");
//                Intent intent = new Intent(mContext, BlackNumSettingActivity.class);
//                startActivity(intent);
            }
        });

        mBtnSelectAll.setOnClickListener(this);
        mBtnSelectReverse.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);
        mBtnHideProgress.setOnClickListener(this);

    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_all:
                toast("全选");
//        //遍历设置为选中
//        for (TaskInfo taskInfo : taskInfos) {
//            if (!taskInfo.packageName.equals(getPackageName())) {
//                taskInfo.isChecked = true;
//            }
//        }
//        adapter.notifyDataSetChanged();
                break;
            case R.id.btn_select_reverse:
                toast("反选");
//        //遍历设置为原来状态相反
//        for (TaskInfo taskInfo : taskInfos) {
//            if (!taskInfo.packageName.equals(getPackageName())) {
//                taskInfo.isChecked = !taskInfo.isChecked;
//            }
//        }
//        adapter.notifyDataSetChanged();
                break;
            case R.id.btn_clear:
                toast("清理");
                break;
            case R.id.btn_hide_progress:
                toast("隐藏系统");
//        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        killProgress(am);
//        break;
//        case R.id.bt_task_set://点击隐藏系统程序
//        showSysTask = !showSysTask;
//        adapter.notifyDataSetChanged();
                break;
        }
    }


}
