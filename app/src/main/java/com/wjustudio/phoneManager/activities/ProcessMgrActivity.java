package com.wjustudio.phoneManager.activities;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.ProgressMgrAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.biz.IProgressBiz;
import com.wjustudio.phoneManager.biz.ProgressBizImpl;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
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
    @Bind(R.id.btn_cancel_progress)
    Button mBtnHideProgress;
    private IProgressBiz mBiz;
    private ProgressMgrAdapter mAdapter;
    private List<ProgressInfo> userProgressList = new ArrayList<>();
    private List<ProgressInfo> systemProgressList = new ArrayList<>();
    private ProgressInfo mProgressInfo;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_progress_mgr;
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
        mAdapter = new ProgressMgrAdapter(mContext);
        mProgressRecyclerView.setAdapter(mAdapter);
        setOnScrollListener();
        setOnItemClickListener();
        createObservable();
    }

    /**
     * RecyclerView的滚动监听
     */
    private void setOnScrollListener() {
        mProgressRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findFirstVisibleItemPosition();
                //这里要注意.获取数据在子线程.要判断是否为空
                if (userProgressList != null && systemProgressList != null) {
                    if (firstVisibleItem <= userProgressList.size()) {
                        mTvProgressListTitle.setText("用户进程(" + userProgressList.size() + ")");
                    } else {
                        mTvProgressListTitle.setText("系统进程(" + systemProgressList.size() + ")");
                    }
                }
            }
        });
    }

    private void setOnItemClickListener() {
        mAdapter.setOnItemClickListener(new BaseRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getCurrentProgress(position);
                ProgressMgrAdapter.ProgressInfoHolder progressInfoHolder =
                        (ProgressMgrAdapter.ProgressInfoHolder) view.getTag();
                boolean checked = progressInfoHolder.progressIsChecked.isChecked();
                progressInfoHolder.progressIsChecked.setChecked(!checked);
               mAdapter.setChecked(position,checked ? ProgressMgrAdapter.UNCHECKED : ProgressMgrAdapter.CHECKED);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private boolean getCurrentProgress(int position) {
        if (position == 0) {
            return true;
        } else if (position < userProgressList.size() + 1) {
            mProgressInfo = userProgressList.get(position - 1);
        } else if (position == userProgressList.size() + 1) {
            return true;
        } else if (position > userProgressList.size() + 1) {
            mProgressInfo = userProgressList.get(position - userProgressList.size() - 2);
        }
        return false;
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

    private void displayProgress(List<ProgressInfo> progressInfoList) {
        mTvProgressNum.setText("进程数:"+ progressInfoList.size());
        try {
            mTvProgressRate.setText("可用内存/总内存:"+ mBiz.getAvailMem()+"/"+mBiz.getTotalMem());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ProgressInfo progressInfo : progressInfoList) {
            if (progressInfo.isUser) {
                userProgressList.add(progressInfo);
            } else {
                systemProgressList.add(progressInfo);
            }
        }
        mTvProgressListTitle.setVisibility(View.VISIBLE);
        mLlLoading.setVisibility(View.GONE);
        mAdapter.setList(progressInfoList);
        mProgressRecyclerView.setVisibility(View.VISIBLE);
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
            case R.id.btn_cancel_progress:
                toast("取消");
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
