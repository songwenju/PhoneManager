package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.biz.AppLockBizImpl;
import com.wjustudio.phoneManager.biz.IAppLockBiz;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件管家对应的adapter
 * 作者： songwenju on 2016/5/1 09:43.
 * 邮箱： songwenju@outlook.com
 */
public class ProgressMgrAdapter extends BaseRecycleViewAdapter<ProgressInfo> {

    private List<ProgressInfo> userProgressList = new ArrayList<>();
    private List<ProgressInfo> systemProgressList = new ArrayList<>();
    //将条目设置为成员变量,方便在点击事件中使用.
    public static final int ITEM_TYPE_PROGRESS_INFO = 0;
    public static final int ITEM_TYPE_TEXT = 1;

    public ProgressMgrAdapter(Context context) {
        super(context);
    }


    public class ProgressInfoHolder extends RecyclerView.ViewHolder {
        public ImageView appIcon, appLock;
        public TextView appName, appWhere, appSize;

        public ProgressInfoHolder(final View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appWhere = (TextView) itemView.findViewById(R.id.app_where);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            appLock = (ImageView) itemView.findViewById(R.id.app_lock);
        }

    }


    public class TextViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvAppListTitle;

        public TextViewHolder(View itemView) {
            super(itemView);
            mTvAppListTitle = (TextView) itemView.findViewById(R.id.tv_app_list_title);
            ViewGroup.LayoutParams params = mTvAppListTitle.getLayoutParams();
            params.width = mWindowWidth;
        }
    }

    @Override
    public void setList(List<ProgressInfo> list) {
        super.setList(list);
        userProgressList.clear();
        systemProgressList.clear();
        for (ProgressInfo progressInfo : list) {
            if (progressInfo.isUser) {
                userProgressList.add(progressInfo);
            } else {
                systemProgressList.add(progressInfo);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == userProgressList.size() + 1) {
            return ITEM_TYPE_TEXT;
        } else {
            return ITEM_TYPE_PROGRESS_INFO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_PROGRESS_INFO) {
            return new ProgressInfoHolder(View.inflate(mContext, R.layout.activity_appmgr_item, null));
        } else {
            return new TextViewHolder(View.inflate(mContext, R.layout.activity_appmgr_text_item, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//        ProgressInfo ProgressInfo = mList.get(position);
        if (holder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = ((TextViewHolder) holder);
            if (position == 0) {
                textViewHolder.mTvAppListTitle.setText("用户程序(" + userProgressList.size() + ")");
            } else {
                textViewHolder.mTvAppListTitle.setText("系统程序(" + systemProgressList.size() + ")");
            }
        } else if (holder instanceof ProgressInfoHolder) {
            final ProgressInfoHolder ProgressInfoHolder = (ProgressInfoHolder) holder;
            ProgressInfo ProgressInfo;
            if (position <= userProgressList.size()) {
                ProgressInfo = userProgressList.get(position - 1);
            } else {
                ProgressInfo = systemProgressList.get(position - userProgressList.size() - 2);
            }
            ProgressInfoHolder.appIcon.setImageDrawable(ProgressInfo.icon);
            ProgressInfoHolder.appName.setText(ProgressInfo.name);
            ProgressInfoHolder.appWhere.setText(ProgressInfo.isUser ? "外部存储" : "手机内存");
            ProgressInfoHolder.appSize.setText(""+ProgressInfo.ramSize);
            IAppLockBiz biz = new AppLockBizImpl(mContext);
            ProgressInfoHolder.appLock.setImageResource(
                    biz.isLock(ProgressInfo.packageName) ? R.mipmap.lock : R.mipmap.unlock);
            ProgressInfoHolder.itemView.setTag(ProgressInfoHolder);
            ProgressInfoHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(ProgressInfoHolder.itemView, ProgressInfoHolder.getLayoutPosition());
                    return true;
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }
}
