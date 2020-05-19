package com.yuong.running;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.yuong.running.common.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends MultiDexApplication {
    private static MyApplication applicationContext;
    private static Handler handler;

    public static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

    }

    public static MyApplication getInstance() {
        return applicationContext;
    }

    public static Handler getHandler() {
        if (null == handler)
            handler = new Handler(Looper.getMainLooper());
        return handler;
    }

    public static void addActivity(Activity activity) {
        if (null != activity && !activityList.contains(activity))
            activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (null != activity && activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
        }
    }

    public static void exitActivity() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeApp(Context context) {

        if (null != handler)
            handler.removeCallbacksAndMessages(null);

        exitActivity();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);

    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            PackageManager pm = applicationContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(applicationContext.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtils.e("VersionInfo Exception", e);
        }
        return versionName;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode() {
        int versioncode = 0;
        try {
            PackageManager pm = applicationContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(applicationContext.getPackageName(), 0);
            versioncode = pi.versionCode;
        } catch (Exception e) {
            LogUtils.e("VersionInfo Exception", e);
        }
        return versioncode;
    }

}
