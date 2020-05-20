package com.yuong.running.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.yuong.running.utils.SportLocationUtils;

/**
 * 地图运动轨迹
 */
public class SportMapActivity extends BaseActivity {

    private MapView mapView;
    private LinearLayout llMode;
    private TextView tvComplete;
    private TextView tvPause;
    private TextView tvContinue;

    private AMap aMap;
    private SportLocationUtils mSportLocationUtils;

    private LocationSource locationSource = new LocationSource() {
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            LogUtils.e("**************************************");
//            AMapLocation location = mSportLocationUtils.getMapLocation();
//            onLocationChangedListener.onLocationChanged(location);
//            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
        }

        @Override
        public void deactivate() {

        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_map;
    }

    @Override
    public void initView() {
        mapView = findViewById(R.id.mapView);
        llMode = findViewById(R.id.ll_mode);
        tvComplete = findViewById(R.id.tv_complete);
        tvPause = findViewById(R.id.tv_pause);
        tvContinue = findViewById(R.id.tv_continue);
    }

    @Override
    public void initListener() {

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        mSportLocationUtils = SportLocationUtils.getInstance();
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
        setPointIcon();
        //aMap.setLocationSource(locationSource);// 设置定位监听
        aMap.setMyLocationEnabled(true);
    }

    private void setPointIcon() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_location_point));// 设置小蓝点的图标
        // 设置定位的类型为定位模式 ，定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle.interval(2000);//设置发起定位请求的时间间隔
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，true 显示，false不显示
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
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
        mapView.onDestroy();
    }
}
