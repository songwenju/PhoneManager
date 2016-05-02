package com.example.rxjavademo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 软件管家对应的adapter
 * 作者： songwenju on 2016/5/1 09:43.
 * 邮箱： songwenju@outlook.com
 */
public class AppMgrAdapter extends BaseRecycleViewAdapter<AppInfo> {
    public AppMgrAdapter(Context context) {
        super(context);
    }


    public class NormalViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon, appLock;
        TextView appName, appWhere, appSize;

        public NormalViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appWhere = (TextView) itemView.findViewById(R.id.app_where);
            appSize = (TextView) itemView.findViewById(R.id.app_size);
            appLock = (ImageView) itemView.findViewById(R.id.app_lock);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(View.inflate(mContext, R.layout.activity_appmgr_item, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        AppInfo appInfo = mList.get(position);
        normalViewHolder.appIcon.setImageDrawable(appInfo.icon);
        normalViewHolder.appName.setText(appInfo.name);
        normalViewHolder.appWhere.setText(appInfo.isUser ? "SD卡":"手机内存");
        normalViewHolder.appSize.setText(appInfo.apkSize);
        normalViewHolder.appLock.setImageResource(R.mipmap.lock);
    }
}
