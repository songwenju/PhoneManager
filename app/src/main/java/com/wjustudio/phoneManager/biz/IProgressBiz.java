package com.wjustudio.phoneManager.biz;

import com.wjustudio.phoneManager.javaBean.ProgressInfo;

import java.io.IOException;
import java.util.List;

/**
 * songwenju on 16-5-6 : 13 : 20.
 * 邮箱：songwenju@outlook.com
 */
public interface IProgressBiz {

    /**
     * 获得所有进程的信息
     * @return
     */
    List<ProgressInfo> getProgressInfoList();


    /**
     * 获得可用内存的大小
     * @return
     */
    String getAvailMem();

    /**
     * 获得所有内存的大小
     * @return
     */
    String getTotalMem() throws IOException;
}
