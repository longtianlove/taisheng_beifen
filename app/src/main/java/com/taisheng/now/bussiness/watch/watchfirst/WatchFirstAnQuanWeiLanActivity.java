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

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.AnquanweiilanPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.map.AddressAdapter;
import com.taisheng.now.map.HomelocationInstance;
import com.taisheng.now.map.MapLocationParser;
import com.taisheng.now.map.MapPetAtHomeView;
import com.taisheng.now.map.addressParseListener;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.th.j.commonlibrary.utils.LogUtilH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.jessyan.autosize.utils.LogUtils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchFirstAnQuanWeiLanActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.iv_location)
    MapPetAtHomeView ivLocation;
    @BindView(R.id.btn_phone_center)
    ImageView btnPhoneCenter;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.et_fanwei)
    EditText etFanwei;
    @BindView(R.id.rv_addresslist)
    RecyclerView rvAddresslist;

    public static boolean isFirst = true;
    private BaiduMap mBaiduMap;
    private Projection projection;

    public static double longitude;
    public static double latitude;
    public static double touch_longitude;
    public static double touch_latitude;
    public static int wait_search = 0;
    private AddressAdapter adapter;
    TranslateAnimation translateAnimation;
    SuggestionSearch mSuggestionSearch;
    private GeoCoder mSearch;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchfirstanquanweilan);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("安全围栏");
        tvRight.setText("确定");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(latitude > 0)) {
                    ToastUtil.showTost("请设置位置中心");
                    return;
                }
                AnquanweiilanPostBean bean = new AnquanweiilanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
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
                                Uiutils.showToast("设置成功");
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
    }

    @OnClick({R.id.iv_location, R.id.btn_cancel, R.id.btn_phone_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_location:
                break;
            case R.id.btn_phone_center:
                HomelocationInstance.getInstance().startPosition();
                break;
            case R.id.btn_cancel:
                etSearch.setText("");
                rvAddresslist.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                }
                break;
        }
    }

    private void initViews() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = etSearch.getText().toString();
                LogUtilH.e(HomelocationInstance.getInstance().city+"==HomelocationInstance.getInstance().city");
                if ("".equals(searchString)) {
                    return;
                }
                mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
                        .city(HomelocationInstance.getInstance().city)
                        .citylimit(true)
                        .keyword(searchString));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etFanwei.addTextChangedListener(new TextWatcher() {
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
                String fanweiString = etFanwei.getText().toString();
                if ("".equals(fanweiString)) {
                    HomelocationInstance.radius = 10;
                    return;
                }
                HomelocationInstance.radius = Integer.parseInt(fanweiString);

                HomelocationInstance.getInstance().refreshMap();

            }
        });

        rvAddresslist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddressAdapter(this);
        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, AddressAdapter.StateHolder holder, int position) {
               /* HomelocationInstance.getInstance().setCenter(poiInfos.get(position).location, 1000);
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
                }*/
            }
        });
        rvAddresslist.setAdapter(adapter);

//        if(MIUIUtils.isMIUI()){
//            mMapView. setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        mBaiduMap = bmapView.getMap();
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
                ivLocation.startAnimation(translateAnimation);

                projection = mBaiduMap.getProjection();
                int indexy = (int) (ivLocation.getHeight());
                Point aimPoint = new Point(mapStatus.targetScreen.x, mapStatus.targetScreen.y + DensityUtil.dip2px(WatchFirstAnQuanWeiLanActivity.this, 10));
                if (projection == null) {
                    return;
                }
                final LatLng position = projection.fromScreenLocation(aimPoint);
                LogUtilH.e("地图" + mapStatus.targetScreen.x + ":" + mapStatus.targetScreen.y);

                MapLocationParser.queryLocationDesc(position, new addressParseListener() {
                    @Override
                    public void onAddressparsed(String address, ReverseGeoCodeResult result) {
                        LogUtilH.e("位置=" + address);

                    }
                });
                LogUtilH.e("longtianlove=" + position.toString());
                HomelocationInstance.phoneLatitude = position.latitude;
                HomelocationInstance.phoneLongitude = position.longitude;
                touch_latitude = position.latitude;
                touch_longitude = position.longitude;
                HomelocationInstance.getInstance().refreshMap();
            }
        });
        HomelocationInstance.getInstance().init(bmapView);

        initAnim();
        initDatas();

        EventBus.getDefault().register(this);

    }


    public void initAnim() {
        translateAnimation = new TranslateAnimation(0, 0, 0, -50);
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new AnticipateOvershootInterpolator());
        ivLocation.startAnimation(translateAnimation);
    }

    public void initDatas() {
        wait_search = 0;
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                LogUtilH.e("打印转换后的地址" + reverseGeoCodeResult.getAddress());
            }
        });
        //下面是传入对应的经纬度

        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {
                    return;
                    //未找到相关结果
                }
                List<SuggestionResult.SuggestionInfo> lists = res.getAllSuggestions();
                if (lists.size() > 0) {
                    rvAddresslist.setVisibility(View.GONE);
                } else {
                    rvAddresslist.setVisibility(View.VISIBLE);
                }
                SuggestionResult.SuggestionInfo bean = lists.get(0);
                LogUtilH.e(bean.key + "--"
                        + bean.city + "--"
                        + bean.district + "--"
                        + bean.tag + "--"
                        + bean.district + "--"
                        + bean.uid + "-uid-"
                        + bean.address + "--");
                LatLng pt = bean.getPt();
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(pt));
                adapter.setMdatas(lists);


                //获取在线建议检索结果
            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
    }


    //宠物信息更新
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void getActivityInfo(EventManage.bindingLocationChanged event) {
        final LatLng position = new LatLng(event.latitude, event.longitude);
        MapLocationParser.queryLocationDesc(position, new addressParseListener() {
            @Override
            public void onAddressparsed(String address, ReverseGeoCodeResult result) {
                /*poiInfos = (ArrayList<PoiInfo>) result.getPoiList();
                adapter.mdatas = poiInfos;*/
                adapter.notifyDataSetChanged();

            }
        });
        double[] temp = HomelocationInstance.bd09_To_Gcj02(position.latitude, position.longitude);
        latitude = temp[0];
        longitude = temp[1];
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
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
        mSearch.destroy();
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            mBaiduMap = null;
        }
        EventBus.getDefault().unregister(this);
    }


}
