package com.taisheng.now.bussiness.me;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.application.SampleAppLike;
import com.taisheng.now.base.BaseHActivity;
import com.taisheng.now.bussiness.login.LoginActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.crop.Crop;
import com.th.j.commonlibrary.utils.LogUtilH;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dragon on 2019/6/29.
 */

public class MeMessageActivity extends BaseHActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final int REQ_CODE_PHOTO_SOURCE = 6;//选择方式
    private final int REQ_CODE_GET_PHOTO_FROM_GALLERY = 10;//从相册获取
    private final int REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO = 11;//拍照完
    @BindView(R.id.sdv_header)
    SimpleDraweeView sdvHeader;
    @BindView(R.id.ll_avatar)
    LinearLayout llAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_nickname)
    LinearLayout llNickname;
    @BindView(R.id.tv_zhanghao)
    TextView tvZhanghao;
    @BindView(R.id.ll_zhanghao)
    LinearLayout llZhanghao;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.ll_bindphone)
    LinearLayout llBindphone;
    @BindView(R.id.ll_updatepwd)
    LinearLayout llUpdatepwd;
    @BindView(R.id.btn_post)
    TextView btnPost;


    @Override
    public void initView() {
        setContentView(R.layout.activity_memessage);
        ButterKnife.bind(this);


    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.personal_data));
    }

    private void modifyAvatar() {
//        WatchInstance.getInstance().isWtch = false;
        WatchInstance.getInstance().uploadimage_type="1";
        Intent intent = new Intent(this, SelectAvatarSourceDialog.class);
        startActivityForResult(intent, REQ_CODE_PHOTO_SOURCE);
    }


    private void onPhotoSource(int mode) {
        if (mode == R.id.btn_pick_from_library) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_GALLERY);

        } else if (mode == R.id.btn_take_photo) {


            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {

                permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITEEXTRENAL_STOR);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(this, "com.taisheng.now.fileprovider", new File(Environment
                                .getExternalStorageDirectory(), "temp.jpg"));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp.jpg")));
                    }
                    startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO);
                }
            }


        }
    }


    public final static int REQUEST_CAMERA = 1;

    public final static int REQUEST_WRITEEXTRENAL_STOR = 2;


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                int permissionCheck;

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITEEXTRENAL_STOR);
                    permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITEEXTRENAL_STOR);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(this, "com.taisheng.now.fileprovider", new File(Environment
                                    .getExternalStorageDirectory(), "temp.jpg"));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                        } else {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                    .getExternalStorageDirectory(), "temp.jpg")));
                        }
                        startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO);
                    }
                }
                break;
            case REQUEST_WRITEEXTRENAL_STOR:

                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(this, "com.taisheng.now.fileprovider", new File(Environment
                                .getExternalStorageDirectory(), "temp.jpg"));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    } else {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp.jpg")));
                    }

                    startActivityForResult(intent, REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO);
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserInstance.getInstance().userInfo.avatar != null) {
            Uri uri = Uri.parse(Constants.Url.File_Host_head + UserInstance.getInstance().userInfo.avatar);
            sdvHeader.setImageURI(uri);
        }
        tvNickname.setText(UserInstance.getInstance().getNickname());
        tvZhanghao.setText(UserInstance.getInstance().getZhanghao());
        tvPhone.setText(UserInstance.getInstance().getPhone());
    }

    private void beginCrop(Uri source, Bundle bundle) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "Bcropped"));
        Crop.of(source, destination).asSquare().start(this, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void uploadImageSuccess(EventManage.uploadImageSuccess event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri source = FileProvider.getUriForFile(this, "com.taisheng.now.fileprovider", new File(Environment
                    .getExternalStorageDirectory(), "temp.jpg"));
            getContentResolver().delete(source, null, null);
        } else {

            File picture = new File(Environment.getExternalStorageDirectory()
                    , "temp.jpg");
            if (picture.exists() && picture.isFile()) {
                picture.delete();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {

            case REQ_CODE_PHOTO_SOURCE:
                if (data != null) {
                    int mode = data.getIntExtra(SelectAvatarSourceDialog.TAG_MODE, -1);
                    onPhotoSource(mode);
                }
                break;
            case REQ_CODE_GET_PHOTO_FROM_GALLERY:
                if (data != null && data.getData() != null) {
                    Bundle bundle = new Bundle();
                    // 选择图片后进入裁剪
                    String path = data.getData().getPath();
                    Uri source = data.getData();
                    beginCrop(source, bundle);
                }
                break;
            case REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO:
                // 判断相机是否有返回

                Uri source;
                Bundle bundle = new Bundle();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    source = FileProvider.getUriForFile(this, "com.taisheng.now.fileprovider", new File(Environment
                            .getExternalStorageDirectory(), "temp.jpg"));
                } else {
                    // 选择图片后进入裁剪
                    File picture = new File(Environment.getExternalStorageDirectory()
                            , "temp.jpg");
                    if (!picture.exists()) {
                        return;
                    }
                    source = Uri.fromFile(picture);
                }


                beginCrop(source, bundle);


                break;

            case Crop.REQUEST_CROP:
//                modifyBean.logo_url = PetInfoInstance.getInstance().getPackBean().logo_url;
                Uri uri = Uri.parse(Constants.Url.File_Host_head + UserInstance.getInstance().userInfo.avatar);
                sdvHeader.setImageURI(uri);

                break;
        }
    }


    public void showGoRecommendDialog() {
        final Dialog dialog = new AppDialog(this, R.layout.dialog_logout, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, R.style.mystyle, Gravity.CENTER);
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

                //注销小米账号
                XMPushManagerInstance.getInstance().stop();
                UserInstance.getInstance().clearUserInfo();
                Intent intent = new Intent(SampleAppLike.mcontext, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SampleAppLike.mcontext.startActivity(intent);
            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_avatar, R.id.ll_nickname, R.id.ll_zhanghao, R.id.ll_bindphone, R.id.ll_updatepwd, R.id.btn_post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_avatar:
                modifyAvatar();
                break;
            case R.id.ll_nickname:
                Intent intent = new Intent(MeMessageActivity.this, UpdateNickActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_zhanghao:
                //                Intent intent = new Intent(MeMessageActivity.this, UpdateZhanghaoActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_bindphone:
                //                Intent intent=new Intent(MeMessageActivity.this,BindPhoneActivity.class);
//                startActivity(intent);
                break;
            case R.id.ll_updatepwd:
                Intent intent2 = new Intent(MeMessageActivity.this, UpdatePasswordFirstActivity.class);
                startActivity(intent2);
                break;
            case R.id.btn_post:
                showGoRecommendDialog();
                break;
        }
    }
}
