package com.yuong.running.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.permission.OnPermissionCallback;
import com.app.permission.PermissionUtil;
import com.yuong.running.R;
import com.yuong.running.common.activity.BaseActivity;
import com.yuong.running.common.utils.DeviceUtils;
import com.yuong.running.common.utils.DialogUtils;

import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    // 要申请的权限
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        findViewById(R.id.tv_start).setOnClickListener(this);
    }

    @Override
    public void initListener() {
        requestPermission();
    }

    //请求权限
    private void requestPermission() {
        PermissionUtil.with(this)
                //.constantRequest()
                .permission(PERMISSIONS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onPermissionGranted(List<String> granted, boolean isAllGranted) {

                    }

                    @Override
                    public void onPermissionDenied(List<String> denied, List<String> permanentDenied) {

                    }

                    @Override
                    public void onPermissionComplete() {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start:
                startSport();
                break;
        }
    }

    private void startSport() {
        boolean enable = DeviceUtils.isLocationEnabled(this);
        if (!enable) {
            showLocationServiceTip();
            return;
        }
        startActivity(new Intent(this, SportActivity.class));
    }

    private void showLocationServiceTip() {
        DialogUtils.showCommonDialog(this, "运动提示", "运动跑步，需要位置服务", true, new DialogUtils.CommonCallBack() {
            @Override
            public void onClick() {

            }
        });
    }
}
