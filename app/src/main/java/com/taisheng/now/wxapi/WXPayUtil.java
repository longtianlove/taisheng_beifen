package com.taisheng.now.wxapi;

import android.content.Context;
import android.media.VolumeShaper;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.yuyin.util.Constant;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayUtil {
    /**
     * 判断微信是否安装
     * @param context
     * @return true 已安装   false 未安装
     */
    public  static boolean isWxAppInstalled(Context context) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(context, null);
        wxApi.registerApp(Constants.WXAPPID);
        boolean bIsWXAppInstalled = false;
        bIsWXAppInstalled = wxApi.isWXAppInstalled();
        if(!bIsWXAppInstalled){
            Uiutils.showToast(context.getString(R.string.wx_installed_tip));
        }
        return bIsWXAppInstalled;
    }

}
