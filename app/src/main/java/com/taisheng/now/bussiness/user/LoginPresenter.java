package com.taisheng.now.bussiness.user;

import android.annotation.SuppressLint;
import android.widget.Toast;


import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.CaptchaPostBean;
import com.taisheng.now.bussiness.bean.post.LoginPostBean;
import com.taisheng.now.bussiness.bean.result.LoginResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by long on 17/4/7.
 */
@SuppressLint("WrongConstant")
public class LoginPresenter {
    private WeakReference<LoginView> loginView;
//    private WeakReference<LogoutView> logoutView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = new WeakReference<>(loginView);
    }

//    public LoginPresenter(LogoutView logoutView) {
//        this.logoutView = new WeakReference<LogoutView>(logoutView);
//    }
//    public LoginPresenter(){
//
//    }

    /**
     * 获取验证码
     *
     * @param phone
     */
    public void getVerifyCode(String phone) {
        final LoginView tloginView = loginView.get();
//
        if (tloginView != null) {
            tloginView.showDialog();
            CaptchaPostBean bean = new CaptchaPostBean();
            bean.phoneNumber = phone;
            ApiUtils.getApiService().appAcquireVerifyCode(bean).enqueue(new TaiShengCallback<BaseBean>() {
                @Override
                public void onSuccess(Response<BaseBean> response, BaseBean message) {
                    switch (message.code) {
                        case Constants.HTTP_SUCCESS:
                            if (tloginView != null) {
                                tloginView.getVerifyNextTime(60);
                            }
                            break;
//                        case EC_FREQ_LIMIT:
//                            ToastUtil.showTost("发送验证码频率多");
//                            break;
                        default:
                            ToastUtil.showTost("网络出错");

                    }
                    tloginView.dismissDialog();
                }

                @Override
                public void onFail(Call<BaseBean> call, Throwable t) {
                    Toast.makeText(SampleAppLike.mcontext, "验证码发送失败", Toast.LENGTH_LONG).show();
                    tloginView.dismissDialog();
                }
            });

//            ApiUtils.getApiService().getVerifyCode(phone, deviceType).enqueue(new XMQCallback<MessageBean>() {
//                @Override
//                public void onSuccess(Response<MessageBean> response, MessageBean message) {
//                    HttpCode ret = HttpCode.valueOf(message.status);
//                    switch (ret) {
//                        case EC_SUCCESS:
//                            if (tloginView != null) {
//                                tloginView.getVerifyNextTime(message.next_req_interval);
//                            }
//                            break;
//                        case EC_FREQ_LIMIT:
//                            ToastUtil.showTost("发送验证码频率多");
//                            break;
//                        default:
//                            ToastUtil.showTost("网络出错");
//
//                    }
//                    tloginView.dismissDialog();
//                }
//
//                @Override
//                public void onFail(Call<MessageBean> call, Throwable t) {
//                    Toast.makeText(PetAppLike.mcontext, "验证码发送失败", Toast.LENGTH_LONG).show();
//                    tloginView.dismissDialog();
//                }
//            });
        }

    }

    /**
     * 登录
     * 账号密码
     * /
     */
    public void loginUsername(String zhanghao, String password) {

    }


    /**
     * 登录
     */
    public void loginPhone(LoginPostBean loginPostBean) {
        final LoginView tloginView = loginView.get();
        if (tloginView != null) {
            tloginView.showDialog();

            ApiUtils.getApiService().applogin(loginPostBean).enqueue(new TaiShengCallback<BaseBean<LoginResultBean>>() {
                @Override
                public void onSuccess(Response<BaseBean<LoginResultBean>> response, BaseBean<LoginResultBean> message) {
                    switch (message.code) {
                        case Constants.HTTP_SUCCESS:
                            //小米push
                            XMPushManagerInstance.getInstance().init();
                            UserInstance.getInstance().saveUserInfo(message.result.sysUser);
                            UserInstance.getInstance().saveHealInfo(message.result.userHealth);
                            EventBus.getDefault().post(new EventManage.getUserInfoEvent());
                            break;
                        case Constants.HTTP_ERROR:
                            ToastUtil.showTost("系统维护中");
                            break;
                        case Constants.LOGIN_VERIFYCODE_FAIL:
                            ToastUtil.showTost("验证码错误");
                            break;
                        case Constants.LOGIN_VERIFYCODE_OVERDUE_FAIL:
                            ToastUtil.showTost("验证码已过期，请重新获取！");
                            break;
                        case Constants.LOGIN_USERNAME_NOT_EXISTENCE:
                            ToastUtil.showTost("账号不存在,请切换手机号登陆");
                            break;
                        case Constants.LOGIN_PASSWORD_ERROR:
                            ToastUtil.showTost("密码错误");
                            break;
                        default:
                            ToastUtil.showTost("网络出错");

                    }
                    if (tloginView != null) {
                        tloginView.dismissDialog();
                    }
                }

                @Override
                public void onFail(Call<BaseBean<LoginResultBean>> call, Throwable t) {
                    if (tloginView != null) {
                        tloginView.dismissDialog();
                    }
                    ToastUtil.showTost("网络出错");
                }
            });

//            ApiUtils.getApiService().login(phone, verifyCode, deviceType, deviceId, URLEncoder.encode(android.os.Build.MODEL.replaceAll(" ", ""), "UTF-8")).enqueue(new XMQCallback<LoginBean>() {
//                @Override
//                public void onSuccess(Response<LoginBean> response, LoginBean message) {
//
//                    HttpCode ret = HttpCode.valueOf(message.status);
//                    switch (ret) {
//                        case EC_SUCCESS:
//                            //保存登录状态
//                            UserInstance.getInstance().saveLoginState(message, phone);
//                            //小米push
//                            XMPushManagerInstance.getInstance().init();
//                           UserInstance.getInstance().getUserInfo();
//
//                            break;
//                        case EC_INVALID_VERIFY_CODE:
//                            ToastUtil.showTost("验证码无效");
//                            if (tloginView != null) {
//                                tloginView.dismissDialog();
//                            }
//                            break;
//                        default:
//                            ToastUtil.showTost("登录异常");
//                            if (tloginView != null) {
//                                tloginView.dismissDialog();
//                            }
//                            break;
//                    }
//
//                }
//
//                @Override
//                public void onFail(Call<LoginBean> call, Throwable t) {
//                    if (tloginView != null) {
//                        tloginView.dismissDialog();
//                    }
//
//                }
//            });
        }
    }

    //退出登录
    public void logout() {
//        ApiUtils.getApiService().logout(UserInstance.getInstance().getUid(), UserInstance.getInstance().getToken()).enqueue(new XMQCallback<BaseBean>() {
//            @Override
//            public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                HttpCode ret = HttpCode.valueOf(message.status);
//                if (ret == HttpCode.EC_SUCCESS) {//退出登陆成功
//                    MainActivity.getLocationWithOneMinute=false;
//                    XMPushManagerInstance.getInstance().stop();
//                    UserInstance.getInstance().clearLoginInfo();
//                    PetInfoInstance.getInstance().clearPetInfo();
//                    DeviceInfoInstance.getInstance().clearDeviceInfo();
//
//                    SPUtil.putLAST_SHOW_ACTIVITY(0);
//
//                    final LogoutView togoutView = logoutView.get();
//                    if (togoutView != null) {
//                        //退出登录成功
//                        togoutView.success();
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean> call, Throwable t) {
//
//            }
//        });
    }
}
