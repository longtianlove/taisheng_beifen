package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.ChiyaolistPostBean;
import com.taisheng.now.bussiness.watch.bean.post.SetChiyaoPostBean;
import com.taisheng.now.bussiness.watch.bean.result.ChiyaoBeann;
import com.taisheng.now.bussiness.watch.bean.result.ChiyaoLIstResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.WithScrolleViewListView;
import com.th.j.commonlibrary.global.Global;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchChiYaoListActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_chiyaos)
    WithScrolleViewListView lvChiyaos;
    @BindView(R.id.tv_addnaozhong)
    TextView tvAddnaozhong;
    private ArticleAdapter madapter;
    private int size = 0;
    private List<ChiyaoBeann> data;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_chiyao);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        data = new ArrayList();
        madapter = new ArticleAdapter(this);
        lvChiyaos.setAdapter(madapter);
        lvChiyaos.setOnItemClickListener(this);
    }

    @Override
    public void addData() {
        getData();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg02));
    }

    @OnClick(R.id.tv_addnaozhong)
    public void onViewClicked() {
        Intent intent = new Intent(WatchChiYaoListActivity.this, WatchChiyaoXinzengActivity.class);
        String takepillsNum = "1";
        switch (size) {
            case 0:
                takepillsNum = "1";
                break;
            case 1:
                takepillsNum = "2";
                break;
            case 2:
                takepillsNum = "3";
                break;
        }

        intent.putExtra("takepillsNum", takepillsNum);
        intent.putExtra(Global.INTENT_TYPE, Global.MEDICINE_ADD);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (data!=null&&data.size()>0) {
            ChiyaoBeann bean = data.get(position);
            Intent intent = new Intent(WatchChiYaoListActivity.this, WatchChiyaoXinzengActivity.class);
            intent.putExtra("takepillsNum", bean.takepillsNum);
            intent.putExtra(Global.INTENT_TYPE, Global.MEDICINE_UPDATA);
            intent.putExtra("startTime", bean.startTime);
            intent.putExtra("frequency", bean.frequency);
            intent.putExtra("takepillsText", bean.takepillsText);

            startActivity(intent);
        }

    }

    private void getData() {
        ChiyaolistPostBean bean = new ChiyaolistPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchTakepillsList(bean).enqueue(new TaiShengCallback<BaseBean<ChiyaoLIstResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ChiyaoLIstResultBean>> response, BaseBean<ChiyaoLIstResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.records.size() >= 0) {
                            //有消息
//                            PAGE_NO++;
                            data.addAll(message.result.records);
                            size = message.result.records.size();
                            if (size >= 3) {
                                tvAddnaozhong.setVisibility(View.GONE);
                            } else {
                                tvAddnaozhong.setVisibility(View.VISIBLE);
                            }


//                            WatchInstance.getInstance().mDataNaoZhong.clear();
//                            WatchInstance.getInstance().mDataNaoZhong.addAll(message.result.watchRemindList);
//                            if(message.result.size()<10){
//                                lv_articles.setHasLoadMore(false);
//                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
//                                lv_articles.setLoadAllFooterVisible(false);
//                            }else{
//                                lv_articles.setHasLoadMore(true);
//                            }
                            madapter.setmData(data);
                        } else {
//                            //没有消息
//                            lv_articles.setHasLoadMore(false);
//                            lv_articles.setLoadAllViewText("暂时只有这么多文章");
//                            lv_articles.setLoadAllFooterVisible(false);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<ChiyaoLIstResultBean>> call, Throwable t) {

            }
        });
    }

    class ArticleAdapter extends BaseAdapter {

        private Context mcontext;

        private List<ChiyaoBeann> mData;

        public void setmData(List<ChiyaoBeann> mData) {
            this.mData = mData;
            this.notifyDataSetChanged();
        }

        public ArticleAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 声明内部类
            Util util = null;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_chiyao, null);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                util.tv_pinlv = convertView.findViewById(R.id.tv_pinlv);
                util.iv_kaiguan_naozhong = convertView.findViewById(R.id.iv_kaiguan_naozhong);
                util.tv_takepillsText = convertView.findViewById(R.id.tv_takepillsText);

                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            ChiyaoBeann bean = mData.get(position);

            util.tv_time.setText(bean.startTime);
            if ("1".equals(bean.frequency)) {
                util.tv_pinlv.setText("一次");

            } else if ("2".equals(bean.frequency)) {
                util.tv_pinlv.setText("每天");

            } else {
                String temp = new String();
                if ("1".equals(bean.isOpenWeek1)) {
                    temp += "一";
                }
                if ("1".equals(bean.isOpenWeek2)) {
                    if ("".equals(temp)) {
                        temp += "二";
                    } else {
                        temp += ",二";
                    }
                }
                if ("1".equals(bean.isOpenWeek3)) {
                    if ("".equals(temp)) {
                        temp += "三";
                    } else {
                        temp += ",三";
                    }
                }
                if ("1".equals(bean.isOpenWeek4)) {
                    if ("".equals(temp)) {
                        temp += "四";
                    } else {
                        temp += ",四";
                    }
                }
                if ("1".equals(bean.isOpenWeek5)) {
                    if ("".equals(temp)) {
                        temp += "五";
                    } else {
                        temp += ",五";
                    }
                }
                if ("1".equals(bean.isOpenWeek6)) {
                    if ("".equals(temp)) {
                        temp += "六";
                    } else {
                        temp += ",六";
                    }
                }
                if ("1".equals(bean.isOpenWeek7)) {
                    if ("".equals(temp)) {
                        temp += "日";
                    } else {
                        temp += ",日";
                    }
                }
                util.tv_pinlv.setText("星期" + temp);
            }
            util.tv_takepillsText.setText(bean.takepillsText);
            Util finalUtil1 = util;
            util.iv_kaiguan_naozhong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetChiyaoPostBean setNaozhongPostBean = new SetChiyaoPostBean();
                    setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                    setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                    setNaozhongPostBean.deviceId = WatchInstance.getInstance().deviceId;
                    setNaozhongPostBean.takepillsNum = bean.takepillsNum;
                    setNaozhongPostBean.frequency = bean.frequency;
                    setNaozhongPostBean.isOpen = finalUtil1.iv_kaiguan_naozhong.isSelected() ? "0" : "1";
                    setNaozhongPostBean.isOpenWeek1 = bean.isOpenWeek1;
                    setNaozhongPostBean.isOpenWeek2 = bean.isOpenWeek2;
                    setNaozhongPostBean.isOpenWeek3 = bean.isOpenWeek3;
                    setNaozhongPostBean.isOpenWeek4 = bean.isOpenWeek4;
                    setNaozhongPostBean.isOpenWeek5 = bean.isOpenWeek5;
                    setNaozhongPostBean.isOpenWeek6 = bean.isOpenWeek6;
                    setNaozhongPostBean.isOpenWeek7 = bean.isOpenWeek7;
                    setNaozhongPostBean.startTime = bean.startTime;
                    setNaozhongPostBean.takepillsText = bean.takepillsText;

//                WatchInstance.getInstance().chiyaobean.isOpen = "1";
//                WatchInstance.getInstance().chiyaobean.startTime = date_tv.getText().toString();
//                WatchInstance.getInstance().mDataNaoZhong.add(WatchInstance.getInstance().chiyaobean);

//                setNaozhongPostBean.watchRemindList = WatchInstance.getInstance().mDataNaoZhong;
                    ApiUtils.getApiService().setWatchTakepills(setNaozhongPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if (finalUtil1.iv_kaiguan_naozhong.isSelected()) {
                                        finalUtil1.iv_kaiguan_naozhong.setSelected(false);
                                    } else {
                                        finalUtil1.iv_kaiguan_naozhong.setSelected(true);
                                    }


                                    break;
//                                case 404000:
//                                    ToastUtil.showAtCenter("请设置重复频率");
//                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }
            });

            if ("1".equals(bean.isOpen)) {
                util.iv_kaiguan_naozhong.setSelected(true);
            } else {
                util.iv_kaiguan_naozhong.setSelected(false);
            }

            return convertView;
        }


        class Util {
            TextView tv_time;
            TextView tv_pinlv;
            ImageView iv_kaiguan_naozhong;
            TextView tv_takepillsText;


        }
    }

}
