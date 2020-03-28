package com.taisheng.now.chat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.taisheng.now.Constants;
import com.taisheng.now.R;

import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.ConnectDoctorPostBean;
import com.taisheng.now.bussiness.bean.result.ConnectDoctorResultBean;
import com.taisheng.now.bussiness.doctor.DoctorCommentActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.shipin.TRTCMainActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.DoubleClickUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.biaoqing.EmotionMainFragment;
import com.tencent.trtc.TRTCCloudDef;

import retrofit2.Call;
import retrofit2.Response;

public class C2CActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    public static String mTargetId;
    public static String doctorAvator;
    public static String doctorName;


    EmotionMainFragment emotionMainFragment;

    @Override
    public void initView() {
        setContentView(R.layout.activity_c2c);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        mTargetId = getIntent().getStringExtra("targetId");

        doctorAvator = getIntent().getStringExtra("doctorAvator");
        doctorName = getIntent().getStringExtra("doctorName");


        nickName = getIntent().getStringExtra("nickName");
//         title=getIntent().getStringExtra("title");
        avatar = getIntent().getStringExtra("avatar");
        doctorId = getIntent().getStringExtra("doctorId");

        mTvContent = (TextView) findViewById(R.id.tv_input_content);


        initBiaoqinng();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(doctorName);
        ivRight.setBackgroundResource(R.drawable.icon_shipin);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DoubleClickUtil.isFastMiniDoubleClick()) {
                    return;
                }
                DialogUtil.showzixunDialog(C2CActivity.this,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatType = "video";

                                check();

                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                chatType = "audio";
                                check();
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

    private TextView mTvContent;


    void initBiaoqinng() {


        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, null);
        emotionMainFragment.bindToContentView(mTvContent);//绑定当前页面控件
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_emotion_view_main, emotionMainFragment);
//        transaction.addToBackStack(null);//fragment添加至回退栈中
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    //检查权限
    void check() {

        connectDoctor();
    }


    public String chatType = "video";

    void connectDoctor() {
        ConnectDoctorPostBean bean = new ConnectDoctorPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.doctorId = mTargetId;
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


//    private void onJoinRoomBySelf(final int roomId, final String userId) {
//        WebrtcUtil.callSingle(this,
//                "",
//                roomId + "",
//                "video".equals(chatType) ? true : false);
//    }


    /**
     * Function: 读取用户输入，并创建（或加入）音视频房间
     * <p>
     * 此段示例代码最主要的作用是组装 TRTC SDK 进房所需的 TRTCParams
     * <p>
     * TRTCParams.sdkAppId => 可以在腾讯云实时音视频控制台（https://console.cloud.tencent.com/rav）获取
     * TRTCParams.userId   => 此处即用户输入的用户名，它是一个字符串
     * TRTCParams.roomId   => 此处即用户输入的音视频房间号，比如 125
     * TRTCParams.userSig  => 此处示例代码展示了两种获取 usersig 的方式，一种是从【控制台】获取，一种是从【服务器】获取
     * <p>
     * （1）控制台获取：可以获得几组已经生成好的 userid 和 usersig，他们会被放在一个 json 格式的配置文件中，仅适合调试使用
     * （2）服务器获取：直接在服务器端用我们提供的源代码，根据 userid 实时计算 usersig，这种方式安全可靠，适合线上使用
     * <p>
     * 参考文档：https://cloud.tencent.com/document/product/647/17275
     */
    private String mUserId = "";
    private String mUserSig = "";

    String nickName;
    //    String title;
    String avatar;
    String doctorId;


    private void onJoinRoomByTecent(final int roomId, final String userId) {
        final Intent intent = new Intent(C2CActivity.this, TRTCMainActivity.class);

        intent.putExtra("nickName", nickName);
//            intent.putExtra("title", title);
        intent.putExtra("avatar", avatar);
        intent.putExtra("doctorId", doctorId);

        intent.putExtra("roomId", roomId);
        intent.putExtra("userId", userId);
        intent.putExtra("AppScene", TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        intent.putExtra("sdkAppId", Constants.SDKAPPID);
        intent.putExtra("userSig", mUserSig);
        intent.putExtra("chatType", chatType);
        startActivityForResult(intent, 1);

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
                Intent intent = new Intent(C2CActivity.this, DoctorCommentActivity.class);
                intent.putExtra("id", mTargetId);
                startActivity(intent);

            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


//
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    public boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = {0, 0};
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        /**
         * 按下返回键，如果表情显示，则隐藏，没有显示则回退页面
         */
        if (!emotionMainFragment.isInterceptBackPress()) {
            super.onBackPressed();
        }
    }
}
