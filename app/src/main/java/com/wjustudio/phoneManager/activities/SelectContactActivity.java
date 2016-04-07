package com.wjustudio.phoneManager.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.LeftMenuAdapter;
import com.wjustudio.phoneManager.adapter.SelectContactAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.Contact;
import com.wjustudio.phoneManager.utils.ContactUtil;
import com.wjustudio.phoneManager.utils.ContactUtils;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;
import com.wjustudio.phoneManager.widgt.QuickIndexBar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：songwenju on 2016/4/2 18:16
 * 邮箱：songwenju01@163.com
 */
public class SelectContactActivity extends BaseActivity {
    private Context mContext;
    private List<Contact> mContactList;
    private String contactNum;
    @Bind(R.id.select_contact_item)
    RecyclerView mSelectContactItem;
    @Bind(R.id.indexBar)
    QuickIndexBar mIndexBar;
    @Bind(R.id.tv_index)
    TextView mTvIndex;

    @Override
    protected int getLayoutID() {
        return R.layout.select_contact_layout;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mContext = this;
        mContactList = ContactUtils.getContact(mContext);
        for (Contact contact:mContactList) {
            LogUtil.i(this,contact.toString());
        }
    }

    @Override
    protected void onSetViewData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView设置布局管理器
        mSelectContactItem.setLayoutManager(layoutManager);
        //添加分割线
        mSelectContactItem.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
        ));
        SelectContactAdapter adapter = new SelectContactAdapter(mContext,mContactList);
        mSelectContactItem.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                contactNum = mContactList.get(position).contact_phoneNum;
                LogUtil.d(this,"contactNum:"+contactNum);
                if (!TextUtils.isEmpty(contactNum)){
                    Intent intent = getIntent();
                    intent.putExtra(AppConstants.CONTACT_NUM, contactNum);
                    setResult(1, intent);
                }
                finish();
            }
        });

    }

    @Override
    protected void onInitListener() {

    }

    @Override
    protected void processClick(View v) {

    }

}
