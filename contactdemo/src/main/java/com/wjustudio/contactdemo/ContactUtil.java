package com.wjustudio.contactdemo;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;


public class ContactUtil {
    public static ArrayList<Contact> getContactList(Context context) {
        ArrayList<Contact> list = new ArrayList<>();

        Cursor contactsCur = null, phoneCur = null;
        try {
            contactsCur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (contactsCur != null && contactsCur.moveToFirst()) {
                LogUtil.e("contactUtil", "ContactCount:" + contactsCur.getCount());

                while (contactsCur.moveToNext()) {
                    //不能放到上面，这里要创建多个对象
                    Contact contact = new Contact();
                    String contactId, phoneNum;
                    contactId = contactsCur.getString(contactsCur.getColumnIndex(ContactsContract.Contacts._ID));
                    contact.name = contactsCur.getString(contactsCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    int hasPhoneNum = contactsCur.getInt(contactsCur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    if (hasPhoneNum > 0) {
                        phoneCur = context.getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                null, "sort_key");
                        if (phoneCur != null && phoneCur.getCount() > 0) {
                            int numberColumn = phoneCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER);
                            while (phoneCur.moveToNext()) {
                                phoneNum = phoneCur.getString(numberColumn);
                                if (phoneNum != null) {
                                    contact.telephoneNumber.add(phoneNum.replaceAll("[^0-9]", ""));
                                }
                            }
                        }
                    }

                    LogUtil.e("contactUtil", "contact:" + contact.toString());

                    list.add(contact);
                }
            }
        } finally {
            if (phoneCur != null) {
                phoneCur.close();
            }
            if (contactsCur != null) {
                contactsCur.close();
            }
        }
        return list;
    }
}
