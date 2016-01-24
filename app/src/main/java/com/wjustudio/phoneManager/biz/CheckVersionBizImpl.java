package com.wjustudio.phoneManager.biz;

import com.alibaba.fastjson.JSON;
import com.wjustudio.phoneManager.commonInterface.Url;
import com.wjustudio.phoneManager.javaBean.VersionInfo;
import com.wjustudio.phoneManager.utils.LogUtil;
import com.wjustudio.phoneManager.utils.OkHttpUtil;

import java.io.IOException;

/**
 * 作者：songwenju on 2015/12/20 18:21
 * 邮箱：songwenju01@163.com
 * 在这里面请求网络去校验版本是否有更新.
 */
public class CheckVersionBizImpl implements ICheckVersionBiz {
    @Override
    public void checkVersion(String versionCode) {
        try {
            String jsonString = OkHttpUtil.getStringFromServer(Url.checkVersion);
            VersionInfo versionInfo = JSON.parseObject(jsonString, VersionInfo.class);
            LogUtil.e(this,versionInfo.des);
            LogUtil.e(this,versionInfo.version);
            LogUtil.e(this,jsonString);
            if (!versionCode.equals(versionInfo.version)){
                //存在新版本

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
