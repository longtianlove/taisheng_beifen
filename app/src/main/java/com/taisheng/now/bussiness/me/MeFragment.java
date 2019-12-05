package com.taisheng.now.bussiness.me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.article.ArticleCollectActivity;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.result.IsSign;
import com.taisheng.now.bussiness.doctor.DoctorCollectActivity;
import com.taisheng.now.bussiness.healthfiles.HealthCheckHistoryActivity;
import com.taisheng.now.bussiness.healthfiles.HealthFileSearchActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by Administrator on 2015/6/12.
 */

@SuppressLint("WrongConstant")
public class MeFragment extends BaseFragment {


    SimpleDraweeView sdv_header;
    TextView tv_nickname;
    TextView tv_zhanghao;
    ImageView iv_jiantou;
    TextView tv_qiandao;

    View ll_healthfile;
    View ll_zixunjilu;
    View ll_doctorcollect;
    View ll_articlecollect;


    View ll_kajuan;
    View ll_dingdan;
    View ll_mypingjia;
    View ll_tousuzhongxin;
    View ll_yijianfankui;
    View ll_aboutus;
    View ll_setting;
    View ll_share;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_me, container, false);

        initView(rootView);


//        EventBus.getDefault().register(this);
        initData();

        return rootView;
    }

    View.OnClickListener toMeMessageActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MeMessageActivity.class);
            startActivity(intent);
        }
    };

    void initView(View rootView) {
        sdv_header = (SimpleDraweeView) rootView.findViewById(R.id.sdv_header);
        sdv_header.setOnClickListener(toMeMessageActivityListener);
        tv_nickname = (TextView) rootView.findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(toMeMessageActivityListener);
        tv_qiandao = rootView.findViewById(R.id.tv_qiandao);
        tv_qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_qiandao.setText("・已签到");
                Intent intent = new Intent(getActivity(), QuqiandaoActivity.class);
                startActivity(intent);
            }
        });

        tv_zhanghao = (TextView) rootView.findViewById(R.id.tv_zhanghao);
        tv_zhanghao.setOnClickListener(toMeMessageActivityListener);

        iv_jiantou = (ImageView) rootView.findViewById(R.id.iv_jiantou);
        iv_jiantou.setOnClickListener(toMeMessageActivityListener);


        ll_healthfile = rootView.findViewById(R.id.ll_healthfile);
        ll_healthfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mActivity, HealthFileSearchActivity.class);
                Intent intent = new Intent(mActivity, HealthCheckHistoryActivity.class);
                startActivity(intent);
            }
        });
        ll_zixunjilu = rootView.findViewById(R.id.ll_zixunjilu);
        ll_zixunjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ZixunjiluActivity.class);
                startActivity(intent);
            }
        });

        ll_doctorcollect = rootView.findViewById(R.id.ll_doctorcollect);
        ll_doctorcollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, DoctorCollectActivity.class);
                startActivity(intent);
            }
        });
        ll_articlecollect = rootView.findViewById(R.id.ll_articlecollect);
        ll_articlecollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ArticleCollectActivity.class);
                startActivity(intent);
            }
        });


        ll_kajuan=rootView.findViewById(R.id.ll_kajuan);
        ll_kajuan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyKajuanActivity.class);
                startActivity(intent);
            }
        });
        ll_dingdan=rootView.findViewById(R.id.ll_dingdan);
        ll_dingdan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyDingdanActivity.class);
                startActivity(intent);
            }
        });

        ll_mypingjia = rootView.findViewById(R.id.ll_mypingjia);
        ll_mypingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPingjiaActivity.class);
                startActivity(intent);
            }
        });
        ll_tousuzhongxin = rootView.findViewById(R.id.ll_tousuzhongxin);
        ll_tousuzhongxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TousuzhongxinActivity.class);
                startActivity(intent);
            }
        });
        ll_yijianfankui = rootView.findViewById(R.id.ll_yijianfankui);
        ll_yijianfankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YijianfankuiActivity.class);
                startActivity(intent);
            }
        });
        ll_aboutus = rootView.findViewById(R.id.ll_aboutus);
        ll_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        ll_share = rootView.findViewById(R.id.ll_share);
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RecommendShareActivity.class);
                startActivity(intent);
            }
        });


    }

    void initData() {
        BasePostBean bean = new BasePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        ApiUtils.getApiService().isSign(bean).enqueue(new TaiShengCallback<BaseBean<IsSign>>() {
            @Override
            public void onSuccess(Response<BaseBean<IsSign>> response, BaseBean<IsSign> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if ("true".equals(message.result.signFlag)) {
                            tv_qiandao.setText("・已签到");
                        }else{
                            tv_qiandao.setText("・去签到");

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<IsSign>> call, Throwable t) {

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
        if (!TextUtils.isEmpty(UserInstance.getInstance().userInfo.userName)) {
            tv_zhanghao.setText(UserInstance.getInstance().userInfo.userName);
        }
    }

    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);

    }
}
