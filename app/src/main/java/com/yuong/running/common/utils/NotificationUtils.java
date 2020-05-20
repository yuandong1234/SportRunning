package com.yuong.running.common.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.yuong.running.R;

import java.util.HashMap;
import java.util.Map;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    private NotificationManager mNotificationManager;
    private Map<Integer, Notification> mNotifications;

    public NotificationUtils(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifications = new HashMap<Integer, Notification>();
    }


    public void showNotification(Context context, int notificationId) {
        if (mNotifications.containsKey(notificationId)) return;
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
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, id);
        //PendingIntent 跳转动作
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);
        mBuilder.setSmallIcon(R.mipmap.icon_sport)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_sport))
                .setTicker("通知来了")
                .setContentTitle("常驻测试")
                .setContentText("使用cancel()方法才可以把我去掉哦")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false);
        Notification notification = mBuilder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE;
        //FLAG_AUTO_CANCEL(在通知栏上点击此通知后自动清除此通知) FLAG_ONGOING_EVENT(常驻)
//		mNotification.flags = Notification.FLAG_ONGOING_EVENT; //
        notification.flags |= Notification.FLAG_NO_CLEAR;//不让手动清除 通知栏常驻
        mNotificationManager.notify(notificationId, notification);
        mNotifications.put(notificationId, notification);
    }

    public void cancelNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
        mNotifications.remove(notificationId);
    }
}
