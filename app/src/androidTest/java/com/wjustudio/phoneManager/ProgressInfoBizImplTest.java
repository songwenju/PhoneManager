package com.wjustudio.phoneManager;

import android.test.AndroidTestCase;

import com.wjustudio.phoneManager.biz.IProgressBiz;
import com.wjustudio.phoneManager.biz.ProgressBizImpl;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.util.List;

/**
 * 作者：songwenju on 2015/12/21 23:08
 * 邮箱：songwenju01@163.com
 */
public class ProgressInfoBizImplTest extends AndroidTestCase {
    public void testProgressInfo() throws Exception {
        IProgressBiz biz = new ProgressBizImpl(this.getContext());
        List<ProgressInfo> progressInfoList = biz.getProgressInfoList();
        for (ProgressInfo progressInfo:progressInfoList) {
            LogUtil.i(this,progressInfo.toString());
        }
    }

}
