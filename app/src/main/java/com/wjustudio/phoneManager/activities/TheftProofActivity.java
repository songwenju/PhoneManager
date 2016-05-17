package com.wjustudio.phoneManager.activities;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.SecurityActivityAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.TheftProofInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
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
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tv_proof_remind)
    TextView mTvProof_remind;

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
    protected void onResume() {
        super.onResume();
        boolean isOpenProof = CommonUtil.isRunningService(mContext, "com.wjustudio.phoneManager.service.TheftProofService");
        mTvProof_remind.setText(isOpenProof ? "手机防盗功能已经激活" : "手机防盗关闭，请去设置页面打开");
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("手机防盗");
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
        mCommonTitleLayout.setOnSettingImgClickListener(new CommonTitleLayout.OnSettingImgClickListener() {
            @Override
            public void onSettingImgClick() {
                Intent intent = new Intent(mContext, TheftProofSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processClick(View v) {
    }


}
