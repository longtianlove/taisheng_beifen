package com.taisheng.now.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.enitity.ThumbViewInfo;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;

import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.bussiness.bean.post.ConnectDoctorPostBean;
import com.taisheng.now.bussiness.bean.result.ConnectDoctorResultBean;
import com.taisheng.now.bussiness.bean.result.PictureBean;
import com.taisheng.now.bussiness.doctor.DoctorCommentActivity;
import com.taisheng.now.bussiness.me.SelectAvatarSourceDialog;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.chat.websocket.WebSocketManager;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.shipin.TRTCMainActivity;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.DoubleClickUtil;
import com.taisheng.now.util.FileUtilcll;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.biaoqing.EmotionMainFragment;
import com.taisheng.now.view.chenjinshi.StatusBarUtil;
import com.tencent.trtc.TRTCCloudDef;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class C2CActivity extends FragmentActivity implements  ActivityCompat.OnRequestPermissionsResultCallback {








    public static String mTargetId;
    public static String doctorAvator;
    public static String doctorName;





    EmotionMainFragment emotionMainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c2c);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        findViewById(R.id.title_left_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.title_left_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.title_right_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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








        mTargetId = getIntent().getStringExtra("targetId");

        doctorAvator = getIntent().getStringExtra("doctorAvator");
        doctorName = getIntent().getStringExtra("doctorName");


        nickName = getIntent().getStringExtra("nickName");
//         title=getIntent().getStringExtra("title");
        avatar = getIntent().getStringExtra("avatar");
        doctorId = getIntent().getStringExtra("doctorId");


        ((TextView) findViewById(R.id.title_text)).setText(doctorName);

        mTvContent = (TextView) findViewById(R.id.tv_input_content);



        initBiaoqinng();



    }

    private TextView mTvContent;


    void initBiaoqinng(){


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
        setSystemBar();
    }

    public void setSystemBar(){
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
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
