package com.taisheng.now.map;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by Administrator on 2017/1/14.
 */

public interface addressParseListener {
    void onAddressparsed(String address, ReverseGeoCodeResult result);
}
