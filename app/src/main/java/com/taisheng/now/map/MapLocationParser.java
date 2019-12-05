package com.taisheng.now.map;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by Administrator on 2017/1/14.
 */

public class MapLocationParser {
    private static GeoCoder geoCoder;

    public static void queryLocationDesc(LatLng loc, final addressParseListener parseListener){
         final OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            // 地理编码查询结果回调函数
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
            }
            // 反地理编码查询结果回调函数
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if(null == parseListener){
                    return;
                }
                parseListener.onAddressparsed(result.getAddress(),result);
                if(geoCoder != null){
                    geoCoder.destroy();
                }
            }
        };
        geoCoder = GeoCoder.newInstance();
        // 设置地理编码检索监听者
        geoCoder.setOnGetGeoCodeResultListener(listener);
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(loc));
    }

}
