package com.taisheng.now.bussiness.watch.watchme;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.MiandaraoShijianduanPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Response;


public class WatchMianDaraoSettingliActivity extends BaseIvActivity {
    private TextView date_tv_start;
    private TimePickerDialog timePickerDialog;
    private String time;


    private TextView date_tv_end;
    private TimePickerDialog timePickerDialog_end;
    private String time_end;


    View tv_save;
    View tv_cancel;

    @Override
    public void initView() {
        setContentView(R.layout.layout_watch_miandarao);




        date_tv_start = (TextView) findViewById(R.id.date_tv_start);
        final int hour;
        final int miniute;
        Calendar c = Calendar.getInstance();
        if (!TextUtils.isEmpty(time)) {
            String[] times = time.split(":");
            hour = Integer.parseInt(times[0]);
            miniute = Integer.parseInt(times[1]);
        } else {
            hour = c.get(Calendar.HOUR_OF_DAY);
            miniute = c.get(Calendar.MINUTE);
        }
        timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                if (hourOfDay < 10) {
                    time = "0" + hourOfDay;
                } else {
                    time = hourOfDay + "";
                }
                if (minuteOfDay < 10) {
                    time = time + ":0" + minuteOfDay;
                } else {
                    time = time + ":" + minuteOfDay;
                }

                date_tv_start.setText(time);
            }
        }, hour, miniute, true);
        date_tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePickerDialog.show();

            }
        });


        date_tv_end = (TextView) findViewById(R.id.date_tv_end);
        final int hour_end;
        final int miniute_end;
        Calendar c_end = Calendar.getInstance();
        if (!TextUtils.isEmpty(time_end)) {
            String[] times_end = time_end.split(":");
            hour_end = Integer.parseInt(times_end[0]);
            miniute_end = Integer.parseInt(times_end[1]);
        } else {
            hour_end = c_end.get(Calendar.HOUR_OF_DAY);
            miniute_end = c_end.get(Calendar.MINUTE);
        }
        timePickerDialog_end = new TimePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                if (hourOfDay < 10) {
                    time_end = "0" + hourOfDay;
                } else {
                    time_end = hourOfDay + "";
                }
                if (minuteOfDay < 10) {
                    time_end = time_end + ":0" + minuteOfDay;
                } else {
                    time_end = time_end + ":" + minuteOfDay;
                }

                date_tv_end.setText(time_end);
            }
        }, hour_end, miniute_end, true);
        date_tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePickerDialog_end.show();

            }
        });


        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("起始时间".equals(date_tv_start.getText().toString()) || "结束时间".equals(date_tv_end.getText().toString())) {
                    ToastUtil.showAtCenter("请选择时间");
                    return;
                }

                MiandaraoShijianduanPostBean bean = new MiandaraoShijianduanPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.deviceId = WatchInstance.getInstance().deviceId;
                String nowTime = date_tv_start.getText() + "-" + date_tv_end.getText();


                if (!TextUtils.isEmpty(oldtime)) {
                    WatchInstance.getInstance().miandaraoList.remove(position);
                    WatchInstance.getInstance().miandaraoList.add(position, nowTime);
                    switch (WatchInstance.getInstance().miandaraoList.size()) {
//                        case 0:
//                            bean.timeSlot1= WatchInstance.getInstance().miandaraoList.get(0);
//                            bean.timeSlot2 = "";
//                            bean.timeSlot3 = "";
//                            bean.timeSlot4 = "";
//                            break;
                        case 1:
                            bean.timeSlot1 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot2 = "";
                            bean.timeSlot3 = "";
                            bean.timeSlot4 = "";
                            break;
                        case 2:
                            bean.timeSlot1 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(1);
                            bean.timeSlot3 = "";
                            bean.timeSlot4 = "";
                            break;
                        case 3:
                            bean.timeSlot1 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(1);
                            bean.timeSlot3 = WatchInstance.getInstance().miandaraoList.get(2);
                            bean.timeSlot4 = "";
                            break;
                        case 4:
                            bean.timeSlot1 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(1);
                            bean.timeSlot3 = WatchInstance.getInstance().miandaraoList.get(2);
                            bean.timeSlot4 = WatchInstance.getInstance().miandaraoList.get(3);
                            break;

                    }
                } else {
                    switch (WatchInstance.getInstance().miandaraoList.size()) {
                        case 0:
                            bean.timeSlot1 = nowTime;
                            bean.timeSlot2 = "";
                            bean.timeSlot3 = "";
                            bean.timeSlot4 = "";
                            break;
                        case 1:
                            bean.timeSlot1 = nowTime;
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot3 = "";
                            bean.timeSlot4 = "";
                            break;
                        case 2:
                            bean.timeSlot1 = nowTime;
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot3 = WatchInstance.getInstance().miandaraoList.get(1);
                            bean.timeSlot4 = "";
                            break;
                        case 3:
                            bean.timeSlot1 = nowTime;
                            bean.timeSlot2 = WatchInstance.getInstance().miandaraoList.get(0);
                            bean.timeSlot3 = WatchInstance.getInstance().miandaraoList.get(1);
                            bean.timeSlot4 = WatchInstance.getInstance().miandaraoList.get(2);
                            break;
                    }
                }


                ApiUtils.getApiService_hasdialog().notDisturbSetting(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("设置成功");
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
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initDatas();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("设定时间");
    }

    int position;
    String oldtime;

    void initDatas() {
        Intent intent = getIntent();
        oldtime = intent.getStringExtra("time");
        if (!TextUtils.isEmpty(oldtime)) {
            String temp[] = oldtime.split("-");
            date_tv_start.setText(temp[0]);
            date_tv_end.setText(temp[1]);
        }


        position = intent.getIntExtra("position", 0);
    }

//    public static String getTime(Date date) {
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        return format.format(date);
//    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }


}
