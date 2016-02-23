package com.wjustudio.phoneManager.javaBean;

/**
 * 作者：songwenju on 2015/12/20 17:58
 * 邮箱：hgdswj@163.com
 */
public class VersionInfo {
    public String version;
    public String downloadUrl;
    public String desc;

    public VersionInfo() {
        super();
    }
    public VersionInfo(String version, String downloadUrl, String des) {
        this.version = version;
        this.downloadUrl = downloadUrl;
        this.desc = des;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "version='" + version + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
