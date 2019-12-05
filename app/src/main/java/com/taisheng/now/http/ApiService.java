package com.taisheng.now.http;


import com.google.gson.JsonObject;
import com.taisheng.now.Constants;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.AddDizhiPostBean;
import com.taisheng.now.bussiness.bean.post.AddgouwuchePostBean;
import com.taisheng.now.bussiness.bean.post.AnswerPostBean;
import com.taisheng.now.bussiness.bean.post.ArticleCollectionBean;
import com.taisheng.now.bussiness.bean.post.ArticleShareBean;
import com.taisheng.now.bussiness.bean.post.ArticleWithDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.post.CartDetePostBean;
import com.taisheng.now.bussiness.bean.post.CollectAddorRemovePostBean;
import com.taisheng.now.bussiness.bean.post.CollectListPostBean;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.ConnectDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.CreateOrderPostBean;
import com.taisheng.now.bussiness.bean.post.DeleteDizhiPostBean;
import com.taisheng.now.bussiness.bean.post.DeleteOrderPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorCommentPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorDetailPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorNumberPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorScorePostBean;
import com.taisheng.now.bussiness.bean.post.DoctorUpdateStatePostBean;
import com.taisheng.now.bussiness.bean.post.FeedbackPostBean;
import com.taisheng.now.bussiness.bean.post.GouwucheListPostBean;
import com.taisheng.now.bussiness.bean.post.GuanzhuPostBean;
import com.taisheng.now.bussiness.bean.post.HealthCheckListPostBean;
import com.taisheng.now.bussiness.bean.post.HealthInfoPostBean;
import com.taisheng.now.bussiness.bean.post.KanjuanPostBean;
import com.taisheng.now.bussiness.bean.post.LingqukajuanPostBean;
import com.taisheng.now.bussiness.bean.post.MarketPostBean;
import com.taisheng.now.bussiness.bean.post.MarketTypePostBean;
import com.taisheng.now.bussiness.bean.post.MoreShipinPostBean;
import com.taisheng.now.bussiness.bean.post.OrderListPostBean;
import com.taisheng.now.bussiness.bean.post.OrderxiangqingPostBean;
import com.taisheng.now.bussiness.bean.post.QuestionPostBean;
import com.taisheng.now.bussiness.bean.post.RecommendDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.RecommendSharePostBean;
import com.taisheng.now.bussiness.bean.post.ShangpinxaingqingPostBean;
import com.taisheng.now.bussiness.bean.post.UpdateArticleReadCountPostBean;
import com.taisheng.now.bussiness.bean.post.UpdatePswPostBean;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.post.VideoOperatePostBean;
import com.taisheng.now.bussiness.bean.post.WexinZhifuPostBean;
import com.taisheng.now.bussiness.bean.post.UpdateCartNumberPostBean;
import com.taisheng.now.bussiness.bean.post.getListDoctorTypePostBean;
import com.taisheng.now.bussiness.bean.result.AnswerResultBean;
import com.taisheng.now.bussiness.bean.result.ArticleContentBean;
import com.taisheng.now.bussiness.bean.post.ArticleContentPostBean;
import com.taisheng.now.bussiness.bean.post.ArticlePostBean;
import com.taisheng.now.bussiness.bean.result.ArticleResultBean;
import com.taisheng.now.bussiness.bean.post.CaptchaPostBean;
import com.taisheng.now.bussiness.bean.post.HotPostBean;
import com.taisheng.now.bussiness.bean.result.CainixihuanResultBean;
import com.taisheng.now.bussiness.bean.result.CheckHistoryResultBean;
import com.taisheng.now.bussiness.bean.result.CollectAddorRemoveResultBean;
import com.taisheng.now.bussiness.bean.result.ArticleCollectListResultBean;
import com.taisheng.now.bussiness.bean.result.ConnectDoctorResultBean;
import com.taisheng.now.bussiness.bean.result.ConsultListResultBean;
import com.taisheng.now.bussiness.bean.result.CreateOrderResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorCollectListResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorCommentResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorNumberResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.bean.result.GouwucheResultBean;
import com.taisheng.now.bussiness.bean.result.HotResultBean;
import com.taisheng.now.bussiness.bean.post.LoginPostBean;
import com.taisheng.now.bussiness.bean.result.IsSign;
import com.taisheng.now.bussiness.bean.result.LoginResultBean;
import com.taisheng.now.bussiness.bean.result.MallBannerResultBanner;
import com.taisheng.now.bussiness.bean.result.MallYouhuiquanResultBanner;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.MyPingjiaResultBean;
import com.taisheng.now.bussiness.bean.result.PictureBean;
import com.taisheng.now.bussiness.bean.result.PostageResultBean;
import com.taisheng.now.bussiness.bean.result.QuestionResultBean;
import com.taisheng.now.bussiness.bean.result.RecommendSharedResultBean;
import com.taisheng.now.bussiness.bean.result.ShipinsResultBean;
import com.taisheng.now.bussiness.bean.result.SignResultBean;
import com.taisheng.now.bussiness.bean.result.market.DingdanxiangqingResultBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.bean.result.market.JsonRootBean;

import com.taisheng.now.bussiness.bean.result.market.OrderListResultBean;
import com.taisheng.now.bussiness.bean.result.market.ShangPinResultBeann;

import com.taisheng.now.bussiness.watch.bean.post.AllSettingPostBean;
import com.taisheng.now.bussiness.watch.bean.post.AnquanweiilanPostBean;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.post.BindDevicePostBean;
import com.taisheng.now.bussiness.watch.bean.post.GuijiPostBean;
import com.taisheng.now.bussiness.watch.bean.post.KaiGuanPostBean;
import com.taisheng.now.bussiness.watch.bean.post.SetNaozhongPostBean;
import com.taisheng.now.bussiness.watch.bean.post.ShishiCollectionBean;
import com.taisheng.now.bussiness.watch.bean.post.UnbindPostBean;
import com.taisheng.now.bussiness.watch.bean.post.XinlvXueyaYujingPostBean;
import com.taisheng.now.bussiness.watch.bean.result.AllSettingResultBean;
import com.taisheng.now.bussiness.watch.bean.result.BushuResultBean;
import com.taisheng.now.bussiness.watch.bean.result.GuijiResultBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongListResultBean;
import com.taisheng.now.bussiness.watch.bean.result.ShiShiCollecgtionResultBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XinLvResultBean;
import com.taisheng.now.bussiness.watch.bean.result.XinlvXueyaYujingBean;
import com.taisheng.now.bussiness.watch.bean.result.XueYaDayResultBean;
import com.taisheng.now.test.WechatResultBean;


import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by long
 */
public interface ApiService {
    /**
     * 手表接口
     */
//  绑定设备  deviceBinding
    @POST(Constants.Url.Watch.deviceBinding)
    Call<BaseBean> deviceBinding(@Body BindDevicePostBean bean);


    //解除绑定
    @POST(Constants.Url.Watch.unbind)
    Call<BaseBean> unbind(@Body UnbindPostBean UnbindPostBean);

    //手表列表
    @POST(Constants.Url.Watch.queryDeviceBinding)
    Call<BaseBean<WatchListResultBean>> queryDeviceBinding(@Body BaseListPostBean bean);


    //            APP-获取血压、心率、步数的实时数据
    @POST(Constants.Url.Watch.getcollection)
    Call<BaseBean<ShiShiCollecgtionResultBean>> getcollection(@Body ShishiCollectionBean bean);


    @POST(Constants.Url.Watch.querythedaybpxy)
    Call<BaseBean<XueYaDayResultBean>> querythedaybpxy(@Body ShishiCollectionBean bean);


    @POST(Constants.Url.Watch.querythedayheart)
    Call<BaseBean<XinLvResultBean>> querythedayheart(@Body ShishiCollectionBean bean);

    //    public static final String querythisweekwalk = "jeecg-boot/app/watch/querythisweekwalk";
    @POST(Constants.Url.Watch.querythisweekwalk)
    Call<BaseBean<BushuResultBean>> querythisweekwalk(@Body ShishiCollectionBean bean);

    @POST(Constants.Url.Watch.querythismonthwalk)
    Call<BaseBean<BushuResultBean>> querythismonthwalk(@Body ShishiCollectionBean bean);

    //            获取手表定位轨迹-批量获取
    @POST(Constants.Url.Watch.getWatchUdList)
    Call<BaseBean<GuijiResultBean>> getWatchUdList(@Body GuijiPostBean bean);


    @POST(Constants.Url.Watch.addwatchElectronicFence)
    Call<BaseBean> addwatchElectronicFence(@Body AnquanweiilanPostBean bean);

    @POST(Constants.Url.Watch.getWatchREMINDList)
    Call<BaseBean<NaozhongListResultBean>> getWatchREMINDList(@Body BaseWatchBean bean);

    @POST(Constants.Url.Watch.setWatchREMIND)
    Call<BaseBean> setWatchREMIND(@Body SetNaozhongPostBean bean);

    @POST(Constants.Url.Watch.allSetting)
    Call<BaseBean<AllSettingResultBean>> allSetting(@Body AllSettingPostBean bean);


    @POST(Constants.Url.Watch.gpsSetting)
    Call<BaseBean> gpsSetting(@Body KaiGuanPostBean bean);


    @POST(Constants.Url.Watch.flipCheckSetting)
    Call<BaseBean> flipCheckSetting(@Body KaiGuanPostBean bean);

    @POST(Constants.Url.Watch.sosAlarmSetting)
    Call<BaseBean> sosAlarmSetting(@Body KaiGuanPostBean bean);

    @POST(Constants.Url.Watch.lowElectSmsSetting)
    Call<BaseBean> lowElectSmsSetting(@Body KaiGuanPostBean bean);


    @POST(Constants.Url.Watch.takeOffWristbandSetting)
    Call<BaseBean> takeOffWristbandSetting(@Body KaiGuanPostBean bean);

    @POST(Constants.Url.Watch.stepCountingSetting)
    Call<BaseBean> stepCountingSetting(@Body KaiGuanPostBean bean);


    //免打扰开关
    @POST(Constants.Url.Watch.notDisturbSwitchSetting)
    Call<BaseBean> notDisturbSwitchSetting(@Body KaiGuanPostBean bean);




    @POST(Constants.Url.Watch.getWatchWarning)
    Call<BaseBean<XinlvXueyaYujingBean>> getWatchWarning(@Body XinlvXueyaYujingPostBean bean);

    @POST(Constants.Url.Watch.setWatchWarning)
    Call<BaseBean> setWatchWarning(@Body XinlvXueyaYujingBean bean);




    /*
    App接口
     */
    //登录
    @POST(Constants.Url.User.applogin)
    Call<BaseBean<LoginResultBean>> applogin(@Body LoginPostBean loginPostBean);

    //发送验证码
    @POST(Constants.Url.User.appAcquireVerifyCode)
    Call<BaseBean> appAcquireVerifyCode(@Body CaptchaPostBean loginPostBean);

    /**
     * 更新用户信息
     */
    @POST(Constants.Url.User.modifyuser)
    Call<BaseBean<ModifyUserInfoResultBean>> modifyuser(@Body UserInfoPostBean bean);

    /**
     * 更新档案信息
     * \
     */
    @POST(Constants.Url.User.addOrUpdateHealth)
    Call<BaseBean> addOrUpdateHealth(@Body HealthInfoPostBean bean);

    /**
     * 更新密码
     */
    @POST(Constants.Url.User.modifypassword)
    Call<BaseBean> modifypasswrod(@Body UpdatePswPostBean bean);


    /**
     * 我的评价列表
     */
    @POST(Constants.Url.User.myDoctorScores)
    Call<BaseBean<MyPingjiaResultBean>> myDoctorScores(@Body BaseListPostBean bean);

    @POST(Constants.Url.User.myrecommendshare)
    Call<BaseBean<RecommendSharedResultBean>> myrecommendshare(@Body RecommendSharePostBean bean);

    //获取热门文章
    @POST(Constants.Url.Article.hotSearchArticle)
    Call<BaseBean<HotResultBean>> hotSearchArticle(@Body HotPostBean hotPostBean);

    //获取文章列表
    @POST(Constants.Url.Article.articleList)
    Call<BaseBean<ArticleResultBean>> articleList(@Body ArticlePostBean articlePostBean);

    //获取文章详情
    @POST(Constants.Url.Article.articleQeryById)
    Call<BaseBean<ArticleContentBean>> articleQeryById(@Body ArticleContentPostBean articleContentPostBean);

    //增加文章的阅读量
    @POST(Constants.Url.Article.updateArticleReadCount)
    Call<BaseBean> updateArticleReadCount(@Body UpdateArticleReadCountPostBean bean);

    //获取题目
    @POST(Constants.Url.CePing.getExtractionSubjectDb)
    Call<BaseBean<QuestionResultBean>> getExtractionSubjectDb(@Body QuestionPostBean questionPostBean);

    //提交答题结果
    @POST(Constants.Url.CePing.saveAnswer)
    Call<BaseBean<AnswerResultBean>> saveAnswer(@Body AnswerPostBean bean);

    /**
     * 测评历史
     */
    @POST(Constants.Url.CePing.answerRecordList)
    Call<BaseBean<CheckHistoryResultBean>> answerRecordList(@Body HealthCheckListPostBean bean);

    //首页热度文章
    @POST(Constants.Url.Article.hotArticleList)
    Call<BaseBean<ArticleResultBean>> hotArticleList(@Body BasePostBean postBean);


    //获取推荐医生
    @POST(Constants.Url.Doctor.recommendList)
    Call<BaseBean<DoctorsResultBean>> recommendList(@Body RecommendDoctorPostBean postBean);

    //文章收藏
    @POST(Constants.Url.Article.saveCollectionArticleLog)
    Call<BaseBean> saveCollectionArticleLog(@Body ArticleCollectionBean bean);


    //文章收藏
    @POST(Constants.Url.Article.saveShareArticleLog)
    Call<BaseBean> saveShareArticleLog(@Body ArticleShareBean bean);


    /**
     * 推荐视频
     */
    public static final String recommendShiPin = "jeecg-boot/app/assessment/getExtractionSubjectDb";

    @POST(Constants.Url.ShiPin.recommendShiPin)
    Call<BaseBean<ShipinsResultBean>> recommendShiPin(@Body BasePostBean bean);

    /**
     * 获取视频列表
     */
//    public static final String moreShiPin = "jeecg-boot/app/assessment/getExtractionSubjectDb";
    @POST(Constants.Url.ShiPin.moreShiPin)
    Call<BaseBean<ShipinsResultBean>> moreShiPin(@Body MoreShipinPostBean bean);

    /**
     * 点赞
     */
    public static final String videoOperate = "jeecg-boot/app/video/operate";

    @POST(Constants.Url.ShiPin.videoOperate)
    Call<BaseBean> videoOperate(@Body VideoOperatePostBean bean);


    //获取所有医生
    @POST(Constants.Url.Doctor.doctorslist)
    Call<BaseBean<DoctorsResultBean>> doctorslist(@Body RecommendDoctorPostBean bean);


    //获取所有医生
    @POST(Constants.Url.Doctor.getListDoctor)
    Call<BaseBean<DoctorsResultBean>> getListDoctor(@Body getListDoctorTypePostBean bean);


    //文章相关的医生
    @POST(Constants.Url.Article.getDoctorTypeList)
    Call<BaseBean<DoctorsResultBean>> getDoctorTypeList(@Body ArticleWithDoctorPostBean bean);


    //获取医生详情
    @POST(Constants.Url.Doctor.doctorQueryById)
    Call<BaseBean<DoctorBean>> doctorQueryById(@Body DoctorDetailPostBean bean);

    /**
     * 获取医生评价
     */
    @POST(Constants.Url.Doctor.doctorScoreList)
    Call<BaseBean<DoctorCommentResultBean>> doctorScoreList(@Body DoctorCommentPostBean bean);

    //咨询记录
    @POST(Constants.Url.Doctor.consultList)
    Call<BaseBean<ConsultListResultBean>> consultList(@Body BaseListPostBean bean);


    //统计被评价总数
    @POST(Constants.Url.Doctor.getBeCommentedNum)
    Call<BaseBean<DoctorNumberResultBean>> getBeCommentedNum(@Body DoctorNumberPostBean bean);

    //            文章被/医生关注（收藏）总数
    @POST(Constants.Url.Doctor.getBeDoctorAttentionNum)
    Call<BaseBean<DoctorNumberResultBean>> getBeDoctorAttentionNum(@Body GuanzhuPostBean bean);

    //医生服务总数
    @POST(Constants.Url.Doctor.getDoctorServerNum)
    Call<BaseBean<DoctorNumberResultBean>> getDoctorServerNum(@Body DoctorNumberPostBean bean);


    @POST(Constants.Url.Doctor.updateDoctorStatus)
    Call<BaseBean> updateDoctorStatus(@Body DoctorUpdateStatePostBean bean);


    //收藏/取消收藏
    @POST(Constants.Url.Doctor.collectionaddOrRemove)
    Call<BaseBean<CollectAddorRemoveResultBean>> collectionaddOrRemove(@Body CollectAddorRemovePostBean bean);


    //文章收藏列表
    @POST(Constants.Url.Doctor.collectionlist)
    Call<BaseBean<ArticleCollectListResultBean>> articlecollectionlist(@Body CollectListPostBean bean);


    //医生收藏列表
    @POST(Constants.Url.Doctor.collectionlist)
    Call<BaseBean<DoctorCollectListResultBean>> doctorcollectionlist(@Body CollectListPostBean bean);

    /**
     * 给医生评分
     */
    @POST(Constants.Url.Doctor.doctorScore)
    Call<BaseBean> doctorScore(@Body DoctorScorePostBean beann);


    //视频建立连接
    @POST(Constants.Url.Doctor.connectDoctor)
    Call<BaseBean<ConnectDoctorResultBean>> connectDoctor(@Body ConnectDoctorPostBean bean);

    //有人进来
    @POST(Constants.Url.Doctor.detectRoomIn)
    Call<BaseBean> detectRoomIn(@Body DoctorUpdateStatePostBean bean);

    //上传头像
    @Multipart
    @POST(Constants.Url.User.uploadImage)
    Call<BaseBean<PictureBean>> uploadLogo(
            @Part MultipartBody.Part file
    );

    //反馈
    @POST(Constants.Url.feedback)
    Call<BaseBean> feedback(@Body FeedbackPostBean bean);


    //签到
    @POST(Constants.Url.User.isSign)
    Call<BaseBean<IsSign>> isSign(@Body BasePostBean bean);


    @POST(Constants.Url.User.nowSign)
    Call<BaseBean<SignResultBean>> nowSign(@Body BasePostBean bean);


    @POST(Constants.Url.ShangCheng.banner)
    Call<BaseBean<MallBannerResultBanner>> banner(@Body BaseListPostBean bean);


    @POST(Constants.Url.ShangCheng.coupon)
    Call<BaseBean<MallYouhuiquanResultBanner>> coupon(@Body BaseListPostBean bean);


    @POST(Constants.Url.ShangCheng.couponlist)
    Call<BaseBean<MallYouhuiquanResultBanner>> couponlist(@Body BaseListPostBean bean);


    @POST(Constants.Url.ShangCheng.cainixihuan)
    Call<BaseBean<CainixihuanResultBean>> cainixihuan(@Body BasePostBean bean);


    @POST(Constants.Url.ShangCheng.gouwuchelist)
    Call<BaseBean<GouwucheResultBean>> gouwuchelist(@Body GouwucheListPostBean bean);


    //    public static final String goodslist="jeecg-boot/app/mall/goods/list";
    @POST(Constants.Url.ShangCheng.goodslist)
    Call<BaseBean<ShangPinResultBeann>> goodslist(@Body MarketPostBean bean);


    //    public static final String shangpinxiangqing="jeecg-boot/app/mall/goods/goods/detail";
    @POST(Constants.Url.ShangCheng.shangpinxiangqing)
    Call<BaseBean<JsonRootBean>> shangpinxiangqing(@Body ShangpinxaingqingPostBean bean);

    //    public static final String addgouwuche="jeecg-boot//app/mall/cart/add";
    @POST(Constants.Url.ShangCheng.addgouwuche)
    Call<BaseBean> addgouwuche(@Body AddgouwuchePostBean bean);

    //    public static final String updateCartNumber="jeecg-boot/app/mall/cart/updateCartNumber";
    @POST(Constants.Url.ShangCheng.updateCartNumber)
    Call<BaseBean> updateCartNumber(@Body UpdateCartNumberPostBean bean);


    @POST(Constants.Url.ShangCheng.fastBuyCheckNumbe)
    Call<BaseBean> fastBuyCheckNumbe(@Body UpdateCartNumberPostBean bean);


    @POST(Constants.Url.ShangCheng.cartDelete)
    Call<BaseBean> cartDelete(@Body CartDetePostBean bean);


    //    public static final String getCouponlist="jeecg-boot/app/mall/coupon/getCouponList";
    @POST(Constants.Url.ShangCheng.getCouponlist)
    Call<BaseBean<MallYouhuiquanResultBanner>> getCouponlist(@Body KanjuanPostBean bean);

    //    public static final String getCoupon="jeecg-boot/app/mall/coupon/getCoupon";
    @POST(Constants.Url.ShangCheng.getCoupon)
    Call<BaseBean> getCoupon(@Body LingqukajuanPostBean bean);

    //    public static final String addressList="jeecg-boot/app/mall/address/list";
    @POST(Constants.Url.ShangCheng.addressList)
    Call<BaseBean<DizhilistResultBean>> addressList(@Body BaseListPostBean bean);

    //    public static final String addressAdd="jeecg-boot/app/mall/address/add";
    @POST(Constants.Url.ShangCheng.addressAdd)
    Call<BaseBean> addressAdd(@Body AddDizhiPostBean bean);


    public static final String addressDelete = "jeecg-boot/app/mall/address/delete";

    @POST(Constants.Url.ShangCheng.addressDelete)
    Call<BaseBean> addressDelete(@Body DeleteDizhiPostBean bean);


    //    public static final String updateAddressById = "jeecg-boot/app/mall/address/updateAddressById";
    @POST(Constants.Url.ShangCheng.updateAddressById)
    Call<BaseBean> updateAddressById(@Body AddDizhiPostBean bean);


    //    public static final String goodsTtype="jeecg-boot/app/mall/goods/goods/type";
    @POST(Constants.Url.ShangCheng.goodsTtype)
    Call<BaseBean<ShangPinResultBeann>> goodsTtype(@Body MarketTypePostBean bean);

    //    public static final String orderList="jeecg-boot/app/mall/order/list";
    @POST(Constants.Url.ShangCheng.orderList)
    Call<BaseBean<OrderListResultBean>> orderList(@Body OrderListPostBean bean);


    //    public static final String getPostage="jeecg-boot/app/mall/postage/getPostage";
    @POST(Constants.Url.ShangCheng.getPostage)
    Call<BaseBean<PostageResultBean>> getPostage(@Body BasePostBean bean);


    @POST(Constants.Url.ShangCheng.createOrder)
    Call<BaseBean<CreateOrderResultBean>> createOrder(@Body CreateOrderPostBean bean);


    @POST(Constants.Url.ShangCheng.deleteOrder)
    Call<BaseBean> deleteOrder(@Body DeleteOrderPostBean bean);

    //    public static final String confirmReceiveGoods = "jeecg-boot/app/mall/order/confirmReceiveGoods";
    @POST(Constants.Url.ShangCheng.confirmReceiveGoods)
    Call<BaseBean> confirmReceiveGoods(@Body DeleteOrderPostBean bean);


    @POST(Constants.Url.ShangCheng.weChatPay)
    Call<BaseBean<WechatResultBean>> weChatPay(@Body WexinZhifuPostBean bean);


    @POST(Constants.Url.ShangCheng.orderDetail)
    Call<BaseBean<DingdanxiangqingResultBean>> orderDetail(@Body OrderxiangqingPostBean bean);


    @POST(Constants.Url.ShangCheng.orderConfirmReceiveGoods)
    Call<BaseBean> orderConfirmReceiveGoods(@Body OrderxiangqingPostBean bean);


}