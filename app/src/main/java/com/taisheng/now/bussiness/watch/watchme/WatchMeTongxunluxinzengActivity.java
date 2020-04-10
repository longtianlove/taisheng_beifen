package com.taisheng.now.bussiness.watch.watchme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.me.SelectAvatarSourceDialog;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SetphonbookPostBean;
import com.taisheng.now.bussiness.watch.bean.result.TongxunluliistBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.crop.Crop;
import com.th.j.commonlibrary.global.Global;
import com.th.j.commonlibrary.utils.PhoneUtil;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.th.j.commonlibrary.wight.CircleImageView;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMeTongxunluxinzengActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.sdv_header)
    CircleImageView sdvHeader;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_mail_list)
    TextView tvMailList;
    @BindView(R.id.et_tongxunlu_name)
    EditText etTongxunluName;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    private String intenType;

    private final int REQ_CODE_PHOTO_SOURCE = 6;//选择方式
    private final int REQ_CODE_GET_PHOTO_FROM_GALLERY = 10;//从相册获取
    private final int REQ_CODE_GET_PHOTO_FROM_TAKEPHOTO = 11;//拍照完
    public final static int REQUEST_CAMERA = 1;
    public final static int REQUEST_WRITEEXTRENAL_STOR = 2;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_tongxunluxinzeng);
        ButterKnife.bind(this);
    }


    int position;
    public String avatarUrl;

    @Override
    public void initData() {
        position = getIntent().getIntExtra("position", -1);
        intenType = getIntent().getStringExtra(Global.INTENT_TYPE);
        avatarUrl = getIntent().getStringExtra("avatarUrl");


        String phbxName = getIntent().getStringExtra("phbxName");
        if (!TextUtils.isEmpty(phbxName)) {
            etTongxunluName.setText(phbxName);
            etTongxunluName.setSelection(TextsUtils.getTexts(etTongxunluName).length());
        }
        String phbxTelephone = getIntent().getStringExtra("phbxTelephone");
        if (!TextUtils.isEmpty(phbxTelephone)) {
            etPhone.setText(phbxTelephone);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void addData() {
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(this)
                    .load(Constants.Url.File_Host_head +avatarUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ad_sculpture)
                            .error(R.drawable.ad_sculpture)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(sdvHeader);
        }

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        if (Global.MAIL_ADD.equals(intenType)) {
            tvTitle.setText(getString(R.string.watch_msg12));
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.colorbbbbbb));
            tvCancel.setText(getString(R.string.cancal));
        } else {
            tvTitle.setText(getString(R.string.watch_msg13));
            tvCancel.setTextColor(ContextCompat.getColor(this, R.color.colorff0202));
            tvCancel.setText(getString(R.string.delete));
        }
    }


    @OnClick({R.id.sdv_header, R.id.tv_header, R.id.tv_mail_list, R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sdv_header:
            case R.id.tv_header:
                modifyAvatar();
                break;
            case R.id.tv_mail_list:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.tv_save:
                if (TextsUtils.isEmpty(TextsUtils.getTexts(etPhone))) {
                    Uiutils.showToast(getString(R.string.watch_msg10));
                    return;
                }
                if (TextsUtils.isEmpty(TextsUtils.getTexts(etTongxunluName))) {
                    Uiutils.showToast(getString(R.string.watch_msg11));
                    return;
                }
                SetphonbookPostBean bean = new SetphonbookPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                bean.type = "1";

                if (position == -1) {
                    TongxunluliistBean tongxunluliistBean = new TongxunluliistBean();
                    tongxunluliistBean.avatarUrl = avatarUrl;
                    tongxunluliistBean.name = TextsUtils.getTexts(etTongxunluName);
                    tongxunluliistBean.mobilePhone = TextsUtils.getTexts(etPhone);
                    tongxunluliistBean.deviceId = WatchInstance.getInstance().deviceId;
                    tongxunluliistBean.type = "1";
                    tongxunluliistBean.userId = UserInstance.getInstance().getUid();
                    WatchMeTongxunluActivity.data.add(tongxunluliistBean);
                } else {
                    TongxunluliistBean tempBean = WatchMeTongxunluActivity.data.get(position);
                    tempBean.avatarUrl = avatarUrl;
                    tempBean.name = TextsUtils.getTexts(etTongxunluName);
                    tempBean.mobilePhone = TextsUtils.getTexts(etPhone);
                }
                bean.list = WatchMeTongxunluActivity.data;
                ApiUtils.getApiService_hasdialog().setphonbook(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                Uiutils.showToast(getString(R.string.add_success));
                                WatchMeTongxunluxinzengActivity.this.finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
            case R.id.tv_cancel:
                if (Global.MAIL_ADD.equals(intenType)) {
                    this.finish();
                } else {
//                    TongxunluDeletePostBean tongxunluDeletePostBean = new TongxunluDeletePostBean();
//                    tongxunluDeletePostBean.userId = UserInstance.getInstance().getUid();
//                    tongxunluDeletePostBean.token = UserInstance.getInstance().getToken();
//                    tongxunluDeletePostBean.deviceId = WatchInstance.getInstance().deviceId;
//
//                    ApiUtils.getApiService().setWatchDphbx(tongxunluDeletePostBean).enqueue(new TaiShengCallback<BaseBean>() {
//                        @Override
//                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
//                            switch (message.code) {
//                                case Constants.HTTP_SUCCESS:
//                                    Uiutils.showToast(getString(R.string.delete_success));
//                                    initData();
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void onFail(Call<BaseBean> call, Throwable t) {
//
//                        }
//                    });


                    SetphonbookPostBean setphonbookPostBean = new SetphonbookPostBean();
                    setphonbookPostBean.userId = UserInstance.getInstance().getUid();
                    setphonbookPostBean.token = UserInstance.getInstance().getToken();
                    setphonbookPostBean.deviceId = WatchInstance.getInstance().deviceId;
                    setphonbookPostBean.type = "1";
                    WatchMeTongxunluActivity.data.remove(position);
                    setphonbookPostBean.list = WatchMeTongxunluActivity.data;
                    ApiUtils.getApiService_hasdialog().setphonbook(setphonbookPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    Uiutils.showToast(getString(R.string.add_success));
                                    WatchMeTongxunluxinzengActivity.this.finish();
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }

                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void uploadImageSuccess(EventManage.uploadTongxunluImageSuccess event) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String[] contacts = PhoneUtil.getPhoneContacts(this, uri);
                etPhone.setText(contacts[1]);
                etTongxunluName.setText(contacts[0]);
                break;
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
                Glide.with(this)
                        .load(Constants.Url.File_Host_head + WatchInstance.getInstance().temp_tongxunlu_headUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.article_default)
                                .error(R.drawable.article_default)
                                .diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(sdvHeader);
                avatarUrl = WatchInstance.getInstance().temp_tongxunlu_headUrl;
                break;
        }
    }

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

    private void modifyAvatar() {
//        WatchInstance.getInstance().isWtch = false;
        WatchInstance.getInstance().uploadimage_type = "3";
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

    private void beginCrop(Uri source, Bundle bundle) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "Bcropped"));
        Crop.of(source, destination).asSquare().start(this, bundle);
    }
}
