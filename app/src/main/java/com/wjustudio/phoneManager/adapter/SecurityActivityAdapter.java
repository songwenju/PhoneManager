package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.javaBean.TheftProofInfo;

import java.util.List;

/**
 * 手机防盗对应的Adapter
 * 作者： songwenju on 2016/4/10 10:39.
 * 邮箱： songwenju@outlook.com
 */
public class SecurityActivityAdapter extends BaseRecycleViewAdapter{

    public SecurityActivityAdapter(Context context, List list) {
        super(context, list);
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView title,content;

        public NormalViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.img_security_item);
            title = (TextView) itemView.findViewById(R.id.tv_security_item_title);
            content = (TextView) itemView.findViewById(R.id.tv_security_item);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) content.getLayoutParams();
            params.width = mWindowWidth - icon.getWidth() - 53;
            content.setLayoutParams(params);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.activity_proof_item,null);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        TheftProofInfo theftProofInfo = (TheftProofInfo) mList.get(position);
        normalViewHolder.icon.setImageResource(theftProofInfo.theftProofIcon);
        normalViewHolder.title.setText(theftProofInfo.theftProofTitle);
        normalViewHolder.content.setText(theftProofInfo.theftProofInfo);
    }
}
