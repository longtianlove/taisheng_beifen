package com.taisheng.now.bussiness.doctor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
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
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.chat.C2CActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.shipin.TRTCMainActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.DoubleClickUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.StarGrade;
import com.taisheng.now.view.TaishengListView;
import com.tencent.trtc.TRTCCloudDef;
import com.th.j.commonlibrary.utils.LogUtilH;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

//import com.taisheng.now.selfshipin.util.WebrtcUtil;

/**
 * Created by dragon on 2019/7/1.
 */

public class DoctorDetailActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.sdv_doctor_header)
    SimpleDraweeView sdvDoctorHeader;
    @BindView(R.id.tv_doctor_name)
    TextView tvDoctorName;
    @BindView(R.id.tv_onlineStatus)
    TextView tvOnlineStatus;
    @BindView(R.id.ll_doctor1)
    LinearLayout llDoctor1;
    @BindView(R.id.tv_title_doctor)
    TextView tvTitleDoctor;
    @BindView(R.id.tv_workage)
    TextView tvWorkage;
    @BindView(R.id.tv_servicenumber)
    TextView tvServicenumber;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.tv_guanzu)
    TextView tvGuanzu;
    @BindView(R.id.scorestar)
    ScoreStar scorestar;
    @BindView(R.id.tv_jobintroduce)
    TextView tvJobintroduce;
    @BindView(R.id.dlwl_doctor_label)
    DoctorLabelWrapLayout dlwlDoctorLabel;
    @BindView(R.id.tv_comment2)
    TextView tvComment2;
    @BindView(R.id.lv_comments)
    TaishengListView lvComments;
    @BindView(R.id.tv_collect_label)
    TextView tvCollectLabel;
    @BindView(R.id.tv_collect_show)
    TextView tvCollectShow;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.ll_zixun)
    LinearLayout llZixun;
    private DoctorCommentAdapter madapter;
    private DoctorCommentPostBean bean;
    private String doctorId;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;
    private DoctorBean doctorBean;
    private String doctorAvator;
    private String doctorName;
    public String chatType = "video";
    private String mUserId = "";
    private String mUserSig = "";
    private List<DoctorCommentBean> scoreList;

    @Override
    public void initView() {
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        scoreList=new ArrayList<>();
        madapter = new DoctorCommentAdapter(this);
        Intent intent = getIntent();
        doctorId = intent.getStringExtra("id");
        initViews();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.doctor_details));
        tvTitle.setTextColor(ContextCompat.getColor(this,R.color.color333333));
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable, null, null, null);
    }

    @OnClick({R.id.ll_collect, R.id.ll_zixun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_collect:
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
                                    tvCollectLabel.setEnabled(false);
                                    tvCollectShow.setText(getString(R.string.article_details06));
                                } else {
                                    tvCollectLabel.setEnabled(true);
                                    tvCollectShow.setText(getString(R.string.article_details04));
                                }
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean<CollectAddorRemoveResultBean>> call, Throwable t) {

                    }
                });
                break;
            case R.id.ll_zixun:
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
                break;
        }
    }

    private void initViews() {
        lvComments.setListViewSlide(false);
        lvComments.setAdapter(madapter);
        lvComments.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                PAGE_NO++;
                getDoctorComment();
            }
        });
    }

    private void connectDoctor() {
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


    private void toChat() {
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


    private void initDatas() {
        getDoctorDetail();
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        bean = new DoctorCommentPostBean();
        if (scoreList!=null) {
            scoreList.clear();
        }
        getDoctorComment();
        getServiceNumber();
        getBeCommentedNum();
        getBeDoctorAttentionNum();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatas();
    }


    private void getDoctorDetail() {
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
                        tvDoctorName.setText(doctorBean.nickName);
                        doctorName = doctorBean.nickName;

                        if ("1".equals(doctorBean.onlineStatus)) {
                            tvOnlineStatus.setText("在线");
                        } else {
                            tvOnlineStatus.setText("忙碌");
                        }

                        tvTitleDoctor.setText(doctorBean.title);
                        String fromMedicineTime = doctorBean.fromMedicineTime;
                        tvWorkage.setText(getWorkYear(fromMedicineTime));
                        tvJobintroduce.setText(doctorBean.jobIntroduction);

                        String goodDiseases = doctorBean.goodDiseases;
                        if (goodDiseases != null) {
                            String[] doctorlabel = goodDiseases.split(",");
                            dlwlDoctorLabel.oneline = false;
                            dlwlDoctorLabel.setData(doctorlabel, DoctorDetailActivity.this, 10, 5, 1, 5, 1, 4, 4, 4, 8);
                        }
                        String score = doctorBean.score;
                        scorestar.setScore(score);

                        if ("1".equals(doctorBean.isSc)) {
                            tvCollectLabel.setEnabled(true);
                            tvCollectShow.setText(getString(R.string.article_details04));

                        } else {
                            tvCollectLabel.setEnabled(false);
                            tvCollectShow.setText(getString(R.string.article_details06));
                        }

                        if (doctorBean.img != null) {
                            Uri uri = Uri.parse(doctorBean.img);
                            sdvDoctorHeader.setImageURI(uri);
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


    private void getServiceNumber() {
        DoctorNumberPostBean bean = new DoctorNumberPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.id = doctorId;
        ApiUtils.getApiService().getDoctorServerNum(bean).enqueue(new TaiShengCallback<BaseBean<DoctorNumberResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorNumberResultBean>> response, BaseBean<DoctorNumberResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tvServicenumber.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });
    }

    private void getBeCommentedNum() {
        DoctorNumberPostBean bean = new DoctorNumberPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.id = doctorId;
        ApiUtils.getApiService().getBeCommentedNum(bean).enqueue(new TaiShengCallback<BaseBean<DoctorNumberResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorNumberResultBean>> response, BaseBean<DoctorNumberResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        tvComment.setText(message.result.countNum + "");
                        tvComment2.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });
    }

    private void getBeDoctorAttentionNum() {
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
                        tvGuanzu.setText(message.result.countNum + "");
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorNumberResultBean>> call, Throwable t) {

            }
        });

    }


    private void getDoctorComment() {
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        bean.doctorId = doctorId;
        LogUtilH.e("--6-");
        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().doctorScoreList(bean).enqueue(new TaiShengCallback<BaseBean<DoctorCommentResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DoctorCommentResultBean>> response, BaseBean<DoctorCommentResultBean> message) {
                DialogUtil.closeProgress();
                LogUtilH.e("--1-");
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result == null) {
                            return;
                        }
                        LogUtilH.e("--2-");
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lvComments.setLoading(false);
                            scoreList.addAll(message.result.records);
                            madapter.setmData(scoreList);
                            if (message.result.records.size() < 10) {
                                lvComments.setHasLoadMore(false);
                                lvComments.setLoadAllViewText("暂时只有这么多评论");
                                lvComments.setLoadAllFooterVisible(true);
                            } else {
                                lvComments.setHasLoadMore(true);
                            }
                            LogUtilH.e("--3-");
                        } else {
                            //没有消息
                            lvComments.setHasLoadMore(false);
                            lvComments.setLoadAllViewText("暂时只有这么多评论");
                            lvComments.setLoadAllFooterVisible(true);
                            LogUtilH.e("--4-");
                        }

                        break;

                }
            }

            @Override
            public void onFail(Call<BaseBean<DoctorCommentResultBean>> call, Throwable t) {
                DialogUtil.closeProgress();
                LogUtilH.e("--5-");
            }
        });
    }


    class DoctorCommentAdapter extends BaseAdapter {

        private Context mcontext;

        private List<DoctorCommentBean> mData ;

        public DoctorCommentAdapter(Context context) {
            this.mcontext = context;
        }

        public void setmData(List<DoctorCommentBean> mData) {
            this.mData = mData;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData==null?0:mData.size();
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
            LogUtilH.e(mData.size()+"---------mData.size()-------------------");

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

            StarGrade starGrade;
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









