package com.wjustudio.phoneManager.biz;

/**
 * 手机防盗设置
 * 作者：songwenju on 2016/3/20 15:52
 * 邮箱：songwenju01@163.com
 */
public interface ISecuritySettingBiz {
    /**
     * 绑定SIM卡
     */
    void bindSIM();


    /**
     * 当SIM卡变更时发送短信到绑定的手机上
     */
    void sendMsgSIMChange();

    /**
     * 播放报警音乐
     */
    void playSecurityMusic();

    /**
     * 获得位置信息
     */
    void getLocation();

    /**
     * 锁屏
     */
    void lockScreen();

    /**
     * 重置开机密码
     */
    void resetPhonePwd(String newNum);

    /**
     * 清除数据
     */
    void cleanData();
}
