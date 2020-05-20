package com.yuong.running.eventbus;

import com.amap.api.location.AMapLocation;

public class LocationEvent {
  private AMapLocation aMapLocation;

    public LocationEvent(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }
}
