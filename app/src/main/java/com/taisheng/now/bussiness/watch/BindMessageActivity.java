package com.taisheng.now.bussiness.watch;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.SampleAppLike;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.me.SelectAvatarSourceDialog;
import com.taisheng.now.bussiness.me.UpdateNickActivity;
import com.taisheng.now.bussiness.user.LoginActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.bean.post.BindDevicePostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.push.XMPushManagerInstance;
import com.taisheng.now.util.IDCardUtil;
import com.taisheng.now.view.AppDialog;
import com.taisheng.now.view.crop.Crop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class BindMessageActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;


    EditText et_relative;
    TextView tv_device_bianhao;
    View ll_nickname;
    View ll_avatar;

    EditText et_nickname;
    EditText et_realname;
    EditText et_idcard;
    EditText et_phonenumber;

    TextView btn_post;
    SimpleDraweeView sdv_header;
    private final int REQ_CODE_PHOTO_SOURCE = 6;//选择方式
    private final int REQ_CODE_GET_PHOTO_FROM_GALLERY = 10;//从相册获取
    private final int REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO = 11;//拍照完

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindmessage);
        initView();
        EventBus.getDefault().register(this);
    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_relative = findViewById(R.id.et_relative);
        et_relative.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check()) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_device_bianhao = findViewById(R.id.tv_device_bianhao);
        tv_device_bianhao.setText(WatchInstance.getInstance().preDeviceNumber);


        ll_avatar = findViewById(R.id.ll_avatar);
        ll_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyAvatar();
            }
        });
        sdv_header = (SimpleDraweeView) findViewById(R.id.sdv_header);

        ll_nickname = findViewById(R.id.ll_nickname);
//        ll_nickname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BindMessageActivity.this, UpdateNickActivity.class);
//                startActivity(intent);
//            }
//        });


        et_nickname = findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check()) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_realname = findViewById(R.id.et_realname);
        et_realname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check()) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_idcard = findViewById(R.id.et_idcard);

        et_idcard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check()) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_phonenumber = findViewById(R.id.et_phonenumber);

        et_phonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check()) {
                    btn_post.setEnabled(true);
                } else {
                    btn_post.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_post = (TextView) findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                showGoRecommendDialog();
                //todo 绑定设备
                BindDevicePostBean bean = new BindDevicePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceType = 1 + "";
                bean.deviceId = tv_device_bianhao.getText().toString();
                bean.deviceNickName = et_nickname.getText().toString();
                bean.relationShip = et_relative.getText().toString();
                bean.headUrl = WatchInstance.getInstance().headUrl;
                bean.realName = et_realname.getText().toString();
                bean.idcard = et_idcard.getText().toString();
                bean.phoneNumber = et_phonenumber.getText().toString();
                ApiUtils.getApiService().deviceBinding(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
//                                todo deviceid到底是哪个
                                String deviceId = bean.deviceId;
                                WatchInstance.getInstance().deviceId = deviceId.substring(1, 3) + deviceId.substring(4, 6) + deviceId.substring(7, 10) + deviceId.substring(11, 14);
                                ;
                                WatchInstance.getInstance().deviceNickName = bean.deviceNickName;
                                WatchInstance.getInstance().relationShip = bean.relationShip;
                                WatchInstance.getInstance().realName = bean.realName;
                                WatchInstance.getInstance().idcard = bean.idcard;
                                WatchInstance.getInstance().phoneNumber = bean.phoneNumber;
                                Intent intent = new Intent(BindMessageActivity.this, WatchMainActivity.class);
                                startActivity(intent);
                                finish();

                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });

    }


    boolean check() {
        if (TextUtils.isEmpty(et_relative.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_nickname.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_realname.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_idcard.getText())) {
            return false;
        }

        if (!IDCardUtil.isValidatedAllIdcard(et_idcard.getText().toString())) {
            return false;
        }
        if (TextUtils.isEmpty(et_phonenumber.getText())) {
            return false;
        }

        return true;
    }


    public void modifyAvatar() {
        WatchInstance.getInstance().isWtch = true;

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


    private void beginCrop(Uri source, Bundle bundle) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "Bcropped"));
        Crop.of(source, destination).asSquare().start(this, bundle);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void uploadImageSuccess(EventManage.uploadWatchImageSuccess event) {
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
                Uri uri = Uri.parse(Constants.Url.File_Host + UserInstance.getInstance().userInfo.avatar);
                if (sdv_header == null) {
                    return;
                }
                sdv_header.setImageURI(uri);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
