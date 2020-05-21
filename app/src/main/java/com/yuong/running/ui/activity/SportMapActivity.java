package com.yuong.running.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.yuong.running.R;
import com.yuong.running.common.activity.BaseActivity;
import com.yuong.running.common.utils.LogUtils;
import com.yuong.running.eventbus.LocationEvent;
import com.yuong.running.utils.LocationUtils;
import com.yuong.running.utils.SportPathHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 地图运动轨迹
 */
public class SportMapActivity extends BaseActivity implements LocationSource {

    private MapView mapView;
    private LinearLayout llMode;

    private AMap aMap;
    private SportPathHelper mSportLocationUtils;
    private LocationUtils mLocationUtils;
    private OnLocationChangedListener mListener;
    private boolean mFirstFix = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_map;
    }

    @Override
    public void initView() {
        mapView = findViewById(R.id.mapView);
        llMode = findViewById(R.id.ll_mode);
    }

    @Override
    public void initListener() {

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mSportLocationUtils = SportPathHelper.getInstance();
        mLocationUtils = LocationUtils.getInstance();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
    }

    private void initMap() {
        aMap = mapView.getMap();
        UiSettings settings = aMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        settings.setZoomControlsEnabled(false);// 设置默认缩放按钮是否显示
        settings.setCompassEnabled(false);// 设置默认指南针是否显示
        settings.setScaleControlsEnabled(true);

        aMap.setLocationSource(this);//设置定位监听

        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_point));// 设置蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // 设置定位的类型为定位模式 ，定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setMyLocationEnabled(true);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mFirstFix = false;
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mapView.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationEvent(LocationEvent event) {
        AMapLocation location = event.getaMapLocation();
        LogUtils.i(TAG, "longitude : " + location.getLongitude());
        LogUtils.i(TAG, "latitude : " + location.getLatitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (!mFirstFix) {
            mFirstFix = true;
            mListener.onLocationChanged(location);// 显示系统小蓝点
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        } else {
            aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
        }
    }
}
