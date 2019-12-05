package com.taisheng.now.bussiness.watch.watchfirst;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.AnquanweiilanPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.map.AddressAdapter;
import com.taisheng.now.map.HomelocationInstance;
import com.taisheng.now.map.MapLocationParser;
import com.taisheng.now.map.NewMapInstance;
import com.taisheng.now.map.addressParseListener;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchFirstAnQuanWeiLanActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;
    View tv_queding;
    View iv_dingwei;

    private MapView mMapView = null;


    public static boolean isFirst = true;


    View btn_go_back;
    TextView tv_next;

    EditText et_search;
    View btn_cancel;

    EditText et_fanwei;

    //    private MapView mMapView;
    private RecyclerView rv_addresslist;
    private View ll_noname_address;
    private TextView tv_location;
    private View iv_selected;
    private BaiduMap mBaiduMap;
    private Projection projection;
    private View btn_phone_center;
    private View iv_location;

    public static double longitude;
    public static double latitude;
    public static double touch_longitude;
    public static double touch_latitude;

    AddressAdapter adapter;
    ArrayList<PoiInfo> poiInfos;


    TranslateAnimation translateAnimation;

    PoiSearch mPoiSearch;
    PoiCitySearchOption citySearchOption;
    PoiNearbySearchOption nearbySearchOption;
    SuggestionSearch mSuggestionSearch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchfirstanquanweilan);
        initView();

    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_queding = findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!(latitude > 0)) {
                    ToastUtil.showTost("请设置位置中心");
                    return;
                }
                AnquanweiilanPostBean bean = new AnquanweiilanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.clientId = WatchInstance.getInstance().deviceId;
                bean.fenceRadius = HomelocationInstance.radius;
                bean.latitude = latitude + "";
                bean.latitudeSign = "N";
                bean.longitude = longitude + "";
                bean.longitudeSign = "E";
                bean.status = "1";
                ApiUtils.getApiService().addwatchElectronicFence(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("设置成功");
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


            }
        });
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


//                    mPoiSearch.searchInCity((citySearchOption)
//                            .city(HomelocationInstance.getInstance().city)
//                            .keyword(searchString)
//                            .pageNum(10));
//                wait_search = 0;
//                poiInfos.clear();
//                if (HomelocationInstance.getInstance().city != null) {
//                    mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                            .keyword(searchString)
//                            .city(HomelocationInstance.getInstance().city));
//                }
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = et_search.getText().toString();
                if ("".equals(searchString)) {
                    return;
                }
                mPoiSearch.searchInCity((citySearchOption)
                        .city(HomelocationInstance.getInstance().city)
                        .keyword(searchString)
                        .pageNum(10));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_fanwei = findViewById(R.id.et_fanwei);
        et_fanwei.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Integer.parseInt(s.toString()) > 3000) {
                    ToastUtil.showAtCenter("不能超过3000米");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String fanweiString = et_fanwei.getText().toString();
                if ("".equals(fanweiString)) {
                    HomelocationInstance.radius = 10;
                    return;
                }
                HomelocationInstance.radius = Integer.parseInt(fanweiString);

                HomelocationInstance.getInstance().refreshMap();

            }
        });


        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
                rv_addresslist.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

            }
        });

        iv_location = findViewById(R.id.iv_location);
        ll_noname_address = findViewById(R.id.ll_noname_address);
        ll_noname_address.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomelocationInstance.getInstance().setCenter(new LatLng(touch_latitude, touch_longitude), 1000);
                latitude = touch_latitude;
                longitude = touch_longitude;
                iv_selected.setVisibility(View.VISIBLE);
                adapter.mposition = -1;
                adapter.notifyDataSetChanged();
            }
        });
        tv_location = (TextView) findViewById(R.id.tv_location);
        iv_selected = findViewById(R.id.iv_selected);
        rv_addresslist = (RecyclerView) findViewById(R.id.rv_addresslist);
        rv_addresslist.setLayoutManager(new LinearLayoutManager(this));
        poiInfos = new ArrayList<>();
        adapter = new AddressAdapter(this, poiInfos);
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, AddressAdapter.StateHolder holder, int position) {
                HomelocationInstance.getInstance().setCenter(poiInfos.get(position).location, 1000);
                HomelocationInstance.getInstance().refreshMap();
                if (position != -1) {
                    iv_selected.setVisibility(View.GONE);
                }
                et_search.setText(poiInfos.get(position).name);
                rv_addresslist.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (poiInfos != null && poiInfos.size() > position) {
                    PoiInfo bean = poiInfos.get(position);
                    double[] temp = HomelocationInstance.bd09_To_Gcj02(bean.location.latitude, bean.location.longitude);
//                    HomelocationInstance.getInstance().setCenter(poiInfos.get(0).location, 1000);
                    latitude = temp[0];
                    longitude = temp[1];
                }


            }
        });
        rv_addresslist.setAdapter(adapter);


        btn_phone_center = findViewById(R.id.btn_phone_center);
        btn_phone_center.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HomelocationInstance.getInstance().startPosition();
                Log.e("longtianlove", "图标" + iv_location.getX() + ":" + iv_location.getY() + ":" + iv_location.getWidth() + ":" + iv_location.getHeight());

            }
        });
        mMapView = (MapView) findViewById(R.id.bmapView);
//        if(MIUIUtils.isMIUI()){
//            mMapView. setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        mBaiduMap = mMapView.getMap();
//        UiSettings UiSettings =mBaiduMap.getUiSettings();
//        UiSettings.setRotateGesturesEnabled(true);//打开旋转
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                iv_location.startAnimation(translateAnimation);

                projection = mBaiduMap.getProjection();
                int indexy = (int) (iv_location.getHeight());
                Point aimPoint = new Point(mapStatus.targetScreen.x, mapStatus.targetScreen.y + DensityUtil.dip2px(WatchFirstAnQuanWeiLanActivity.this, 10));
                if(projection==null){
                    return;
                }
                final LatLng position = projection.fromScreenLocation(aimPoint);
                Log.e("longtianlove", "地图" + mapStatus.targetScreen.x + ":" + mapStatus.targetScreen.y);

                MapLocationParser.queryLocationDesc(position, new addressParseListener() {
                    @Override
                    public void onAddressparsed(String address, ReverseGeoCodeResult result) {
//                        Log.e("longtianlove","位置"+address);
                        tv_location.setText(address);
                        poiInfos = (ArrayList<PoiInfo>) result.getPoiList();
                        adapter.mdatas = poiInfos;
                        adapter.notifyDataSetChanged();
//                        if(nearbySearchOption==null||position==null){
//                            return;
//                        }
//                        mPoiSearch.searchNearby((nearbySearchOption)
//                                .location(position)
//                                .keyword("")
//                                .radius(2000)
//                                .pageNum(10));

//                        String[] tempStrings = address.split("市");
//                        if (tempStrings.length < 2) {
//                            return;
//                        }
//                        String temp = address.split("市")[1];
//                        if (temp.length() > 2) {
//                            temp = temp.substring(0, 2);
//                        }

//                        mPoiSearch.searchInCity((citySearchOption)
//                                .city(HomelocationInstance.getInstance().city)
//                                .keyword(temp)
//                                .pageNum(10));
//                        mGeoSearch. geocode(new GeoCodeOption()
//                                .city(HomelocationInstance.getInstance().city)
//                                .address(address));

                    }
                });

                Log.e("longtianlove", position.toString());
                HomelocationInstance.phoneLatitude = position.latitude;
                HomelocationInstance.phoneLongitude = position.longitude;
                touch_latitude = position.latitude;
                touch_longitude = position.longitude;
                HomelocationInstance.getInstance().refreshMap();
                if (iv_selected.getVisibility() == View.VISIBLE) {
                    double[] temp = HomelocationInstance.bd09_To_Gcj02(position.latitude, position.longitude);
                    latitude = temp[0];
                    longitude = temp[1];
                }
            }
        });
        HomelocationInstance.getInstance().init(mMapView);

        initAnim();
        initData();

        EventBus.getDefault().register(this);

//        //获取地图控件引用
//        mMapView = (MapView) findViewById(R.id.bmapView);
//        NewMapInstance.getInstance().init(mMapView);
//
//        iv_dingwei = findViewById(R.id.iv_dingwei);
//        iv_dingwei.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                NewMapInstance.getInstance().startLoc();
//            }
//        });

    }


    public void initAnim() {
        translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        iv_location.startAnimation(translateAnimation);
    }

    public void initData() {
        mPoiSearch = PoiSearch.newInstance();
        wait_search = 0;

        citySearchOption = new PoiCitySearchOption();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            public void onGetPoiResult(PoiResult result) {
                //获取POI检索结果
                poiInfos = (ArrayList<PoiInfo>) result.getAllPoi();
                adapter.mdatas = poiInfos;
                if (adapter.mdatas != null && adapter.mdatas.size() > 0) {
                    rv_addresslist.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                } else if (adapter.mdatas != null && adapter.mdatas.size() == 0) {
                    adapter.notifyDataSetChanged();
                    rv_addresslist.setVisibility(View.GONE);
                }


//                if(poiInfos!=null&&poiInfos.size()>1) {
//                    PoiInfo bean = poiInfos.get(0);
//                    double[] temp = HomelocationInstance.bd09_To_Gcj02(bean.location.latitude, bean.location.longitude);
////                    HomelocationInstance.getInstance().setCenter(poiInfos.get(0).location, 1000);
//                    latitude = temp[0];
//                    longitude = temp[1];
//                }

            }

            public void onGetPoiDetailResult(PoiDetailResult result) {
                //获取Place详情页检索结果
                wait_search--;
                //获取Place详情页检索结果
                if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //详情检索失败
                    // result.error请参考SearchResult.ERRORNO
                } else {
                    //检索成功
                    //获取Place详情页检索结果
                    PoiInfo poiInfoBean = new PoiInfo();
                    poiInfoBean.name = result.name;
                    poiInfoBean.address = result.address;
                    poiInfoBean.location = result.location;
                    poiInfos.add(poiInfoBean);

                }
                if (wait_search == 0) {
                    adapter.mdatas = poiInfos;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);


//        nearbySearchOption = new PoiNearbySearchOption();


        mSuggestionSearch = SuggestionSearch.newInstance();
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return;
                    //未找到相关结果
                }
                List<SuggestionResult.SuggestionInfo> lists = res.getAllSuggestions();
                for (SuggestionResult.SuggestionInfo bean : lists) {
                    if (bean.pt != null) {
                        //uid是POI检索中获取的POI ID信息
                        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(bean.uid));
                        wait_search++;
                    }

                }


                //获取在线建议检索结果
            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);

//        mGeoSearch= GeoCoder.newInstance();
//        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
//            public void onGetGeoCodeResult(GeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    //没有检索到结果
//                }
//                //获取地理编码结果
//
//            }
//
//            @Override
//            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//                    //没有找到检索结果
//                }
//                //获取反向地理编码结果
//            }
//        };
//        mGeoSearch.setOnGetGeoCodeResultListener(listener);

    }

    public static int wait_search = 0;


    //宠物信息更新
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void getActivityInfo(EventManage.bindingLocationChanged event) {
        final LatLng position = new LatLng(event.latitude, event.longitude);
        MapLocationParser.queryLocationDesc(position, new addressParseListener() {
            @Override
            public void onAddressparsed(String address, ReverseGeoCodeResult result) {
//                        Log.e("longtianlove","位置"+address);
                tv_location.setText(address);
                poiInfos = (ArrayList<PoiInfo>) result.getPoiList();
                adapter.mdatas = poiInfos;
                adapter.notifyDataSetChanged();
//                mGeoSearch. geocode(new GeoCodeOption()
//                        .city(HomelocationInstance.getInstance().city)
//                        .address(address));
//                if(nearbySearchOption==null||position==null){
//                    return;
//                }
//                mPoiSearch.searchNearby((nearbySearchOption)
//                        .location(position)
//                        .keyword("")
//                        .radius(2000)
//                        .pageNum(10));
//                String[] tempStrings = address.split("市");
//                if (tempStrings.length < 2) {
//                    return;
//                }
//                String temp = address.split("市")[1];
//                if (temp.length() > 2) {
//                    temp = temp.substring(0, 2);
//                }

//                mPoiSearch.searchInCity((citySearchOption)
//                        .city(HomelocationInstance.getInstance().city)
//                        .keyword(temp)
//                        .pageNum(10));
            }
        });
        double[] temp = HomelocationInstance.bd09_To_Gcj02(position.latitude, position.longitude);
        latitude = temp[0];
        longitude = temp[1];
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        Log.e("longtianlove-point","width:"+(mMapView.getWidth() / 2)+"height:"+mMapView.getHeight() /2);
        if (isFirst) {
            HomelocationInstance.getInstance().setHomeCenter();
            isFirst = false;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HomelocationInstance.getInstance().Destroy();
        mSuggestionSearch.destroy();
        mPoiSearch.destroy();
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            mBaiduMap = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
