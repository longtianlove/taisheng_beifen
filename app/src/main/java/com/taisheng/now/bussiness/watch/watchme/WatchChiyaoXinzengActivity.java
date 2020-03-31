package com.taisheng.now.bussiness.watch.watchme;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.SetChiyaoPostBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.naozhong.SelectRemindCyclePopup;
import com.th.j.commonlibrary.contrarywind.adapter.ArrayWheelAdapter;
import com.th.j.commonlibrary.contrarywind.adapter.ArrayWheelAdapter2;
import com.th.j.commonlibrary.contrarywind.listener.OnItemSelectedListener;
import com.th.j.commonlibrary.contrarywind.view.WheelView;
import com.th.j.commonlibrary.global.Global;
import com.th.j.commonlibrary.utils.DateUtil;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


public class WatchChiyaoXinzengActivity extends BaseIvActivity {

    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.wheelview)
    WheelView wheelview;
    @BindView(R.id.wheelview2)
    WheelView wheelview2;
    @BindView(R.id.wheelview3)
    WheelView wheelview3;
    @BindView(R.id.tv_repeat_value)
    TextView tvRepeatValue;
    @BindView(R.id.repeat_rl)
    LinearLayout repeatRl;
    @BindView(R.id.et_shuru)
    EditText etShuru;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    private int cycle;

    public String takepillsNum;
    private List<String> hour;
    private List<String> min;
    private String time;
    private String timeH;
    private String timeM;
    private String intenType;

    @Override
    public void initView() {
        setContentView(R.layout.layout_watchchiyao);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        takepillsNum = getIntent().getStringExtra("takepillsNum");
        String takepillsText = getIntent().getStringExtra("takepillsText");
        WatchInstance.getInstance().chiyaobean = new SetChiyaoPostBean();
        intenType = getIntent().getStringExtra(Global.INTENT_TYPE);
        if (Global.MEDICINE_UPDATA.equals(intenType)) {
            tvCancel.setText(getString(R.string.delete));
            etShuru.setText(takepillsText);
        } else {
            tvCancel.setText(getString(R.string.cancal));
        }
    }

    @Override
    public void addData() {
        wheelview.setCyclic(true); //是否循环展示
        wheelview2.setCyclic(true); //是否循环展示
        hour = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hour.add("0" + i);
            } else {
                hour.add(i + "");
            }
        }
        wheelview.setGravity(Gravity.RIGHT);
        wheelview.setTextColorCenter(ContextCompat.getColor(this, R.color.color00c8aa));
        wheelview.setAdapter(new ArrayWheelAdapter(this, hour));
        wheelview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtilH.e("" + hour.get(index));
                timeH = hour.get(index);

            }
        });
        min = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                min.add("0" + i);
            } else {
                min.add(i + "");
            }
        }
        wheelview2.setGravity(Gravity.LEFT);
        wheelview2.setTextColorCenter(ContextCompat.getColor(this, R.color.color00c8aa));
        wheelview2.setAdapter(new ArrayWheelAdapter2(this, min));
        wheelview2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                LogUtilH.e("" + min.get(index));
                timeM = min.get(index);
            }
        });
        String[] startTime = null;
        if (Global.MEDICINE_UPDATA.equals(intenType)) {
            startTime = getIntent().getStringExtra("startTime").split(":");
        } else {
            startTime = DateUtil.stampToDate2(DateUtil.getTime()).split(":");
            LogUtilH.e(DateUtil.stampToDate2(DateUtil.getTime()) + "*****");
        }
        int indext = 0;
        for (int i = 0; i < hour.size(); i++) {
            if (hour.get(i).equals(startTime[0])) {
                indext = i;
            }
        }
        wheelview.setCurrentItem(indext);
        int indext2 = 0;
        for (int i = 0; i < min.size(); i++) {
            if (min.get(i).equals(startTime[1])) {
                indext2 = i;
            }
        }
        wheelview2.setCurrentItem(indext2);

        timeH = hour.get(wheelview.getInitPosition());
        timeM = min.get(wheelview2.getInitPosition());


        wheelview3.setGravity(Gravity.CENTER);
        wheelview3.setTextColorOut(ContextCompat.getColor(this, R.color.colorWhite));
        wheelview3.setTextColorCenter(ContextCompat.getColor(this, R.color.colorWhite));
        wheelview3.setAdapter(new ArrayWheelAdapter2(this, min));

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        if (Global.MEDICINE_UPDATA.equals(intenType)) {
            tvTitle.setText("修改提醒");
        } else {
            tvTitle.setText("新增吃药");
        }
    }

    public void selectRemindCycle() {
        final SelectRemindCyclePopup fp = new SelectRemindCyclePopup(this);
        fp.showPopup(llAll);
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
                        tvRepeatValue.setText(parseRepeat(repeat, 0));
                        cycle = repeat;
                        fp.dismiss();
                        break;
                    case 8:
                        tvRepeatValue.setText("重复");
                        WatchInstance.getInstance().chiyaobean.frequency = "2";
                        cycle = 0;
                        fp.dismiss();
                        break;
                    case 9:
                        tvRepeatValue.setText("一次");
                        WatchInstance.getInstance().chiyaobean.frequency = "1";
                        cycle = -1;
                        fp.dismiss();
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
            WatchInstance.getInstance().chiyaobean.frequency = "2";
            return cycle;
        }
        WatchInstance.getInstance().chiyaobean.frequency = "3";
        if (repeat % 2 == 1) {
            cycle = "星期一";
            WatchInstance.getInstance().chiyaobean.isOpenWeek1 = "1";
            weeks = "1";
        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek1 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek2 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek3 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek4 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";

        }
        if (repeat % 4 >= 2) {
            if ("".equals(cycle)) {
                cycle = "星期二";
                weeks = "2";
            } else {
                cycle = cycle + "," + "星期二";
                weeks = weeks + "," + "2";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek2 = "1";

        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek2 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek3 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek4 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }
        if (repeat % 8 >= 4) {
            if ("".equals(cycle)) {
                cycle = "星期三";
                weeks = "3";
            } else {
                cycle = cycle + "," + "星期三";
                weeks = weeks + "," + "3";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek3 = "1";

        } else {

            WatchInstance.getInstance().chiyaobean.isOpenWeek3 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek4 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }
        if (repeat % 16 >= 8) {
            if ("".equals(cycle)) {
                cycle = "星期四";
                weeks = "4";
            } else {
                cycle = cycle + "," + "星期四";
                weeks = weeks + "," + "4";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek4 = "1";

        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek4 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }
        if (repeat % 32 >= 16) {
            if ("".equals(cycle)) {
                cycle = "星期五";
                weeks = "5";
            } else {
                cycle = cycle + "," + "星期五";
                weeks = weeks + "," + "5";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "1";

        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek5 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }
        if (repeat % 64 >= 32) {
            if ("".equals(cycle)) {
                cycle = "星期六";
                weeks = "6";
            } else {
                cycle = cycle + "," + "星期六";
                weeks = weeks + "," + "6";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "1";

        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek6 = "0";
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }
        if (repeat / 64 == 1) {
            if ("".equals(cycle)) {
                cycle = "星期日";
                weeks = "7";
            } else {
                cycle = cycle + "," + "星期日";
                weeks = weeks + "," + "7";
            }
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "1";

        } else {
            WatchInstance.getInstance().chiyaobean.isOpenWeek7 = "0";
        }

        return flag == 0 ? cycle : weeks;
    }

    @OnClick({R.id.repeat_rl, R.id.tv_save, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repeat_rl:
                selectRemindCycle();
                break;
            case R.id.tv_save:
                time = timeH + ":" + timeM;
                LogUtilH.e(hour.get(wheelview.getCurrentItem()) + "--" + wheelview.getCurrentItem());
                if (TextsUtils.isEmpty(time)) {
                    Uiutils.showToast("请选择时间");
                    return;
                }
                if (TextsUtils.isEmpty(TextsUtils.getTexts(etShuru))) {
                    Uiutils.showToast("请输入提醒内容");
                    return;
                }
                SetChiyaoPostBean setNaozhongPostBean = new SetChiyaoPostBean();
                setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                setNaozhongPostBean.deviceId = WatchInstance.getInstance().deviceId;
                setNaozhongPostBean.takepillsNum = takepillsNum;
                setNaozhongPostBean.frequency = WatchInstance.getInstance().chiyaobean.frequency;
                setNaozhongPostBean.isOpen = "1";
                setNaozhongPostBean.isOpenWeek1 = WatchInstance.getInstance().chiyaobean.isOpenWeek1;
                setNaozhongPostBean.isOpenWeek2 = WatchInstance.getInstance().chiyaobean.isOpenWeek2;
                setNaozhongPostBean.isOpenWeek3 = WatchInstance.getInstance().chiyaobean.isOpenWeek3;
                setNaozhongPostBean.isOpenWeek4 = WatchInstance.getInstance().chiyaobean.isOpenWeek4;
                setNaozhongPostBean.isOpenWeek5 = WatchInstance.getInstance().chiyaobean.isOpenWeek5;
                setNaozhongPostBean.isOpenWeek6 = WatchInstance.getInstance().chiyaobean.isOpenWeek6;
                setNaozhongPostBean.isOpenWeek7 = WatchInstance.getInstance().chiyaobean.isOpenWeek7;
                setNaozhongPostBean.startTime = time;
                setNaozhongPostBean.takepillsText = TextsUtils.getTexts(etShuru);

                ApiUtils.getApiService().setWatchTakepills(setNaozhongPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                Uiutils.showToast("设置成功");
                                finish();
                                break;
                            case 404000:
                                Uiutils.showToast("请设置重复频率");
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
                break;
            case R.id.tv_cancel:
                if (Global.MEDICINE_UPDATA.equals(intenType)) {

                } else {
                    this.finish();
                }
                break;
        }
    }
}
