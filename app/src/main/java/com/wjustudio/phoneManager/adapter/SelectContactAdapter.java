package com.wjustudio.phoneManager.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.javaBean.Contact;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 联系人选择的adapter
 */
public class SelectContactAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Contact> mContactList;
    private OnItemClickListener mOnItemClickListener;
    private int mWindowHeight;
    private int mWindowWidth;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public SelectContactAdapter(Context context, List<Contact> contactList) {
        LogUtil.i(this, "SelectContactAdapter");
        mContext = context;
        mContactList = contactList;
        HashMap<String, Integer> windowSize = CommonUtil.getWindowSize((Activity) mContext);
        mWindowHeight = windowSize.get(AppConstants.WINDOW_HEIGHT);
        mWindowWidth = windowSize.get(AppConstants.WINDOW_WIDTH);
        LogUtil.i(this, "mContactList.size:" + mContactList.size());
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView pingYin, contactName, contactNum;

        public NormalViewHolder(View itemView) {
            super(itemView);
            LogUtil.i(this, "NormalViewHolder");
            pingYin = (TextView) itemView.findViewById(R.id.tv_pinyin);
            contactName = (TextView) itemView.findViewById(R.id.contact_name);
            contactNum = (TextView) itemView.findViewById(R.id.contact_num);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contactNum.getLayoutParams();
            params.width = mWindowWidth - contactName.getWidth();
            contactNum.setLayoutParams(params);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.i(this, "onCreateViewHolder");
        View view = View.inflate(mContext, R.layout.select_contact_item, null);
        return new NormalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        LogUtil.i(this, "onBindViewHolder");
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
        LogUtil.d(this, "name:" + mContactList.get(position).name);
        Contact contact = mContactList.get(position);
        normalViewHolder.contactName.setText(contact.name);
        normalViewHolder.contactNum.setText(contact.telephoneNumber.get(0));
        String pinYin = String.valueOf(contact.pinYin.charAt(0));
        if (CommonUtil.isInLatter(pinYin)) {
            normalViewHolder.pingYin.setText(pinYin);
        }else {
            normalViewHolder.pingYin.setText("#");
        }

        boolean isNotLatter =  CommonUtil.isInLatter(""+contact.pinYin.charAt(0)) &&
                CommonUtil.isInLatter(""+ mContactList.get(position - 1).pinYin.charAt(0)) ;
        
        if (position > 0 && (contact.pinYin.charAt(0) ==
                mContactList.get(position - 1).pinYin.charAt(0) || isNotLatter)) {
            normalViewHolder.pingYin.setVisibility(View.GONE);
        } else {
            normalViewHolder.pingYin.setVisibility(View.VISIBLE);
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d(this, "onClick");
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mContactList == null ? 0 : mContactList.size();
    }
}
