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

/**
 * Created by dragon on 2019/6/29.
 */

public class StarGradeCanclick extends LinearLayout {

    float score;
    TextView tv_score;
    Button btn_star1;
    Button btn_star2;
    Button btn_star3;
    Button btn_star4;
    Button btn_star5;

    public StarGradeCanclick(Context context) {
        super(context);
    }

    public StarGradeCanclick(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StarGradeCanclick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_star_grade_canclick, this, true);
        tv_score = (TextView) rootView.findViewById(R.id.tv_score);
        btn_star1 = (Button) rootView.findViewById(R.id.btn_star1);
        btn_star1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setScore(1);
            }
        });
        btn_star2 = (Button) rootView.findViewById(R.id.btn_star2);
        btn_star2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setScore(2);
            }
        });
        btn_star3 = (Button) rootView.findViewById(R.id.btn_star3);
        btn_star3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setScore(3);
            }
        });
        btn_star4 = (Button) rootView.findViewById(R.id.btn_star4);
        btn_star4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setScore(4);
            }
        });
        btn_star5 = (Button) rootView.findViewById(R.id.btn_star5);
        btn_star5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setScore(5);
            }
        });
        setScore(5);
    }


    public int resultScore = 0;


    public void setScore(float score) {

        int score_int = (int) Math.floor(score);
        resultScore = score_int;
        tv_score.setText("一般");
        btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_disabled));
        btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_disabled));
        btn_star3.setBackground(getResources().getDrawable(R.drawable.star_click_disabled));
        btn_star4.setBackground(getResources().getDrawable(R.drawable.star_click_disabled));
        btn_star5.setBackground(getResources().getDrawable(R.drawable.star_click_disabled));
        switch (score_int) {
            case 0:

                break;
            case 1:
                tv_score.setText("非常差");
                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
            case 2:
                tv_score.setText("差");

                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
            case 3:
                tv_score.setText("一般");
                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star3.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
            case 4:
                tv_score.setText("好");

                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star3.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star4.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
            case 5:
                tv_score.setText("非常好");

                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star3.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star4.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star5.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
            default:
                tv_score.setText("非常好");
                btn_star1.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star2.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star3.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star4.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                btn_star5.setBackground(getResources().getDrawable(R.drawable.star_click_enable));
                break;
        }

    }


}
