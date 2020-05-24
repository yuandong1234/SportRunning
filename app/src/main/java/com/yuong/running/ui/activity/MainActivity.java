package com.yuong.running.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.app.permission.OnPermissionCallback;
import com.app.permission.PermissionUtil;
import com.yuong.running.R;
import com.yuong.running.common.activity.BaseActivity;
import com.yuong.running.common.constans.AppConfig;
import com.yuong.running.common.utils.AppUtils;
import com.yuong.running.common.utils.DeviceUtils;
import com.yuong.running.common.utils.DialogUtils;
import com.yuong.running.widget.PressProgressView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final static int MSG_LOCATION_SERVICE_OPENED = 1;
    private final static int MSG_PERMISSIONS_APPLY_COMPLETED = 2;

    private PressProgressView pressProgressView;

    // 要申请的权限
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_LOCATION_SERVICE_OPENED:
                    startSport();
                    break;
                case MSG_PERMISSIONS_APPLY_COMPLETED:
                    startActivity(new Intent(MainActivity.this, SportActivity.class));
//                    startActivity(new Intent(MainActivity.this, SportMapActivity.class));
                    break;
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (DeviceUtils.isOverMarshmallow()) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 100);
        }
    }

    @Override
    public void initView() {
        findViewById(R.id.tv_start).setOnClickListener(this);
        pressProgressView = findViewById(R.id.pressProgressView);
        pressProgressView.setOnPressClickListener(new PressProgressView.OnPressClickListener() {
            @Override
            public void onPressClick() {
                Toast.makeText(MainActivity.this, "长按完成结束........", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, MapTrackActivity.class));
                startActivity(new Intent(MainActivity.this, MyRunningTrackActivity.class));
            }
        });
    }

    @Override
    public void initListener() {

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
        applyPermission();
    }

    private void showLocationServiceTip() {
        DialogUtils.showCommonDialog(this, "运动提示", "运动跑步，需要位置服务", true, new DialogUtils.CommonCallBack() {
            @Override
            public void onClick() {
                AppUtils.openLocationSetting(MainActivity.this);
            }
        });
    }

    //请求权限
    private void applyPermission() {
        PermissionUtil.with(this)
                .constantRequest()
                .permission(PERMISSIONS)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onPermissionGranted(List<String> granted, boolean isAllGranted) {
                        if (isAllGranted) {
                            sendMessage(MSG_PERMISSIONS_APPLY_COMPLETED);
                        }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConfig.LOCATION_SERVICE_REQUEST_CODE) {
            sendMessage(MSG_LOCATION_SERVICE_OPENED);
        }
    }

    private void sendMessage(int what) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        mHandler.sendMessage(msg);
    }
}
