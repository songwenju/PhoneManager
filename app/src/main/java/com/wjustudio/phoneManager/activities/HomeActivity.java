package com.wjustudio.phoneManager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.adapter.HomeMainAdapter;
import com.wjustudio.phoneManager.adapter.LeftMenuAdapter;
import com.wjustudio.phoneManager.base.BaseActivity;
import com.wjustudio.phoneManager.javaBean.IconInfo;
import com.wjustudio.phoneManager.lib.dicview.DiscView;
import com.wjustudio.phoneManager.service.BlackNumService;
import com.wjustudio.phoneManager.service.TheftProofService;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.MD5Utils;
import com.wjustudio.phoneManager.utils.SpUtil;
import com.wjustudio.phoneManager.widgt.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：songwenju on 2016/1/31 20:26
 * 邮箱：songwenju01@163.com
 */
public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_appList)
    ListView mLvAppList;
    @Bind(R.id.disc_view)
    DiscView mDiscView;
    @Bind(R.id.tb_custom)
    Toolbar mToolbar;
    @Bind(R.id.dl_left)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.iv_avatar)
    SimpleDraweeView mIvAvatar;
    @Bind(R.id.user_name)
    TextView mUserName;
    @Bind(R.id.tv_left_setting)
    TextView mTvLeftSetting;
    @Bind(R.id.rv_left_menu_content)
    RecyclerView mRvLeftMenuContent;
    @Bind(R.id.rl_left_head)
    RelativeLayout mRlLeftHead;
    @Bind(R.id.btn_speed)
    Button mBtnSpeed;

    private List<IconInfo> mMainPageIcons;
    private List<IconInfo> mLeftPageIcons;

    private int[] mIconArray = {R.mipmap.icon_safe, R.mipmap.icon_contacts,
            R.mipmap.icon_progress, R.mipmap.icon_app, R.mipmap.icon_cache};

    private int[] mLeftIconArray = {R.mipmap.icon_safe, R.mipmap.icon_safe, R.mipmap.icon_safe};
    private ActionBar mActionBar;
    private AlertDialog mPwdSetDialog, mPwdInputDialog;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void onInitView() {
        ButterKnife.bind(this);
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        initPwdSetDialog(builder);
        initPwdInputDialog(builder);

    }

    /**
     * 初始化输入密码的dialog
     *
     * @param builder
     */
    private void initPwdInputDialog(AlertDialog.Builder builder) {
        mPwdInputDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_input_password, null);
        mPwdInputDialog.setView(dialogView, 0, 0, 0, 0);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdRaw = pwd.getText().toString().trim();
                String pwdStr = MD5Utils.decode(pwdRaw);
                String spPwd = SpUtil.getString(AppConstants.ENTER_PROOF_PWD, "");
                if (TextUtils.isEmpty(pwdRaw)) {
                    toast("密码不能为空！");
                } else if (!pwdStr.equals(spPwd)) {
                    toast("密码输入错误！");
                } else {
                    enterSecurityActivity();
                    mPwdInputDialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdInputDialog.dismiss();
            }
        });
    }

    /**
     * 初始化设置密码的dialog
     *
     * @param builder
     */
    private void initPwdSetDialog(AlertDialog.Builder builder) {
        mPwdSetDialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_set_password, null);
        mPwdSetDialog.setView(dialogView, 0, 0, 0, 0);
        final EditText pwd = (EditText) dialogView.findViewById(R.id.et_pwd);
        final EditText rePwd = (EditText) dialogView.findViewById(R.id.et_rePwd);
        Button cancelBtn = (Button) dialogView.findViewById(R.id.btn_cancel);
        Button confirmBtn = (Button) dialogView.findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdStr = pwd.getText().toString().trim();
                String rePwdStr = rePwd.getText().toString().trim();
                if (TextUtils.isEmpty(pwdStr)) {
                    toast("密码不能为空！");
                } else if (TextUtils.isEmpty(rePwdStr)) {
                    toast("再次输入的密码不能为空！");
                } else if (!pwdStr.equals(rePwdStr)) {
                    toast("两次密码不一致！");
                } else {
                    SpUtil.putString(AppConstants.ENTER_PROOF_PWD, MD5Utils.decode(pwdStr));
                    enterSecurityActivity();
                    mPwdSetDialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPwdSetDialog.dismiss();
            }
        });
    }


    @Override
    protected void onInitData() {
        //设置Toolbar
        mToolbar.setTitle("手机管理系统");
        setSupportActionBar(mToolbar);
        //设置drawerLayout
        setDrawerLayout();

        mMainPageIcons = new ArrayList<>();
        String[] iconNames = CommonUtil.getStringArray(R.array.icon_name);
        for (int i = 0; i < mIconArray.length; i++) {
            IconInfo iconInfo = new IconInfo(mIconArray[i], iconNames[i]);
            LogUtil.i(this, iconNames[i]);
            mMainPageIcons.add(iconInfo);
        }

        mLeftPageIcons = new ArrayList<>();
        iconNames = CommonUtil.getStringArray(R.array.left_menu_text);
        for (int j = 0; j < mLeftIconArray.length; j++) {
            IconInfo iconInfo = new IconInfo(mLeftIconArray[j], iconNames[j]);
            mLeftPageIcons.add(iconInfo);
        }
    }

    @Override
    protected void onSetViewData() {
        mDiscView.setValue(20);
        HashMap<String, Integer> windowSize = CommonUtil.getWindowSize(this);
        int windowHeight = windowSize.get("height");
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDiscView.getLayoutParams();
        params.height = (int) (windowHeight / 3 + 0.5) - mActionBar.getHeight();
        mDiscView.setLayoutParams(params);
        //设置主页面的listView的布局
        mLvAppList.setAdapter(new HomeMainAdapter(mContext, mMainPageIcons, windowHeight));
        mLvAppList.setOnItemClickListener(this);

        //设置左侧布局各部分的高度
        params = (LinearLayout.LayoutParams) mRlLeftHead.getLayoutParams();
        params.height = (int) (windowHeight / 3 + 0.5);
        mRlLeftHead.setLayoutParams(params);
        mRvLeftMenuContent.setLayoutParams(params);

        //设置RecyclerView
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置垂直滚动，也可以设置横向滚动
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView设置布局管理器
        mRvLeftMenuContent.setLayoutManager(layoutManager);
        //设置侧拉菜单的recyclerView的布局

        LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(mContext, mLeftPageIcons, windowSize);
        mRvLeftMenuContent.setAdapter(leftMenuAdapter);
        leftMenuAdapter.setOnItemClickListener(new LeftMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Intent intent = new Intent(mContext, QrCodeCaptureActivity.class);
                    mContext.startActivity(intent);
                } else if (position == 1) {
                    String user = SpUtil.getString(AppConstants.LOGIN_USER, "");
                    if (TextUtils.isEmpty(user)) {
                        toast("请先登录，然后使用备份功能");
                    } else {
                        Intent intent = new Intent(mContext, BackUpActivity.class);
                        mContext.startActivity(intent);

                    }
                } else if (position == 2) {
                    Intent intent = new Intent(mContext, PhoneLocationActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
        //添加分割线
        mRvLeftMenuContent.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
        ));
        //mRvLeftMenuContent.setOnClickListener(this);
    }

    /**
     * 设置drawerLayout
     */
    private void setDrawerLayout() {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setHomeButtonEnabled(true); //设置返回键可用
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case AppConstants.THEFT_PROOF:
                //进入手机防盗
                if (isSetPwd()) {
                    mPwdInputDialog.show();
                } else {
                    mPwdSetDialog.show();
                }
                break;
            case AppConstants.COMM_GUARD:
                Intent serviceIntent = new Intent(this, BlackNumService.class);
                startService(serviceIntent);

                intent = new Intent(this, BlackNumActivity.class);
                startActivity(intent);
                break;
            case AppConstants.PROCESS_MANAGER:
                intent = new Intent(this, ProcessMgrActivity.class);
                startActivity(intent);
                break;
            case AppConstants.SOFT_MANAGER:
                intent = new Intent(this, SoftMgrActivity.class);
                startActivity(intent);
                break;
            case AppConstants.CACHE_CLEAN:
                intent = new Intent(this, CacheCleanActivity.class);
                startActivity(intent);
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(this, "resume");
        String spUrl = SpUtil.getString(AppConstants.AVATAR_SERVER_PATH, "");
        LogUtil.i(this, "spUrl：" + spUrl);
        Uri uri = Uri.parse(spUrl);
        mIvAvatar.setImageURI(uri);
        String userName = SpUtil.getString(AppConstants.LOGIN_USER, "");
        mUserName.setText(userName.equals("") ? "未登录" : userName);
    }

    /**
     * 进入手机防盗的activity界面
     */
    private void enterSecurityActivity() {
        LogUtil.i(this, "进入手机防盗的设置界面");
        Intent serviceIntent = new Intent(this, TheftProofService.class);
        startService(serviceIntent);
        Intent intent = new Intent(this, TheftProofActivity.class);
        startActivity(intent);
    }


    /**
     * 在点击手机防盗页面时是否设置了密码
     *
     * @return
     */
    private boolean isSetPwd() {
        String password = SpUtil.getString(AppConstants.ENTER_PROOF_PWD, null);
        return !TextUtils.isEmpty(password);
    }

    @Override
    protected void onInitListener() {
        mIvAvatar.setOnClickListener(this);
        mTvLeftSetting.setOnClickListener(this);
        mBtnSpeed.setOnClickListener(this);
    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                if (!TextUtils.isEmpty(SpUtil.getString(AppConstants.LOGIN_USER, ""))) {
                    //显示用户信息
                    Intent intent = new Intent(this, UserInfoActivity.class);
                    startActivity(intent);
                } else {
                    //注册登录界面
                    Intent intent = new Intent(this, LoginRegActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_left_setting:
                Intent intent = new Intent(this, AppSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_speed:
                toast("一键加速！");
                mDiscView.setTwoValue(20,70);
            break;
        }
    }

}
