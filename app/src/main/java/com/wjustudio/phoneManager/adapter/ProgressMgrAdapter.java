package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进程管理对应的adapter
 * 作者： songwenju on 2016/5/1 09:43.
 * 邮箱： songwenju@outlook.com
 */
public class ProgressMgrAdapter extends BaseRecycleViewAdapter<ProgressInfo> implements View.OnClickListener {

    private List<ProgressInfo> userProgressList = new ArrayList<>();
    private List<ProgressInfo> systemProgressList = new ArrayList<>();
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

        for (int i = 0; i < mList.size(); i++) {
            mHashMap.put(i, UNCHECKED);
        }
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
        if (position == 0 || position == userProgressList.size() + 1) {
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
//        ProgressInfo ProgressInfo = mList.get(position);
        if (holder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = ((TextViewHolder) holder);
            if (position == 0) {
                textViewHolder.mTvProgressListTitle.setText("用户进程(" + userProgressList.size() + ")");
            } else {
                textViewHolder.mTvProgressListTitle.setText("系统进程(" + systemProgressList.size() + ")");
            }
        } else if (holder instanceof ProgressInfoHolder) {

            final ProgressInfoHolder progressInfoHolder = (ProgressInfoHolder) holder;
            ProgressInfo ProgressInfo;
            if (position <= userProgressList.size()) {
                ProgressInfo = userProgressList.get(position - 1);
            } else {
                ProgressInfo = systemProgressList.get(position - userProgressList.size() - 2);
            }
            progressInfoHolder.progressIcon.setImageDrawable(ProgressInfo.icon);
            progressInfoHolder.progressName.setText(ProgressInfo.name);
            progressInfoHolder.progressSize.setText(""+ProgressInfo.ramSize);
            LogUtil.i(this,"isChecked"+mHashMap.get(position));
            progressInfoHolder.progressIsChecked.setChecked(mHashMap.get(position) == CHECKED);
            progressInfoHolder.progressIsChecked.setOnClickListener(this);
            progressInfoHolder.progressIsChecked.setTag(R.id.progress_isChecked,position);
            progressInfoHolder.itemView.setTag(progressInfoHolder);
        }

    }

    /**
     * 设置选择状态
     * @param position
     * @param status
     */
    public void setChecked(int position,int status){
        LogUtil.i(this,"setChecked"+",position:"+position+" status:"+status);
        mHashMap.put(position,status);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.progress_isChecked:
                int position = (Integer) v.getTag(R.id.progress_isChecked);
                LogUtil.i(this,"position:"+position);
                if (mHashMap.get(position) == 1) {
                    mHashMap.put(position, UNCHECKED);
                }else{
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
        return super.getItemCount() + 2;
    }
}
