package com.wjustudio.phoneManager.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通讯卫士
 */
public class CommGuardActivity extends BaseActivity {
    @Bind(R.id.theft_proof_setting)
    ImageView mTheftProofSetting;
    @Bind(R.id.tv_callSmsSafe_isEmpty)
    TextView mTvCallSmsSafeIsEmpty;
    @Bind(R.id.lv_callsms_safe)
    RecyclerView mLvCallsmsSafe;
    @Bind(R.id.ll_callSmsSafe_progress)
    LinearLayout mLlCallSmsSafeProgress;
    @Bind(R.id.id_fab)
    FloatingActionButton mIdFab;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comm_guard;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {

    }

    @Override
    protected void onInitListener() {
        mIdFab.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        if (v.getId() == R.id.id_fab){
            toast("点击了浮动按钮");
            mIdFab.setVisibility(View.GONE);
        }
    }

}
