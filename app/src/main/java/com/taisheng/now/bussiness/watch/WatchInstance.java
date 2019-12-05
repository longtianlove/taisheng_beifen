package com.taisheng.now.bussiness.watch;

import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.result.PictureBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongLIstBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class WatchInstance {
    public boolean isWtch = false;

    private static WatchInstance watchInstance;

    private WatchInstance() {
    }

    public static WatchInstance getInstance() {
        if (watchInstance == null) {
            watchInstance = new WatchInstance();
        }
        return watchInstance;
    }


    public String preDeviceNumber = "";


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


    public ArrayList<NaozhongLIstBean> mDataNaoZhong=new ArrayList<>();
    public NaozhongLIstBean naozhongLIstBean=new NaozhongLIstBean();


    //上传头像信息
    public void uploadImage(final String path) {
        try {

            //把Bitmap保存到sd卡中
            File fImage = new File(path);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fImage);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", fImage.getName(), requestFile);
            ApiUtils.getApiService().uploadLogo(body).enqueue(new TaiShengCallback<BaseBean<PictureBean>>() {

                                                                  @Override
                                                                  public void onSuccess(Response<BaseBean<PictureBean>> response, BaseBean<PictureBean> message) {
                                                                      switch (message.code) {
                                                                          case Constants.HTTP_SUCCESS:
                                                                              String path = message.result.path;
                                                                              headUrl = path;
                                                                              SPUtil.putWatchAVATAR(headUrl);
                                                                              EventBus.getDefault().post(new EventManage.uploadWatchImageSuccess(path));
                                                                              break;
                                                                      }


                                                                  }

                                                                  @Override
                                                                  public void onFail(Call<BaseBean<PictureBean>> call, Throwable t) {

                                                                  }
                                                              }
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String watchBpxyHigh;
    public String watchBpxyLow;
    public String stepNum;
    public String watchHeart;



    public String watchSilencetimeSwitch;



    public int temp_bpxyHighMax;//血压
    public int temp_bpxyHighMin;
    public int temp_bpxyLowMax;
    public int temp_bpxyLowMin;
    public int temp_bpxyPressureDifferenceMax;//脉压差
    public int temp_bpxyPressureDifferenceMin;
    public int temp_heartNumMax;
    public int temp_heartNumMin;

}
