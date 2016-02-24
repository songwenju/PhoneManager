package com.wjustudio.phoneManager.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.base.BaiscAdapter;
import com.wjustudio.phoneManager.javaBean.IconInfo;
import com.wjustudio.phoneManager.lib.dicview.DiscView;
import com.wjustudio.phoneManager.utils.CommonUtil;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：songwenju on 2016/1/31 20:26
 * 邮箱：songwenju01@163.com
 */
public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_appList)
    ListView mLvAppList;
    @Bind(R.id.disc_view)
    DiscView mDiscView;
    @Bind(R.id.tb_custom)
    Toolbar mToolbar;
    @Bind(R.id.dl_left)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.ll_left_menu)
    LinearLayout mLlLeftMenu;

    private List<IconInfo> mIcons;
    private Context mContext;
    private int mWindowHeight;

    private int[] mIconArray = {R.mipmap.icon_safe, R.mipmap.icon_contacts,
            R.mipmap.icon_progress, R.mipmap.icon_app, R.mipmap.icon_cache,};
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;
    private Integer mWindowWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * 初始化相关数据
     */
    private void init() {
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mContext = this;
        //设置Toolbar
        mToolbar.setTitle("手机管理系统");
        setSupportActionBar(mToolbar);
        //设置drawerLayout
        setDrawerLayout();
        //获得数据
        getIconList();
        //初始化view
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mDiscView.setValue(300);
        HashMap<String, Integer> windowSize = CommonUtil.getWindowSize(this);
        mWindowHeight = windowSize.get("height");
        mWindowWidth = windowSize.get("width");
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDiscView.getLayoutParams();
        params.height = (int) (mWindowHeight / 3 + 0.5) - mActionBar.getHeight();
        mDiscView.setLayoutParams(params);
        mLvAppList.setAdapter(new AppListAdapter(mIcons));
        mLvAppList.setOnItemClickListener(this);

        params = (LinearLayout.LayoutParams)mLlLeftMenu.getLayoutParams();
        params.width = mWindowWidth *2 / 3;
        mLlLeftMenu.setLayoutParams(params);
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
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    /**
     * 获得图标的列表
     */
    private void getIconList() {
        mIcons = new ArrayList<>();
        String[] iconNames = CommonUtil.getStringArray(R.array.icon_name);
        for (int i = 0; i < mIconArray.length; i++) {
            IconInfo iconInfo = new IconInfo(mIconArray[i], iconNames[i]);
            LogUtil.e(this, iconNames[i]);
            mIcons.add(iconInfo);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //进入手机防盗
                if (isSetPwd()) {
                    showEnterPwdDialog();
                } else {
                    showSetPwdDialog();
                }
                break;
        }
    }

    /**
     * 显示设置密码的Dialog
     */
    private void showSetPwdDialog() {

    }

    /**
     * 显示填写密码的Dialog
     */
    private void showEnterPwdDialog() {
    }

    /**
     * 是否设置了密码
     *
     * @return
     */
    private boolean isSetPwd() {
        String password = SpUtil.getString("password", null);
        return !TextUtils.isEmpty(password);
    }

    /**
     * AppListAdapter列表适配器
     */
    class AppListAdapter extends BaiscAdapter {
        public AppListAdapter(List list) {
            super(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_home, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
                holder.iconName = (TextView) convertView.findViewById(R.id.tv_icon_name);
                convertView.setTag(holder);
                RelativeLayout.LayoutParams params =
                        (RelativeLayout.LayoutParams) holder.icon.getLayoutParams();
                params.height = mWindowHeight * 4 / (getCount() * 9);
                convertView.setLayoutParams(params);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.icon.setImageResource(mIconArray[position]);
            holder.iconName.setText(mIcons.get(position).iconName);
            return convertView;
        }
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView iconName;
    }
}
