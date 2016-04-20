package com.wjustudio.phoneManager.javaBean;

/**
 * songwenju on 16-4-20 : 12 : 23.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumInfo {
    public String blackNum;
    public int mode;

    public BlackNumInfo() {
    }

    public BlackNumInfo(String blackNum, int mode) {
        this.blackNum = blackNum;
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumInfo{" +
                "blackNum='" + blackNum + '\'' +
                ", mode=" + mode +
                '}';
    }
}
