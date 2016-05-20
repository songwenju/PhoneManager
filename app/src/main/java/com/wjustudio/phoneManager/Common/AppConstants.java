package com.wjustudio.phoneManager.Common;

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
    public static final int REQUEST_CODE_PICK = 5;        // 相册选图标记
    public static final int REQUEST_CODE_TAKE = 6;        // 相机拍照标记
    public static final int REQUEST_CODE_CUTTING = 7;    // 图片裁切标记
    public static final String FIRST_OPEN = "first_open";
    public static final String SAFE_NUM = "safeNum";
    public static final String IS_OPEN_PROTECT = "isOpenProtect";
    public static final String SIM_SERIAL_NUM = "simSerialNum";

    public static final String CONTACT_NUM = "contactNum";
    public static final String WINDOW_HEIGHT = "height";
    public static final String WINDOW_WIDTH = "width";
    public static final String ENTER_PROOF_PWD = "enterProofPwd";
    public static final String LOCK_PASSWORD = "lockPassword";

    public static final String BASE_URL = "http://www.wjustudio.com/PhoneManagerServer/";
//    public static final String BASE_URL = "http://192.168.191.1/PhoneManagerServer/";
    public static final String LOGIN_PATH = BASE_URL+"loginServlet";
    public static final String REGISTER_PATH =  BASE_URL+"registerServlet";
    public static final String CHECK_CODE_PATH = BASE_URL+"checkCodeServlet";
    public static final String RESET_PWD_PATH =  BASE_URL+"resetPwdServlet";
    public static final String PIC_URL_PATH = BASE_URL + "uploadAvatarServlet";
    public static final String JSON_URL_PATH = BASE_URL + "uploadContactServlet";
    public static final String CONTACT_BACKUP_URL_PATH = BASE_URL + "getContactServlet";
    public static final String ABOUT_US = BASE_URL+"aboutUs.html";
    public static final String VERSION_URL = BASE_URL+"versionInfo.json";
    public static final String LOGIN_USER = "loginUser";


    public static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    public static final String AVATAR_SERVER_PATH = "avatarServerPath";// 头像文件名称

    //app service name
    public static final String THEFT_PROOF_SERVICE = "com.wjustudio.phoneManager.service.TheftProofService";
    public static final String BLACK_NUM_SERVICE = "com.wjustudio.phoneManager.service.BlackNumService";
    public static final String LOCK_APP_SERVICE = "com.wjustudio.phoneManager.service.LockAppService";

}
