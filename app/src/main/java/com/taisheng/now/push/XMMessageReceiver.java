package com.taisheng.now.push;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;


import com.taisheng.now.EventManage;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.DeviceUtils;
import com.taisheng.now.util.LogUtil;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by long on 17/5/1.
 */

public class XMMessageReceiver extends PushMessageReceiver {
    public static final String TAG = "xmpush";
    private String mRegId;
    private long mResultCode = -1;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

    // 非ui线程
    //通透消息到达
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        LogUtil.e(TAG, mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        try {
            PushDataCenter.getInstance().notifyData(mMessage);
        }catch (Exception e){
            Log.e("xmpush-parse_error",e.getLocalizedMessage());
        }


    }

    //通知消息推送被点击
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        dealReport(mMessage, PUSHACTION_OPEN);
        LogUtil.e(TAG, mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
//        PushDataCenter.getInstance().notifyData(mMessage);
//        String url = getTargetUrl();
//        com.chinahr.android.common.instance.UrlManager.getInstance().filterPushUrl(context, url, false);
        //判断逻辑
        messageClick(context);


    }

    //通知消息到达，对于应用在前台时不弹出通知的通知消息，
    // SDK会将消息通过广播方式传给AndroidManifest中注册的PushMessageReceiver的子类的onNotificationMessageArrived方法
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        mMessage = message.getContent();
        dealReport(mMessage, PUSHACTION_RECEIVE);
        LogUtil.e(TAG, mMessage);
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
//        setRedPoint(context);
        try {
        DeviceUtils.vibrate(SampleAppLike.mcontext, 500);             //让手机振动500ms
        if(DeviceUtils.isScreenLocked(SampleAppLike.mcontext))            //判断手机是否处于屏幕关闭状态
            DeviceUtils.wakeScreen(SampleAppLike.mcontext);           //如果处于关闭屏幕状态则唤醒屏幕
        }catch (Exception e){

        }
    }

    //命令结果
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        LogUtil.e(TAG, "command:" + command);
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                LogUtil.e(TAG, "mRegId:" + mRegId);
                // 注册成功
                EventBus.getDefault().post(new EventManage.XMPushRegister());
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
//                if(XMPushManagerInstance.getInstance().getOnRegisterResult()!=null){
//                    XMPushManagerInstance.getInstance().getOnRegisterResult().onSuccess();
//                }
                LogUtil.e(TAG, "mAlias:" + cmdArg1);
                //设置set-alias成功
                EventBus.getDefault().post(new EventManage.setAlias());
            } else {
//                if(XMPushManagerInstance.getInstance().getOnRegisterResult()!=null){
//                    XMPushManagerInstance.getInstance().getOnRegisterResult().onFailed();
//                }
                // 设置set-alias失败后，重新发送
                EventBus.getDefault().post(new EventManage.setAliasFail());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
//            mUserAccount=cmdArg1;
//            LogUtil.e(TAG,"mUserAccount:"+cmdArg1);
//            // 设置set-mUserAccount成功
//            EventBus.getDefault().post(new EventManage.setAlias());
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mUserAccount = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }

    //注册结果回调
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        LogUtil.e(TAG, "command:" + command);
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;

            }
        }
    }

    //解析服务端返回的url
    private String getTargetUrl() {
        String url = "";
        try {
            JSONObject jsonObject = new JSONObject(mMessage);
            JSONObject messageObject = new JSONObject(jsonObject.optString("msg"));
            url = messageObject.optString("url");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    private static int PUSHACTION_RECEIVE = 1;//收到消息
    private static int PUSHACTION_OPEN = 2;//点击消息，打开通知。

//    /**
//     * 设置红点
//     */
//    private void setRedPoint(Context context){
//        try {
//            JSONObject jsonObject = new JSONObject(mMessage);
//            JSONObject messageObject = new JSONObject(jsonObject.optString("msg"));
//            String url = messageObject.optString("url");
//            String type = messageObject.optString("type");
//            String appPushId = messageObject.optString("appPushId");
//            reoprtToServer(Integer.parseInt(type),appPushId,PUSHACTION_RECEIVE);
//            if(TextUtils.isEmpty(type))return;
//            if(!TextUtils.isEmpty(url)){
//                PushRedPointManager.addRedPoint(type, 1);
//                new PushRedPointBrocastReceiver().sendRedPointReceiver(context,type);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 处理统计
     *
     * @param messageContent
     */
    public void dealReport(String messageContent, int action) {
        try {
            JSONObject jsonObject = new JSONObject(messageContent);
            JSONObject messageObject = new JSONObject(jsonObject.optString("msg"));
            String type = messageObject.optString("type");
            String appPushId = messageObject.optString("appPushId");
//            reoprtToServer(Integer.parseInt(type),appPushId,action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 接收到消息以及点击消息的时候上报。
//     * @param type
//     * @param appPushId
//     * @param action
//     */
//    public void reoprtToServer(int type,String appPushId,int action){
//        ApiUtils.getAppConfigService().clickPush(appPushId,action,type,0).enqueue(new ChinaHrCallBack<CommonJson>() {
//            @Override
//            public void onSuccess(Call<CommonJson> call, Response<CommonJson> response) {
//                LogUtil.i("lz","reoprtToServer,onSuccess");
//            }
//
//            @Override
//            public void onFail(Call<CommonJson> call, Throwable t) {
//                LogUtil.i("lz","reoprtToServer,onFail");
//            }
//        });
//    }


    void messageClick(Context context) {
        //判断app进程是否存活
        if(Apputil.isAppAlive(context,"com.taisheng.now")){
            //如果存活的话，就直接启动MainActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
            //DeviceActivity,再按Back键就不会返回MainActivity了。所以在启动
            //DeviceActivity前，要先启动MainActivity。
            Log.i("NotificationReceiver", "the app process is alive");
            Intent mainIntent = new Intent(context, MainActivity.class);
            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
            //如果Task栈不存在MainActivity实例，则在栈顶创建
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        }else{
            //如果app进程已经被杀死，先重新启动app
            Log.i("NotificationReceiver", "the app process is dead");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.taisheng.now");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }

    }

}
