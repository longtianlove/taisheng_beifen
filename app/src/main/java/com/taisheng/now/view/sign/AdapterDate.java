package com.taisheng.now.view.sign;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taisheng.now.Constants;
import com.taisheng.now.EventManage;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.result.SignBean;
import com.taisheng.now.bussiness.bean.result.SignResultBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AdapterDate extends BaseAdapter {

    private Context context;
    private List<Integer> days = new ArrayList<>();
    //日历数据
    private List<Boolean> status = new ArrayList<>();
    //签到状态，实际应用中初始化签到状态可通过该字段传递
    private OnSignedSuccess onSignedSuccess;
    //签到成功的回调方法，相应的可自行添加签到失败时的回调方法

    public AdapterDate(Context context) {
        this.context = context;
        int maxDay = DateUtil.getCurrentMonthLastDay();//获取当月天数
        for (int i = 0; i < DateUtil.getFirstDayOfMonth() - 1; i++) {
            //DateUtil.getFirstDayOfMonth()获取当月第一天是星期几，星期日是第一天，依次类推
            days.add(0);
            //0代表需要隐藏的item
            status.add(false);
            //false代表为签到状态
        }


        for (int i = 0; i < maxDay; i++) {
            days.add(i + 1);
            //初始化日历数据
            status.add(false);
            //初始化日历签到状态
        }


    }

    void qiandao() {
//        int position = DateUtil.getFirstDayOfMonth() - 1+DateUtil.getPostion();
//        status.set(position, true);
        days.clear();
        status.clear();
        for (int i = 0; i < DateUtil.getFirstDayOfMonth() - 1; i++) {
            //DateUtil.getFirstDayOfMonth()获取当月第一天是星期几，星期日是第一天，依次类推
            days.add(0);
            //0代表需要隐藏的item
            status.add(false);
            //false代表为签到状态
        }


        BasePostBean bean = new BasePostBean();
        bean.token = UserInstance.getInstance().getToken();
        bean.userId = UserInstance.getInstance().getUid();
        ApiUtils.getApiService().nowSign(bean).enqueue(new TaiShengCallback<BaseBean<SignResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<SignResultBean>> response, BaseBean<SignResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        ArrayList<SignBean> signArraylist = message.result.list;
                        if (signArraylist != null && !signArraylist.isEmpty()) {
                            for (int i = 0; i < signArraylist.size(); i++) {
                                days.add(i + 1);

                                if ("true".equals(signArraylist.get(i).signFlag)) {
                                    //初始化日历数据
                                    status.add(true);
                                } else {
                                    status.add(false);

                                }

                                //初始化日历签到状态
                            }
                            notifyDataSetChanged();
                        }


                        EventManage.qiaodaoSuccess event = new EventManage.qiaodaoSuccess(message.result.tomorrowPoints, message.result.points);
                        EventBus.getDefault().post(event);
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<SignResultBean>> call, Throwable t) {

            }
        });


    }


    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int i) {
        return days.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gv, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv = view.findViewById(R.id.tvWeek);
        viewHolder.rlItem = view.findViewById(R.id.rlItem);
        viewHolder.ivStatus = view.findViewById(R.id.ivStatus);
        viewHolder.tv.setText(days.get(i) + "");
        if (days.get(i) == 0) {
            viewHolder.rlItem.setVisibility(View.GONE);
        }
        //这里有个bug
        if(days.get(i)==1){
            viewHolder.rlItem.setVisibility(View.VISIBLE);
        }
        if (status.get(i)) {
            viewHolder.tv.setTextColor(Color.parseColor("#FD0000"));
            viewHolder.ivStatus.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv.setTextColor(Color.parseColor("#666666"));
            viewHolder.ivStatus.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (status.get(i)) {
//                    Toast.makeText(context, "已签到", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "签到成功", Toast.LENGTH_SHORT).show();
//                    status.set(i, true);
//                    notifyDataSetChanged();
//                    if (onSignedSuccess != null) {
//                        onSignedSuccess.OnSignedSuccess();
//                    }
//                }
            }
        });
        return view;
    }

    class ViewHolder {
        RelativeLayout rlItem;
        TextView tv;
        ImageView ivStatus;
    }

    public void setOnSignedSuccess(OnSignedSuccess onSignedSuccess) {
        this.onSignedSuccess = onSignedSuccess;
    }
}
