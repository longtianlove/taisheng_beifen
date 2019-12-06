package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.post.ChiyaolistPostBean;
import com.taisheng.now.bussiness.watch.bean.post.SetChiyaoPostBean;
import com.taisheng.now.bussiness.watch.bean.post.SetNaozhongPostBean;
import com.taisheng.now.bussiness.watch.bean.result.ChiyaoBeann;
import com.taisheng.now.bussiness.watch.bean.result.ChiyaoLIstResultBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongLIstBean;
import com.taisheng.now.bussiness.watch.bean.result.NaozhongListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.WithScrolleViewListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchChiYaoListActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    ImageView iv_back;

    View iv_addnaozhong;

    WithScrolleViewListView lv_chiyaos;
    ArticleAdapter madapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchme_chiyao);
        initView();
//        EventBus.getDefault().register(this);

    }

    void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_addnaozhong = findViewById(R.id.iv_addnaozhong);
        iv_addnaozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                startActivity(intent);
            }
        });

        lv_chiyaos = (WithScrolleViewListView) findViewById(R.id.lv_chiyaos);
        madapter = new ArticleAdapter(this);
        lv_chiyaos.setAdapter(madapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    int size = 0;

    void initData() {
        ChiyaolistPostBean bean = new ChiyaolistPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.clientId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().getWatchTakepillsList(bean).enqueue(new TaiShengCallback<BaseBean<ChiyaoLIstResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<ChiyaoLIstResultBean>> response, BaseBean<ChiyaoLIstResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.records.size() >= 0) {
                            //有消息
//                            PAGE_NO++;
                            madapter.mData.clear();
                            madapter.mData.addAll(message.result.records);

                            size = message.result.records.size();
                            if (size >= 3) {
                                iv_addnaozhong.setVisibility(View.GONE);
                            } else {
                                iv_addnaozhong.setVisibility(View.VISIBLE);

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
                            madapter.notifyDataSetChanged();
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

        public Context mcontext;

        ArrayList<ChiyaoBeann> mData = new ArrayList<ChiyaoBeann>();

        public ArticleAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
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
            ArticleAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new ArticleAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_chiyao, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                util.tv_pinlv = convertView.findViewById(R.id.tv_pinlv);
                util.iv_kaiguan_naozhong = convertView.findViewById(R.id.iv_kaiguan_naozhong);
                util.tv_takepillsText = convertView.findViewById(R.id.tv_takepillsText);

                convertView.setTag(util);
            } else {
                util = (ArticleAdapter.Util) convertView.getTag();
            }
            ChiyaoBeann bean = mData.get(position);
            Util finalUtil = util;
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WatchChiYaoListActivity.this, WatchChiyaoXinzengActivity.class);

                    intent.putExtra("takepillsNum", bean.takepillsNum);
                    intent.putExtra("startTime", bean.startTime);

                    startActivity(intent);
                }
            });
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

                util.tv_takepillsText.setText(bean.takepillsText);

            }
            Util finalUtil1 = util;
            util.iv_kaiguan_naozhong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetChiyaoPostBean setNaozhongPostBean = new SetChiyaoPostBean();
                    setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                    setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                    setNaozhongPostBean.clientId = WatchInstance.getInstance().deviceId;
                    setNaozhongPostBean.takepillsNum = bean.takepillsNum;
                    setNaozhongPostBean.frequency = bean.frequency;
                    setNaozhongPostBean.isOpen = finalUtil1.iv_kaiguan_naozhong.isSelected()?"0":"1";
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
                                    }else{
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
            View ll_all;
            TextView tv_time;
            TextView tv_pinlv;
            ImageView iv_kaiguan_naozhong;
            TextView tv_takepillsText;


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
