package com.wjustudio.phoneManager.javaBean;

import org.litepal.crud.DataSupport;

/**
 * songwenju on 16-4-20 : 12 : 23.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumInfo extends DataSupport{
    private String blackNum;
    private int mode;

    public String getBlackNum() {
        return blackNum;
    }

    public void setBlackNum(String blackNum) {
        this.blackNum = blackNum;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public BlackNumInfo() {
    }
}
