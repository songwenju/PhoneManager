package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.BlackNumAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.biz.BlackNumBizImpl;
import com.wjustudio.phoneManager.javaBean.BlackNumInfo;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 通讯卫士
 */
public class BlackNumActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.tv_black_num_remind)
    TextView mTvBlackNumRemind;
    @Bind(R.id.lv_black_num)
    RecyclerView mRecyclerView;
    @Bind(R.id.id_fab)
    FloatingActionButton mIdFab;

    private AlertDialog mSetBlackNumDialog;
    private List<BlackNumInfo> mBlackNumInfos;
    private BlackNumAdapter mBlackNumAdapter;
    private BlackNumBizImpl mBlackNumBiz;
    private int mBlackNumMode;
    private EditText mPhoneNum;
    private CheckBox mCbPhone;
    private CheckBox mCbSMS;
    private BlackNumInfo mBlackNumInfo;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_black_num;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/yizhi.ttf");
        mTvBlackNumRemind.setTypeface(typeface);
    }

    /**
     * 初始化设置黑名单的dialog
     */
    public void showSetBlackNumDialog(final String blackNum, String title, final int mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        mSetBlackNumDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_add_black_num, null);
        mSetBlackNumDialog.setView(dialogView, 0, 0, 0, 0);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mPhoneNum = (EditText) dialogView.findViewById(R.id.et_phone_num);
        mCbPhone = (CheckBox) dialogView.findViewById(R.id.cb_phone);
        mCbSMS = (CheckBox) dialogView.findViewById(R.id.cb_sms);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);

        //设置对话框的属性
        dialogTitle.setText(title);
        if (!TextUtils.isEmpty(blackNum)) {
            mPhoneNum.setText(blackNum);
        }
        updateMode(mode, mCbPhone, mCbSMS);
        mBlackNumInfo = new BlackNumInfo();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumPtr = mPhoneNum.getText().toString().trim();

                if (TextUtils.isEmpty(phoneNumPtr)) {
                    toast("手机号不能为空！");
                } else if (!CommonUtil.isMobile(phoneNumPtr)) {
                    toast("手机号格式错误！");
                } else if (mode == 0 && mBlackNumBiz.isExist(phoneNumPtr)) {
                    toast("该手机号已经在黑名单.");
                } else {

                    mBlackNumInfo.setBlackNum(phoneNumPtr);
                    if (mCbPhone.isChecked() && mCbSMS.isChecked()) {
                        mBlackNumMode = BlackNumBizImpl.BLACK_NUM_ALL;
                    } else {
                        if (mCbPhone.isChecked()) {
                            mBlackNumMode = BlackNumBizImpl.BLACK_NUM_PHONE;
                        } else {
                            if (mCbSMS.isChecked()) {
                                mBlackNumMode = BlackNumBizImpl.BLACK_NUM_SMS;
                            } else {
                                toast("请选择拦截类型！");
                                return;
                            }
                        }
                    }
                    mBlackNumInfo.setMode(mBlackNumMode);
                    if (mode == 0) {
                        LogUtil.i(this, "insert");
                        mBlackNumBiz.insertBlackNum(mBlackNumInfo);
                        mBlackNumInfos.add(mBlackNumInfo);
                        //mBlackNumInfos = mBlackNumBiz.getAllBlackNum();这样不行，因为list的地址值变了。
                        //LogUtil.i(this, "blackNum size:"+mBlackNumInfos.size());
                        if (mTvBlackNumRemind.getVisibility() == View.VISIBLE) {
                            mTvBlackNumRemind.setVisibility(View.GONE);
                        }
                    } else {
                        LogUtil.i(this, "update");
                        mBlackNumBiz.updateBlackNum(mBlackNumInfo);
                        for (int i = 0; i < mBlackNumInfos.size(); i++) {
                            if (mBlackNumInfos.get(i).getBlackNum().equals(blackNum)) {
                                LogUtil.i(this, "blackNumInfo blackNum:" + blackNum);

                                mBlackNumInfos.get(i).setMode(mBlackNumMode);
                                updateMode(mBlackNumMode, mCbPhone, mCbSMS);
                                break;
                            }
                        }
                    }
                    if (mBlackNumAdapter != null) {
                        mBlackNumAdapter.notifyDataSetChanged();
                    }
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
        mSetBlackNumDialog.show();
    }

    private void updateMode(int mode, CheckBox cbPhone, CheckBox cbSMS) {
        switch (mode) {
            case BlackNumBizImpl.BLACK_NUM_PHONE:
                cbPhone.setChecked(true);
                cbSMS.setChecked(false);
                break;
            case BlackNumBizImpl.BLACK_NUM_SMS:
                cbSMS.setChecked(true);
                cbPhone.setChecked(false);
                break;
            case BlackNumBizImpl.BLACK_NUM_ALL:
                cbPhone.setChecked(true);
                cbSMS.setChecked(true);
                break;
        }
    }

    private void dialogDismiss() {
        mSetBlackNumDialog.dismiss();
    }


    @Override
    protected void onInitData() {
        mBlackNumBiz = new BlackNumBizImpl();
        mBlackNumInfos = mBlackNumBiz.getAllBlackNum();
        LogUtil.i(this, "blackNumInfos size:" + mBlackNumInfos.size());
        mBlackNumAdapter = new BlackNumAdapter(mContext, mBlackNumInfos, this, mBlackNumBiz, mTvBlackNumRemind);
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("通讯卫士");
        if (mBlackNumInfos.size() == 0) {
            mTvBlackNumRemind.setVisibility(View.VISIBLE);
        } else {
            mTvBlackNumRemind.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView设置布局管理器
        mRecyclerView.setLayoutManager(layoutManager);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
        ));
        mRecyclerView.setAdapter(mBlackNumAdapter);


    }

    @Override
    protected void onInitListener() {
        mIdFab.setOnClickListener(this);
        mCommonTitleLayout.setOnSettingImgClickListener(new CommonTitleLayout.OnSettingImgClickListener() {
            @Override
            public void onSettingImgClick() {
                Intent intent = new Intent(mContext, BlackNumSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.id_fab:
                showSetBlackNumDialog("", "添加黑名单", 0);
                break;
        }

    }

}
