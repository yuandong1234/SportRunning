package com.yuong.running.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yuong.running.MyApplication;
import com.yuong.running.common.utils.LogUtils;

public class LocationUtils {
    private static final String TAG = LocationUtils.class.getSimpleName();
    private static final int LOCATION_INTERVAL = 4 * 1000;// //定位的时间间隔，单位是毫秒
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationCallback mLocationCallback;

    public void setLocationCallback(LocationCallback callback) {
        this.mLocationCallback = callback;
    }

    private LocationUtils() {
        init();
    }

    private static class SingletonHolder {
        private static final LocationUtils INSTANCE = new LocationUtils();
    }

    public static LocationUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private void init() {
        mLocationClient = new AMapLocationClient(MyApplication.getInstance());
        //设置定位属性
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mLocationOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mLocationOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mLocationOption.setInterval(LOCATION_INTERVAL);//可选，设置定位间隔。默认为2秒
        mLocationOption.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
        mLocationOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mLocationOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mLocationOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mLocationOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mLocationOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mLocationOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.ZH);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
//        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);

        // 设置定位监听
        mLocationClient.setLocationListener(aMapLocationListener);
    }

    /**
     * 定位结果回调
     *
     * @param aMapLocation 位置信息类
     */
    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            LocationLogUtils.log(aMapLocation);
            if (null == aMapLocation)
                return;
            if (aMapLocation.getErrorCode() == 0) {
                if (mLocationCallback != null) {
                    mLocationCallback.onLocation(aMapLocation);
                }
            }
        }
    };

    /**
     * 开始定位
     */
    public void start() {
        LogUtils.i(TAG, "****************开启高德定位**************************");
        if (mLocationClient == null) init();

        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stop() {
        LogUtils.i(TAG, "****************结束高德定位**************************");
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    public void destroy() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.unRegisterLocationListener(aMapLocationListener);
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    public interface LocationCallback {
        void onLocation(AMapLocation location);
    }
}
