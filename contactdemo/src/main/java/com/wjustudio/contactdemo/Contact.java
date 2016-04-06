package com.wjustudio.contactdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：songwenju on 2016/4/2 22:01
 * 邮箱：songwenju01@163.com
 */
public class Contact {
    public String name;
    public String pinYin;
    public List<String> telephoneNumber = new ArrayList<>();
    public String email;

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", pinYin='" + pinYin + '\'' +
                ", telephoneNumber=" + telephoneNumber +
                ", email='" + email + '\'' +
                '}';
    }
}
