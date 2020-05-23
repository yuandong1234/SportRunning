package com.yuong.running.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yuong.running.bean.PathRecord;
import com.yuong.running.eventbus.UpdatePathEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 运动轨迹数据处理
 */
public class SportPathHelper {
    private PathRecord mPathRecord;

    public PathRecord getPathRecord() {
        return mPathRecord;
    }

    private SportPathHelper() {

    }

    private static class SingletonHolder {
        private static final SportPathHelper INSTANCE = new SportPathHelper();
    }

    public static SportPathHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        mPathRecord = new PathRecord();
    }


    public void updateLocation(AMapLocation location) {
        mPathRecord.addpoint(new LatLng(location.getLatitude(), location.getLongitude()));
        //mPathRecord.setDistance(getDistance(mPathRecord.getPathLinePoints()));

        //更新地图轨迹
        EventBus.getDefault().post(new UpdatePathEvent(location));
    }

    //计算距离
    private double getDistance(List<LatLng> list) {
        double distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            LatLng firstLatLng = list.get(i);
            LatLng secondLatLng = list.get(i + 1);
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng, secondLatLng);
            distance = distance + betweenDis;
        }
        return distance;
    }
}
