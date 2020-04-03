package com.taisheng.now.evbusbean;

import com.baidu.mapapi.model.LatLng;

public class BaiduSearchAddr {
    private String key;
    private String addr;
    private LatLng pt ;

    public LatLng getPt() {
        return pt;
    }

    public void setPt(LatLng pt) {
        this.pt = pt;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
