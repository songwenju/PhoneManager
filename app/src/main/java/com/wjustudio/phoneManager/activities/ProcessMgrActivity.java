package com.wjustudio.phoneManager.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.biz.IProgressBiz;
import com.wjustudio.phoneManager.biz.ProgressBizImpl;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    //    @Bind(R.id.btn_select_reverse)
//    Button mBtnSelectReverse;
    @Bind(R.id.btn_clear)
    Button mBtnClear;
    @Bind(R.id.btn_cancel_progress)
    Button mBtnHideProgress;
    private IProgressBiz mBiz;
    private ProgressMgrAdapter mAdapter;
    private List<ProgressInfo> mProgressList = new ArrayList<>();
    private List<ProgressInfo> mUserProgressList = new ArrayList<>();
    private List<ProgressInfo> mSystemProgressList = new ArrayList<>();
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
                mContext, DividerItemDecoration.VERTICAL_LIST
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
                if (mUserProgressList != null && mSystemProgressList != null) {
                    if (firstVisibleItem <= mUserProgressList.size()) {
                        mTvProgressListTitle.setText("用户进程(" + mUserProgressList.size() + ")");
                    } else {
                        mTvProgressListTitle.setText("系统进程(" + mSystemProgressList.size() + ")");
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
                if (progressInfoHolder != null) {
                    boolean checked = progressInfoHolder.progressIsChecked.isChecked();
                    progressInfoHolder.progressIsChecked.setChecked(!checked);
                    mAdapter.setChecked(position,
                            checked ? ProgressMgrAdapter.UNCHECKED : ProgressMgrAdapter.CHECKED);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private boolean getCurrentProgress(int position) {
        if (position == 0) {
            return true;
        } else if (position < mUserProgressList.size() + 1) {
            mProgressInfo = mUserProgressList.get(position - 1);
        } else if (position == mUserProgressList.size() + 1) {
            return true;
        } else if (position > mUserProgressList.size() + 1) {
            mProgressInfo = mSystemProgressList.get(position - mUserProgressList.size() - 2);
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
        mProgressList.clear();
        mProgressList.addAll(progressInfoList);

        for (ProgressInfo progressInfo : progressInfoList) {
            if (progressInfo.isUser) {
                mUserProgressList.add(progressInfo);
            } else {
                mSystemProgressList.add(progressInfo);
            }
        }
        mAdapter.notifyDataSetChanged();
        setTitleText();
        mTvProgressListTitle.setVisibility(View.VISIBLE);
        mLlLoading.setVisibility(View.GONE);
        mAdapter.setList(mProgressList);
        mProgressRecyclerView.setVisibility(View.VISIBLE);
    }

    private void setTitleText() {
        mTvProgressNum.setText("进程数:" + (mUserProgressList.size() + mSystemProgressList.size()));
        try {
            mTvProgressRate.setText("可用内存/总内存:" + mBiz.getAvailMem() + "/" + mBiz.getTotalMem());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
//        mBtnSelectReverse.setOnClickListener(this);
        mBtnClear.setOnClickListener(this);
        mBtnHideProgress.setOnClickListener(this);

    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_all:
                toast("全选");
                //遍历设置为选中
                for (int i = 0; i < mProgressList.size(); i++) {
                    if (!mProgressList.get(i).packageName.equals(getPackageName())) {
                        mProgressList.get(i).isChecked = true;
                    }
                }
//                mAdapter.setAllChecked();
//                for (ProgressInfo progressInfo : mProgressList) {
//                    if (!progressInfo.packageName.equals(getPackageName())) {
//                        progressInfo.isChecked = true;
//                    }
//                }
                mAdapter.notifyDataSetChanged();
                break;
//            case R.id.btn_select_reverse:
//                toast("反选");
//                //遍历设置为原来状态相反
////                mAdapter.setConvertChecked();
//                //遍历设置为原来状态相反
//                for (ProgressInfo progressInfo : mProgressList) {
//                    if (!progressInfo.packageName.equals(getPackageName())) {
//                        progressInfo.isChecked = !progressInfo.isChecked;
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
//
//                break;
            case R.id.btn_clear:
                toast("清理");
                killProgress();

		   /*这样写会出现只能清除用户进程的非本身进程
            * ProgressInfos.removeAll(deleteList);
			fillData();*/
//                fillBarText();
//                adapter.notifyDataSetChanged();
//                deleteList.clear();
//                deleteList = null;
//                isCleared = true;
                break;
            case R.id.btn_cancel_progress:
                toast("取消");
                //遍历设置为选中
                for (ProgressInfo progressInfo : mProgressList) {
                    if (!progressInfo.packageName.equals(getPackageName())) {
                        progressInfo.isChecked = false;
                    }
                }
//                mAdapter.setAllUnchecked();
                mAdapter.notifyDataSetChanged();
//        case R.id.bt_task_set://点击隐藏系统程序
//        showSysTask = !showSysTask;
//        adapter.notifyDataSetChanged();
                break;
        }
    }

    private void killProgress() {
        LogUtil.i(this, "killProgress");
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ProgressInfo> deleteProgressList = new ArrayList<>();
        for (ProgressInfo progressInfo : mProgressList) {
            if (progressInfo.isChecked) {
                //这里需要权限
                LogUtil.i(this, "isChecked:" + progressInfo.packageName);
                am.killBackgroundProcesses(progressInfo.packageName);
                deleteProgressList.add(progressInfo);
            }
        }
        for (ProgressInfo progressInfo : deleteProgressList) {
            if (progressInfo.isUser) {
                mUserProgressList.remove(progressInfo);
            } else {
                mSystemProgressList.remove(progressInfo);
            }
        }
        //数据变化了,adapter去重新为listview设置数据.
        //而这里面的数据是userTaskInfos和systemTaskInfos
        mAdapter.notifyDataSetChanged();
        deleteProgressList.clear();
        //清理完之后去更新titlebar中的数据
        setTitleText();
    }

    /**
     * 进程管理对应的adapter
     * 作者： songwenju on 2016/5/1 09:43.
     * 邮箱： songwenju@outlook.com
     */
    public class ProgressMgrAdapter extends BaseRecycleViewAdapter<ProgressInfo> implements View.OnClickListener {
        //将条目设置为成员变量,方便在点击事件中使用.
        public static final int ITEM_TYPE_PROGRESS_INFO = 0;
        public static final int ITEM_TYPE_TEXT = 1;

        // 用来控制CheckBox的选中状况
        private Map<Integer, Integer> mHashMap;
        public static final int UNCHECKED = 0;
        public static final int CHECKED = 1;

        public ProgressMgrAdapter(Context context) {
            super(context);
        }

        @Override
        public void setList(List<ProgressInfo> list) {
            super.setList(list);

            mHashMap = new HashMap<>();

            setAllUnchecked();
            mUserProgressList.clear();
            mSystemProgressList.clear();
            for (ProgressInfo progressInfo : list) {
                if (progressInfo.isUser) {
                    mUserProgressList.add(progressInfo);
                } else {
                    mSystemProgressList.add(progressInfo);
                }
            }
        }

        public void setAllUnchecked() {
            for (int i = 0; i < mList.size(); i++) {
                mHashMap.put(i, UNCHECKED);
            }
        }

        public void setAllChecked() {
            for (int i = 0; i < mList.size(); i++) {
                mHashMap.put(i, UNCHECKED);
            }
        }


        //        public void setConvertChecked(){
//            //设置两个集合的属性都为相反
//            for (ProgressInfo progressInfo : mUserProgressList) {
//                if (!progressInfo.packageName.equals(mContext.getPackageName())) {
//                    progressInfo.isChecked = !progressInfo.isChecked;
//                }
//            }
//            for (ProgressInfo progressInfo : mSystemProgressList) {
//                progressInfo.isChecked = !progressInfo.isChecked;
//            }
//            notifyDataSetChanged();
//        }
        public class ProgressInfoHolder extends RecyclerView.ViewHolder {
            public ImageView progressIcon;
            public TextView progressName, progressSize;
            public CheckBox progressIsChecked;

            public ProgressInfoHolder(final View itemView) {
                super(itemView);
                progressIcon = (ImageView) itemView.findViewById(R.id.progress_icon);
                progressName = (TextView) itemView.findViewById(R.id.progress_name);
                progressSize = (TextView) itemView.findViewById(R.id.progress_size);
                progressIsChecked = (CheckBox) itemView.findViewById(R.id.progress_isChecked);
            }

        }

        public class TextViewHolder extends RecyclerView.ViewHolder {
            public TextView mTvProgressListTitle;

            public TextViewHolder(View itemView) {
                super(itemView);
                mTvProgressListTitle = (TextView) itemView.findViewById(R.id.tv_progress_list_title);
                ViewGroup.LayoutParams params = mTvProgressListTitle.getLayoutParams();
                params.width = mWindowWidth;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mUserProgressList.size() + 1) {
                return ITEM_TYPE_TEXT;
            } else {
                return ITEM_TYPE_PROGRESS_INFO;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            if (viewType == ITEM_TYPE_PROGRESS_INFO) {
                return new ProgressInfoHolder(View.inflate(mContext, R.layout.activity_progressmgr_item, null));
            } else {
                return new TextViewHolder(View.inflate(mContext, R.layout.activity_progressmgr_text_item, null));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            if (holder instanceof TextViewHolder) {
                TextViewHolder textViewHolder = ((TextViewHolder) holder);
                if (position == 0) {
                    textViewHolder.mTvProgressListTitle.setText("用户进程(" + mUserProgressList.size() + ")");
                } else {
                    textViewHolder.mTvProgressListTitle.setText("系统进程(" + mSystemProgressList.size() + ")");
                }
            } else if (holder instanceof ProgressInfoHolder) {

                final ProgressInfoHolder progressInfoHolder = (ProgressInfoHolder) holder;
                ProgressInfo progressInfo = null;
                if (position <= mUserProgressList.size()) {
                    progressInfo = mUserProgressList.get(position - 1);
                } else {
                    LogUtil.i(this, "position:" + position + " mUserProgressList.size():" + mUserProgressList.size());
                    if (mSystemProgressList.size() > 0) {
                        progressInfo = mSystemProgressList.get(position - mUserProgressList.size() - 2);
                    }
                }
                if (progressInfo != null) {

                    progressInfoHolder.progressIcon.setImageDrawable(progressInfo.icon);
                    progressInfoHolder.progressName.setText(progressInfo.name);
                    progressInfoHolder.progressSize.setText(progressInfo.ramSize);
//                LogUtil.i(this,"isChecked"+mHashMap.get(position));

                    progressInfoHolder.progressIsChecked.setChecked(progressInfo.isChecked
                            || mHashMap.get(position) == CHECKED);
                    progressInfoHolder.progressIsChecked.setChecked(progressInfo.isChecked);
                    progressInfoHolder.progressIsChecked.setOnClickListener(this);
                    progressInfoHolder.progressIsChecked.setTag(R.id.progress_isChecked, position);

                    //设置自己应用没有checkBox,在listView中有if必有else否则会出现复用错误.
                    if (progressInfo.packageName.equals(mContext.getPackageName())) {
                        progressInfoHolder.progressIsChecked.setVisibility(View.INVISIBLE);
                    } else {
                        progressInfoHolder.progressIsChecked.setVisibility(View.VISIBLE);
                    }
                }
                progressInfoHolder.itemView.setTag(progressInfoHolder);
            }

        }

        /**
         * //         * 设置选择状态
         * //         * @param position
         * //         * @param status
         * //
         */
        public void setChecked(int position, int status) {
            LogUtil.i(this, "setChecked" + ",position:" + position + " status:" + status);
            mHashMap.put(position, status);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.progress_isChecked:
                    int position = (Integer) v.getTag(R.id.progress_isChecked);
                    LogUtil.i(this, "position:" + position);
                    if (mHashMap.get(position) == 1) {
                        mHashMap.put(position, UNCHECKED);
                    } else {
                        mHashMap.put(position, CHECKED);
                    }
//                notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mUserProgressList.size() + mSystemProgressList.size() + 2;
        }
    }

}
