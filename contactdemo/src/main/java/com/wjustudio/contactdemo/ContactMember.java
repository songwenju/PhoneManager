package com.wjustudio.contactdemo;

/**
 * author：songwenju on 16-4-7 11 : 07
 * Email: songwenju01@163.com
 */
public class ContactMember {
    public String contact_name;
    public String contact_phoneNum;
    public int contact_id;

    public String pinYin;

    @Override
    public String toString() {
        return "ContactMember{" +
                "contact_name='" + contact_name + '\'' +
                ", contact_phoneNum='" + contact_phoneNum + '\'' +
                ", contact_id=" + contact_id +
                ", pinYin='" + pinYin + '\'' +
                '}';
    }
}

