package com.wjustudio.phoneManager.activities;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.biz.IPhoneLocationBiz;
import com.wjustudio.phoneManager.biz.PhoneLocationBizImpl;
import com.wjustudio.phoneManager.widgt.CommonTitleLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * songwenju on 16-5-12 : 11 : 13.
 * 邮箱：songwenju@outlook.com
 */
public class PhoneLocationActivity extends BaseActivity {
    @Bind(R.id.ctl_common_title)
    CommonTitleLayout mCommonTitleLayout;
    @Bind(R.id.et_phoneLocate_phoneNum)
    EditText mEtPhoneLocatePhoneNum;
    @Bind(R.id.bt_phoneLocate_location)
    Button mBtPhoneLocateLocation;
    @Bind(R.id.tv_phoneLocate_location)
    TextView mTvPhoneLocateLocation;
    private IPhoneLocationBiz mBiz;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_phone_location;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onInitData() {
        mBiz = new PhoneLocationBizImpl(mContext);
    }

    @Override
    protected void onSetViewData() {
        mCommonTitleLayout.setTitle("电话归属地查询");
        mCommonTitleLayout.setImgSettingVisible(false);
    }

    @Override
    protected void onInitListener() {
        mBtPhoneLocateLocation.setOnClickListener(this);
        mEtPhoneLocatePhoneNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String location = mBiz.query(s.toString());
                mTvPhoneLocateLocation.setText(location);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.bt_phoneLocate_location:
                String phoneNum = mEtPhoneLocatePhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    //抖动效果
                    Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                    mEtPhoneLocatePhoneNum.startAnimation(shake);
                    toast("不能为空");
                } else {
                    String location = mBiz.query(phoneNum);
                    mTvPhoneLocateLocation.setText(location);

                }
                break;
        }
    }

}
