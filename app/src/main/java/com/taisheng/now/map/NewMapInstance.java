package com.taisheng.now.map;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.SampleApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class NewMapInstance extends BDAbstractLocationListener {


    public static NewMapInstance instance;

    private NewMapInstance() {

    }


    public static NewMapInstance getInstance() {
        if (instance == null) {
            synchronized (NewMapInstance.class) {
                if (instance == null) {
                    instance = new NewMapInstance();
                }
            }
        }
        return instance;
    }

    private MapView mapView;
    public BaiduMap mBaiduMap;

    public void init(MapView mapView) {
        this.mapView = mapView;
        initMap();

    }


    public int LocationMapType = BaiduMap.MAP_TYPE_NORMAL;

    private void initMap() {
        mapView.showZoomControls(false);//不显示放大缩小控件
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(LocationMapType);//只显示普通地图就好
//        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//卫星地图
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
        UiSettings UiSettings = mBaiduMap.getUiSettings();
        UiSettings.setRotateGesturesEnabled(false);//屏蔽旋转
        initLocation();
        initPhoneMarker();
    }

    public static double phoneLatitude;
    public static double phoneLongitude;
    LatLng commonphoneLatlng;
    BitmapDescriptor phonebitmapDescriptor;
    Marker mPhoneMarker;

    Marker startMarker;
    BitmapDescriptor startbitmapDescriptor;

    Marker endMarker;
    BitmapDescriptor endbitmapDescriptor;

    /**
     * 初始化手机位置地图标识
     */
    private void initPhoneMarker() {
        commonphoneLatlng = new LatLng(phoneLatitude, phoneLongitude);
        phonebitmapDescriptor = BitmapDescriptorFactory.fromView(new MapPhoneAvaterView(SampleAppLike.mcontext));
        OverlayOptions options = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(phonebitmapDescriptor)
                .draggable(true)
                .position(commonphoneLatlng)
                .visible(true);
        mPhoneMarker = (Marker) (mBaiduMap.addOverlay(options));

    }

    public void initStartMarker(LatLng latLng) {
        startbitmapDescriptor = BitmapDescriptorFactory.fromView(new StartLineAvaterView(SampleAppLike.mcontext));
        OverlayOptions options = new MarkerOptions()
                .anchor(0.5f, 1f)
                .icon(startbitmapDescriptor)
                .draggable(true)
                .position(latLng)
                .visible(true);
        startMarker = (Marker) (mBaiduMap.addOverlay(options));
    }

    public void initendMarker(LatLng latLng) {
        endbitmapDescriptor = BitmapDescriptorFactory.fromView(new EndLineAvaterView(SampleAppLike.mcontext));
        OverlayOptions options = new MarkerOptions()
                .anchor(0.5f, 1f)
                .icon(endbitmapDescriptor)
                .draggable(true)
                .position(latLng)
                .visible(true);
        endMarker = (Marker) (mBaiduMap.addOverlay(options));
    }

    /**
     * 绘制手机位置
     */
    private void setPhonePos() {
        LatLng postion = new LatLng(phoneLatitude, phoneLongitude);
        float f = mBaiduMap.getMaxZoomLevel();//19.0
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(postion, f - 2);
        mBaiduMap.animateMapStatus(u);
//        if (PetInfoInstance.getInstance().PET_MODE != Constants.PET_STATUS_FIND) {
//            setCenter(postion, 300);
//        }
//        refreshMap();
        try {
            mPhoneMarker.setPosition(postion);
        } catch (Exception E) {

        }
    }


    /**
     * 刷新地图
     */
    public void refreshMap() {
        mBaiduMap.clear();


        addRouteLine(routeList);
        initStartMarker(routeList.get(0));
        initendMarker(routeList.get(routeList.size() - 1));
        initPhoneMarker();


    }


    public LocationClient mLocationClient = null;

    /**
     * 初始化LocationClient类
     */
    public void initLocation() {

        // 定位初始化
        mLocationClient = new LocationClient(SampleAppLike.mcontext);
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


//        startLoc();
    }


    /**
     * 开始定位
     */
    public void startLoc() {
        startLocListener(1000);
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
    public void stopLocListener() {
        if (null != mLocationClient && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        int errorCode = location.getLocType();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明


        phoneLatitude = location.getLatitude();
        phoneLongitude = location.getLongitude();

//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        float f = mBaiduMap.getMaxZoomLevel();//19.0
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, f - 2);
//        mBaiduMap.animateMapStatus(u);
//        setCenter(latLng, 300);
        stopLocListener();

        setPhonePos();


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


//    //纠偏
//    public LatLng jiupian(){
//        String url = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=113.540124&y=23.517846";
//        JSONObject json = getAllEmployee(url);
//        //将经纬度解码后进行打印
//        String latitude = decode(json.getString("x"));
//        String longitude = decode(json.getString("y"));
//
//    }


//    /**
//     * Java后台访问url链接，返回JSON格式的数据
//     * @return
//     */
//    public static JSONObject getAllEmployee(String url) {
//        try {
//            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(url);
//            ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
//                // 成功调用连接后，对返回数据进行的操作
//                public JSONObject handleResponse(final HttpResponse response)
//                        throws ClientProtocolException, IOException {
//                    int status = response.getStatusLine().getStatusCode();
//                    if (status >= 200 && status < 300) {
//                        // 获得调用成功后 返回的数据
//                        HttpEntity entity = response.getEntity();
//                        if (null != entity) {
//                            String result = EntityUtils.toString(entity);
//                            // 根据字符串生成JSON对象
//                            JSONObject resultObj = JSONObject.fromObject(result);
//                            return resultObj;
//                        } else {
//                            return null;
//                        }
//                    } else {
//                        throw new ClientProtocolException("Unexpected response status: " + status);
//                    }
//                }
//            };
//            // 返回的json对象
//            JSONObject responseBody = httpclient.execute(httpPost, responseHandler);
//            return responseBody;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    /**
//     * Base64解码
//     * @param str
//     * @return
//     */
//    public static String decode(String str) {
//        byte[] bt = null;
//        String s= "";
//        try {
//            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
//            bt = decoder.decodeBuffer(str);
//            s = new String(bt, "GB2312");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return s;
//    }


//    //    将GPS设备采集的原始GPS坐标转换成百度坐标
//    //初始化坐标转换工具类，指定源坐标类型和坐标数据
//    // sourceLatLng待转换坐标
//    public static LatLng converterLatLng(LatLng sourceLatLng) {
//        CoordinateConverter converter = new CoordinateConverter()
//                .from(CoordinateConverter.CoordType.GPS)
//                .coord(sourceLatLng);
//
////desLatLng 转换后的坐标
//        LatLng desLatLng = converter.convert();
//        return desLatLng;
//    }


    //将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
    public static LatLng converterLatLng(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }

    public List<LatLng> routeList;

    public void addRouteLine(List<LatLng> routeList) {
        this.routeList = routeList;
//        mBaiduMap.clear();
        // 百度最多支持10000个点连线
        if (routeList.size() > 10000) {
            routeList = routeList.subList(0, 10000);
        }
        mBaiduMap.addOverlay(new PolylineOptions().width(10).color(0xFF1694FF)
                .points(routeList));
        moveToLocation(routeList.get(routeList.size() / 2), true);

    }


    /**
     * 移动到指定位置 并缩放
     *
     * @param latlng
     */
    private void moveToLocation(LatLng latlng, boolean flag) {
//        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlng);// 设置新的中心点
//        mBaiduMap.animateMapStatus(u);


        float f = mBaiduMap.getMaxZoomLevel();//19.0
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latlng, f - 2);
        mBaiduMap.animateMapStatus(u);
    }

}
