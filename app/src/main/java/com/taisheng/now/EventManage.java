package com.taisheng.now;


/**
 * Created by long on 17/4/8.
 */

public class EventManage {

    //小米push注册成功
    public static class XMPushRegister {

    }

    //小米设置uid成功
    public static class setAlias {

    }

    //小米设置uid成功
    public static class setAliasFail {

    }


    //用户信息获取成功
    public static class getUserInfoEvent {

    }


    //上传头像成功
    public static class uploadImageSuccess {
        public String path;

        public uploadImageSuccess(String path) {
            this.path = path;
        }
    }
    //上传聊天图片成功
    public static class uploadChatPictureSuccess {
        public String path;

        public uploadChatPictureSuccess(String path) {
            this.path = path;
        }
    }

    //手表上传头像成功
    public static class uploadWatchImageSuccess {
        public String path;

        public uploadWatchImageSuccess(String path) {
            this.path = path;
        }
    }
    //通讯录上传头像成功
    public static class uploadTongxunluImageSuccess {
        public String path;

        public uploadTongxunluImageSuccess(String path) {
            this.path = path;
        }
    }

    //医生收到消息
    public static class  AEVENT_C2C_REV_MSG{
        public Object message;
    }


    //手表收到消息
    public static class  Watch_AEVENT_C2C_REV_MSG{
        public Object message;
    }


    //签到完成
    public static class qiaodaoSuccess{
        public String tomorrowPoints;
        public String points;
        public qiaodaoSuccess(String tomorrowPoints,String points){
            this.tomorrowPoints=tomorrowPoints;
            this.points=points;
        }
    }


    //绑定流程中位置信息变更提醒
    public static class bindingLocationChanged {
        public double latitude;
        public double longitude;
    }


    public static class getYujingxinxi{

    }
    public static class tongzhiWeidu{
        public String weidu="0";
    }

}
