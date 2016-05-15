package com.wjustudio.phoneManager.javaBean;

import java.util.List;

/**
 * 作者： songwenju on 2016/5/15 18:45.
 * 邮箱： songwenju@outlook.com
 */
public class UserContactInfo {

    /**
     * jsonUrl : http://192.168.191.1/PhoneManagerServer/song_051503.json
     */

    private List<UserContactBean> userContact;

    public List<UserContactBean> getUserContact() {
        return userContact;
    }

    public void setUserContact(List<UserContactBean> userContact) {
        this.userContact = userContact;
    }

    public static class UserContactBean {
        private String jsonUrl;

        public String getJsonUrl() {
            return jsonUrl;
        }

        public void setJsonUrl(String jsonUrl) {
            this.jsonUrl = jsonUrl;
        }
    }
}
