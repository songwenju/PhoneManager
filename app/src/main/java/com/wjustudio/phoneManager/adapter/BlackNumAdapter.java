package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.activities.BlackNumActivity;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.biz.BlackNumBizImpl;
import com.wjustudio.phoneManager.javaBean.BlackNumInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.List;

/**
 * 通信卫士对应的adapter
 * 作者： songwenju on 2016/4/24 14:03.
 * 邮箱： songwenju@outlook.com
 */
public class BlackNumAdapter extends BaseRecycleViewAdapter{
    private BlackNumActivity mBlackNumActivity;
    private BlackNumBizImpl mBlackNumBiz;
    private TextView tvEmpty;
    public BlackNumAdapter(Context context, List list, BlackNumActivity activity,
                           BlackNumBizImpl blackNumBiz, TextView mTvCallSmsSafeIsEmpty) {

        super(context, list);
        LogUtil.i(this,"BlackNumAdapter");
        mBlackNumActivity = activity;
        mBlackNumBiz = blackNumBiz;
        tvEmpty = mTvCallSmsSafeIsEmpty;
    }



    public class NormalViewHolder extends RecyclerView.ViewHolder{
        ImageView delete, modify;
        TextView blackNum,mode;
        public NormalViewHolder(View itemView) {
            super(itemView);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            modify = (ImageView) itemView.findViewById(R.id.modify);
            blackNum = (TextView) itemView.findViewById(R.id.tv_black_num);
            mode = (TextView) itemView.findViewById(R.id.tv_mode);

        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(View.inflate(mContext,
                R.layout.activity_black_num_item,null));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        final BlackNumInfo blackNumInfo = (BlackNumInfo) mList.get(position);
        normalViewHolder.blackNum.setText(blackNumInfo.getBlackNum());
        String modeStr = "";
        switch (blackNumInfo.getMode()){
            case BlackNumBizImpl.BLACK_NUM_PHONE:
                modeStr = "电话拦截";
                break;
            case BlackNumBizImpl.BLACK_NUM_SMS:
                modeStr = "短信拦截";
                break;
            case BlackNumBizImpl.BLACK_NUM_ALL:
                modeStr = "全部拦截";
                break;
        }

        normalViewHolder.mode.setText(modeStr);

        normalViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(this, "delete");
                mList.remove(blackNumInfo);
                mBlackNumBiz.deleteBlackNum(blackNumInfo);
                notifyDataSetChanged();
                if (mList.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }
        });

        normalViewHolder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(this, "modify blackNum");
                mBlackNumActivity.showSetBlackNumDialog(blackNumInfo.getBlackNum(),
                        "修改黑名单", blackNumInfo.getMode());
            }
        });
    }
}
