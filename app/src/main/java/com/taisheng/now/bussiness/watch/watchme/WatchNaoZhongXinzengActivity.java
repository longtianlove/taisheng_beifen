package com.taisheng.now.bussiness.watch.watchme;

import android.app.TimePickerDialog;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SetNaozhongPostBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongLIstBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.naozhong.SelectRemindCyclePopup;
import com.taisheng.now.view.naozhong.SelectRemindWayPopup;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;


public class WatchNaoZhongXinzengActivity extends BaseActivity implements View.OnClickListener {
    public View iv_back;
    private TextView date_tv;
    //    private TimePickerView pvTime;
    private TimePickerDialog timePickerDialog;
    private RelativeLayout repeat_rl, ring_rl;
    private TextView tv_repeat_value, tv_ring_value;
    private RelativeLayout allLayout;
    public View tv_save;
    public View tv_cancel;


    private String time;
    private int cycle;
    private int ring;
    private JobScheduler mJobScheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_watchnaozhong);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        allLayout = (RelativeLayout) findViewById(R.id.all_layout);
        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("选择时间".equals(date_tv.getText())) {
                    ToastUtil.showAtCenter("请选择时间");
                    return;
                }
                //TODO 设置时间
                SetNaozhongPostBean setNaozhongPostBean = new SetNaozhongPostBean();
                setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                setNaozhongPostBean.clientId = WatchInstance.getInstance().deviceId;
//                setNaozhongPostBean.startTime=date_tv.getText().toString();
                WatchInstance.getInstance().naozhongLIstBean.isOpen = "1";
                WatchInstance.getInstance().naozhongLIstBean.startTime=date_tv.getText().toString();
                WatchInstance.getInstance().mDataNaoZhong.add(WatchInstance.getInstance().naozhongLIstBean);

                setNaozhongPostBean.watchRemindList = WatchInstance.getInstance().mDataNaoZhong;
                ApiUtils.getApiService().setWatchREMIND(setNaozhongPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:

                                ToastUtil.showAtCenter("闹钟设置成功");
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
        WatchInstance.getInstance().naozhongLIstBean = new NaozhongLIstBean();
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_tv = (TextView) findViewById(R.id.date_tv);
        repeat_rl = (RelativeLayout) findViewById(R.id.repeat_rl);
        repeat_rl.setOnClickListener(this);
        ring_rl = (RelativeLayout) findViewById(R.id.ring_rl);
        ring_rl.setOnClickListener(this);
        tv_repeat_value = (TextView) findViewById(R.id.tv_repeat_value);
        tv_ring_value = (TextView) findViewById(R.id.tv_ring_value);
//        pvTime = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
//        pvTime.setTime(new Date());
//        pvTime.setCyclic(false);
//        pvTime.setCancelable(true);
//        //时间选择后回调
//        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//
//            @Override
//            public void onTimeSelect(Date date) {
//                time = getTime(date);
//                date_tv.setText(time);
//            }
//        });


//        timePickerDialog.setTitle("pick");
//        timePickerDialog.show();
        final int hour;
        final int miniute;
//        if (mAlarm != null) {
//            time = mAlarm.getAlarmTime();
//            date_tv.setText(mAlarm.getAlarmTime());
//            tv_repeat_value.setText(mAlarm.getAlarmTypeName());
//            cycle = mAlarm.getCycle();
//            ring = mAlarm.getAlarmType();
//            switch (mAlarm.getAlarmType()) {
//                case 0:
//                    tv_ring_value.setText("Normal to wake up");
//                    break;
//                case 1:
//                    tv_ring_value.setText("Easy to wake up");
//                    break;
//                case 2:
//                    tv_ring_value.setText("Force to wake up");
//                    break;
//            }
//        }
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

                date_tv.setText(time);
            }
        }, hour, miniute, true);
        date_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePickerDialog.show();
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//            JobInfo.Builder builder = new JobInfo.Builder(12345,
//                    new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
//                builder.setPeriodic(3000); //每隔60秒运行一次
//                builder.setRequiresCharging(true);
//                builder.setPersisted(true);  //设置设备重启后，是否重新执行任务
//                builder.setRequiresDeviceIdle(true);
//              mJobScheduler.schedule(builder.build());          }
//
//
            }
        });
//        myConn = new MyConn();
//        Intent daemonIntent = new Intent(this, DaemonService.class);
////        bindService(daemonIntent, myConn, BIND_AUTO_CREATE);
//        startService(daemonIntent);

    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.ring_rl:
                selectRingWay();
                break;
//            case R.id.set_btn:
//                setClock();
//                break;
//            case R.id.cancel_btn:
//                finish();
            default:
                break;
        }
    }

//    private class MyConn implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            //iBinder为服务里面onBind()方法返回的对象，所以可以强转为IMyBinder类型
//            myBinder = (IMyBinder) iBinder;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//        }
//    }


    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindCyclePopupListener(new SelectRemindCyclePopup
                .SelectRemindCyclePopupOnClickListener() {

            @Override
            public void obtainMessage(int flag, String ret) {
                switch (flag) {
                    // 星期一
                    case 0:

                        break;
                    // 星期二
                    case 1:

                        break;
                    // 星期三
                    case 2:

                        break;
                    // 星期四
                    case 3:

                        break;
                    // 星期五
                    case 4:

                        break;
                    // 星期六
                    case 5:

                        break;
                    // 星期日
                    case 6:

                        break;
                    // 确定
                    case 7:
                        int repeat = Integer.valueOf(ret);
                        tv_repeat_value.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tv_repeat_value.setText("每天");
                        WatchInstance.getInstance().naozhongLIstBean.frequency = "2";
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tv_repeat_value.setText("一次");
                        WatchInstance.getInstance().naozhongLIstBean.frequency = "1";
                        cycle = -1;
                        fp.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }


    public void selectRingWay() {
        SelectRemindWayPopup fp = new SelectRemindWayPopup(this);
        fp.showPopup(allLayout);
        fp.setOnSelectRemindWayPopupListener(new SelectRemindWayPopup
                .SelectRemindWayPopupOnClickListener() {

            @Override
            public void obtainMessage(int flag) {
                switch (flag) {
                    // 震动
                    case 0:
                        tv_ring_value.setText("Normal to wake up");
                        ring = 0;
                        break;
                    // 铃声
                    case 1:
                        tv_ring_value.setText("Easy to wake up");
                        ring = 1;
                        break;
                    case 2:
                        tv_ring_value.setText("Force to wake up");
                        ring = 2;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * @param repeat 解析二进制闹钟周期
     * @param flag   flag=0返回带有汉字的周一，周二cycle等，flag=1,返回weeks(1,2,3)
     * @return
     */
    public static String parseRepeat(int repeat, int flag) {
        String cycle = "";
        String weeks = "";
        if (repeat == 0) {
//            repeat = 127;
            cycle = "每天";
            WatchInstance.getInstance().naozhongLIstBean.frequency = "2";
            return cycle;
        }
        WatchInstance.getInstance().naozhongLIstBean.frequency = "3";
        if (repeat % 2 == 1) {
            cycle = "星期一";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek1 = "1";
            weeks = "1";
        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek1 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek2 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek3 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek4 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";

        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "星期二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "星期二";
                weeks = weeks + "," + "2";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek2 = "1";

        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek2 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek3 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek4 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "星期三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "星期三";
                weeks = weeks + "," + "3";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek3 = "1";

        }else{

            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek3 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek4 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "星期四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "星期四";
                weeks = weeks + "," + "4";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek4 = "1";

        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek4 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "星期五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "星期五";
                weeks = weeks + "," + "5";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "1";

        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek5 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "星期六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "星期六";
                weeks = weeks + "," + "6";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "1";

        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek6 = "0";
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "星期日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "星期日";
                weeks = weeks + "," + "7";
            }
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "1";

        }else{
            WatchInstance.getInstance().naozhongLIstBean.isOpenWeek7 = "0";
        }

        return flag == 0 ? cycle : weeks;
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

//    private int getAlarmId() {
//        int id = 0;
//        List<Alarm> alarmList = MyApp.instances.getDaoSession().getAlarmDao().queryBuilder().orderDesc(AlarmDao.Properties.Id).list();
//        if (alarmList != null && alarmList.size() > 0) {
//            id = alarmList.get(0).getId().intValue();
//        }
//        return id;
//    }

    private long calMethod(int weekflag, long dateTime) {
        long time = 0;
        //weekflag == 0表示是按天为周期性的时间间隔或者是一次行的，weekfalg非0时表示每周几的闹钟并以周为时间间隔
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        if (weekflag != 0) {
            if (1 == week) {
                week = 7;
            } else if (2 == week) {
                week = 1;
            } else if (3 == week) {
                week = 2;
            } else if (4 == week) {
                week = 3;
            } else if (5 == week) {
                week = 4;
            } else if (6 == week) {
                week = 5;
            } else if (7 == week) {
                week = 6;
            }

            if (weekflag == week) {
                if (dateTime > System.currentTimeMillis()) {
                    time = dateTime;
                } else {
                    time = dateTime + 7 * 24 * 3600 * 1000;
                }
            } else if (weekflag > week) {
                time = dateTime + (weekflag - week) * 24 * 3600 * 1000;
            } else if (weekflag < week) {
                time = dateTime + (weekflag - week + 7) * 24 * 3600 * 1000;
            }
        } else {
            if (dateTime > System.currentTimeMillis()) {
                time = dateTime;
            } else {
                time = dateTime + 24 * 3600 * 1000;
            }
        }
        return time;
    }
}
