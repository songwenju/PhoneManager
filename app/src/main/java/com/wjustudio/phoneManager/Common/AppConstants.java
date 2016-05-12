package com.wjustudio.phoneManager.Common;

import android.content.Context;

/**
 * App所用的常量
 * 作者：songwenju on 2016/1/31 11:39
 * 邮箱：songwenju01@163.com
 */
public class AppConstants {
    public static final int THEFT_PROOF = 0;
    public static final int COMM_GUARD = 1;
    public static final int PROCESS_MANAGER = 2;
    public static final int SOFT_MANAGER = 3;
    public static final int CACHE_CLEAN = 4;

    public static final String FIRST_OPEN = "first_open";
    public static final Context CONTEXT = BaseApplication.getContext();
    public static final String SAFE_NUM = "safeNum";
    public static final String IS_OPEN_PROTECT = "isOpenProtect";
    public static final String SIM_SERIAL_NUM = "simSerialNum";
    public static final String RAW_CONTACTS = "content://com.android.contacts/raw_contacts";
    public static final String DATA_CONTACTS = "content://com.android.contacts/data";
    public static final String CONTACT_NUM = "contactNum";
    public static final String WINDOW_HEIGHT = "height";
    public static final String WINDOW_WIDTH = "width";
    public static final String IS_OPEN_PROOF = "isOpenProof";
    public static final String ENTER_PROOF_PWD = "enterProofPwd";
    public static final String LOCK_PASSWORD = "lockPassword";

    public static final String BASE_URL = "http://www.wjustudio.com/DoodleServer/";
    public static final String LOGIN_PATH = BASE_URL+"loginServlet";
    public static final String REGISTER_PATH =  BASE_URL+"registerServlet";
    public static final String CHECK_CODE_PATH = BASE_URL+"checkCodeServlet";
    public static final String RESET_PWD_PATH =  BASE_URL+"resetPwdServlet";
    public static final String LOGIN_USER = "loginUser";

}
