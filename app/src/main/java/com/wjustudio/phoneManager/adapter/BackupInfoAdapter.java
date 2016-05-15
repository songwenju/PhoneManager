package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.javaBean.UserContactInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.List;

/**
 * 通信卫士对应的adapter
 * 作者： songwenju on 2016/4/24 14:03.
 * 邮箱： songwenju@outlook.com
 */
public class BackupInfoAdapter extends BaseRecycleViewAdapter<UserContactInfo.UserContactBean> {

    private onReloadClickListener mOnReloadClickListener;

    public void setOnReloadClickListener(onReloadClickListener onReloadClickListener) {
        mOnReloadClickListener = onReloadClickListener;
    }

    public interface onReloadClickListener{
        void onReloadClick(int position);
    }
    public BackupInfoAdapter(Context context, List<UserContactInfo.UserContactBean> list) {
        super(context, list);
        LogUtil.i(this, "BackupInfoAdapter");
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView tvPosition, tvBackupTime;
        Button btnReload;

        public NormalViewHolder(View itemView) {
            super(itemView);
            tvPosition = (TextView) itemView.findViewById(R.id.tv_position);
            tvBackupTime = (TextView) itemView.findViewById(R.id.tv_backup_time);
            btnReload = (Button) itemView.findViewById(R.id.btn_reload);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(View.inflate(mContext,
                R.layout.activity_backup_info_item, null));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        normalViewHolder.tvPosition.setText("备份" + (position + 1)+":");
        UserContactInfo.UserContactBean userContactInfo = mList.get(position);
        String time = getTimeString(userContactInfo);
        normalViewHolder.tvBackupTime.setText("时间："+time);
        normalViewHolder.btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnReloadClickListener.onReloadClick(position);
            }
        });
    }

    private String getTimeString(UserContactInfo.UserContactBean userContactInfo) {
        String url = userContactInfo.getJsonUrl();
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String timeStr = fileName.substring(fileName.lastIndexOf("_")+1,fileName.lastIndexOf("."));
        return timeStr.substring(0, 4) + "/" + timeStr.substring(4, 6) + "/" +
                timeStr.substring(6, 8) + " " + timeStr.substring(8, 10) + ":" +
                timeStr.substring(10, 12) + ":" + timeStr.substring(12, 14);
    }
}
