package com.yuong.running.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

import com.yuong.running.common.constans.AppConfig;

public class AppUtils {
    /**
     * 打开位置服务界面
     */
    public static void openLocationSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, AppConfig.LOCATION_SERVICE_REQUEST_CODE);
    }
}
