/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taisheng.now.view.crop;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.MainActivity;
import com.taisheng.now.bussiness.bean.post.UserInfoPostBean;
import com.taisheng.now.bussiness.bean.result.ModifyUserInfoResultBean;
import com.taisheng.now.bussiness.bean.result.UserInfo;
import com.taisheng.now.bussiness.me.FillInMessageActivity;
import com.taisheng.now.bussiness.me.FillInMessageSecondActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Apputil;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Response;

/*
 * Modified from original in AOSP.
 */
public class CropImageActivity extends MonitoredActivity implements CropImageViewInterface {


//    private UploadImagePresenter presenter = new UploadImagePresenter(new WeakReference<>(this));

    private String clipImagePath;// 裁剪生成的图片路径


    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private final Handler handler = new Handler();

    private int aspectX;
    private int aspectY;

    // Output image
    private int maxX;
    private int maxY;
    private int exifRotation;
    private boolean saveAsPng;

    private Uri sourceUri;
    private Uri saveUri;

    private boolean isSaving;

    private int sampleSize;
    private RotateBitmap rotateBitmap;
    private CropImageView imageView;
    private HighlightView cropView;
    private String dirPath = Apputil.sdNormalPath + "/cropImage";// 存储裁剪图片目录

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 0)
    public void upLoadSucess(EventManage.uploadImageSuccess event) {


        UserInfoPostBean bean = new UserInfoPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.sysUser = new UserInfo();
        bean.sysUser.id = UserInstance.getInstance().getUid();
        bean.sysUser.token = UserInstance.getInstance().getToken();

        bean.sysUser.age = UserInstance.getInstance().userInfo.age;
        bean.sysUser.phone = UserInstance.getInstance().userInfo.phone;
        bean.sysUser.realName = UserInstance.getInstance().userInfo.realName;
        bean.sysUser.sex = UserInstance.getInstance().userInfo.sex;
        bean.sysUser.nickName=UserInstance.getInstance().userInfo.nickName;
        bean.sysUser.userName=UserInstance.getInstance().userInfo.userName;
        bean.sysUser.avatar = event.path;

        ApiUtils.getApiService().modifyuser(bean).enqueue(new TaiShengCallback<BaseBean<ModifyUserInfoResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ModifyUserInfoResultBean>> response, BaseBean<ModifyUserInfoResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        UserInstance.getInstance().userInfo.age = bean.sysUser.age;
                        SPUtil.putAge(UserInstance.getInstance().userInfo.age);
                        UserInstance.getInstance().userInfo.phone = bean.sysUser.phone;
                        SPUtil.putPhone(UserInstance.getInstance().userInfo.phone);
                        UserInstance.getInstance().userInfo.realName = bean.sysUser.realName;
                        SPUtil.putRealname(UserInstance.getInstance().userInfo.realName);
                        UserInstance.getInstance().userInfo.sex = bean.sysUser.sex;
                        SPUtil.putSex(UserInstance.getInstance().userInfo.sex);
                        UserInstance.getInstance().userInfo.avatar = bean.sysUser.avatar;
                        SPUtil.putAVATAR(bean.sysUser.avatar);
                        UserInstance.getInstance().userInfo.nickName=bean.sysUser.nickName;
                        SPUtil.putNickname(bean.sysUser.nickName);
                        finish();
                        break;

                }
            }

            @Override
            public void onFail(Call<BaseBean<ModifyUserInfoResultBean>> call, Throwable t) {

            }
        });


    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setupWindowFlags();
        setupViews();
        loadInput();
        if (rotateBitmap == null) {
//            ToastUtil.showTost("图片已损坏，请重新选择");
            finish();
            return;
        }
        startCrop();
        EventBus.getDefault().register(this);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setupWindowFlags() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setupViews() {
        setContentView(R.layout.crop__activity_crop);

        imageView = (CropImageView) findViewById(R.id.crop_image);
        imageView.context = this;
        imageView.setRecycler(new ImageViewTouchBase.Recycler() {
            @Override
            public void recycle(Bitmap b) {
                b.recycle();
                System.gc();
            }
        });

        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                PBIManager.updatePoint(new PBIPointer(PBIConstant.PHOTO_CROP).putPBI(PBIConstant.PHOTO_CROP_CANCEL));
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    onSaveClicked();
                } catch (Exception e) {
                    finish();
                }
            }
        });
    }

    private void loadInput() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            aspectX = extras.getInt(Crop.Extra.ASPECT_X);
            aspectY = extras.getInt(Crop.Extra.ASPECT_Y);
            maxX = extras.getInt(Crop.Extra.MAX_X);
            maxY = extras.getInt(Crop.Extra.MAX_Y);
            saveAsPng = extras.getBoolean(Crop.Extra.AS_PNG, false);
            saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
        }

        sourceUri = intent.getData();
        if (sourceUri != null) {
            exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri));

            InputStream is = null;
            try {
                sampleSize = calculateBitmapSampleSize(sourceUri);
                is = getContentResolver().openInputStream(sourceUri);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = sampleSize;
                rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(is, null, option), exifRotation);
            } catch (IOException e) {
                setResultException(e);
            } catch (OutOfMemoryError e) {
                setResultException(e);
            } finally {
                CropUtil.closeSilently(is);
            }
        }
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void startCrop() {
        if (isFinishing()) {
            return;
        }
        imageView.setImageRotateBitmapResetBase(rotateBitmap, true);
        CropUtil.startBackgroundJob(this, null, getResources().getString(R.string.crop__wait),
                new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        handler.post(new Runnable() {
                            public void run() {
                                if (imageView.getScale() == 1F) {
                                    imageView.center();
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        new Cropper().crop();
                    }
                }, handler
        );
    }

    private class Cropper {

        private void makeDefault() {
            if (rotateBitmap == null) {
                return;
            }

            HighlightView hv = new HighlightView(imageView);
            final int width = rotateBitmap.getWidth();
            final int height = rotateBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // Make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            @SuppressWarnings("SuspiciousNameCombination")
            int cropHeight = cropWidth;

            if (aspectX != 0 && aspectY != 0) {
                if (aspectX > aspectY) {
                    cropHeight = cropWidth * aspectY / aspectX;
                } else {
                    cropWidth = cropHeight * aspectX / aspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(imageView.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
            imageView.add(hv);
        }

        public void crop() {
            handler.post(new Runnable() {
                public void run() {
                    makeDefault();
                    imageView.invalidate();
                    if (imageView.highlightViews.size() == 1) {
                        cropView = imageView.highlightViews.get(0);
                        cropView.setFocus(true);
                    }
                }
            });
        }
    }

    private void onSaveClicked() {
//      if (cropView == null || isSaving) {
//            return;
//      }
        if (cropView == null) {// by gengjiarong
//            DialogUtil.closeProgress();
            return;
        }
        isSaving = true;

        Bitmap croppedImage;
        Rect r = cropView.getScaledCropRect(sampleSize);
        int width = r.width();
        int height = r.height();

        int outWidth = width;
        int outHeight = height;
        if (maxX > 0 && maxY > 0 && (width > maxX || height > maxY)) {
            float ratio = (float) width / (float) height;
            if ((float) maxX / (float) maxY > ratio) {
                outHeight = maxY;
                outWidth = (int) ((float) maxY * ratio + .5f);
            } else {
                outWidth = maxX;
                outHeight = (int) ((float) maxX / ratio + .5f);
            }
        }

        try {
            croppedImage = decodeRegionCrop(r, outWidth, outHeight);
        } catch (IllegalArgumentException e) {
            setResultException(e);
            finish();
            return;
        }

        RotateBitmap rotateBitmap = new RotateBitmap(croppedImage, exifRotation);
        if (croppedImage != null) {
            imageView.setImageRotateBitmapResetBase(rotateBitmap, true);
            imageView.center();
            imageView.highlightViews.clear();
        }
        if (rotateBitmap != null) {
            clipImagePath = saveImage(rotateBitmap);
        } else {
            clipImagePath = saveImage(rotateBitmap);
        }


        DialogUtil.showProgress(this, "");

        if(WatchInstance.getInstance().isWtch){
            WatchInstance.getInstance().uploadImage(clipImagePath);
        }else{
            // 上传头像
            UserInstance.getInstance().uploadImage(clipImagePath);
        }

//        if(PetInfoInstance.getInstance().isnewAdd){
//            NewPetInfoInstance.getInstance().uploadImage(clipImagePath);
//        }else {
//            PetInfoInstance.getInstance().uploadImage(clipImagePath);
//        }


//        if (C_PHOTO.equals(source)) {
//            // C端图片上传
//            DialogUtil.showProgress(this,"请稍等");
////            presenter.uploadCImagePresenter(clipImagePath, cvid);
//        } else if (B_PHOTO.equals(source)) {
//            // B端图片上传
//            DialogUtil.showProgress(this,"请稍等");
////            presenter.uploadBImagePresenter(clipImagePath);
//        } else if(GET_PHOTO.equals(source)){
////            EventBus.getDefault().post(new EventManager.PhotoSuccessEvent(clipImagePath));
//            finish();
//        }
    }

    private String saveImage(RotateBitmap rotateBitmap) {
        Matrix matrix = rotateBitmap.getRotateMatrix();
        Bitmap croppedImage = Bitmap.createBitmap(rotateBitmap.getBitmap(), 0, 0, rotateBitmap.getWidth() == 0 ? 100 : rotateBitmap.getWidth(),
                rotateBitmap.getHeight() == 0 ? 100 : rotateBitmap.getHeight(), matrix, true);
        File dir = new File(dirPath);// 裁剪图片目录
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, getFileNameByTime());
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            boolean isSuccess = croppedImage.compress(Bitmap.CompressFormat.JPEG, 40, bos);
            bos.flush();
            if (isSuccess) {
                return file.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            CrashReport.postCatchedException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
        return null;
    }

    public static String getFileNameByTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date()) + ".jpg";
    }

    private Bitmap decodeRegionCrop(Rect rect, int outWidth, int outHeight) {
        // Release memory now
        clearImageView();

        InputStream is = null;
        Bitmap croppedImage = null;
        try {
            is = getContentResolver().openInputStream(sourceUri);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            final int width = decoder.getWidth();
            final int height = decoder.getHeight();

            if (exifRotation != 0) {
                // Adjust crop area to account for image rotation
                Matrix matrix = new Matrix();
                matrix.setRotate(-exifRotation);

                RectF adjusted = new RectF();
                matrix.mapRect(adjusted, new RectF(rect));

                // Adjust to account for origin at 0,0
                adjusted.offset(adjusted.left < 0 ? width : 0, adjusted.top < 0 ? height : 0);
                rect = new Rect((int) adjusted.left, (int) adjusted.top, (int) adjusted.right, (int) adjusted.bottom);
            }

            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
                if (croppedImage != null && (rect.width() > outWidth || rect.height() > outHeight)) {
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) outWidth / rect.width(), (float) outHeight / rect.height());
                    croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.getWidth(), croppedImage.getHeight(), matrix, true);
                }
            } catch (IllegalArgumentException e) {
                // Rethrow with some extra information
                throw new IllegalArgumentException("Rectangle " + rect + " is outside of the image ("
                        + width + "," + height + "," + exifRotation + ")", e);
            }

        } catch (IOException e) {
            setResultException(e);
        } catch (OutOfMemoryError e) {
            setResultException(e);
        } finally {
            CropUtil.closeSilently(is);
        }
        return croppedImage;
    }

    private void clearImageView() {
        imageView.clear();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
        System.gc();
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(saveAsPng ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG,
                            90,     // note: quality is ignored when using PNG
                            outputStream);
                }
            } catch (IOException e) {
                setResultException(e);
            } finally {
                CropUtil.closeSilently(outputStream);
            }

            CropUtil.copyExifRotation(
                    CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri),
                    CropUtil.getFromMediaUri(this, getContentResolver(), saveUri)
            );

            setResultUri(saveUri);
        }

        final Bitmap b = croppedImage;
        handler.post(new Runnable() {
            public void run() {
                imageView.clear();
                b.recycle();
            }
        });

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
//        presenter.onDestory();
    }

    @Override
    public boolean onSearchRequested() {
        return false;
    }

    public boolean isSaving() {
        return isSaving;
    }

    private void setResultUri(Uri uri) {
        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
    }

    private void setResultException(Throwable throwable) {
        setResult(Crop.RESULT_ERROR, new Intent().putExtra(Crop.Extra.ERROR, throwable));
    }

}
