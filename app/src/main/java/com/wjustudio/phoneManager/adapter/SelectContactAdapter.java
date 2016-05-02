package com.wjustudio.phoneManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseRecycleViewAdapter;
import com.wjustudio.phoneManager.javaBean.ContactInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.List;

/**
 * 联系人选择的adapter
 */
public class SelectContactAdapter extends BaseRecycleViewAdapter {

    public SelectContactAdapter(Context context, List<ContactInfo> contactList) {
       super(context,contactList);
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView pingYin, contactName, contactNum;

        public NormalViewHolder(View itemView) {
            super(itemView);
            LogUtil.i(this, "AppInfoHolder");
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
        super.onBindViewHolder(holder,position);
        LogUtil.i(this, "onBindViewHolder");
        NormalViewHolder normalViewHolder = (NormalViewHolder) holder;

        ContactInfo contact = (ContactInfo) mList.get(position);
        normalViewHolder.contactName.setText(contact.contact_name);
        normalViewHolder.contactNum.setText(contact.contact_phoneNum);
        String pinYin = String.valueOf(contact.pinYin.charAt(0));

        if (CommonUtil.isInLatter(pinYin)) {
            normalViewHolder.pingYin.setText(pinYin);
        } else {
            normalViewHolder.pingYin.setText("#");
        }

        if (position > 0) {
            ContactInfo contactNext = (ContactInfo) mList.get(position - 1);
            boolean isLatter = CommonUtil.isInLatter("" + contact.pinYin.charAt(0)) &&
                    CommonUtil.isInLatter("" + contactNext.pinYin.charAt(0));

            if ((contact.pinYin.charAt(0) ==
                   contactNext.pinYin.charAt(0)) || !isLatter) {
                LogUtil.d(this, "position:" + position);
                normalViewHolder.pingYin.setVisibility(View.GONE);
            } else {
                normalViewHolder.pingYin.setVisibility(View.VISIBLE);
            }

        }
        if (position == 0){
            normalViewHolder.pingYin.setVisibility(View.VISIBLE);
        }

    }

}
