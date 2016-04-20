package com.wjustudio.phoneManager.biz;

import com.wjustudio.phoneManager.javaBean.BlackNumInfo;

import java.util.ArrayList;

/**
 * songwenju on 16-4-20 : 13 : 23.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumBizImpl implements IBlackNumBiz{
    public static final int BLACK_NUM_PHONE = 1;
    public static final int BLACK_NUM_SMS = 2;
    public static final int BLACK_NUM_ALL = 3;

    @Override
    public ArrayList<BlackNumInfo> getAllBlackNum() {
        return null;
    }

    @Override
    public void insertBlackNum(BlackNumInfo blackNumInfo) {

    }

    @Override
    public void deleteBlackNum(BlackNumInfo blackNumInfo) {

    }

    @Override
    public void updateBlackNum(BlackNumInfo blackNumInfo) {

    }

    @Override
    public boolean isExist(String blackNum) {
        return false;
    }


}
