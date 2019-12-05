package com.taisheng.now.bussiness.first;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.MoreShipinPostBean;
import com.taisheng.now.bussiness.bean.post.VideoOperatePostBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.bean.result.ShipinBean;
import com.taisheng.now.bussiness.bean.result.ShipinsResultBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.doctor.DoctorFragment;
import com.taisheng.now.bussiness.me.FuwuxieyiActivity;
import com.taisheng.now.bussiness.me.YisixieyiActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.jiankangjiangtang.SampleCoverVideo;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class MoreShipinActivity extends BaseActivity {
    View iv_back;

    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_shipins;
    ShipinsAdapter madapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreshipin);
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


        ptr_refresh = (MaterialDesignPtrFrameLayout)findViewById(R.id.ptr_refresh);
        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                getShipins();

            }
        });
        lv_shipins=findViewById(R.id.lv_shipins);
        madapter=new ShipinsAdapter(this);
        lv_shipins.setAdapter(madapter);
        lv_shipins.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getShipins();
            }
        });
        lv_shipins.setOnMScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(ShipinsAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        if(GSYVideoManager.isFullState(MoreShipinActivity.this)) {
                            return;
                        }
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        GSYVideoManager.releaseAllVideos();
                        madapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    void  initData(){
        getShipins();
    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getShipins(){
        MoreShipinPostBean bean=new MoreShipinPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().moreShiPin(bean).enqueue(new TaiShengCallback<BaseBean<ShipinsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShipinsResultBean>> response, BaseBean<ShipinsResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_shipins.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_shipins.setHasLoadMore(false);
                                lv_shipins.setLoadAllViewText("暂时只有这么多视频");
                                lv_shipins.setLoadAllFooterVisible(true);
                            } else {
                                lv_shipins.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_shipins.setHasLoadMore(false);
                            lv_shipins.setLoadAllViewText("暂时只有这么多视频");
                            lv_shipins.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ShipinsResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });

    }


    class ShipinsAdapter extends BaseAdapter{

        public static final String TAG = "ListNormalAdapter22";

        public Context mcontext;
        List<ShipinBean> mData = new ArrayList<ShipinBean>();

        public ShipinsAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.item_shipins, null);
                util.gsyVideoPlayer=convertView.findViewById(R.id.video_player);
                util.tv_shipintitle=convertView.findViewById(R.id.tv_shipintitle);
                util.ll_shipindianzan=convertView.findViewById(R.id.ll_shipindianzan);
                util.tv_shipindianzan=convertView.findViewById(R.id.tv_shipindianzan);
                util.ll_shipinguanzhu=convertView.findViewById(R.id.ll_shipinguanzhu);
                util.tv_shipinguanzhu=convertView.findViewById(R.id.tv_shipinguanzhu);
                util.tv_shipinbofangshu=convertView.findViewById(R.id.tv_shipinbofangshu);
                util.tv_dianzan=convertView.findViewById(R.id.tv_dianzan);
                util.tv_guanzhu=convertView.findViewById(R.id.tv_guanzhu);
                 convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ShipinBean bean = mData.get(position);
            Util finalUtil = util;

            if ("NO".equals(bean.videoPraiseFlag)) {
                finalUtil.tv_dianzan.setEnabled(true);
            } else {
                finalUtil.tv_dianzan.setEnabled(false);
            }

            if ("NO".equals(bean.userCollected)) {
                finalUtil.tv_guanzhu.setEnabled(true);
            } else {
                finalUtil.tv_guanzhu.setEnabled(false);
            }
            util.ll_shipindianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoOperatePostBean mbean=new VideoOperatePostBean();
                    mbean.userId=UserInstance.getInstance().getUid();
                    mbean.token=UserInstance.getInstance().getToken();
                    mbean.id=bean.id;
                    mbean.operateType="praise";

                    ApiUtils.getApiService().videoOperate(mbean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if(finalUtil.tv_dianzan.isEnabled()){
                                        String dianzanshuString=finalUtil.tv_shipindianzan.getText().toString();
                                        int dianzanshuint=Integer.parseInt(dianzanshuString)+1;
                                        finalUtil.tv_shipindianzan.setText(dianzanshuint+"");
                                    }else{
                                        String dianzanshuString=finalUtil.tv_shipindianzan.getText().toString();
                                        int dianzanshuint=Integer.parseInt(dianzanshuString)-1;
                                        finalUtil.tv_shipindianzan.setText(dianzanshuint+"");
                                    }
                                    finalUtil.tv_dianzan.setEnabled(!finalUtil.tv_dianzan.isEnabled());
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });

                }
            });
            util.ll_shipinguanzhu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoOperatePostBean mbean=new VideoOperatePostBean();
                    mbean.userId=UserInstance.getInstance().getUid();
                    mbean.token=UserInstance.getInstance().getToken();
                    mbean.id=bean.id;
                    mbean.operateType="collection";

                    ApiUtils.getApiService().videoOperate(mbean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if(finalUtil.tv_guanzhu.isEnabled()){
                                        String dianzanshuString=finalUtil.tv_shipinguanzhu.getText().toString();
                                        int dianzanshuint=Integer.parseInt(dianzanshuString)+1;
                                        finalUtil.tv_shipinguanzhu.setText(dianzanshuint+"");
                                    }else{
                                        String dianzanshuString=finalUtil.tv_shipinguanzhu.getText().toString();
                                        int dianzanshuint=Integer.parseInt(dianzanshuString)-1;
                                        finalUtil.tv_shipinguanzhu.setText(dianzanshuint+"");
                                    }
                                    finalUtil.tv_guanzhu.setEnabled(!finalUtil.tv_guanzhu.isEnabled());
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });

                }
            });


            util.gsyVideoPlayer.loadCoverImage(bean.videoBanner, R.mipmap.xxx1);
            util.gsyVideoPlayer.setUpLazy(bean.videoUrl, true, null, null, "");
            //增加title
            util.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
            //设置返回键
            util.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
            //设置全屏按键功能
            util.gsyVideoPlayer.getFullscreenButton().setVisibility(View.VISIBLE);
            //设置全屏按键功能
            Util finalUtil1 = util;
            util.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalUtil1.gsyVideoPlayer.startWindowFullscreen(MoreShipinActivity.this, false, true);
                }
            });
            //防止错位设置
            util.gsyVideoPlayer.setPlayTag(TAG);
            util.gsyVideoPlayer.setPlayPosition(position);
            //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
            util.gsyVideoPlayer.setAutoFullWithSize(false);
            //音频焦点冲突时是否释放
            util.gsyVideoPlayer.setReleaseWhenLossAudio(false);
            //全屏动画
            util.gsyVideoPlayer.setShowFullAnimation(true);
            //小屏时不触摸滑动
            util.gsyVideoPlayer.setIsTouchWiget(false);


            util.tv_shipintitle.setText(bean.videoTitle);
            util.tv_shipindianzan.setText(bean.videoPraise+"");
            util.tv_shipinguanzhu.setText(bean.collectionCount+"");
            util.tv_shipinbofangshu.setText(bean.videoPlayTimes+"");
            
            

            return convertView;
        }


        class Util {

            SampleCoverVideo gsyVideoPlayer;
            TextView tv_shipintitle;
            View ll_shipindianzan;
            TextView tv_shipindianzan;
            View ll_shipinguanzhu;
            TextView tv_shipinguanzhu;
            TextView tv_shipinbofangshu;
            TextView tv_dianzan;
            TextView tv_guanzhu;
        }

    }



    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
