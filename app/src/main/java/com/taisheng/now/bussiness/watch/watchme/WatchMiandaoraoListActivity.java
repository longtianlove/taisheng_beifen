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
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.result.Miandaraobean;
import com.taisheng.now.bussiness.watch.bean.result.NewMiandaraoListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.WithScrolleViewListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/29.
 */

public class WatchMiandaoraoListActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    View iv_addnaozhong;

    WithScrolleViewListView lv_articles;
    ArticleAdapter madapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_miandaraolist);
        initViews();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
tvTitle.setText("免打扰时间段");
    }

    void initViews() {

        iv_addnaozhong = findViewById(R.id.iv_addnaozhong);
        iv_addnaozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMiandaoraoListActivity.this, WatchMianDaraoSettingliActivity.class);
                startActivity(intent);
            }
        });

        lv_articles = (WithScrolleViewListView) findViewById(R.id.lv_naozhongs);
        madapter = new ArticleAdapter(this);
        lv_articles.setAdapter(madapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        initDatas();
    }

    void initDatas() {
        BaseWatchBean bean = new BaseWatchBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        ApiUtils.getApiService().notDisturbSwitchSetting(bean).enqueue(new TaiShengCallback<BaseBean<NewMiandaraoListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NewMiandaraoListResultBean>> response, BaseBean<NewMiandaraoListResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.records != null) {
//                            String[] miandaraolist = message.result.watchSilencetime.split(",");
//                            ArrayList<String> times = new ArrayList();
                            WatchInstance.getInstance().miandaraoList.clear();
                            for (int i = 0; i <message.result.records.size(); i++) {
                                Miandaraobean bean=message.result.records.get(i);
                                String temp=bean.startTime+"-"+bean.endTime;
                                if (!"00:00-00:00".equals(temp)) {
                                    WatchInstance.getInstance().miandaraoList.add(temp);
                                }
                            }
                            if (WatchInstance.getInstance().miandaraoList.size() <= 4) {
                                iv_addnaozhong.setVisibility(View.VISIBLE);
                            } else {
                                iv_addnaozhong.setVisibility(View.GONE);
                            }
                            madapter.mData.clear();
                            madapter.mData.addAll(WatchInstance.getInstance().miandaraoList);
                            madapter.notifyDataSetChanged();
                        }

//                        if (message.result != null && message.result.watchRemindList.size() > 0) {
//                            //有消息
////                            PAGE_NO++;
//                            madapter.mData.clear();
//                            madapter.mData.addAll(message.result.watchRemindList);
//
//                            WatchInstance.getInstance().mDataNaoZhong.clear();
//                            WatchInstance.getInstance().mDataNaoZhong.addAll(message.result.watchRemindList);
////                            if(message.result.size()<10){
////                                lv_articles.setHasLoadMore(false);
////                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
////                                lv_articles.setLoadAllFooterVisible(false);
////                            }else{
////                                lv_articles.setHasLoadMore(true);
////                            }
//                            madapter.notifyDataSetChanged();
//                        } else {
////                            //没有消息
////                            lv_articles.setHasLoadMore(false);
////                            lv_articles.setLoadAllViewText("暂时只有这么多文章");
////                            lv_articles.setLoadAllFooterVisible(false);
//                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<NewMiandaraoListResultBean>> call, Throwable t) {

            }
        });
    }

    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        ArrayList<String> mData = new ArrayList<String>();

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
                convertView = inflater.inflate(R.layout.item_miandaraolist, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_time = convertView.findViewById(R.id.tv_time);


                convertView.setTag(util);
            } else {
                util = (ArticleAdapter.Util) convertView.getTag();
            }
            String bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WatchMiandaoraoListActivity.this, WatchMianDaraoSettingliActivity.class);
                    intent.putExtra("time", bean);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
            util.tv_time.setText(bean);


            return convertView;
        }


        class Util {
            View ll_all;
            TextView tv_time;


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
