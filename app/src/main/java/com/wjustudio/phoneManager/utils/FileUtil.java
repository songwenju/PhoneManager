package com.wjustudio.phoneManager.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtil {
    private static final String TAG = "FileUtil";

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return 保存的文件
     */
    public static String saveFile(String fileName, Bitmap bitmap) {
        return saveFile("", fileName, bitmap);
    }

    public static String saveFile(String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveFile(filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(CompressFormat.JPEG, 100, bao);
        return bao.toByteArray();
    }

    public static String saveFile(String filePath, String fileName, byte[] bytes) {
        String fileFullName = "";
        FileOutputStream fos = null;
        String dateStr = new SimpleDateFormat("MMddSS", Locale.CHINA)
                .format(new Date());
        try {
            String suffix = ".jpg";
            LogUtil.i(TAG, "filePath1:" + filePath);
            if (TextUtils.isEmpty(filePath)) {
                filePath = Environment.getExternalStorageDirectory() + "/phoneMgr/";
            }
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(filePath + fileName +"_"+dateStr+ suffix);
            fileFullName = fullFile.getPath();
            fos = new FileOutputStream(fullFile);
            LogUtil.i(TAG, "filePath2:" +filePath + fileName +"_"+dateStr+ suffix);
            fos.write(bytes);
        } catch (Exception e) {
            fileFullName = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullName = "";
                }
            }
        }
        return fileFullName;
    }
}
