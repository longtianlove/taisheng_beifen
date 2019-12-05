package com.taisheng.now.map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.R;


/**
 * Created by Administrator on 2017/1/14.
 */

public class EndLineAvaterView extends LinearLayout {
    private String avaterUrl;
    private SimpleDraweeView avater;
    private boolean isShowed = false;

    public EndLineAvaterView(Context context) {
        this(context, null);
    }

    public EndLineAvaterView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public EndLineAvaterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.map_end, this, true);
//        avater = (SimpleDraweeView) rootView.findViewById(R.id.map_phone_loca_avater);
    }


}
