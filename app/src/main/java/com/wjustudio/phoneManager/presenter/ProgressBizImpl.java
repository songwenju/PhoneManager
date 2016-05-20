package com.wjustudio.phoneManager.presenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.text.format.Formatter;

import com.wjustudio.phoneManager.lib.processes.ProcessManager;
import com.wjustudio.phoneManager.lib.processes.models.AndroidAppProcess;
import com.wjustudio.phoneManager.R;
import com.wjustudio.phoneManager.javaBean.ProgressInfo;
import com.wjustudio.phoneManager.utils.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * songwenju on 16-5-6 : 13 : 21.
 * 邮箱：songwenju@outlook.com
 */
public class ProgressBizImpl implements IProgressBiz {

    private Context mContext;

    public ProgressBizImpl(Context context) {
        mContext = context;
    }

    @Override
    public List<ProgressInfo> getProgressInfoList() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getTaskInfosForHighLevel();
        } else {
            return getTaskInfosForLowLevel();
        }
    }


    /**
     * Android Lollipop版本以下获得进程的方法
     * @return
     */
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
            progressInfo.ramSize = Formatter.formatFileSize(mContext,(long) (totalPss * 1024));
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
     *
     * @return
     */
    public List<ProgressInfo> getTaskInfosForHighLevel() {
        // 应用程序管理器
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // 应用程序包管理器
        PackageManager pm = mContext.getPackageManager();
        // 获取正在运行的程序信息, 就是以下粗体的这句代码,获取系统运行的进程     要使用这个方法，需要加载

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
            progressInfo.ramSize = Formatter.formatFileSize(mContext,(long) (totalPss * 1024));
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
     * 获取可用的内存大小
     *
     * @return 可用内存
     */
    public String getAvailMem() {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(outInfo);
        return Formatter.formatFileSize(mContext,outInfo.availMem);
    }


    /**
     * 获取总的内存大小,API16以上的版本可用
     *
     * @return 总内存
     */
    public String getTotalMem() throws IOException {
        if (Build.VERSION.SDK_INT > 16) {
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(outInfo);
            return  Formatter.formatFileSize(mContext,outInfo.totalMem);
        } else {
            StringBuilder sb = new StringBuilder();
            File file = new File("/proc/meminfo");
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            String line = br.readLine();
            LogUtil.i("ProgressUtils", "获取的数据" + line);
        /*获取的数据MemTotal:      999688 kB*/

            char[] charArray = line.toCharArray();
            for (char c : charArray) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            br.close();
            //获取数据的单位是kb
            return  Formatter.formatFileSize(mContext,Long.parseLong(sb.toString()) * 1024);
        }
    }

}
