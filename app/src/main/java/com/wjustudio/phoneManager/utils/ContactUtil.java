package com.wjustudio.phoneManager.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.wjustudio.phoneManager.Common.AppConstants;
import com.wjustudio.phoneManager.javaBean.Contact;

import java.util.ArrayList;

/**
 * 作者：songwenju on 2016/4/2 22:00
 * 邮箱：songwenju01@163.com
 */
public class ContactUtil {

    /**
     * 获得系统联系人
     *
     * @param context
     * @return
     */
    public static ArrayList<Contact> getContact(Context context) {
        ArrayList<Contact> contacts = new ArrayList<>();
        Contact contact;
        String[] projection = {"data1", "mimetype"};
        Cursor rawCursor = context.getContentResolver().query(Uri.parse(AppConstants.RAW_CONTACTS),
                new String[]{"contact_id"}, null, null, null);
        if (rawCursor != null && rawCursor.getCount() > 0) {
            LogUtil.i("ContactUtil", "the number of contact:" + rawCursor.getCount());
            while (rawCursor.moveToNext()) {
                String contact_id = rawCursor.getString(rawCursor.getColumnIndex("contact_id"));
                LogUtil.i("contactUtil","contact_id:"+contact_id);
                if (contact_id != null) {
                    Cursor dataCursor = context.getContentResolver().query(
                            Uri.parse(AppConstants.DATA_CONTACTS), projection,
                            "raw_contact_id = ?", new String[]{contact_id}, null);
                    if (dataCursor != null && dataCursor.getCount() > 0) {
                        contact = new Contact();
                        while (dataCursor.moveToNext()) {
                            String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            data1 = data1.trim().replaceAll("-","").replaceAll(" ","");
                            String mimetype = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                            switch (mimetype) {
                                case "vnd.android.cursor.item/name":
                                    contact.contact_name = data1;
                                    if (ChineseUtil.isChinese(data1.charAt(0))){
                                        contact.pinYin = PinYinUtil.toPinyin(data1);
                                    }else {
                                        contact.pinYin = data1;
                                    }
                                    break;
                                case "vnd.android.cursor.item/email_v2":
                                    contact.email = data1;
                                    break;
                                case "vnd.android.cursor.item/phone_v2":
                                    contact.contact_phoneNum = data1;
                                    break;
                            }
                        }
                        contacts.add(contact);
                    }
                }
            }
        }
        LogUtil.e("contactUtil","contactNum:"+contacts.size());
        return contacts;
    }

    /**
     * 还原联系人
     */
    public static void restoreContact(Context context, ArrayList<Contact> contacts) {
        context.getContentResolver().query(Uri.parse(AppConstants.RAW_CONTACTS), null, null, null, null);
        //两数据进行比较，如果没有有就插入，如果有就进行差异化处理。
    }


}
