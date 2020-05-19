package com.yuong.running.common.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.yuong.running.MyApplication;

public class ToastUtils {

    public static void show(String msg) {
        if(TextUtils.isEmpty(msg)) return;
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
