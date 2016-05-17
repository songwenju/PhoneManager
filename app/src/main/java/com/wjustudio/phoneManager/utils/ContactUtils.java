package com.wjustudio.phoneManager.utils;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.wjustudio.phoneManager.javaBean.ContactInfo;
import com.wjustudio.phoneManager.javaBean.ContactJson;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;

public class ContactUtils {

    /**
     * 获得系统所有联系人信息
     * @param context
     * @return
     */
    public static ArrayList<ContactInfo> getContactList(Context context) {
        ArrayList<ContactInfo> listMembers = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = context.getContentResolver().query(
                    uri,
                    new String[]{"display_name", "contact_id", "data1"},
                    null, null, "sort_key");

            if (cursor != null && cursor.moveToFirst()) {
                LogUtil.e("contactUtil", "ContactCount:" + cursor.getCount());
                do {
                    ContactInfo contact = new ContactInfo();
                    String contact_phoneNum = cursor.getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String name = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    int contact_id = cursor.getInt(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    contact.contact_name = name;
                    contact.contact_phoneNum = contact_phoneNum.replaceAll("[^0-9]", "");
                    contact.contact_id = contact_id;
                    contact.pinYin = getPingYin(name);
                    LogUtil.e("contactUtils", "contact:" + contact.toString());
                    if (name != null)
                        listMembers.add(contact);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return listMembers;
    }

    public static void writeToPhone(Context context,ContactJson.ContactsBean contact) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getContact_name());
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        values.clear();
        values.put(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getContact_phoneNum());
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }
    public static void writeContact(Context context, ContactJson.ContactsBean contact) {
        Cursor cursor = null;
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // 这里是获取联系人表的电话里的信息  包括：名字，联系人id,电话号码；
        //先清空，再插入：
        context.getContentResolver().delete(uri, null, null);
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString 输入的中文汉字
     * @return 获得的拼音
     */
    public static String getPingYin(String inputString) {
        if (TextUtils.isEmpty(inputString)) {
            return "";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (char anInput : input) {
                if (Character.toString(anInput).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            anInput, format);
                    if (temp == null || TextUtils.isEmpty(temp[0])) {
                        continue;
                    }
                    output += temp[0].replaceFirst(temp[0].substring(0, 1),
                            temp[0].substring(0, 1).toUpperCase());
                } else
                    output += Character.toString(anInput);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


}
