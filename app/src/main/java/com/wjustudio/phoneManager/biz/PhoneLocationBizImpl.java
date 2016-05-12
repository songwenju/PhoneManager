package com.wjustudio.phoneManager.biz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lidroid.xutils.util.IOUtils;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.io.File;

public class PhoneLocationBizImpl implements IPhoneLocationBiz{
	private static final String TAG = "PhoneLocationEngine";
	private static SQLiteDatabase db;
	private static String sql;
	private static Cursor cursor;
	private Context mContext;

	public PhoneLocationBizImpl(Context context){
		mContext = context;
	}

	/**
	 * 查询电话号码归属地
	 * @param phoneNum
	 * @return 号码归属地
	 */
	public String query(String phoneNum){
		String location = "";
		String path = new File(mContext.getFilesDir(),"address.db").getAbsolutePath();
		//使用正则表达式对phoneNum进行过滤
		if (phoneNum.matches("^1[34578]\\d{9}$")) {
			LogUtil.i(TAG, "正常号码");
			db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			sql = "select location from data1,data2 where  data1.id=? and data1.outkey = data2.id";
			cursor = db.rawQuery(sql, new String[]{phoneNum.substring(0, 7)});
			if (cursor != null && cursor.getColumnCount()>0) {
				if (cursor.moveToNext()) {
					location = cursor.getString(0);
					LogUtil.i(TAG, location);
				}
				IOUtils.closeQuietly(cursor);
				db.close();
			}
		}else {
			switch (phoneNum.length()) {
			case 3:
				LogUtil.i(TAG, "特殊号码");
				location = "特殊号码";
				break;
			case 4:
				LogUtil.i(TAG, "虚拟号码");
				location = "虚拟号码";
				break;
			case 5:
				LogUtil.i(TAG, "客服号码");
				location = "客服号码";
				break;
			case 7:
			case 8:
				LogUtil.i(TAG, "本地号码");
				location = "本地号码";
				break;
			default:
				//开头为三位:010,开头为四位0371
				if (phoneNum.length() >=10 && phoneNum.startsWith("0")) {
					LogUtil.i(TAG, "长途号码");
					db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
					sql = "select location from data2 where area = ?";
					cursor = db.rawQuery(sql, new String[]{phoneNum.substring(1, 3)});
					LogUtil.i(TAG, phoneNum.substring(1, 3));
					if (cursor.moveToNext()) {
						location = cursor.getString(0);
						location = location.substring(0, location.length()-2);
						LogUtil.i(TAG, location);
					}else {
						//如果三位查询不出来,就查询四位
						cursor = db.rawQuery(sql, new String[]{phoneNum.substring(1, 4)});
						LogUtil.i(TAG, phoneNum.substring(1, 4));
						if (cursor.moveToNext()) {
							location = cursor.getString(0);
							location = location.substring(0, location.length()-2);
							LogUtil.i(TAG, location);
						}
					}
					IOUtils.closeQuietly(cursor);
					db.close();
				}
				break;
			}
		}

		return location;
	}
}
