package com.wjustudio.phoneManager.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.utils.LogUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置页面的第一个fragment
 */
public class SetUpTwoFragment extends BaseFragment {
    @Bind(R.id.et_safe_number)
    EditText mEtSafeNumber;
    @Bind(R.id.btn_select_number)
    ImageButton mIbtnSelectNumber;
    @Bind(R.id.btn_complete)
    Button mBtnComplete;
    private String mSafeNum;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_proof_setup_two;
    }

    @Override
    protected void onInitView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onInitData() {

    }

    @Override
    protected void onSetViewData() {
    }

    @Override
    protected void onIntListener() {
        mIbtnSelectNumber.setOnClickListener(this);
        mBtnComplete.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        mSafeNum = mEtSafeNumber.getText().toString().trim();
        LogUtil.e(this,"====="+mSafeNum);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
