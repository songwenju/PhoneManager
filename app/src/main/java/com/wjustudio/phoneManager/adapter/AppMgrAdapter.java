package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.javaBean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件管家对应的adapter
 * 作者： songwenju on 2016/5/1 09:43.
 * 邮箱： songwenju@outlook.com
 */
public class AppMgrAdapter extends BaseRecycleViewAdapter<AppInfo> {
    private List<AppInfo> userAppList = new ArrayList<>();
    private List<AppInfo> systemAppList = new ArrayList<>();
    public static final int ITEM_TYPE_APP_INFO = 0;

    public static final int ITEM_TYPE_TEXT = 1;

    public AppMgrAdapter(Context context) {
        super(context);
    }


    public static class AppInfoHolder extends RecyclerView.ViewHolder {
        ImageView appIcon, appLock;
        TextView appName, appWhere, appSize;

        public AppInfoHolder(View itemView) {
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
    public void setList(List<AppInfo> list) {
        super.setList(list);
        userAppList.clear();
        systemAppList.clear();
        for (AppInfo appInfo : list) {
            if (appInfo.isUser) {
                userAppList.add(appInfo);
            } else {
                systemAppList.add(appInfo);
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == userAppList.size() + 1) {
            return ITEM_TYPE_TEXT;
        } else {
            return ITEM_TYPE_APP_INFO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_APP_INFO) {
            return new AppInfoHolder(View.inflate(mContext, R.layout.activity_appmgr_item, null));
        } else {
            return new TextViewHolder(View.inflate(mContext, R.layout.activity_appmgr_text_item, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//        AppInfo appInfo = mList.get(position);
        if (holder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = ((TextViewHolder) holder);
            if (position == 0) {
                textViewHolder.mTvAppListTitle.setText("用户程序(" + userAppList.size() + ")");
            } else {
                textViewHolder.mTvAppListTitle.setText("系统程序(" + systemAppList.size() + ")");
            }
        } else if (holder instanceof AppInfoHolder) {
            AppInfoHolder normalViewHolder = (AppInfoHolder) holder;
            AppInfo appInfo;
            if (position <= userAppList.size()) {
                appInfo = userAppList.get(position - 1);
            } else {
                appInfo = systemAppList.get(position - userAppList.size() - 2);
            }
            normalViewHolder.appIcon.setImageDrawable(appInfo.icon);
            normalViewHolder.appName.setText(appInfo.name);
            normalViewHolder.appWhere.setText(appInfo.isUser ? "外部存储" : "手机内存");
            normalViewHolder.appSize.setText(appInfo.apkSize);
            normalViewHolder.appLock.setImageResource(R.mipmap.unlock);
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }
}
