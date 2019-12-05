package com.taisheng.now.bussiness.user;


import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.HealthInfo;
import com.taisheng.now.bussiness.bean.result.PictureBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by long on 17/4/7.
 */

public class UserInstance {
    private static UserInstance userInstance;

    private UserInstance() {
    }

    public static UserInstance getInstance() {
        if (userInstance == null) {
            userInstance = new UserInstance();
            userInstance.userInfo = new UserInfo();

            userInstance.userInfo.id = SPUtil.getUid();
            userInstance.userInfo.token = SPUtil.getToken();
            userInstance.userInfo.nickName = SPUtil.getNickname();
            userInstance.userInfo.phone = SPUtil.getPhone();
            userInstance.userInfo.userName = SPUtil.getZhanghao();
            userInstance.userInfo.realName = SPUtil.getRealname();
            userInstance.userInfo.age = SPUtil.getAge();
            userInstance.userInfo.sex = SPUtil.getSex();
            userInstance.userInfo.avatar = SPUtil.getAVATAR();


            userInstance.healthInfo = new HealthInfo();
            userInstance.healthInfo.bloodType = SPUtil.getBLOODTYPE();
            userInstance.healthInfo.height = SPUtil.getHEIGHT();
            userInstance.healthInfo.weight = SPUtil.getWEIGHT();

        }
        return userInstance;
    }


//    public String uid;
//
//    public String token;

    public UserInfo userInfo;
    public HealthInfo healthInfo;


//    //获取用户基本信息
//    public void getUserInfo() {
//        //获取用户基本信息
//        ApiUtils.getApiService().getUserInfo(UserInstance.getInstance().getUid(),
//                UserInstance.getInstance().getToken()
//        ).enqueue(new TaiShengCallback<UserBean>() {
//            @Override
//            public void onSuccess(Response<UserBean> response, UserBean message) {
////                HttpCode ret = HttpCode.valueOf(message.status);
////                switch (ret) {
////                    case EC_SUCCESS:
//                        UserInstance.getInstance().saveUserInfo(message);
////                        break;
////                }
////                EventBus.getDefault().post(new EventManage.getUserInfoEvent());
//
//            }
//
//            @Override
//            public void onFail(Call<UserBean> call, Throwable t) {
////                EventBus.getDefault().post(new EventManage.getUserInfoEvent());
//            }
//        });
//
//    }


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
                                                                              UserInstance.getInstance().userInfo.avatar = path;
                                                                              SPUtil.putAVATAR(UserInstance.getInstance().userInfo.avatar);
                                                                              EventBus.getDefault().post(new EventManage.uploadImageSuccess(path));
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


    public void clearUserInfo() {
//        uid = "";
        this.userInfo.id = "";
        SPUtil.putUid("");
        this.userInfo.token = "";
        SPUtil.putToken("");
        this.userInfo.nickName = "";
        SPUtil.putNickname("");
        this.userInfo.phone = "";
        SPUtil.putPhone("");
        this.userInfo.userName = "";
        SPUtil.putZhanghao("");
        this.userInfo.realName = "";
        SPUtil.putRealname("");
        this.userInfo.age = "";
        SPUtil.putAge("");
        this.userInfo.sex = 0;
        SPUtil.putSex(0);
        this.healthInfo.height = "";
        SPUtil.putHEIGHT("");
        userInfo.avatar = "";
        SPUtil.putAVATAR("");

        this.healthInfo.bloodType = "";
        SPUtil.putBLOODTYPE("");
        this.healthInfo.weight = "";
        SPUtil.putWEIGHT("");


    }

    public void saveUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        SPUtil.putUid(userInfo.id);
        SPUtil.putToken(userInfo.token);
        SPUtil.putNickname(userInfo.nickName);
        SPUtil.putZhanghao(userInfo.userName);
        SPUtil.putPhone(userInfo.phone);
        SPUtil.putZhanghao(userInfo.userName);
        SPUtil.putRealname(userInfo.realName);
        SPUtil.putAge(userInfo.age);
        SPUtil.putSex(userInfo.sex);

        SPUtil.putAVATAR(userInfo.avatar);
    }

    public void saveHealInfo(HealthInfo healthInfo) {
        if (healthInfo == null) {
            return;
        }
        this.healthInfo = healthInfo;
        SPUtil.putHEIGHT(healthInfo.height);
        SPUtil.putWEIGHT(healthInfo.weight);
        SPUtil.putBLOODTYPE(healthInfo.bloodType);

        //todo 随后写过敏史
    }

    public String getUid() {
        return userInfo.id;
    }

    public String getToken() {
        return userInfo.token;
    }

    public String getNickname() {
        return userInfo.nickName;
    }

    public String getPhone() {
        return userInfo.phone;
    }

    public String getZhanghao() {
        return userInfo.userName;
    }

    public String getRealname() {
        return userInfo.realName;
    }

    public String getAge() {
        return userInfo.age;
    }

    public String getHeight() {
        return healthInfo.height;
    }

}
