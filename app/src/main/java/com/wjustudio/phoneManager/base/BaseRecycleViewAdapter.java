package com.wjustudio.phoneManager.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.utils.CommonUtil;

import java.util.ArrayList;
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
    protected List<T> mList = new ArrayList<>();

    public BaseRecycleViewAdapter(Context context) {
        mContext = context;
        getWindowSize();
    }

    /**
     * RxJava使用时调用
     */
    public void setList(List<T> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    private void getWindowSize() {
        HashMap<String, Integer> windowSize = CommonUtil.getWindowSize((Activity) mContext);
        mWindowHeight = windowSize.get(AppConstants.WINDOW_HEIGHT);
        mWindowWidth = windowSize.get(AppConstants.WINDOW_WIDTH);
    }

    public BaseRecycleViewAdapter(Context context, List<T> list) {
        mContext = context;
//        mList.clear();
//        mList.addAll(list);
        mList = list;
        getWindowSize();
        this.notifyDataSetChanged();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView,holder.getLayoutPosition());
                    //返回false会同时触发点击和长按事件
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


}
