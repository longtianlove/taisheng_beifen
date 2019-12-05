package com.taisheng.now.map;

import android.graphics.Color;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.taisheng.now.EventManage;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by long on 2017/6/22.
 */

public class HomelocationInstance implements BDLocationListener {

    public static HomelocationInstance instance;

    private HomelocationInstance() {

    }

    public static HomelocationInstance getInstance() {
        if (instance == null) {
            instance = new HomelocationInstance();
        }
        return instance;
    }


    private BaiduMap mBaiduMap;
    private MapView mapView;
    private LocationClient mLocationClient;

    public static double phoneLatitude;
    public static double phoneLongitude;
    public static int radius=10;

    public static String city;

    public void init(MapView mapView) {
        this.mapView = mapView;
        initMap();
        startLocListener(1000);
    }

    private void initMap() {
        mapView.showZoomControls(false);//不显示放大缩小控件
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//只显示普通地图就好
        mBaiduMap.setBuildingsEnabled(true);//设置是否允许楼块效果

//        mBaiduMap.setOnBaseIndoorMapListener(new BaiduMap.OnBaseIndoorMapListener() {
//            @Override
//            public void onBaseIndoorMapMode(boolean b, MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
//                if (b) {
//                    // 进入室内图
//                    // 通过获取回调参数 mapBaseIndoorMapInfo 来获取室内图信息，包含楼层信息，室内ID等
//                } else {
//                    // 移除室内图
//                }
//            }
//        });

//        mBaiduMap.setIndoorEnable(true);//设置是否显示室内图, 默认室内图不显示
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        UiSettings UiSettings =mBaiduMap.getUiSettings();
        UiSettings.setAllGesturesEnabled(true);
        UiSettings.setScrollGesturesEnabled(true);

        initLocation();
    }


    //设置中心
    public void setHomeCenter(){
        LatLng postion = new LatLng(phoneLatitude, phoneLongitude);
//        postion= MapInstance.converterLatLng(postion);
        float f = mBaiduMap.getMaxZoomLevel();//19.0
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(postion, f-2);
        mBaiduMap.animateMapStatus(u);
        setCenter(postion, 300);
    }

    /**
     * 初始化定位
     */
    public void initLocation() {

        // 定位初始化
        mLocationClient = new LocationClient(mapView.getContext().getApplicationContext());
        mLocationClient.registerLocationListener(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setAddrType("all");// 返回的定位结果包含地址信息,(注意如果想获取关于地址的信息，这里必须进行设置)
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);

        // 设置定位方式的优先级。
        // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
    }

    /**
     * 按动画移动到中心点
     *
     * @param centerPoint
     */
    public void setCenter(LatLng centerPoint, int delayMills) {
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(centerPoint);
        mBaiduMap.animateMapStatus(mapStatusUpdate, delayMills);
    }


    /**
     * 开始位置监听
     */
    public void startLocListener(int scan) {
        if (null != mLocationClient && !mLocationClient.isStarted()) {
            mLocationClient.getLocOption().setScanSpan(scan);
            mLocationClient.start();
        }
    }

    /**
     * 停止位置监听
     */
    private void stopLocListener() {
        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }


    /**
     * 重新定位
     */
    public void startPosition() {
        startLocListener(1000);
    }



    private addressParseListener addressListener;

    public void setAddressParseListener(addressParseListener listener) {
        this.addressListener = listener;
    }


    CircleOptions mCircleOptions;
    CircleOptions mCircleOptions1;

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        city=bdLocation.getCity();
        Log.e("longtianlove海拔",bdLocation.getAltitude()+"");
        phoneLatitude = bdLocation.getLatitude();
        phoneLongitude = bdLocation.getLongitude();
        EventManage.bindingLocationChanged event=new EventManage.bindingLocationChanged();
        event.latitude=phoneLatitude;
        event.longitude=phoneLongitude;
        EventBus.getDefault().post(event);
        setHomeCenter();
        mBaiduMap.clear();
        mCircleOptions = new CircleOptions()
                .center(new LatLng(phoneLatitude,phoneLongitude)) // 圆心坐标
                .radius((int) radius) // 半径 单位 米
                .visible(true)
//                .stroke(new Stroke(2, Color.parseColor("#ffffff"))) // 设置边框 Stroke 参数 宽度单位像素默认5px 颜色
                .fillColor(Color.parseColor("#1B2e68AA")); // 设置圆的填充颜色
//        mCircleOptions1 = new CircleOptions()
//                .center(new LatLng(phoneLatitude,phoneLongitude)) // 圆心坐标
//                .radius((int) 2) // 半径 单位 米
//                .visible(true)
////                .stroke(new Stroke(2, Color.parseColor("#ffffff"))) // 设置边框 Stroke 参数 宽度单位像素默认5px 颜色
//                .fillColor(Color.parseColor("#4769e7")); // 设置圆的填充颜色
        mBaiduMap.addOverlay(mCircleOptions);
//        mBaiduMap.addOverlay(mCircleOptions1);
        stopLocListener();
    }


    public void refreshMap(){
        mBaiduMap.clear();
        mCircleOptions = new CircleOptions()
                .center(new LatLng(phoneLatitude,phoneLongitude)) // 圆心坐标
                .radius((int) radius) // 半径 单位 米
                .visible(true)
//                .stroke(new Stroke(2, Color.parseColor("#ffffff"))) // 设置边框 Stroke 参数 宽度单位像素默认5px 颜色
                .fillColor(Color.parseColor("#1B2e68AA")); // 设置圆的填充颜色
//        mCircleOptions1 = new CircleOptions()
//                .center(new LatLng(phoneLatitude,phoneLongitude)) // 圆心坐标
//                .radius((int) 2) // 半径 单位 米
//                .visible(true)
////                .stroke(new Stroke(2, Color.parseColor("#ffffff"))) // 设置边框 Stroke 参数 宽度单位像素默认5px 颜色
//                .fillColor(Color.parseColor("#4769e7")); // 设置圆的填充颜色
        mBaiduMap.addOverlay(mCircleOptions);
    }








    public static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    /**
     * * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换算法 * * 将 BD-09 坐标转换成GCJ-02 坐标 * * @param
     * bd_lat * @param bd_lon * @return
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    public void Destroy(){
        this.mapView.onDestroy();
        this.mBaiduMap.clear();
        this.mBaiduMap=null;
        this.mLocationClient.stop();
        this.mLocationClient=null;
        this.mapView=null;
    }
}
