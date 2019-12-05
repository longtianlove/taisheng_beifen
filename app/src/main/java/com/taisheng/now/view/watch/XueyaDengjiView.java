package com.taisheng.now.view.watch;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.taisheng.now.R;

/**
 * Created by dragon on 2019/6/29.
 */

public class XueyaDengjiView extends LinearLayout {



    public XueyaDengjiView(Context context) {
        super(context);
    }

    public XueyaDengjiView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public XueyaDengjiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_xueyadengji, this, true);

    }




//    public void setScore(float score) {
//
//        int score_int = (int) Math.floor(score);
//        resultScore = score_int;
//        tv_score.setText("一般");
//        btn_star1.setEnabled(false);
//        btn_star2.setEnabled(false);
//        btn_star3.setEnabled(false);
//        btn_star4.setEnabled(false);
//        btn_star5.setEnabled(false);
//        switch (score_int) {
//            case 0:
//
//                break;
//            case 1:
//                tv_score.setText("非常差");
//                btn_star1.setEnabled(true);
//                break;
//            case 2:
//                tv_score.setText("差");
//
//                btn_star1.setEnabled(true);
//                btn_star2.setEnabled(true);
//                break;
//            case 3:
//                tv_score.setText("一般");
//                btn_star1.setEnabled(true);
//                btn_star2.setEnabled(true);
//                btn_star3.setEnabled(true);
//                break;
//            case 4:
//                tv_score.setText("好");
//
//                btn_star1.setEnabled(true);
//                btn_star2.setEnabled(true);
//                btn_star3.setEnabled(true);
//                btn_star4.setEnabled(true);
//                break;
//            case 5:
//                tv_score.setText("非常好");
//
//                btn_star1.setEnabled(true);
//                btn_star2.setEnabled(true);
//                btn_star3.setEnabled(true);
//                btn_star4.setEnabled(true);
//                btn_star5.setEnabled(true);
//                break;
//            default:
//                tv_score.setText("非常好");
//                btn_star1.setEnabled(true);
//                btn_star2.setEnabled(true);
//                btn_star3.setEnabled(true);
//                btn_star4.setEnabled(true);
//                btn_star5.setEnabled(true);
//                break;
//        }
//
//    }


}
