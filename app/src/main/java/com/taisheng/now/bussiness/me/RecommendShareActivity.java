package com.taisheng.now.bussiness.me;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mob.MobSDK;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.RecommendSharePostBean;
import com.taisheng.now.bussiness.bean.result.RecommendShareBean;
import com.taisheng.now.bussiness.bean.result.RecommendSharedResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ZXingUtils;
import com.taisheng.now.view.TaishengListView;
import com.th.j.commonlibrary.utils.LogUtilH;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class RecommendShareActivity extends BaseIvActivity {

    @BindView(R.id.sdv_header)
    SimpleDraweeView sdvHeader;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.iv_share_qr)
    ImageView ivShareQr;
    @BindView(R.id.ll_weixin)
    LinearLayout llWeixin;
    @BindView(R.id.ll_pengyouquan)
    LinearLayout llPengyouquan;
    @BindView(R.id.ll_sharelabel)
    LinearLayout llSharelabel;
    @BindView(R.id.lv_shared)
    TaishengListView lvShared;
    @BindView(R.id.ll_share_all)
    LinearLayout llShareAll;
    private RecommendHistoryAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_recommendshare);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        madapter = new RecommendHistoryAdapter(this);
        lvShared.setAdapter(madapter);
        lvShared.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getMore();
            }
        });
    }

    @Override
    public void addData() {
        initDatas();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.qr_code));
    }


    @OnClick({R.id.ll_weixin, R.id.ll_pengyouquan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_weixin:
                showShare(Wechat.NAME);
                break;
            case R.id.ll_pengyouquan:
                showShare(WechatMoments.NAME);
                break;
        }
    }






    private void initDatas() {
        PAGE_NO = 1;
        RecommendSharePostBean bean = new RecommendSharePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().myrecommendshare(bean).enqueue(new TaiShengCallback<BaseBean<RecommendSharedResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<RecommendSharedResultBean>> response, BaseBean<RecommendSharedResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            llSharelabel.setVisibility(View.VISIBLE);
                            llShareAll.setVisibility(View.VISIBLE);
                            lvShared.setLoading(false);

                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10 && message.result.records.size() > 0) {
                                lvShared.setHasLoadMore(false);
                                lvShared.setLoadAllViewText("暂时只有这么多结果");
                                lvShared.setLoadAllFooterVisible(true);
                            } else {
                                lvShared.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            llSharelabel.setVisibility(View.GONE);
                            llShareAll.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<RecommendSharedResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
                llSharelabel.setVisibility(View.GONE);
                llShareAll.setVisibility(View.GONE);
            }
        });

    }


    void getMore() {
        RecommendSharePostBean bean = new RecommendSharePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().myrecommendshare(bean).enqueue(new TaiShengCallback<BaseBean<RecommendSharedResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<RecommendSharedResultBean>> response, BaseBean<RecommendSharedResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvShared.setLoading(false);


                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10 && message.result.records.size() > 0) {
                                lvShared.setHasLoadMore(false);
                                lvShared.setLoadAllViewText("暂时只有这么多结果");
                                lvShared.setLoadAllFooterVisible(true);
                            } else {
                                lvShared.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<RecommendSharedResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        if (UserInstance.getInstance().userInfo.avatar != null) {
            Uri uri = Uri.parse(Constants.Url.File_Host + UserInstance.getInstance().userInfo.avatar);
            sdvHeader.setImageURI(uri);
        }
        if (!TextUtils.isEmpty(UserInstance.getInstance().userInfo.nickName)) {
            tvNickname.setText(UserInstance.getInstance().userInfo.nickName);
        }
    }

    Bitmap bitmap;

    @Override
    protected void onResume() {
        super.onResume();
        //todo 修改分享的url
//        bitmap = ZXingUtils.createQRImage("http://192.168.1.111:8020/article/app_downLoad.html?recommender=" + UserInstance.getInstance().getUid() + "&extensionSource=EXTENSION_02", DensityUtil.dip2px(this, 230), DensityUtil.dip2px(this, 230));
        LogUtilH.e(Constants.Url.Host + "article/app_downLoad.html?recommender=" + UserInstance.getInstance().getUid() + "&extensionSource=EXTENSION_02");
        bitmap = ZXingUtils.createQRImage(Constants.Url.Host + "article/app_downLoad.html?recommender=" + UserInstance.getInstance().getUid() + "&extensionSource=EXTENSION_02", DensityUtil.dip2px(this, 230), DensityUtil.dip2px(this, 230));
        ivShareQr.setImageBitmap(bitmap);
    }


    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        oks.setTitle("泰晟健康");
        // titleUrl QQ和QQ空间跳转链接
//        oks.setTitleUrl(Constants.Url.Article.articleContent+articleId);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，确保SDcard下面存在此张图片
//        oks.setImagePath("/sdcard/test.jpg");
        oks.setImageData(bitmap);
        // url在微信、Facebook等平台中使用
//        oks.setUrl(Constants.Url.Article.articleContent+articleId);
        //启动分享
        oks.show(MobSDK.getContext());
    }




    class RecommendHistoryAdapter extends BaseAdapter {

        public Context mcontext;

        List<RecommendShareBean> mData = new ArrayList<RecommendShareBean>();

        public RecommendHistoryAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            if (mData == null) {
                return 0;
            } else {
                return mData.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 声明内部类
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_sharedhistory, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_recommenderName = convertView.findViewById(R.id.tv_recommenderName);
                util.tv_createTime = (TextView) convertView.findViewById(R.id.tv_createTime);
                util.tv_recommendstatus = convertView.findViewById(R.id.tv_recommenderName);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }

            RecommendShareBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(RecommendShareActivity.this, HealthCheckResultHistoryActivity.class);
//                    intent.putExtra("completeBatch", bean.completeBatch);
//                    intent.putExtra("remarks", bean.remarks);
//                    startActivity(intent);
                }
            });
            util.tv_recommenderName.setText(bean.recommendeeName);
            util.tv_createTime.setText(bean.createTime);
            if ("0".equals(bean.informationImprovementStatus)) {
                util.tv_recommendstatus.setText("用户尚未完成注册");
            } else {
                util.tv_recommendstatus.setText("邀请成功");
            }

            return convertView;
        }


        class Util {
            View ll_all;
            TextView tv_recommenderName;
            TextView tv_createTime;
            TextView tv_recommendstatus;
        }
    }
}
