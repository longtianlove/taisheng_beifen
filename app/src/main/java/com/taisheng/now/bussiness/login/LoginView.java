package com.taisheng.now.bussiness.login;

/**
 * Created by long on 17/4/7.
 */

public interface LoginView {
    public void showDialog();
    public void dismissDialog();


    public void getVerifyNextTime(int nSecond);

}
