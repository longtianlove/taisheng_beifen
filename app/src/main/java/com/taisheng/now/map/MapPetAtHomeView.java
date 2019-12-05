package com.taisheng.now.map;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.R;


/**
 * Created by Administrator on 2017/1/14.
 */

public class MapPetAtHomeView extends LinearLayout {
    private String avaterUrl;
    private SimpleDraweeView avater;
    private boolean isShowed = false;

    public MapPetAtHomeView(Context context) {
        this(context, null);
    }

    public MapPetAtHomeView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MapPetAtHomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.map_pet_athomeview, this, true);
        avater = (SimpleDraweeView) rootView.findViewById(R.id.map_pet_athome_avater);
//        String url = PetInfoInstance.getInstance().packBean.logo_url;
//        setAvaterUrl(url);

    }

    //    public void setAvaterUrl(String url) {
//        if (TextUtils.isEmpty(url)) {
//            Log.e("longtianlove","url为空");
//            return;
//        }
//        Log.e("longtianlove",url);
//        avaterUrl = url;
//        if (null == avater) {
//            return;
//        }
//        Uri uri = Uri.parse(avaterUrl);
//        avater.setImageURI(uri);
////        AsyncImageTask.INSTANCE.loadImage(avater, avaterUrl, this);
//    }
    public void setAvaterUrl1(String url) {
        if (url == null || "".equals(url)) {
//            avater.setBackgroundResource(R.drawable.header_default_big);
            avater.setImageDrawable(getResources().getDrawable(R.drawable.doctor_default));

            return;
        }
        Uri uri = Uri.parse(url);
        avater.setImageURI(uri);
    }

}
