package com.wjustudio.phoneManager.biz;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.wjustudio.phoneManager.javaBean.BlackNumInfo;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * songwenju on 16-4-20 : 13 : 23.
 * 邮箱：songwenju@outlook.com
 */
public class BlackNumBizImpl implements IBlackNumBiz {
    public static final int BLACK_NUM_PHONE = 1;
    public static final int BLACK_NUM_SMS = 2;
    public static final int BLACK_NUM_ALL = 3;
    private final SQLiteDatabase mDb;


    public BlackNumBizImpl() {
        mDb = Connector.getDatabase();
    }

    @Override
    public List<BlackNumInfo> getAllBlackNum() {
        return DataSupport.findAll(BlackNumInfo.class);
    }

    @Override
    public void insertBlackNum(BlackNumInfo blackNumInfo) {
        blackNumInfo.saveThrows();
    }

    @Override
    public void deleteBlackNum(BlackNumInfo blackNumInfo) {
        if (!blackNumInfo.isSaved()) {
            blackNumInfo.save();
        }

        blackNumInfo.delete();
    }

    @Override
    public void updateBlackNum(BlackNumInfo blackNumInfo) {
        ContentValues values = new ContentValues();
        values.put("mode", blackNumInfo.getMode());
        DataSupport.updateAll(BlackNumInfo.class, values, "blackNum = ?", blackNumInfo.getBlackNum());
    }

    @Override
    public boolean isExist(String blackNum) {
        return false;
    }


}
