package com.wjustudio.contactdemo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

    }

    public void cli(View view) {
        LogUtil.e(this, "-------cli");
        Cursor rawCursor = mContext.getContentResolver().query(Uri.parse(AppConstants.RAW_CONTACTS),
                null, null, null, null);

        LogUtil.e(this, "rawCursor:" + rawCursor);
        if (rawCursor != null) {
            LogUtil.i(this, "the number of contact:" + rawCursor.getCount());
            while (rawCursor.moveToNext()) {
                String contact_id = rawCursor.getString(rawCursor.getColumnIndex("contact_id"));
                LogUtil.i(this, "contact_id:" + contact_id);
                if (contact_id != null) {
                    Cursor dataCursor = getContentResolver().query(
                            Uri.parse(AppConstants.DATA_CONTACTS), null,
                            "raw_contact_id = ?", new String[]{contact_id}, null);
                    if (dataCursor != null) {
                        LogUtil.i(this, "dataCursor count:" + dataCursor.getCount());
                        Contact contact = new Contact();
                        while (dataCursor.moveToNext()) {
                            String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                            String mimetype = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                            // LogUtil.i(this, "data1:" + data1);
                            if (data1 != null) {
                                data1 = data1.trim().replaceAll("-", "").replaceAll(" ", "");
                                switch (mimetype) {
                                    case "vnd.android.cursor.item/name":
                                        contact.name = data1;
                                        break;
                                    case "vnd.android.cursor.item/email_v2":
                                        contact.email = data1;
                                        break;
                                    case "vnd.android.cursor.item/phone_v2":
                                        contact.telephoneNumber.add(data1);
                                        break;
                                }
                            }
                        }
                        LogUtil.e(this, contact.toString());
                    }
                }
            }
        }

        if (rawCursor != null) {
            rawCursor.close();
        }

    }
}
