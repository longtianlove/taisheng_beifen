package com.taisheng.now.ZXing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.taisheng.now.R;
import com.taisheng.now.base.BaseFragmentActivity;
import com.taisheng.now.util.ImageUtil;
import com.taisheng.now.util.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;


/**
 * Created by Administrator on 2017/1/1.
 */

public class ZXingActivity extends BaseFragmentActivity {

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int REQ_ANALYZE_BITMAP=1;

    private CaptureFragment captureFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("扫一扫");
        setContentView(R.layout.activity_zxing);
//        setNextView("相册", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               openPhotoAlbum();
//            }
//        });
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.activity_zxing_findview);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_zxing_container, captureFragment).commit();
    }

    /**
     * 打开系统相册选择照片
     */
    private void openPhotoAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, REQ_ANALYZE_BITMAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQ_ANALYZE_BITMAP == requestCode){
            analyzeBitmapFromAlbum(data);
        }
    }

    /**
     * 解析从相册选择的数据
     * @param data
     */
    private void analyzeBitmapFromAlbum(Intent data){
        if(null == data){
            return;
        }
        try {
            Uri uri=data.getData();
            CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(ZXingActivity.this,uri),analyzeCallback);
        } catch (Exception e) {
            analyzeCallback.onAnalyzeFailed();
        }
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ZXingActivity.this.setResult(RESULT_OK, resultIntent);
            ZXingActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtil.showAtCenter("解析失败");
        }
    };

    public static void skipToAsResult(Activity context, int Request_code){
        Intent intent=new Intent(context,ZXingActivity.class);
        context.startActivityForResult(intent,Request_code);
    }
}
