package com.wjustudio.phoneManager.javaBean;

import java.util.List;

/**
 * 作者： songwenju on 2016/5/15 21:40.
 * 邮箱： songwenju@outlook.com
 */
public class ContactJson {

    /**
     * contact_name : song
     * contact_phoneNum : 13522410383
     * email : null
     */

    private List<ContactsBean> contacts;

    public List<ContactsBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsBean> contacts) {
        this.contacts = contacts;
    }

    public static class ContactsBean {
        private String contact_name;
        private String contact_phoneNum;
        private String email;

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getContact_phoneNum() {
            return contact_phoneNum;
        }

        public void setContact_phoneNum(String contact_phoneNum) {
            this.contact_phoneNum = contact_phoneNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
