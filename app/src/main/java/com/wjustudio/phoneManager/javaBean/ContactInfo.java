package com.wjustudio.phoneManager.javaBean;

/**
 * author：songwenju on 16-4-7 11 : 07
 * Email: songwenju01@163.com
 */
public class ContactInfo {
    public String contact_name;
    public String contact_phoneNum;
    public int contact_id;
    public String email;
    //public List<String> telephoneNumber = new ArrayList<>();
    public String pinYin;
    @Override
    public String toString() {
        return "ContactInfo{" +
                "contact_name='" + contact_name + '\'' +
                ", contact_phoneNum='" + contact_phoneNum + '\'' +
                ", contact_id=" + contact_id +
                ", email='" + email + '\'' +
                ", pinYin='" + pinYin + '\'' +
                '}';
    }

}

