package com.wjustudio.phoneManager.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseFragment;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置页面的第一个fragment
 */
public class SetUpTwoFragment extends BaseFragment {
    @Bind(R.id.et_safe_number)
    EditText mEtSafeNumber;
    @Bind(R.id.ibtn_select_number)
    ImageButton mIbtnSelectNumber;
    @Bind(R.id.btn_complete)
    Button mBtnComplete;
    private String mSafeNum;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setup_two;
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
        if (v.getId() == R.id.btn_complete){
            if (TextUtils.isEmpty(mSafeNum)) {
                ToastUtil.showToast("手机号码不能为空!");
            }else if (!CommonUtil.isMobile(mSafeNum)){
                ToastUtil.showToast("手机号码的格式不正确！");
            }else {
                SpUtil.putString("safeNum", MD5Utils.decode(mSafeNum));
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
