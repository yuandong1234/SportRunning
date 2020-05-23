package com.yuong.running.eventbus;

import com.amap.api.location.AMapLocation;

/**
 * 刷新地图轨迹
 */
public class UpdatePathEvent {
    private AMapLocation location;

    public UpdatePathEvent(AMapLocation location) {
        this.location = location;
    }

    public AMapLocation getLocation() {
        return location;
    }
}
