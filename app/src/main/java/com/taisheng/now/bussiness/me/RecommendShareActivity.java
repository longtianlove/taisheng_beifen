package com.taisheng.now.bussiness.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mob.MobSDK;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.RecommendSharePostBean;
import com.taisheng.now.bussiness.bean.result.CheckHistoryBean;
import com.taisheng.now.bussiness.bean.result.RecommendShareBean;
import com.taisheng.now.bussiness.bean.result.RecommendSharedResultBean;
import com.taisheng.now.bussiness.healthfiles.HealthCheckResultHistoryActivity;
import com.taisheng.now.bussiness.healthfiles.ZhongyitizhiFragment;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.DensityUtil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ZXingUtils;
import com.taisheng.now.view.TaishengListView;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class RecommendShareActivity extends BaseActivity {
    View iv_back;


    SimpleDraweeView sdv_header;
    TextView tv_nickname;

    ImageView iv_share_qr;


    View ll_weixin;
    View ll_pengyouquan;


    View ll_sharelabel;
    View ll_share_all;
    TaishengListView lv_shared;
    RecommendHistoryAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendshare);
        initView();
        initData();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sdv_header = (SimpleDraweeView) findViewById(R.id.sdv_header);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);

        iv_share_qr = (ImageView) findViewById(R.id.iv_share_qr);

        ll_weixin = findViewById(R.id.ll_weixin);
        ll_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(Wechat.NAME);
            }
        });

        ll_pengyouquan = findViewById(R.id.ll_pengyouquan);
        ll_pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShare(WechatMoments.NAME);
            }
        });

        ll_sharelabel = findViewById(R.id.ll_sharelabel);
        ll_share_all = findViewById(R.id.ll_share_all);

        lv_shared = findViewById(R.id.lv_shared);
        madapter = new RecommendHistoryAdapter(this);
        lv_shared.setAdapter(madapter);
        lv_shared.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getMore();
            }
        });
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void initData() {
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
                            ll_sharelabel.setVisibility(View.VISIBLE);
                            ll_share_all.setVisibility(View.VISIBLE);
                            lv_shared.setLoading(false);


                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10 && message.result.records.size() > 0) {
                                lv_shared.setHasLoadMore(false);
                                lv_shared.setLoadAllViewText("暂时只有这么多结果");
                                lv_shared.setLoadAllFooterVisible(true);
                            } else {
                                lv_shared.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            ll_sharelabel.setVisibility(View.GONE);
                            ll_share_all.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<RecommendSharedResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
                ll_sharelabel.setVisibility(View.GONE);
                ll_share_all.setVisibility(View.GONE);
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
                            lv_shared.setLoading(false);


                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10 && message.result.records.size() > 0) {
                                lv_shared.setHasLoadMore(false);
                                lv_shared.setLoadAllViewText("暂时只有这么多结果");
                                lv_shared.setLoadAllFooterVisible(true);
                            } else {
                                lv_shared.setHasLoadMore(true);
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
            sdv_header.setImageURI(uri);
        }
        if (!TextUtils.isEmpty(UserInstance.getInstance().userInfo.nickName)) {
            tv_nickname.setText(UserInstance.getInstance().userInfo.nickName);
        }


    }

    Bitmap bitmap;

    @Override
    protected void onResume() {
        super.onResume();
        //todo 修改分享的url
        bitmap = ZXingUtils.createQRImage("http://192.168.1.111:8020/article/app_downLoad.html?recommender=" + UserInstance.getInstance().getUid() + "&extensionSource=EXTENSION_02", DensityUtil.dip2px(this, 230), DensityUtil.dip2px(this, 230));
        iv_share_qr.setImageBitmap(bitmap);
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
