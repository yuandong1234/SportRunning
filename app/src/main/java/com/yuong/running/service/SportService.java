package com.yuong.running.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.yuong.running.R;
import com.yuong.running.common.utils.LogUtils;
import com.yuong.running.eventbus.LocationEvent;
import com.yuong.running.ui.activity.SportActivity;
import com.yuong.running.utils.LocationUtils;

import org.greenrobot.eventbus.EventBus;

import androidx.core.app.NotificationCompat;

/**
 * 跑步服务
 */
public class SportService extends Service implements LocationUtils.LocationCallback {
    public static final String TAG = "SportService";
    private final IBinder mBinder = new LocalBinder();
    private LocationUtils mLocationUtils;
    private static final int NOTIFICATION_ID = 100;//通知ID

    public class LocalBinder extends Binder {
        public SportService getService() {
            return SportService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "****************跑步服务创建**************************");
        mLocationUtils = LocationUtils.getInstance();
        mLocationUtils.setLocationCallback(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i(TAG, "****************跑步服务绑定成功**************************");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG, "****************跑步服务销毁**************************");
        mLocationUtils.destroy();
    }

    public void startSport() {
        mLocationUtils.start();
        showNotification();
    }

    public void stopSport() {
        mLocationUtils.stop();
        cancelNotification();
    }


    @Override
    public void onLocation(AMapLocation location) {
        EventBus.getDefault().post(new LocationEvent(location));
    }


    private void showNotification() {
        String id = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            //通知渠道的ID
            id = "channel_01";
            //用户可以看到的通知渠道的名字
            String name = "通知渠道1";
            //用户可看到的通知描述
            String description = "通知渠道描述1";
            //构建NotificationChannel实例
            NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            //配置通知渠道的属性
            notificationChannel.setDescription(description);
            //设置通知出现时的闪光灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //设置通知出现时的震动
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100});
            //在notificationManager中创建通知渠道
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, id);
        //PendingIntent 跳转动作
        Intent intent = new Intent(this, SportActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mBuilder.setSmallIcon(R.mipmap.icon_sport)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_sport))
                .setTicker("开始运动")
                .setContentTitle("跑步运动")
                .setContentText("跑步运动记录你的运动轨迹")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setAutoCancel(false);
        Notification notification = mBuilder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        //FLAG_AUTO_CANCEL(在通知栏上点击此通知后自动清除此通知) FLAG_ONGOING_EVENT(常驻)
//		mNotification.flags = Notification.FLAG_ONGOING_EVENT; //
        // mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        startForeground(NOTIFICATION_ID, notification);
    }

    private void cancelNotification() {
        stopForeground(true);
    }
}
