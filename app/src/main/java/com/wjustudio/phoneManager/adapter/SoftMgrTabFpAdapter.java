package com.wjustudio.phoneManager.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wjustudio.phoneManager.base.BaseFragment;

import java.util.List;

/**
 * 作者：songwenju on 2016/3/13 10:29
 * 邮箱：songwenju01@163.com
 */
public class SoftMgrTabFpAdapter extends FragmentPagerAdapter{

    private List<BaseFragment> mFragmentList;         //fragments列表
    private List<String> mTitleList;                  //tab名列表

    public SoftMgrTabFpAdapter(FragmentManager fm, List<BaseFragment> fragments,List<String> titleList) {
        super(fm);
        mFragmentList = fragments;
        mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position % mTitleList.size());
    }
}
