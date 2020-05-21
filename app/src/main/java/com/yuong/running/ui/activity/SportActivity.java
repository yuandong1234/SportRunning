package com.yuong.running.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuong.running.R;
import com.yuong.running.common.activity.BaseActivity;
import com.yuong.running.common.utils.LogUtils;
import com.yuong.running.eventbus.LocationEvent;
import com.yuong.running.service.SportService;
import com.yuong.running.utils.SportPathHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SportActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout llMode;
    private Chronometer cmPasstime;
    private TextView tvMileage;
    private TextView tvSpeed;
    private TextView tvComplete;
    private TextView tvPause;
    private TextView tvContinue;
    private FrameLayout flCountTimer;
    private TextView tvNumberAnim;

    private boolean isBind = false;
    private SportService mService;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            isBind = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i(TAG, "location service connected....");
            SportService.LocalBinder binder = (SportService.LocalBinder) service;
            mService = binder.getService();
            isBind = true;

            //开始定位
            mService.startSport();
        }
    };

    private SportPathHelper mSportLocationHelper;


    @Override
    public int getLayoutId() {
        return R.layout.activity_sport;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (!isBind) {
            bindService();
        }
        mSportLocationHelper = SportPathHelper.getInstance();
        mSportLocationHelper.init();
    }

    @Override
    public void initView() {
        llMode = findViewById(R.id.ll_mode);
        cmPasstime = findViewById(R.id.cm_passtime);
        tvMileage = findViewById(R.id.tvMileage);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvComplete = findViewById(R.id.tv_complete);
        tvPause = findViewById(R.id.tv_pause);
        tvContinue = findViewById(R.id.tv_continue);
        flCountTimer = findViewById(R.id.fl_count_timer);
        tvNumberAnim = findViewById(R.id.tv_number_anim);
    }

    @Override
    public void initListener() {
        llMode.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        tvPause.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mode:
                startActivity(new Intent(SportActivity.this, SportMapActivity.class));
                break;
            case R.id.tv_complete:
                break;
            case R.id.tv_pause:
                break;
        }
    }

    private void bindService() {
        Intent intent = new Intent(this, SportService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    private void unbindService() {
        unbindService(mConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (isBind) {
            unbindService();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent event) {
        LogUtils.i(TAG, "longitude : " + event.getaMapLocation().getLongitude());
        LogUtils.i(TAG, "latitude : " + event.getaMapLocation().getLatitude());
        mSportLocationHelper.updateLocation(event.getaMapLocation());
    }
}
