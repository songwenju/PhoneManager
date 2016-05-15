package com.wjustudio.phoneManager.javaBean;

/**
 * 用户类
 */
public class UserInfo {
    public String name;
    public String pwd;
    public String email;
    public String headImgUrl;

    public UserInfo(String name, String pwd, String email,String headImgUrl) {
        this.name = name;
        this.pwd = pwd;
        this.email = email;
        this.headImgUrl = headImgUrl;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", email='" + email + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                '}';
    }
}
