package com.taisheng.now.bussiness.first;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.bean.post.MoreShipinPostBean;
import com.taisheng.now.bussiness.bean.post.VideoOperatePostBean;
import com.taisheng.now.bussiness.bean.result.ShipinBean;
import com.taisheng.now.bussiness.bean.result.ShipinsResultBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.jiankangjiangtang.SampleCoverVideo;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class MoreShipinActivity extends BaseHActivity {

    @BindView(R.id.lv_shipins)
    TaishengListView lvShipins;
    @BindView(R.id.ptr_refresh)
    MaterialDesignPtrFrameLayout ptrRefresh;
    private ShipinsAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;


    @Override
    public void initView() {
        setContentView(R.layout.activity_moreshipin);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    public void initData() {
        madapter = new ShipinsAdapter(this);
        lvShipins.setAdapter(madapter);

    }

    @Override
    public void addData() {
        getShipins();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.more_video));
    }

    void initViews() {

        /**
         * 下拉刷新
         */
        ptrRefresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                getShipins();

            }
        });

        lvShipins.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getShipins();
            }
        });
        lvShipins.setOnMScrollListener(new AbsListView.OnScrollListener() {
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
                        if (GSYVideoManager.isFullState(MoreShipinActivity.this)) {
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


    void getShipins() {
        MoreShipinPostBean bean = new MoreShipinPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().moreShiPin(bean).enqueue(new TaiShengCallback<BaseBean<ShipinsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShipinsResultBean>> response, BaseBean<ShipinsResultBean> message) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvShipins.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lvShipins.setHasLoadMore(false);
                                lvShipins.setLoadAllViewText("暂时只有这么多视频");
                                lvShipins.setLoadAllFooterVisible(true);
                            } else {
                                lvShipins.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lvShipins.setHasLoadMore(false);
                            lvShipins.setLoadAllViewText("暂时只有这么多视频");
                            lvShipins.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ShipinsResultBean>> call, Throwable t) {
                ptrRefresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });

    }

    class ShipinsAdapter extends BaseAdapter {

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
                util.gsyVideoPlayer = convertView.findViewById(R.id.video_player);
                util.tv_shipintitle = convertView.findViewById(R.id.tv_shipintitle);
                util.ll_shipindianzan = convertView.findViewById(R.id.ll_shipindianzan);
                util.tv_shipindianzan = convertView.findViewById(R.id.tv_shipindianzan);
                util.ll_shipinguanzhu = convertView.findViewById(R.id.ll_shipinguanzhu);
                util.tv_shipinguanzhu = convertView.findViewById(R.id.tv_shipinguanzhu);
                util.tv_shipinbofangshu = convertView.findViewById(R.id.tv_shipinbofangshu);
                util.iv_shipindianzan = convertView.findViewById(R.id.iv_shipindianzan);
                util.iv_shipinguanzhu = convertView.findViewById(R.id.iv_shipinguanzhu);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ShipinBean bean = mData.get(position);
            Util finalUtil = util;

            if ("NO".equals(bean.videoPraiseFlag)) {
                finalUtil.tv_shipindianzan.setSelected(false);
                finalUtil.iv_shipindianzan.setSelected(false);
            } else {
                finalUtil.tv_shipindianzan.setSelected(true);
                finalUtil.iv_shipindianzan.setSelected(true);
            }

            if ("NO".equals(bean.userCollected)) {
                finalUtil.tv_shipinguanzhu.setSelected(false);
                finalUtil.iv_shipinguanzhu.setSelected(false);
            } else {
                finalUtil.tv_shipinguanzhu.setSelected(true);
                finalUtil.iv_shipinguanzhu.setSelected(true);
            }
            util.ll_shipindianzan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VideoOperatePostBean mbean = new VideoOperatePostBean();
                    mbean.userId = UserInstance.getInstance().getUid();
                    mbean.token = UserInstance.getInstance().getToken();
                    mbean.id = bean.id;
                    mbean.operateType = "praise";

                    ApiUtils.getApiService().videoOperate(mbean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if (!finalUtil.tv_shipindianzan.isSelected()) {
                                        String dianzanshuString = finalUtil.tv_shipindianzan.getText().toString();
                                        int dianzanshuint = Integer.parseInt(dianzanshuString) + 1;
                                        finalUtil.tv_shipindianzan.setText(dianzanshuint + "");
                                    } else {
                                        String dianzanshuString = finalUtil.tv_shipindianzan.getText().toString();
                                        int dianzanshuint = Integer.parseInt(dianzanshuString) - 1;
                                        finalUtil.tv_shipindianzan.setText(dianzanshuint + "");
                                    }
                                    finalUtil.tv_shipindianzan.setSelected(!finalUtil.tv_shipindianzan.isSelected());
                                    finalUtil.iv_shipindianzan.setSelected(!finalUtil.iv_shipindianzan.isSelected());
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
                    VideoOperatePostBean mbean = new VideoOperatePostBean();
                    mbean.userId = UserInstance.getInstance().getUid();
                    mbean.token = UserInstance.getInstance().getToken();
                    mbean.id = bean.id;
                    mbean.operateType = "collection";

                    ApiUtils.getApiService().videoOperate(mbean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if (!finalUtil.tv_shipinguanzhu.isSelected()) {
                                        String dianzanshuString = finalUtil.tv_shipinguanzhu.getText().toString();
                                        int dianzanshuint = Integer.parseInt(dianzanshuString) + 1;
                                        finalUtil.tv_shipinguanzhu.setText(dianzanshuint + "");
                                    } else {
                                        String dianzanshuString = finalUtil.tv_shipinguanzhu.getText().toString();
                                        int dianzanshuint = Integer.parseInt(dianzanshuString) - 1;
                                        finalUtil.tv_shipinguanzhu.setText(dianzanshuint + "");
                                    }
                                    finalUtil.tv_shipinguanzhu.setSelected(!finalUtil.tv_shipinguanzhu.isSelected());
                                    finalUtil.iv_shipinguanzhu.setSelected(!finalUtil.iv_shipinguanzhu.isSelected());
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });

                }
            });

            util.gsyVideoPlayer.setEnlargeImageRes(R.drawable.icon_full);
            util.gsyVideoPlayer.loadCoverImage(bean.videoBanner, R.mipmap.health01);
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
            util.tv_shipindianzan.setText(bean.videoPraise + "");
            util.tv_shipinguanzhu.setText(bean.collectionCount + "");
            util.tv_shipinbofangshu.setText(bean.videoPlayTimes + "");


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
            ImageView iv_shipindianzan;
            ImageView iv_shipinguanzhu;
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
