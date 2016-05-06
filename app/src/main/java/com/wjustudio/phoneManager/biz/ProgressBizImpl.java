package com.wjustudio.phoneManager.biz;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;

import com.jaredrummler.android.processes.ProcessManager;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * songwenju on 16-5-6 : 13 : 21.
 * 邮箱：songwenju@outlook.com
 */
public class ProgressBizImpl implements IProgressBiz{

    private Context mContext;

    public ProgressBizImpl(Context context) {
        mContext = context;
    }

    @Override
    public List<ProgressInfo> getProgressInfoList() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return getTaskInfosForHighLevel();
        }else {
            return getTaskInfosForLowLevel();
        }
    }
    public List<ProgressInfo> getTaskInfosForLowLevel() {
        List<ProgressInfo> progressInfoList = new ArrayList<>();
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            ProgressInfo progressInfo = new ProgressInfo();
            //包名
            String packageName = processInfo.processName;
            progressInfo.packageName = packageName;
            //获取应用运行时的占用内存的大小.
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            int totalPss = memoryInfo[0].getTotalPss();
            progressInfo.ramSize = (long) (totalPss * 1024);
            //获取
            PackageManager pm = mContext.getPackageManager();
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                progressInfo.icon = applicationInfo.loadIcon(pm);
                progressInfo.name = applicationInfo.loadLabel(pm).toString();
                int flags = applicationInfo.flags;
                progressInfo.isUser = (flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // 系统内核进程 没有名称
                progressInfo.packageName = packageName;
                progressInfo.icon = mContext.getResources().getDrawable(
                        R.mipmap.ic_default);
            }

            progressInfoList.add(progressInfo);

        }

        return progressInfoList;
    }


    /**
     * 高版本获取系统运行的进程信息
     * @return
     */
    public  List<ProgressInfo> getTaskInfosForHighLevel() {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = mContext.getPackageManager();
        // 获取正在运行的程序信息, 就是以下粗体的这句代码,获取系统运行的进程     要使用这个方法，需要加载
        //　import com.jaredrummler.android.processes.ProcessManager;
        //　import com.jaredrummler.android.processes.models.AndroidAppProcess;  这两个包, 这两个包附件可以下载

        List<AndroidAppProcess> androidAppProcessList = ProcessManager.getRunningAppProcesses();
        List<ProgressInfo> progressInfoList = new ArrayList<>();
        // 遍历运行的程序,并且获取其中的信息
        for (AndroidAppProcess processInfo : androidAppProcessList) {
            ProgressInfo progressInfo = new ProgressInfo();
            //包名
            String packageName = processInfo.getPackageName();
            progressInfo.packageName = packageName;

            //获取应用运行时的占用内存的大小.
            Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            int totalPss = memoryInfo[0].getTotalPss();
            progressInfo.ramSize = (long) (totalPss * 1024);
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                progressInfo.icon = applicationInfo.loadIcon(pm);
                progressInfo.name = applicationInfo.loadLabel(pm).toString();
                int flags = applicationInfo.flags;
                progressInfo.isUser = (flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                // 系统内核进程 没有名称
                progressInfo.packageName = packageName;
                progressInfo.icon = mContext.getResources().getDrawable(
                        R.mipmap.ic_default);
            }
            progressInfoList.add(progressInfo);
        }
        return progressInfoList;
    }
}
