package com.taisheng.now;

import android.widget.SimpleCursorTreeAdapter;

/**
 * Created by long on 17/4/6.
 */

public class Constants {

    public final static String DEFAULT_TAG = "taisheng";
    public final static String BUGLY_APP_ID = "24b876d82d";
    //todo 需要修改
    //腾讯视频appid
    public final static int SDKAPPID = 1400229730;

    //微信支付appid
    public final static String WXAPPID = "wxa371cf7bae0024bf";


    //访问成功
    public final static int HTTP_SUCCESS = 200;
    public final static int HTTP_ERROR = 500;
    public final static int LOGIN_VERIFYCODE_FAIL = 1004;//验证码不正确，请重新输入
    public final static int LOGIN_VERIFYCODE_OVERDUE_FAIL = 1007;//"验证码已过期，请重新获取！"
    public final static int LOGIN_USERNAME_NOT_EXISTENCE = 1002;//账号不存在,请切换手机号登陆！
    public final static int LOGIN_PASSWORD_ERROR = 101;//密码错误
    public final static int USER_FREEZE = 401020;//用户冻结
    public final static int USER_ACCOUNT_SOURCE_ERROR = 401021;//用户来源错误
    public final static int USER_DATABASE_TOKEN_NOT_EXIST = 401022;//token异常，数据库token不存在
    public final static int TOKEN_DIFFERENCE = 401023;//token异常,数据库token和用户参数token不一致
    public final static int DOCTOR_NOEXIST = 70000;//医生不存在
    public final static int DOCTOR_BUSY = 70001;//医生忙碌中,请稍后联系


    //男女0是男，1是女
    public final static int MALE = 0;
    public final static int FEMALE = 1;

    public final static String SUSHENHUFU = "塑身护肤";
    public final static String JIANSHENYUNDONG = "健身运动";
    public final static String SHILIAOYANGSHENG = "食疗养生";
    public final static String YONGYAOZHIDAO = "用药指导";
    public final static String MUYINGYUNYU = "母婴孕育";


    public static class Url {

        //手表服务器
//      public static String Host = "http://193.112.19.18:8080/";
//      public static String File_Host = "http://193.112.19.18:8080/jeecg-boot/sys/common/view/";
//      public static String WEB_SOCKET_URL = "ws://193.112.19.18:8879";
        //正式服务器
//        public static String Host = "https://taishenghealth.com/";
        public static String Host = "http://192.168.1.19:8080/";
        public static String File_Host = "https://taishenghealth.com/image-video/";
        public static String File_Host_head = "https://taishenghealth.com/image-video/";
        public static String WEB_SOCKET_URL = "wss://taishenghealth.com/jeecg-wss/";
        //测试
       /* public static String Host = "https://test.taishenghealth.com/";
        public static String File_Host = "https://test.taishenghealth.com/image-video/";
        public static String File_Host_head = "https://test.taishenghealth.com/image-video/";
        public static String WEB_SOCKET_URL = "wss://test.taishenghealth.com/jeecg-wss/";*/
        //测试服务器
       /* public static String Host = "http://49.234.71.11:8080/";//有视频的
//        public static String File_Host = "http://49.234.71.11:8888/";
        public static String File_Host = "http://49.234.71.11/jeecg-boot/sys/common/view/";
        public static String File_Host_head = "http://49.234.71.11:8888/img/";
        public static String WEB_SOCKET_URL = "ws://49.234.71.11:8879";*/

       /* public static String Host = "http://192.168.1.15:8080/";//有视频的
        //        public static String File_Host = "http://49.234.71.11:8888/";
        public static String File_Host = "http://49.234.71.11/jeecg-boot/sys/common/view/";
        public static String File_Host_head = "https://test.taishenghealth.com/image-video/";
        public static String WEB_SOCKET_URL = "ws://192.168.1.15:8879";*/


       /* public static String Host = "http://192.168.1.18:8080/";//有视频的
        //        public static String File_Host = "http://49.234.71.11:8888/";
        public static String File_Host = "http://192.168.1.18/jeecg-boot/sys/common/view/";
        public static String File_Host_head = "http://192.168.1.18:8888/img/";
        public static String WEB_SOCKET_URL = "ws://49.234.71.11:8879";*/

        public static class Watch {
            //上传语音
            public static final String microcharVoice = "jeecg-boot/app/microchar/getvoice";


            //            绑定设备  deviceBinding
            public static final String deviceBinding = "jeecg-boot/app/watchDevice/deviceBinding";
            //设备列表
            public static final String queryDeviceBinding = "jeecg-boot/app/watchDevice/queryDeviceBinding";
            //解除绑定
            public static final String unbind = "jeecg-boot/app/watchDevice/unbind";


            //            APP-获取血压、心率、步数的实时数据
            public static final String getcollection = "jeecg-boot/app/watch/getcollection";


            //根据日期获取血压列表
            public static final String obtainBpxyList = "jeecg-boot/app/watchBpxy/obtainBpxyList";
            //根据日期获取心率列表
            public static final String obtainHeartList = "jeecg-boot/app/watchHeart/obtainHeartList";


            //            APP-获取血压日统计量
            public static final String querythedaybpxy = "jeecg-boot/app/watch/querythedaybpxy";

            //获取心率日统计量
            public static final String querythedayheart = "jeecg-boot/app/watch/querythedayheart";

            //            APP-获取步数周统计量
            public static final String querythisweekwalk = "jeecg-boot/app/watch/querythisweekwalk";

            //            APP-获取步数月统计量
            public static final String querythismonthwalk = "jeecg-boot/app/watch/querythismonthwalk";


            //            获取手表定位轨迹-批量获取
            public static final String getWatchUdList = "jeecg-boot/app/watchUd/getWatchUdList";

//            手表电子围栏-添加

            public static final String addwatchElectronicFence = "jeecg-boot/app/watchUd/WatchUdSetting";


            //            获取手表老人电话本-批量获取
            public static final String getWatchPhbxList = "jeecg-boot/app/watchPhbx/getWatchPhbxList";

//            老人版电话本删除

            public static final String setWatchDphbx = "jeecg-boot/app/watchPhbx/setWatchDphbx";


            //            闹钟设置-批量获取
            public static final String getWatchREMINDList = "jeecg-boot/app/watchRemind/getWatchREMINDList";
            //            闹钟设置-闹钟参数设置
            public static final String setWatchREMIND = "jeecg-boot/app/watchRemind/setWatchREMIND";


            //            获取最新定位信息
//            public static final String getNewPosition = "jeecg-boot/app/watchUd/getNewPosition";
            public static final String getNewPosition = "jeecg-boot/app/watchlocation/getNewPosition";


            //所有的开关状态
//            public static final String allSetting = "jeecg-boot/app/watch/setting/allSetting";
            public static final String allSetting = "jeecg-boot/app/switch/obtainAllSwitch";
            //所有开关开关用一个接口
            public static final String watchSwitchConfig = "jeecg-boot/app/switch/watchSwitchConfig";

            //            Watch-翻转检测开关设置
            public static final String flipCheckSetting = "jeecg-boot/app/watch/setting/flipCheckSetting";

            //            Watch-GPS开关设置接口
            public static final String gpsSetting = "jeecg-boot/app/watch/setting/gpsSetting";


            //            Watch-SOS报警开关设置
            public static final String sosAlarmSetting = "jeecg-boot/app/watch/setting/sosAlarmSetting";


            //            Watch-GPS低电短信报警设置
            public static final String lowElectSmsSetting = "jeecg-boot/app/watch/setting/lowElectSmsSetting";

            //            Watch-取下手环报警开关设置接口
            public static final String takeOffWristbandSetting = "jeecg-boot/app/watch/setting/takeOffWristbandSetting";

            //            Watch-计步开关设置
            public static final String stepCountingSetting = "jeecg-boot/app/watch/setting/stepCountingSetting";

            //免打扰开关
            public static final String notDisturbSwitchSetting = "jeecg-boot/app/watch/setting/notDisturbSwitchSetting";

            //免打扰时间段列表
            public static final String notDisturbSettingList = "jeecg-boot/app/watchperiod/setting/silencetimeList";

            //            心率血压预警设置-查询
            public static final String getWatchWarning = "jeecg-boot/app/watchWarning/getWatchWarning";

//            心率血压预警设置-修改

            public static final String setWatchWarning = "jeecg-boot/app/watchWarning/setWatchWarning";

            //            手表预警记录-查询
            public static final String getWatchWarningAll = "jeecg-boot/app/watchWarning/getWatchWarningAll";


            //更新昵称
            public static final String updateDeviceInfo = "jeecg-boot/app/watchDevice/updateDeviceInfo";


//            免打扰时间段设-添加

            public static final String notDisturbSetting = "jeecg-boot/app/watch/setting/notDisturbSetting";


            //            APP-SOS联系人列表
            public static final String listSosContactSetting = "jeecg-boot/app/watch/setting/listSosContactSetting";

            //            APP-【SOS增加】联系人
            public static final String insertSosContactSetting = "jeecg-boot/app/watch/setting/insertSosContactSetting";


            //            APP-【SOS更新】联系人
            public static final String updateSosContactSetting = "jeecg-boot/app/watch/setting/updateSosContactSetting";

            //            手表预警记录-修改阅读状态
            public static final String watchWarningupdateBykey = "jeecg-boot/app/watchWarning/updateBykey";


            //            语音版吃药提醒-批量获取
            public static final String getWatchTakepillsList = "jeecg-boot/app/watchTakepills/getWatchTakepillsList";


            //            语音版吃药提醒-设置
            public static final String setWatchTakepills = "jeecg-boot/app/watchTakepills/setWatchTakepills";


            //            设置老人版电话本
            public static final String setWatchPhbx = "jeecg-boot/app/watchPhbx/setWatchPhbx";


            //重启接口
            public static final String reboot = "jeecg-boot/app/watch/setting/reboot";

            //            恢复出厂设置
            public static final String restoreFactorySettings = "jeecg-boot/app/watch/setting/restoreFactorySettings";


        }


        //用户相关
        public static class User {

            /**
             * 获取验证码
             */
            public static final String appAcquireVerifyCode = "jeecg-boot/app/sms/appAcquireVerifyCode";

            /**
             * 手机APP登录
             */
            public static final String applogin = "jeecg-boot/app/login/applogin";

            /**
             * 上传头像
             */
            public static final String uploadImage = "jeecg-boot/sys/common/uploadImage";

            /**
             * 更新用户信息
             */
            public static final String modifyuser = "jeecg-boot/app/user/modifyuser";
            /**
             * 更新档案信息
             */
            public static final String addOrUpdateHealth = "jeecg-boot/app/user/addOrUpdateHealth";
            /**
             * 更新密码
             */
            public static final String modifypassword = "jeecg-boot/app/user/modifypassword";

            /**
             * 我的评价列表
             */
            public static final String myDoctorScores = "jeecg-boot/app/score/doctorScore/myDoctorScores";


            //邀请历史
            public static final String myrecommendshare = "jeecg-boot/app/extension/getExtensionManagement";


            //签到
            public static final String isSign = "jeecg-boot/app/sign/isSign";

            //当前签到的状态
            public static final String nowSign = "jeecg-boot/app/sign/user/sign";


        }

        //文章
        public static class Article {
            /**
             * 获取热门文章
             */
            public static final String hotSearchArticle = "jeecg-boot/app/article/hotSearchArticle";

            /**
             * 文章列表
             */
            public static final String articleList = "jeecg-boot/app/article/list";

            /**
             * 文章详情
             */
            public static final String articleQeryById = "jeecg-boot/app/article/queryById";

            /**
             * 文章内容
             */
            //todo 上线改host
//            public static final String articleContent=Host+"jeecg-boot/app/article/v2/h5/articleDetail?articleId=";
            public static final String articleContent = Host + "jeecg-boot/app/article/v2/h5/articleDetail?articleId=";

            /**
             * 首页热度文章
             */
            public static final String hotArticleList = "jeecg-boot/app/article/index/hotArticleList";


            //文章相关的医生
            public static final String getDoctorTypeList = "jeecg-boot/app/doctor/getDoctorTypeList";

            //增加文章的阅读量
            public static final String updateArticleReadCount = "jeecg-boot/app/article/updateArticleReadCount";

            //文章收藏
            public static final String saveCollectionArticleLog = "jeecg-boot/app/article/v2/saveCollectionArticleLog";
            //文章分享
            public static final String saveShareArticleLog = "jeecg-boot/app/article/v2/saveShareArticleLog";

        }

        //医生
        public static class Doctor {


            /**
             * 获取推荐医生
             */
            public static final String recommendList = "jeecg-boot/app/doctor/recommendList";
            /**
             * 获取所有医生
             */
            public static final String doctorslist = "jeecg-boot/app/doctor/list";
            /**
             * 按照擅长方向获取全部医生
             */
            public static final String getListDoctor = "jeecg-boot/app/doctor/getListDoctor";
            /**
             * 获取医生详情
             */
            public static final String doctorQueryById = "jeecg-boot/app/doctor/queryById";
            /**
             * 获取医生评价
             */
            public static final String doctorScoreList = "jeecg-boot/app/score/doctorScore/scoreList";


            /**
             * 给医生评分
             */
            public static final String doctorScore = "jeecg-boot/app/score/doctorScore/add";

            //统计被评价总数
            public static final String getBeCommentedNum = "jeecg-boot/app/doctor/getBeCommentedNum";

            //文章被/医生关注（收藏）总数
            public static final String getBeDoctorAttentionNum = "jeecg-boot/app/doctor/getBeDoctorAttentionNum";
            //医生服务总数
            public static final String getDoctorServerNum = "jeecg-boot/app/doctor/getDoctorServerNum";

            //收藏/取消收藏
            public static final String collectionaddOrRemove = "jeecg-boot/app/collection/addOrRemove";

            //收藏管理
            public static final String collectionlist = "jeecg-boot/app/collection/getDoctorOrArticlePageList";

            //咨询记录
            public static final String consultList = "jeecg-boot/app/consult/consultList";


            //视频建立连接
            public static final String connectDoctor = "jeecg-boot/app/consult/connectDoctor";
            //有人进来
            public static final String detectRoomIn = "jeecg-boot/app/consult/detectRoomIn";
            //视频完接口回调
            public static final String updateDoctorStatus = "jeecg-boot/app/consult/updateDoctorStatus";
        }

        //视频
        public static class ShiPin {

            /**
             * 推荐视频
             */
            public static final String recommendShiPin = "jeecg-boot/app/video/home/recommend";

            /**
             * 获取视频列表
             */
            public static final String moreShiPin = "jeecg-boot/app/video/list";


            /**
             * 点赞
             */
            public static final String videoOperate = "jeecg-boot/app/video/operate";


        }


        //测评
        public static class CePing {
            /**
             * 获取测评题目
             */
            public static final String getExtractionSubjectDb = "jeecg-boot/app/assessment/getExtractionSubjectDb";
            /**
             * 提交答题结果
             */
            public static final String saveAnswer = "jeecg-boot/app/assessment/saveAnswer";

            /**
             * 测评历史
             */
            public static final String answerRecordList = "jeecg-boot/app/assessment/answerRecordList";

        }


        //商城
        public static class ShangCheng {
            public static final String banner = "jeecg-boot/app/mall/banner/list";
            public static final String coupon = "jeecg-boot/app/mall/coupon/list";
            //优惠券列表
            public static final String couponlist = "jeecg-boot/app/mall/coupon/more/list";
            //我的优惠券
            public static final String getCouponlist = "jeecg-boot/app/mall/coupon/getCouponList";

            //商品列表
            public static final String goodslist = "jeecg-boot/app/mall/goods/list";


            //商品详情
            public static final String shangpinxiangqing = "jeecg-boot/app/mall/goods/goods/detail";

            public static final String gouwuchelist = "jeecg-boot/app/mall/cart/list";

            public static final String cainixihuan = "jeecg-boot/app/mall/goods/hot/like/list";
            //添加到购物车
            public static final String addgouwuche = "jeecg-boot/app/mall/cart/add";
            //            【更改购物车中的数量】
            public static final String updateCartNumber = "jeecg-boot/app/mall/cart/updateCartNumber";
            //            【立即购买中的数量】
            public static final String fastBuyCheckNumbe = "jeecg-boot/app/mall/cart/fastBuyCheckNumber";
            //从购物车删除
            public static final String cartDelete = "jeecg-boot/app/mall/cart/delete";

            //商城首页优惠券-领取用户优惠券
            public static final String getCoupon = "jeecg-boot/app/mall/coupon/getCoupon";
            //用户收货地址
            public static final String addressList = "jeecg-boot/app/mall/address/list";
            //添加地址
            public static final String addressAdd = "jeecg-boot/app/mall/address/add";
            //            删除用户收货地址
            public static final String addressDelete = "jeecg-boot/app/mall/address/delete";
            //            用户收货地址接口-编辑收货地址
            public static final String updateAddressById = "jeecg-boot/app/mall/address/updateAddressById";


            //不同类别下的商品列表
            public static final String goodsTtype = "jeecg-boot/app/mall/goods/goods/type";

            //            订单信息-【订单列表】
            public static final String orderList = "jeecg-boot/app/mall/order/list";
            //            快递费用显示】
            public static final String getPostage = "jeecg-boot/app/mall/postage/getPostage";

            //创建订单
            public static final String createOrder = "jeecg-boot/app/mall/order/createOrder";


            //            订单信息-【取消订单信息】
            public static final String deleteOrder = "jeecg-boot/app/mall/order/deleteOrder";
            //            订单信息-【确认收货】
            public static final String confirmReceiveGoods = "jeecg-boot/app/mall/order/confirmReceiveGoods";


            public static final String weChatPay = "jeecg-boot/app/pay/payment/wxpay/weChatPay";


            public static final String orderDetail = "jeecg-boot/app/mall/order/detail";
            //            订单信息-【确认收货】
            public static final String orderConfirmReceiveGoods = "jeecg-boot/app/mall/order/confirmReceiveGoods";


        }


        /**
         * 投诉中心
         */

        public static final String feedback = "jeecg-boot/app/feedback/add";


        public static class Test {
            public static final String weChatPay = "jeecg-boot/app/pay/payment/wxpay/weChatPay";

        }

    }


}
