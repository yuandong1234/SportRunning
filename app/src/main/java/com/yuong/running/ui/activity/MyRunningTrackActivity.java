package com.yuong.running.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.yuong.running.R;
import com.yuong.running.common.activity.BaseActivity;
import com.yuong.running.utils.PathSmoothTool;
import com.yuong.running.utils.TraceAsset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyRunningTrackActivity extends BaseActivity implements AMap.OnMapLoadedListener {
    private MapView mMapView = null;
    private AMap amap = null;
    private List<LatLng> mOriginList = new ArrayList<LatLng>();
    private Polyline mOriginPolyline, mkalmanPolyline;
    private PathSmoothTool mpathSmoothTool;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_running_track;
    }


    @Override
    public void initView() {
        mMapView = (MapView) findViewById(R.id.map);
        amap = mMapView.getMap();
    }


    @Override
    public void initListener() {
        amap.setOnMapLoadedListener(this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mpathSmoothTool = new PathSmoothTool();
        mpathSmoothTool.setIntensity(4);
        addLocpath();
    }


    //在地图上添加本地轨迹数据，并处理
    private void addLocpath() {
        mOriginList = TraceAsset.parseLocationsData(this.getAssets(),
                "traceRecord" + File.separator + "AMapTrace2.csv");
        if (mOriginList != null && mOriginList.size() > 0) {
            mOriginPolyline = amap.addPolyline(new PolylineOptions().addAll(mOriginList).color(Color.GREEN));
//            amap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mOriginList), 300));
            amap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(getBounds(mOriginList), 200,200,100,900));
//            amap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOriginList.get(0),15));
        }
        pathOptimize(mOriginList);
    }

    //轨迹平滑优化
    public List<LatLng> pathOptimize(List<LatLng> originlist) {
        List<LatLng> pathoptimizeList = mpathSmoothTool.pathOptimize(originlist);
        mkalmanPolyline = amap.addPolyline(new PolylineOptions().addAll(pathoptimizeList).color(Color.parseColor("#FFC125")));
        return pathoptimizeList;
    }

    @Override
    public void onMapLoaded() {
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    private LatLngBounds getBounds(List<LatLng> pointlist) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (pointlist == null) {
            return b.build();
        }
        for (int i = 0; i < pointlist.size(); i++) {
            b.include(pointlist.get(i));
        }
        return b.build();
    }


//    private void temp(){
//        LatLngBounds.Builder newbounds = new LatLngBounds.Builder();
//        for (int i = 0; i < trajectorylist.size(); i++) {//trajectorylist为轨迹集合
//            newbounds.include(trajectorylist.get(i));
//        }
//        amap.animateCamera(CameraUpdateFactory.newLatLngBounds(newbounds.build(), 15));//第二个参数为四周留空宽度
//    }
}
