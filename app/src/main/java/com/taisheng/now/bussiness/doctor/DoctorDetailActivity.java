package com.taisheng.now.bussiness.doctor;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.CollectAddorRemovePostBean;
import com.taisheng.now.bussiness.bean.post.ConnectDoctorPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorCommentPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorDetailPostBean;
import com.taisheng.now.bussiness.bean.post.DoctorNumberPostBean;
import com.taisheng.now.bussiness.bean.post.GuanzhuPostBean;
import com.taisheng.now.bussiness.bean.result.CollectAddorRemoveResultBean;
import com.taisheng.now.bussiness.bean.result.ConnectDoctorResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorBean;
import com.taisheng.now.bussiness.bean.result.DoctorCommentBean;
import com.taisheng.now.bussiness.bean.result.DoctorCommentResultBean;
import com.taisheng.now.bussiness.bean.result.DoctorNumberResultBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.chat.C2CActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
//import com.taisheng.now.selfshipin.util.WebrtcUtil;
import com.taisheng.now.shipin.TRTCMainActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.DoubleClickUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.StarGrade;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.chenjinshi.StatusBarUtil;
import com.tencent.trtc.TRTCCloudDef;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/7/1.
 */

public class DoctorDetailActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    View iv_back;


    SimpleDraweeView sdv_doctor_header;

    TextView tv_doctor_name;
    TextView tv_onlineStatus;
    TextView tv_title;
    TextView tv_workage;
    ScoreStar scorestar;
    TextView tv_jobintroduce;
    DoctorLabelWrapLayout dlwl_doctor_label;


    TextView tv_servicenumber;
    TextView tv_comment;
    TextView tv_guanzu;


    TextView tv_comment2;

    View ll_collect;
    TextView tv_collect_label;
    TextView tv_collect_show;
    View ll_zixun;
    TaishengListView lv_comments;
    DoctorCommentAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //沉浸式代码配置
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        //用来设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        setContentView(R.layout.activity_doctor_detail);


        initView();
        Intent intent = getIntent();
        doctorId = intent.getStringExtra("id");


    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        sdv_doctor_header = (SimpleDraweeView) findViewById(R.id.sdv_doctor_header);

        tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
        tv_onlineStatus = (TextView) findViewById(R.id.tv_onlineStatus);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_workage = (TextView) findViewById(R.id.tv_workage);
        scorestar = (ScoreStar) findViewById(R.id.scorestar);

        tv_jobintroduce = (TextView) findViewById(R.id.tv_jobintroduce);
        dlwl_doctor_label = (DoctorLabelWrapLayout) findViewById(R.id.dlwl_doctor_label);

        tv_servicenumber = (TextView) findViewById(R.id.tv_servicenumber);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_guanzu = (TextView) findViewById(R.id.tv_guanzu);
        tv_comment2 = (TextView) findViewById(R.id.tv_comment2);

        lv_comments = (TaishengListView) findViewById(R.id.lv_comments);
        madapter = new DoctorCommentAdapter(this);
        lv_comments.setListViewSlide(false);
        lv_comments.setAdapter(madapter);
        lv_comments.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctorComment();
            }
        });


        ll_collect = findViewById(R.id.ll_collect);
        ll_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastMiniDoubleClick()) {
                    return;
                }
                CollectAddorRemovePostBean bean = new CollectAddorRemovePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.collectionType = "1";
                bean.dataId = doctorId;
                ApiUtils.getApiService().collectionaddOrRemove(bean).enqueue(new TaiShengCallback<BaseBean<CollectAddorRemoveResultBean>>() {
                    @Override
                    public void onSuccess(Response<BaseBean<CollectAddorRemoveResultBean>> response, BaseBean<CollectAddorRemoveResultBean> message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                String resultFeedback = message.result.resultFeedback;
                                if ("0".equals(resultFeedback)) {
                                    tv_collect_label.setEnabled(false);
                                    tv_collect_show.setText("收藏");
                                } else {
                                    tv_collect_label.setEnabled(true);
                                    tv_collect_show.setText("已收藏");
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<CollectAddorRemoveResultBean>> call, Throwable t) {

                    }
                });
            }
        });
        tv_collect_label = (TextView) findViewById(R.id.tv_collect_label);
        tv_collect_show = (TextView) findViewById(R.id.tv_collect_show);
        ll_zixun = findViewById(R.id.ll_zixun);
        ll_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastMiniDoubleClick()) {
                    return;
                }
                DialogUtil.showToChatDialog(DoctorDetailActivity.this, new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                toChat();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatType = "video";

                                connectDoctor();

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatType = "audio";
                                connectDoctor();
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }
                );


            }
        });
    }



    public String chatType = "video";

    void connectDoctor() {
        ConnectDoctorPostBean bean = new ConnectDoctorPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.doctorId = doctorId;
        if ("video".equals(chatType)) {
            bean.type = "1";
        } else {
            bean.type = "0";
        }
        ApiUtils.getApiService().connectDoctor(bean).enqueue(new TaiShengCallback<BaseBean<ConnectDoctorResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ConnectDoctorResultBean>> response, BaseBean<ConnectDoctorResultBean> message) {
                ConnectDoctorResultBean bean = message.result;
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        mUserSig = bean.userSign;
                        onJoinRoomByTecent(bean.roomId, bean.userId);
//                        onJoinRoomBySelf(bean.roomId, bean.userId);
                        break;
                    case Constants.DOCTOR_BUSY:
                        ToastUtil.showTost("医生忙碌中,请稍后联系");
                        break;
                    case Constants.DOCTOR_NOEXIST:
                        ToastUtil.showTost("医生不存在");

                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ConnectDoctorResultBean>> call, Throwable t) {

            }
        });

    }

    private String mUserId = "";
    private String mUserSig = "";

    private void onJoinRoomByTecent(final int roomId, final String userId) {
        final Intent intent = new Intent(DoctorDetailActivity.this, TRTCMainActivity.class);
        if (doctorBean != null) {
            intent.putExtra("nickName", doctorBean.nickName);
            intent.putExtra("title", doctorBean.title);
            intent.putExtra("avatar", doctorBean.avatar);
            intent.putExtra("doctorId", doctorBean.id);
        }
        intent.putExtra("roomId", roomId);
        intent.putExtra("userId", userId);
        intent.putExtra("AppScene", TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        intent.putExtra("sdkAppId", Constants.SDKAPPID);
        intent.putExtra("userSig", mUserSig);
        intent.putExtra("chatType", chatType);
        startActivityForResult(intent, 1);
    }


    void toChat() {
        Intent intent = new Intent(DoctorDetailActivity.this, C2CActivity.class);
        intent.putExtra("targetId", doctorId);
        intent.putExtra("doctorAvator", doctorAvator);
        intent.putExtra("doctorName", doctorName);

        intent.putExtra("nickName", doctorBean.nickName);
//        intent.putExtra("title",doctorBean.title);
        intent.putExtra("avatar", doctorBean.avatar);
        intent.putExtra("doctorId", doctorBean.id);


        startActivity(intent);
    }


    void initData() {
        getDoctorDetail();
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new DoctorCommentPostBean();
        getDoctorComment();
        getServiceNumber();
        getBeCommentedNum();
        getBeDoctorAttentionNum();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    DoctorBean doctorBean;

    String doctorAvator;
    String doctorName;

    void getDoctorDetail() {
        DoctorDetailPostBean bean = new DoctorDetailPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.id = doctorId;
        ApiUtils.getApiService().doctorQueryById(bean).enqueue(new TaiShengCallback<BaseBean<DoctorBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorBean>> response, BaseBean<DoctorBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        doctorBean = message.result;
                        tv_doctor_name.setText(doctorBean.nickName);
                        doctorName = doctorBean.nickName;

                        if ("1".equals(doctorBean.onlineStatus)) {
                            tv_onlineStatus.setText("在线");
                        } else {
                            tv_onlineStatus.setText("忙碌");
                        }

                        tv_title.setText(doctorBean.title);
                        String fromMedicineTime = doctorBean.fromMedicineTime;
                        tv_workage.setText(getWorkYear(fromMedicineTime));
                        tv_jobintroduce.setText(doctorBean.jobIntroduction);

                        String goodDiseases = doctorBean.goodDiseases;
                        if (goodDiseases != null) {
                            String[] doctorlabel = goodDiseases.split(",");
                            dlwl_doctor_label.oneline = false;
                            dlwl_doctor_label.setData(doctorlabel, DoctorDetailActivity.this, 10, 5, 1, 5, 1, 4, 4, 4, 8);
                        }
                        String score = doctorBean.score;
                        scorestar.setScore(score);

                        if ("1".equals(doctorBean.isSc)) {
                            tv_collect_label.setEnabled(true);
                            tv_collect_show.setText("已收藏");

                        } else {
                            tv_collect_label.setEnabled(false);
                            tv_collect_show.setText("收藏");
                        }

                        if (doctorBean.img != null) {
                            Uri uri = Uri.parse(doctorBean.img);
                            sdv_doctor_header.setImageURI(uri);
                        }
                        doctorAvator = doctorBean.avatar;
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorBean>> call, Throwable t) {

            }
        });
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


    void getServiceNumber() {
        DoctorNumberPostBean bean = new DoctorNumberPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.id = doctorId;
        ApiUtils.getApiService().getDoctorServerNum(bean).enqueue(new TaiShengCallback<BaseBean<DoctorNumberResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorNumberResultBean>> response, BaseBean<DoctorNumberResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tv_servicenumber.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });
    }

    void getBeCommentedNum() {
        DoctorNumberPostBean bean = new DoctorNumberPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.id = doctorId;
        ApiUtils.getApiService().getBeCommentedNum(bean).enqueue(new TaiShengCallback<BaseBean<DoctorNumberResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorNumberResultBean>> response, BaseBean<DoctorNumberResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tv_comment.setText(message.result.countNum + "");
                        tv_comment2.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });
    }

    void getBeDoctorAttentionNum() {
        GuanzhuPostBean bean = new GuanzhuPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.dataId = doctorId;
        bean.collectionType = "1";
        ApiUtils.getApiService().getBeDoctorAttentionNum(bean).enqueue(new TaiShengCallback<BaseBean<DoctorNumberResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorNumberResultBean>> response, BaseBean<DoctorNumberResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tv_guanzu.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });

    }

    DoctorCommentPostBean bean;
    String doctorId;
    int PAGE_NO = 1;
    int PAGE_SIZE = 10;


    void getDoctorComment() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        bean.doctorId = doctorId;
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().doctorScoreList(bean).enqueue(new TaiShengCallback<BaseBean<DoctorCommentResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorCommentResultBean>> response, BaseBean<DoctorCommentResultBean> message) {
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:

                        if (message.result == null) {
                            return;
                        }
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_comments.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_comments.setHasLoadMore(false);
                                lv_comments.setLoadAllViewText("暂时只有这么多评论");
                                lv_comments.setLoadAllFooterVisible(true);
                            } else {
                                lv_comments.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_comments.setHasLoadMore(false);
                            lv_comments.setLoadAllViewText("暂时只有这么多评论");
                            lv_comments.setLoadAllFooterVisible(true);
                        }

                        break;

                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorCommentResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();

            }
        });
    }


    class DoctorCommentAdapter extends BaseAdapter {

        public Context mcontext;

        List<DoctorCommentBean> mData = new ArrayList<DoctorCommentBean>();

        public DoctorCommentAdapter(Context context) {
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
            Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_comment, null);
                util.sdv_header = (SimpleDraweeView) convertView.findViewById(R.id.sdv_header);
                util.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                util.tv_createTime = (TextView) convertView.findViewById(R.id.tv_createTime);
                util.starGrade = (StarGrade) convertView.findViewById(R.id.starGrade);
                util.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
//                util.tv_createtime= (TextView) convertView.findViewById(R.id.tv_createtime);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            DoctorCommentBean bean = mData.get(position);


            String temp_url = Constants.Url.File_Host + bean.avatar;
            if (bean.avatar == null || "".equals(bean.avatar)) {
                util.sdv_header.setBackgroundResource(R.drawable.icon_comment_avatar);

            } else {
                Uri uri = Uri.parse(temp_url);
                util.sdv_header.setImageURI(uri);
            }
            util.tv_nickname.setText(bean.nickName);
            util.tv_createTime.setText(bean.createTime);
            util.starGrade.setScore(bean.consultationScore);
            util.tv_content.setText(bean.content);
            return convertView;
        }


        class Util {
            SimpleDraweeView sdv_header;
            TextView tv_nickname;
            TextView tv_createTime;

            com.taisheng.now.view.StarGrade starGrade;
            TextView tv_content;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        switch (resultCode) {
            case TRTCMainActivity.TRTC_Normal_EXIT_RESULT:
                showGoRecommendDialog();
                break;


        }
    }



    public void showGoRecommendDialog() {
        final Dialog dialog = new AppDialog(this, R.layout.dialog_go_recommend, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, R.style.mystyle, Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(0);

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_go_recommend = (Button) dialog.findViewById(R.id.btn_go_recommend);


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btn_go_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Intent intent = new Intent(DoctorDetailActivity.this, DoctorCommentActivity.class);
                intent.putExtra("id", doctorId);
                startActivity(intent);

            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}









