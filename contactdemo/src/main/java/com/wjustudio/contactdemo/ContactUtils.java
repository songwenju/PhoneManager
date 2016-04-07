package com.wjustudio.contactdemo;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.ArrayList;

public class ContactUtils {

    public static ArrayList<ContactMember> getContact(Activity context) {
        ArrayList<ContactMember> listMembers = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = context.getContentResolver().query(
                    uri,
                    new String[] { "display_name", "contact_id", "data1" },
                    null, null, "sort_key");

            if (cursor != null && cursor.moveToFirst()) {
                LogUtil.e("contactUtil", "ContactCount:" + cursor.getCount());
                while (cursor.moveToNext()){
                    ContactMember contact = new ContactMember();
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
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return listMembers;
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
