package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.utils.CommonUtil;

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

    private AlertDialog mSetBlackNumDialog;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comm_guard;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        initSetBlackNumDialog();
    }

    /**
     * 初始化设置黑名单的dialog
     */
    private void initSetBlackNumDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        mSetBlackNumDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_add_black_num, null);
        mSetBlackNumDialog.setView(dialogView, 0, 0, 0, 0);
        final EditText phoneNum = (EditText) dialogView.findViewById(R.id.et_phone_num);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumPtr = phoneNum.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumPtr)) {
                    toast("手机号不能为空！");
                } else if (!CommonUtil.isPhone(phoneNumPtr)) {
                    toast("手机号格式错误！");
                } else {
                    //TODO
                    dialogDismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });
    }

    private void dialogDismiss() {
        mSetBlackNumDialog.dismiss();
        if (mIdFab != null) {
            mIdFab.setVisibility(View.VISIBLE);
        }
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
        if (v.getId() == R.id.id_fab) {
            mIdFab.setVisibility(View.GONE);
            mSetBlackNumDialog.show();
        }
    }

}
