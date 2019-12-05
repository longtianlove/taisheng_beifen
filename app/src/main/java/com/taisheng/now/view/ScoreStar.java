package com.taisheng.now.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.R;

import java.text.DecimalFormat;

/**
 * Created by dragon on 2019/6/29.
 */

public class ScoreStar extends LinearLayout {

    float score;
    TextView tv_score;
    Button btn_star1;
    Button btn_star2;
    Button btn_star3;
    Button btn_star4;
    Button btn_star5;

    public ScoreStar(Context context) {
        super(context);
    }

    public ScoreStar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ScoreStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_score_star, this, true);
        tv_score = (TextView) rootView.findViewById(R.id.tv_score);
        btn_star1 = (Button) rootView.findViewById(R.id.btn_star1);
        btn_star2 = (Button) rootView.findViewById(R.id.btn_star2);
        btn_star3 = (Button) rootView.findViewById(R.id.btn_star3);
        btn_star4 = (Button) rootView.findViewById(R.id.btn_star4);
        btn_star5 = (Button) rootView.findViewById(R.id.btn_star5);
        setScore("0");
    }


    public void setScore(String score) {
        if(score==null){
            return;
        }
        float d= Float.parseFloat(score);

        int score_int = (int) Math.floor(d);

        DecimalFormat decimalFormat=new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        score=decimalFormat.format(d);//format 返回的是字符串
        tv_score.setText(score + "");
        btn_star1.setEnabled(false);
        btn_star2.setEnabled(false);
        btn_star3.setEnabled(false);
        btn_star4.setEnabled(false);
        btn_star5.setEnabled(false);
        switch (score_int) {
            case 0:

                break;
            case 1:
                btn_star1.setEnabled(true);
                break;
            case 2:
                btn_star1.setEnabled(true);
                btn_star2.setEnabled(true);
                break;
            case 3:
                btn_star1.setEnabled(true);
                btn_star2.setEnabled(true);
                btn_star3.setEnabled(true);
                break;
            case 4:
                btn_star1.setEnabled(true);
                btn_star2.setEnabled(true);
                btn_star3.setEnabled(true);
                btn_star4.setEnabled(true);
                break;
            case 5:
                btn_star1.setEnabled(true);
                btn_star2.setEnabled(true);
                btn_star3.setEnabled(true);
                btn_star4.setEnabled(true);
                btn_star5.setEnabled(true);
                break;
            default:
                tv_score.setText(5 + "");
                btn_star1.setEnabled(true);
                btn_star2.setEnabled(true);
                btn_star3.setEnabled(true);
                btn_star4.setEnabled(true);
                btn_star5.setEnabled(true);
                break;
        }

    }


}
