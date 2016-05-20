package com.wjustudio.phoneManager.presenter;

import android.content.Context;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.utils.CommonUtil;

/**
 * songwenju on 16-5-20 : 16 : 50.
 * 邮箱：songwenju@outlook.com
 */
public class HomeActivityPresenter {
    private Context mContext;

    public HomeActivityPresenter(Context context){
        mContext = context;
    }
    /**
     * 获得当前的分数
     */
    public int getCurrentScore(){
        int score = 30;
        if (CommonUtil.isRunningService(mContext, AppConstants.THEFT_PROOF_SERVICE)){
            score += 30;

        }

        if (CommonUtil.isRunningService(mContext, AppConstants.BLACK_NUM_SERVICE)) {
            score += 30;
        }


        if (CommonUtil.isRunningService(mContext, AppConstants.LOCK_APP_SERVICE)) {
            score += 30;
        }


        return score;

    }

}
