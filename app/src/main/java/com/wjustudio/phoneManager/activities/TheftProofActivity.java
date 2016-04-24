package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.SecurityActivityAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.TheftProofInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 手机防盗的activity
 */
public class TheftProofActivity extends BaseActivity {
    @Bind(R.id.rv_security)
    RecyclerView mRvSecurity;
    @Bind(R.id.img_setting)
    ImageView mTheftProofSetting;

    private ArrayList<TheftProofInfo> mList;
    private int[] mIconArray = {
            R.mipmap.phone_lost_sim,
            R.mipmap.phone_lost_location,
            R.mipmap.phone_lost_locket,
            R.mipmap.phone_lost_delete,
            R.mipmap.phone_lost_warm
            // R.mipmap.phone_lost_find,
    };

    @Override
    protected int getLayoutID() {
        return R.layout.activity_proof;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        //判断应用是否是设置了相关数据
        boolean securitySetting = TextUtils.isEmpty(SpUtil.getString("safeNum", null));
        mList = new ArrayList<>();
        if (securitySetting) {
            //进入设置界面
            Intent intent = new Intent(this, TheftProofSelectOneActivity.class);
            startActivity(intent);
            finish();
        }
        String[] proofTitle = CommonUtil.getStringArray(R.array.proof_title);
        String[] proofInfo = CommonUtil.getStringArray(R.array.proof_info);
        for (int i = 0; i < mIconArray.length; i++) {
            mList.add(new TheftProofInfo(mIconArray[i], proofTitle[i], proofInfo[i]));
        }

    }

    @Override
    protected void onSetViewData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView设置布局管理器
        mRvSecurity.setLayoutManager(layoutManager);
        //添加分割线
        mRvSecurity.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
        ));

        mRvSecurity.setAdapter(new SecurityActivityAdapter(this, mList));
    }

    @Override
    protected void onInitListener() {
        mTheftProofSetting.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        if (v.getId() == R.id.img_setting){
            Intent intent = new Intent(this,TheftProofSettingActivity.class);
            startActivity(intent);
        }
    }


}
