package com.pg.pgguardkiki.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroupOverlay;
import android.view.Window;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.pg.pgguardkiki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzj on 10/4/16.
 */
public class MapDemo  extends Activity{
    private static final String ClassName = "MapDemo";
    private LocationClient mLocationClient;
    private BDLocationListener mBDLocationListener;
    private BaiduMap mBaiduMap;
    private MapView mapView;
    private double start1 = 39.958271;
    private double start2 = 116.42652;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ClassName, "=onCreate=11==");
        SDKInitializer.initialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mapdemo);

        mapView = (MapView) findViewById(R.id.mapView);

        mBaiduMap = mapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        mBaiduMap.setTrafficEnabled(true);

        Log.d(ClassName, "=onCreate=22==");
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        mBDLocationListener = new MyBDLocationListener();
        // 注册监听
        mLocationClient.registerLocationListener(mBDLocationListener);

        getLocation(mapView);

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param point 点击的地理坐标
             */
            public void onMapClick(LatLng point) {
                Log.d(ClassName, "=OnMapClickListener=point.latitude==" + point.latitude);
                Log.d(ClassName, "=OnMapClickListener=point.longitude==" + point.longitude);
//                ViewGroupOverlay vgos = mapView.getOverlay();
//                vgos.remove();
            }

            /**
             * 地图内 Poi 单击事件回调函数
             * @param arg0 点击的 poi 信息
             */
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                Log.d(ClassName, "=OnMapClickListener=arg0.getName()==" + arg0.getName());
                Log.d(ClassName, "=OnMapClickListener=arg0.getPosition()==" + arg0.getPosition());
                return false;
            }
        });
        DrawLines();
    }

    public void  DrawLines(){

        //1 point 40.1443  116.649957
        //2 point 40.1413  116.651957



        double startlats=40.1443;
        double startlongs=116.649957;

        double rulats=40.1443;
        double rplongs=116.651957;

        double ldlats=40.1413;
        double ldlongs=116.651957;

        double latline=40.1413;
        double lngline=116.649957;

        LatLng p1 = new LatLng(startlats, startlongs);
        LatLng p2 = new LatLng(rulats, rplongs);
        LatLng p3 = new LatLng(ldlats, ldlongs);
        LatLng p4 = new LatLng(latline, lngline);

        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p1);
        OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
        /*
         * 调用Distance方法获取两点间x,y轴之间的距离
        */
        double cc= Distance(startlats,  startlongs,latline,lngline);
        Log.d(ClassName, "=OnMapClickListener=cc=="+cc);
    }

        /** 获得所在位置经纬度及详细地址 */
    public void getLocation(View view) {
        // 声明定位参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式 高精度
        option.setCoorType("bd09ll");// 设置返回定位结果是百度经纬度 默认gcj02
        option.setScanSpan(5000);// 设置发起定位请求的时间间隔 单位ms
        option.setIsNeedAddress(true);// 设置定位结果包含地址信息
        option.setNeedDeviceDirect(true);// 设置定位结果包含手机机头 的方向
        // 设置定位参数
        mLocationClient.setLocOption(option);
        // 启动定位
        mLocationClient.start();

    }

    private class MyBDLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 非空判断
            if (location != null) {
                List<LatLng> points = new ArrayList<LatLng>();
                LatLng p1 = new LatLng(start1, start2);
                start1 = start1 + 0.01;
                start2 = start2 + 0.01;
                LatLng p2 = new LatLng(start1, start2);
                points.add(p1);
                points.add(p2);
                OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).points(points);
                mBaiduMap.addOverlay(ooPolyline);
                // 根据BDLocation 对象获得经纬度以及详细地址信息

//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                String address = location.getAddrStr();
//
//                MyLocationData locData = new MyLocationData.Builder()
//                        .accuracy(100)
//                                // 此处设置开发者获取到的方向信息，顺时针0-360
//                        .direction(90.0f)
//                        .latitude(latitude)
//                        .longitude(longitude).build();
//                float f = mBaiduMap.getMaxZoomLevel();//19.0 最小比例尺
//                //float m = mBaiduMap.getMinZoomLevel();//3.0 最大比例尺
//                mBaiduMap.setMyLocationData(locData);
//                mBaiduMap.setMyLocationEnabled(true);
//                LatLng ll = new LatLng(latitude,longitude);
//                //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll,f);
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 2);//设置缩放比例
//                mBaiduMap.animateMapStatus(u);
//
//                // 开发者可根据自己实际的业务需求，利用标注覆盖物，在地图指定的位置上添加标注信息。具体实现方法如下：
//                //定义Maker坐标点
//                LatLng point = new LatLng(latitude, longitude);
//
//
//                //构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.mipmap.tubiao);
//                //构建MarkerOption，用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions()
//                        .position(point)
//                        .icon(bitmap);
//                //在地图上添加Marker，并显示
//                mBaiduMap.addOverlay(option);
//
//
//                //文字，在地图中也是一种覆盖物，开发者可利用相关的接口，快速实现在地图上书写文字的需求。实现方式如下：
//                //定义文字所显示的坐标点
//                LatLng llText = new LatLng(latitude, longitude);
//                //构建文字Option对象，用于在地图上添加文字
//                OverlayOptions textOption = new TextOptions()
//                        .bgColor(0xAAFFFF00)
//                        .fontSize(28)
//                        .fontColor(0xFFFF00FF)
//                        .text("1111111111111")
//                        .rotate(0)
//                        .position(llText);
//                //在地图上添加该文字对象并显示
//                mBaiduMap.addOverlay(textOption);
//
//                Log.i(ClassName, "address:" + address + " latitude:" + latitude
//                        + " longitude:" + longitude + "---");
//                if (mLocationClient.isStarted()) {
//                    // 获得位置之后停止定位
//                    mLocationClient.stop();
//                }
            }
        }
    }

    public Double Distance(double lat1, double lng1,double lat2, double lng2) {
        Double R=6370996.81;  //地球的半径
        /*
        * 获取两点间x,y轴之间的距离
        */
        Double x = (lng2 - lng1)*Math.PI*R*Math.cos(((lat1+lat2)/2)*Math.PI/180)/180;
        Double y = (lat2 - lat1)*Math.PI*R/180;
        Double distance = Math.hypot(x, y);   //得到两点之间的直线距离
        return   distance;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
