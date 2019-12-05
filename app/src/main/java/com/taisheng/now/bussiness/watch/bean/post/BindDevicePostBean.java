package com.taisheng.now.bussiness.watch.bean.post;

import com.taisheng.now.bussiness.bean.post.BasePostBean;

public class BindDevicePostBean extends BasePostBean {
    //绑定设备信息
    public String deviceType;//1 手表
    public String deviceId; //设备号。通过扫码获取
    public String deviceNickName;//设备昵称
    public String relationShip;//  设备与APP（亲属）关系 必传
    //设备使用人基础信息
    public String headUrl;//头像地址  设备头像 必传
    public String realName;//设备使用人姓名 必传
    public String sex;//性别 非必传
    public Integer age;//年龄 非必传
    public String idcard;//身份证号  必传
    public String phoneNumber;//手机号 必传 设备使用人手机号
}
