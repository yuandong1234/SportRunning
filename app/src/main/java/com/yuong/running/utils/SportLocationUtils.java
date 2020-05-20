package com.yuong.running.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yuong.running.bean.PathRecord;

import java.util.List;

/**
 * 运动轨迹数据处理
 */
public class SportLocationUtils {
    private PathRecord mPathRecord;
    private AMapLocation mMapLocation;

    public PathRecord getPathRecord() {
        return mPathRecord;
    }

    private SportLocationUtils() {
    }

    private static class SingletonHolder {
        private static final SportLocationUtils INSTANCE = new SportLocationUtils();
    }

    public static SportLocationUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        mPathRecord = new PathRecord();
    }

    public AMapLocation getMapLocation() {
        return mMapLocation;
    }

    public void updateLocation(AMapLocation location) {
        mMapLocation = location;
        mPathRecord.addpoint(new LatLng(location.getLatitude(), location.getLongitude()));
        //mPathRecord.setDistance(getDistance(mPathRecord.getPathLinePoints()));
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
