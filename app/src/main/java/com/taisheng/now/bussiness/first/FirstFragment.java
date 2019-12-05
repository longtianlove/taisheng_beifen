package com.taisheng.now.bussiness.first;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.article.SecretActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.post.RecommendDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.VideoOperatePostBean;
import com.taisheng.now.bussiness.bean.result.ArticleBean;
import com.taisheng.now.bussiness.bean.result.ArticleResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorsResultBean;
import com.taisheng.now.bussiness.bean.result.ShipinBean;
import com.taisheng.now.bussiness.bean.result.ShipinsResultBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.doctor.DoctorsFenleiActivity;
import com.taisheng.now.bussiness.healthfiles.HealthCheckActivity;
import com.taisheng.now.bussiness.article.ArticleContentActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.BindWatchsActivity;
import com.taisheng.now.bussiness.watch.WatchsListActivity;
import com.taisheng.now.bussiness.watch.bean.result.WatchListBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.GuideView;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.WithScrolleViewListView;
import com.taisheng.now.view.banner.BannerViewPager;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class FirstFragment extends BaseFragment {
    TextView tv_location_city;
    View ll_search;


    public ScrollView scl_bag;


    MaterialDesignPtrFrameLayout ptr_refresh;

    private FrameLayout bannerContaner;
    BannerViewPager bannerViewPager;
    private View bannerView;


//    HorizontalListViewAdapter horizontalListViewAdapter;

    ZhuanjiaAdapter zhuanjiaAdapter;

    TextView tv_doctor_more;
    TextView tv_secret_more;

//    View ll_shipin_all;

    View ll_shishizixun;
    View ll_sushenhufu;
    View ll_yiliaoyangsheng;
    View ll_muyingyunyu;
    View ll_yingjizixun;
    View ll_jianshenyundong;
    View ll_yongyaozhidao;

    View ll_jiankangceping;

    GuideView guideView;


    com.taisheng.now.view.WithScrolleViewListView lv_articles;
    ArticleAdapter madapter;


    View tv_shipin_more;
    public StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();


        return rootView;
    }

    void initView(View rootView) {
        scl_bag = (ScrollView) rootView.findViewById(R.id.scl_bag);


        ptr_refresh = (MaterialDesignPtrFrameLayout) rootView.findViewById(R.id.ptr_refresh);


        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }
        });
//        ll_shipin_all=rootView.findViewById(R.id.ll_shipin_all);
        ll_shishizixun = rootView.findViewById(R.id.ll_shishizixun);
        ll_shishizixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(1);
                Intent intent = new Intent(getActivity(), DoctorsFenleiActivity.class);
                DoctorsFenleiActivity.selectTab = 0;
                startActivity(intent);

            }
        });
        ll_sushenhufu = rootView.findViewById(R.id.ll_sushenhufu);
        ll_sushenhufu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(2);
//                SecretActivity.selectTab = 0;
//                if (SecretActivity.tl_tab != null) {
//                    (SecretActivity.tl_tab.getTabAt(0)).select();
//                }
//                Intent intent=new Intent(getActivity(), SecretActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), DoctorsFenleiActivity.class);
                DoctorsFenleiActivity.selectTab = 1;
                startActivity(intent);
            }
        });
        ll_yiliaoyangsheng = rootView.findViewById(R.id.ll_yiliaoyangsheng);
        ll_yiliaoyangsheng.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(2);
//                ((MainActivity) getActivity()).marketFragment.selectTab = 2;
//                if (((MainActivity) getActivity()).marketFragment.tl_tab != null) {
//                    ((MainActivity) getActivity()).marketFragment.tl_tab.getTabAt(2).select();
//                }


//                SecretActivity.selectTab = 2;
//                if (SecretActivity.tl_tab != null) {
//                    (SecretActivity.tl_tab.getTabAt(2)).select();
//                }
//                Intent intent=new Intent(getActivity(), SecretActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(), DoctorsFenleiActivity.class);
                DoctorsFenleiActivity.selectTab = 2;
                startActivity(intent);
            }
        });
        ll_muyingyunyu = rootView.findViewById(R.id.ll_muyingyunyu);
        ll_muyingyunyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(2);
//                ((MainActivity) getActivity()).marketFragment.selectTab = 4;
//                if (((MainActivity) getActivity()).marketFragment.tl_tab != null) {
//                    ((MainActivity) getActivity()).marketFragment.tl_tab.getTabAt(4).select();
//                }


//                SecretActivity.selectTab = 4;
//                if (SecretActivity.tl_tab != null) {
//                    (SecretActivity.tl_tab.getTabAt(4)).select();
//                }
//                Intent intent=new Intent(getActivity(), SecretActivity.class);
//                startActivity(intent);
                //跳转到商城
                ((MainActivity) getActivity()).showFragment(2);

            }
        });
        ll_yingjizixun = rootView.findViewById(R.id.ll_yingjizixun);
        ll_yingjizixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(1);

                Intent intent = new Intent(getActivity(), MoreShipinActivity.class);
                startActivity(intent);
            }
        });
        ll_jianshenyundong = rootView.findViewById(R.id.ll_jianshenyundong);
        ll_jianshenyundong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(2);
//                ((MainActivity) getActivity()).marketFragment.selectTab = 1;
//                if (((MainActivity) getActivity()).marketFragment.tl_tab != null) {
//                    ((MainActivity) getActivity()).marketFragment.tl_tab.getTabAt(1).select();
//                }


//                SecretActivity.selectTab = 1;
//                if (SecretActivity.tl_tab != null) {
//                    (SecretActivity.tl_tab.getTabAt(1)).select();
//                }
//                Intent intent=new Intent(getActivity(), SecretActivity.class);
//                startActivity(intent);


                BaseListPostBean bean = new BaseListPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.pageNo=1;
                bean.pageSize=10;
                ApiUtils.getApiService().queryDeviceBinding(bean).enqueue(new TaiShengCallback<BaseBean<WatchListResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<WatchListResultBean>> response, BaseBean<WatchListResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (message == null || message.result.records == null || message.result.records.size() == 0) {
                                    //跳转到绑定手表页
                                    Intent intent = new Intent(getActivity(), BindWatchsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), WatchsListActivity.class);
                                    startActivity(intent);
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<WatchListResultBean>> call, Throwable t) {

                    }
                });


            }
        });
        ll_yongyaozhidao = rootView.findViewById(R.id.ll_jiangkangbaodian);
        ll_yongyaozhidao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((MainActivity) getActivity()).showFragment(2);
//                if (((MainActivity) getActivity()).marketFragment.tl_tab != null) {
//                    ((MainActivity) getActivity()).marketFragment.tl_tab.getTabAt(3).select();
//                }

//                SecretActivity.selectTab = 3;
//                if (SecretActivity.tl_tab != null) {
//                    (SecretActivity.tl_tab.getTabAt(3)).select();
//                }
                Intent intent = new Intent(getActivity(), SecretActivity.class);
                startActivity(intent);
            }
        });
        ll_jiankangceping = rootView.findViewById(R.id.ll_jiankangceping);
        ll_jiankangceping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HealthCheckActivity.class);
                startActivity(intent);
            }
        });

        tv_location_city = (TextView) rootView.findViewById(R.id.tv_location_city);
        ll_search = rootView.findViewById(R.id.ll_search);

        bannerContaner = (FrameLayout) rootView.findViewById(R.id.bannerContaner);
        bannerContaner.setVisibility(View.VISIBLE);
        bannerViewPager = new BannerViewPager(mActivity);
        bannerViewPager.setLocalPictureIds();
        bannerViewPager.setmScrollSpeed(500);
        bannerViewPager.madapter.notifyDataSetChanged();
        bannerView = bannerViewPager.getContentView();
        bannerViewPager.setOnItemClickListener(new BannerViewPager.ViewPagerItemListener() {
            @Override
            public void onViewPagerItemClick(int i) {

            }
        });
        bannerContaner.addView(bannerView);


//        hl_zhuanjia = (HorizontalListView) rootView.findViewById(R.id.hl_zhuanjia);
//        horizontalListViewAdapter = new HorizontalListViewAdapter();
//        hl_zhuanjia.setAdapter(horizontalListViewAdapter);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_zhuanjia);

        LinearLayoutManager layout = new LinearLayoutManager(mActivity);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        recyclerView.setLayoutManager(layout);
//        //添加Android自带的分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration());


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        zhuanjiaAdapter = new ZhuanjiaAdapter();
        recyclerView.setAdapter(zhuanjiaAdapter);


        tv_doctor_more = (TextView) rootView.findViewById(R.id.tv_doctor_more);
        tv_doctor_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showFragment(1);
            }
        });
        tv_secret_more = (TextView) rootView.findViewById(R.id.tv_secret_more);
        tv_secret_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).showFragment(2);
            }
        });
        lv_articles = (WithScrolleViewListView) rootView.findViewById(R.id.lv_articles);
        madapter = new ArticleAdapter(mActivity);
        lv_articles.setAdapter(madapter);
        if (!SPUtil.getGUIDE()) {
//文字图片
            final ImageView iv1 = new ImageView(getActivity());
            iv1.setImageResource(R.drawable.guide_word);
            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv1.setLayoutParams(params1);

            //我知道啦
            final ImageView iv2 = new ImageView(getActivity());
            iv2.setImageResource(R.drawable.guide_know);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv2.setLayoutParams(params2);

            guideView = GuideView.Builder
                    .newInstance(getActivity())
                    .setTargetView(ll_jiankangceping) //设置目标view
                    .setTextGuideView(iv1)   //设置文字图片
                    .setCustomGuideView(iv2)  //设置 我知道啦图片
                    .setOffset(0, 80)      //偏移量 x=0 y=80
                    .setDirction(GuideView.Direction.BOTTOM)  //方向
                    .setShape(GuideView.MyShape.CIRCULAR)  //圆形
                    .setRadius(0)               //圆角
                    .setContain(false)             //透明的方块时候包含目标view 默认false
                    .setBgColor(getResources().getColor(R.color.bg_shadow))  //背景颜色

                    .build();

            guideView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideView.hide();
                    SPUtil.putGUIDE(true);
                }
            });
            iv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    guideView.hide();
                    SPUtil.putGUIDE(true);

                }
            });
            guideView.show();
        }
        tv_shipin_more = rootView.findViewById(R.id.tv_shipin_more);
        tv_shipin_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MoreShipinActivity.class);
                startActivity(intent);
            }
        });
        videoPlayer = (StandardGSYVideoPlayer) rootView.findViewById(R.id.video_player);
        initShipin();
        initShipinDianzan(rootView);


    }


    String source1;
    ImageView imageView;

    void initShipin() {
        source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";

        //增加封面
        imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);
        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.GONE);
        //设置旋转
        orientationUtils = new OrientationUtils(getActivity(), videoPlayer);
        videoPlayer.getFullscreenButton().setVisibility(View.VISIBLE);
//        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                orientationUtils.resolveByClick();
                videoPlayer.startWindowFullscreen(getActivity(), false, true);

            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(false);
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        videoPlayer.startPlayLogic();


    }

    TextView tv_shipintitle;
    View ll_shipindianzan;
    TextView tv_dianzan;
    TextView tv_shipindianzan;
    View ll_shipinguanzhu;
    TextView tv_guanzhu;
    TextView tv_shipinguanzhu;
    TextView tv_shipinbofangshu;


    String shipinId;

    void initShipinDianzan(View rootView) {
        tv_shipintitle = rootView.findViewById(R.id.tv_shipintitle);
        tv_dianzan = rootView.findViewById(R.id.tv_dianzan);
        ll_shipindianzan = rootView.findViewById(R.id.ll_shipindianzan);
        ll_shipindianzan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                VideoOperatePostBean bean = new VideoOperatePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.id = shipinId;
                bean.operateType = "praise";

                ApiUtils.getApiService().videoOperate(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (tv_dianzan.isEnabled()) {
                                    String dianzanshuString = tv_shipindianzan.getText().toString();
                                    int dianzanshuint = Integer.parseInt(dianzanshuString) + 1;
                                    tv_shipindianzan.setText(dianzanshuint + "");
                                } else {
                                    String dianzanshuString = tv_shipindianzan.getText().toString();
                                    int dianzanshuint = Integer.parseInt(dianzanshuString) - 1;
                                    tv_shipindianzan.setText(dianzanshuint + "");
                                }
                                tv_dianzan.setEnabled(!tv_dianzan.isEnabled());
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        tv_shipindianzan = rootView.findViewById(R.id.tv_shipindianzan);
        ll_shipinguanzhu = rootView.findViewById(R.id.ll_shipinguanzhu);
        ll_shipinguanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoOperatePostBean bean = new VideoOperatePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.id = shipinId;
                bean.operateType = "collection";

                ApiUtils.getApiService().videoOperate(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                if (tv_guanzhu.isEnabled()) {
                                    String dianzanshuString = tv_shipinguanzhu.getText().toString();
                                    int dianzanshuint = Integer.parseInt(dianzanshuString) + 1;
                                    tv_shipinguanzhu.setText(dianzanshuint + "");
                                } else {
                                    String dianzanshuString = tv_shipinguanzhu.getText().toString();
                                    int dianzanshuint = Integer.parseInt(dianzanshuString) - 1;
                                    tv_shipinguanzhu.setText(dianzanshuint + "");
                                }
                                tv_guanzhu.setEnabled(!tv_guanzhu.isEnabled());
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });
        tv_guanzhu = rootView.findViewById(R.id.tv_guanzhu);
        tv_shipinguanzhu = rootView.findViewById(R.id.tv_shipinguanzhu);
        tv_shipinbofangshu = rootView.findViewById(R.id.tv_shipinbofangshu);

    }


    void initData() {

        getRecommendDoctors();
        getRecommendShipin();
        getHotArticle();

    }

    void getRecommendDoctors() {
        RecommendDoctorPostBean bean = new RecommendDoctorPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 5;
        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().recommendList(bean).enqueue(new TaiShengCallback<BaseBean<DoctorsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorsResultBean>> response, BaseBean<DoctorsResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        zhuanjiaAdapter.doctors = message.result.records;
                        zhuanjiaAdapter.notifyDataSetChanged();
                        break;
                }


            }

            @Override
            public void onFail(Call<BaseBean<DoctorsResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });
    }


    public class ZhuanjiaAdapter extends RecyclerView.Adapter<ZhuanjiaAdapter.ViewHolder> {

        public List<DoctorBean> doctors;

        @Override
        public ZhuanjiaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zhuanjia, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;

        }

        @Override
        public void onBindViewHolder(ZhuanjiaAdapter.ViewHolder util, int position) {
            DoctorBean bean = doctors.get(position);
            if (position == (doctors.size() - 1)) {
                util.view_label.setVisibility(View.GONE);
            } else {
                util.view_label.setVisibility(View.VISIBLE);

            }
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, DoctorDetailActivity.class);
                    intent.putExtra("id", bean.id);
                    intent.putExtra("nickName", bean.nickName);
                    intent.putExtra("title", bean.title);
                    intent.putExtra("fromMedicineTime", bean.fromMedicineTime);
                    intent.putExtra("jobIntroduction", bean.jobIntroduction);
                    intent.putExtra("score", bean.score);
                    intent.putExtra("goodDiseases", bean.goodDiseases);
                    startActivity(intent);
                }
            });

            if (bean.avatar != null) {
                Uri uri = Uri.parse(bean.avatar);
                util.sdv_header.setImageURI(uri);
            }
            util.tv_doctor_name.setText(bean.nickName);


            util.tv_workage.setText(getWorkYear(bean.fromMedicineTime));
            if (bean.goodDiseases != null) {
                String[] doctorlabel = bean.goodDiseases.split(",");
                util.dlwl_doctor_label.setData(doctorlabel, mActivity, 10, 5, 1, 5, 1, 4, 4, 4, 8);

            }

            if (bean.score != null) {
                util.scorestar.setScore(bean.score);
            }
        }


        String getWorkYear(String fromMedicineTime) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            ParsePosition pos = new ParsePosition(0);
            Date strtodate = null;
            try {
                strtodate = formatter.parse(fromMedicineTime);
                Date currentTime = new Date();
                return currentTime.getYear() - strtodate.getYear() <= 0 ? "1" : currentTime.getYear() - strtodate.getYear() + "";

            } catch (Exception e) {
                Log.e("firstfragment-getwork", e.getMessage());
                return "1";
            }


        }

        @Override
        public int getItemCount() {
            if (doctors == null) {
                return 0;
            } else {
                return doctors.size();
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View ll_all;
            SimpleDraweeView sdv_header;
            TextView tv_doctor_name;
            TextView tv_workage;
            DoctorLabelWrapLayout dlwl_doctor_label;
            ScoreStar scorestar;
            View view_label;

            public ViewHolder(View view) {
                super(view);
                ll_all = view.findViewById(R.id.ll_all);
                sdv_header = (SimpleDraweeView) view.findViewById(R.id.sdv_header);
                tv_doctor_name = (TextView) view.findViewById(R.id.tv_doctor_name);
                tv_workage = (TextView) view.findViewById(R.id.tv_workage);
                dlwl_doctor_label = (DoctorLabelWrapLayout) view.findViewById(R.id.dlwl_doctor_label);
                scorestar = (ScoreStar) view.findViewById(R.id.scorestar);
                view_label = view.findViewById(R.id.view_label);
            }
        }


    }


    void getRecommendShipin() {
        BasePostBean bean = new BasePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        DialogUtil.showProgress(getActivity(), "");
        ApiUtils.getApiService().recommendShiPin(bean).enqueue(new TaiShengCallback<BaseBean<ShipinsResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ShipinsResultBean>> response, BaseBean<ShipinsResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
//                            ll_shipin_all.setVisibility(View.VISIBLE);
//                            madapter.mData.addAll(message.result.records);
                            ShipinBean bean = message.result.records.get(0);
                            shipinId = bean.id;
                            source1 = bean.videoUrl;
                            videoPlayer.setUp(bean.videoUrl, true, "测试视频");
                            Glide.with(getContext().getApplicationContext())
                                    .setDefaultRequestOptions(
                                            new RequestOptions()
                                                    .frame(1000000)
                                                    .centerCrop()
                                                    .error(R.mipmap.xxx1)
                                                    .placeholder(R.mipmap.xxx1))
                                    .load(bean.videoBanner)
                                    .into(imageView);

                            tv_shipintitle.setText(bean.videoTitle);
                            tv_shipindianzan.setText(bean.videoPraise + "");
                            if ("NO".equals(bean.videoPraiseFlag)) {
                                tv_dianzan.setEnabled(true);
                            } else {
                                tv_dianzan.setEnabled(false);
                            }

                            tv_shipinguanzhu.setText(bean.collectionCount + "");
                            if ("NO".equals(bean.userCollected)) {
                                tv_guanzhu.setEnabled(true);
                            } else {
                                tv_guanzhu.setEnabled(false);
                            }
                            tv_shipinbofangshu.setText(bean.videoPlayTimes + "");
                        } else {
//                            //没有消息

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


    void getHotArticle() {

        BasePostBean bean = new BasePostBean();
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();


//        DialogUtil.showProgress(mActivity, "");
        ApiUtils.getApiService().hotArticleList(bean).enqueue(new TaiShengCallback<BaseBean<ArticleResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ArticleResultBean>> response, BaseBean<ArticleResultBean> message) {
//                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result != null && message.result.records.size() > 0) {
                            //有消息
//                            PAGE_NO++;
                            madapter.mData.clear();
                            madapter.mData.addAll(message.result.records);
//                            if(message.result.size()<10){
//                                lv_articles.setHasLoadMore(false);
//                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
//                                lv_articles.setLoadAllFooterVisible(false);
//                            }else{
//                                lv_articles.setHasLoadMore(true);
//                            }
                            madapter.notifyDataSetChanged();
                        } else {
//                            //没有消息
//                            lv_articles.setHasLoadMore(false);
//                            lv_articles.setLoadAllViewText("暂时只有这么多文章");
//                            lv_articles.setLoadAllFooterVisible(false);
                        }

                        break;

                }

            }

            @Override
            public void onFail(Call<BaseBean<ArticleResultBean>> call, Throwable t) {
//                DialogUtil.closeProgress();
            }
        });
    }

    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        List<ArticleBean> mData = new ArrayList<ArticleBean>();

        public ArticleAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
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
            ArticleAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new ArticleAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_article, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_article = (SimpleDraweeView) convertView.findViewById(R.id.sdv_article);
                util.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                util.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                util.tv_typename = (TextView) convertView.findViewById(R.id.tv_typename);
                util.tv_createtime = (TextView) convertView.findViewById(R.id.tv_createtime);

                convertView.setTag(util);
            } else {
                util = (ArticleAdapter.Util) convertView.getTag();
            }
            ArticleBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ArticleContentActivity.class);
                    intent.putExtra("articleId", bean.id);
                    intent.putExtra("articlePic", bean.picUrl);
                    intent.putExtra("summary", bean.summary);
                    intent.putExtra("title", bean.title);
                    startActivity(intent);
                }
            });

            String temp_url = bean.picUrl;
            if (temp_url == null || "".equals(temp_url)) {
                util.sdv_article.setBackgroundResource(R.drawable.article_default);

            } else {
                Uri uri = Uri.parse(temp_url);
                util.sdv_article.setImageURI(uri);
            }
            util.tv_title.setText(bean.title);
            util.tv_content.setText(bean.summary);
//            try {
//                if (bean.content != null) {
//                    util.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
//                    RichText.fromHtml(bean.content).into(util.tv_content);
//                }
//            } catch (Exception e) {
//                Log.e("article", e.getMessage());
//            }


            util.tv_typename.setText(bean.typeName);
            util.tv_createtime.setText(bean.createTime);

            return convertView;
        }


        class Util {
            View ll_all;
            SimpleDraweeView sdv_article;
            TextView tv_title;
            TextView tv_content;
            TextView tv_typename;
            TextView tv_createtime;

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        videoPlayer.onVideoResume();

        videoPlayer.setUp(source1, true, "测试视频");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        GSYVideoManager.releaseAllVideos();
//        if (orientationUtils != null)
//            orientationUtils.releaseListener();
    }


}
