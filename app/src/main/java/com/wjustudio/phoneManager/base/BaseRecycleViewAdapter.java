package com.wjustudio.phoneManager.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.utils.CommonUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 作者： songwenju on 2016/4/10 10:44.
 * 邮箱： songwenju@outlook.com
 */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    //屏幕的宽高，主要用于代码适配
    protected int mWindowHeight;
    protected int mWindowWidth;
    protected OnItemClickListener mOnItemClickListener;
    protected List<T> mList;

    public BaseRecycleViewAdapter(Context context, List<T> list){
        mContext = context;
        mList = list;
        HashMap<String, Integer> windowSize = CommonUtil.getWindowSize((Activity) mContext);
        mWindowHeight = windowSize.get(AppConstants.WINDOW_HEIGHT);
        mWindowWidth = windowSize.get(AppConstants.WINDOW_WIDTH);
        this.notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


}
