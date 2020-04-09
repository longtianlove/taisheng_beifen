package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.BaseWatchBean;
import com.taisheng.now.bussiness.watch.bean.post.SetNaozhongPostBean;
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

public class WatchMeNaozhongListActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    boolean bianji = false;


    View iv_addnaozhong;

    com.taisheng.now.view.WithScrolleViewListView lv_articles;
    ArticleAdapter madapter;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_naozhong);
        initViews();
    }

    @Override
    public void initData() {

    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText("闹钟设置");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.edit));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bianji = !bianji;
                if (bianji) {
                    tvRight.setText("完成");
                } else {
                    tvRight.setText("编辑");

                }
                madapter.notifyDataSetChanged();
            }
        });
    }

    void initViews() {

        iv_addnaozhong = findViewById(R.id.iv_addnaozhong);
        iv_addnaozhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WatchMeNaozhongListActivity.this, WatchNaoZhongXinzengActivity.class);
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
        ApiUtils.getApiService_hasdialog().getWatchREMINDList(bean).enqueue(new TaiShengCallback<BaseBean<NaozhongListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<NaozhongListResultBean>> response, BaseBean<NaozhongListResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.watchRemindList.size() >= 0) {


                            if (message.result.watchRemindList.size() >= 10) {
                                iv_addnaozhong.setVisibility(View.GONE);
                            } else {
                                iv_addnaozhong.setVisibility(View.VISIBLE);
                            }
                            //有消息
//                            PAGE_NO++;
                            madapter.mData.clear();
                            madapter.mData.addAll(message.result.watchRemindList);

                            WatchInstance.getInstance().mDataNaoZhong.clear();
                            WatchInstance.getInstance().mDataNaoZhong.addAll(message.result.watchRemindList);
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
            public void onFail(Call<BaseBean<NaozhongListResultBean>> call, Throwable t) {

            }
        });
    }

    class ArticleAdapter extends BaseAdapter {

        public Context mcontext;

        ArrayList<NaozhongLIstBean> mData = new ArrayList<NaozhongLIstBean>();

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
                convertView = inflater.inflate(R.layout.item_naozhong, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.iv_delete = convertView.findViewById(R.id.iv_delete);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                util.tv_pinlv = convertView.findViewById(R.id.tv_pinlv);
                util.iv_kaiguan_naozhong = convertView.findViewById(R.id.iv_kaiguan_naozhong);

                convertView.setTag(util);
            } else {
                util = (ArticleAdapter.Util) convertView.getTag();
            }
            NaozhongLIstBean bean = mData.get(position);
            Util finalUtil = util;
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("1".equals(bean.isOpen)) {
                        bean.isOpen = "0";
                    } else {
                        bean.isOpen = "1";
                    }
                    SetNaozhongPostBean setNaozhongPostBean = new SetNaozhongPostBean();
                    setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                    setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                    setNaozhongPostBean.deviceId = WatchInstance.getInstance().deviceId;
                    setNaozhongPostBean.watchRemindList = mData;
                    ApiUtils.getApiService_hasdialog().setWatchREMIND(setNaozhongPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    if (finalUtil.iv_kaiguan_naozhong.isSelected()) {
                                        finalUtil.iv_kaiguan_naozhong.setSelected(false);
                                    } else {
                                        finalUtil.iv_kaiguan_naozhong.setSelected(true);
                                    }

                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });


                }
            });
            if (bianji) {
                util.iv_delete.setVisibility(View.VISIBLE);
            } else {
                util.iv_delete.setVisibility(View.GONE);
            }
            util.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetNaozhongPostBean setNaozhongPostBean = new SetNaozhongPostBean();
                    setNaozhongPostBean.userId = UserInstance.getInstance().getUid();
                    setNaozhongPostBean.token = UserInstance.getInstance().getToken();
                    setNaozhongPostBean.deviceId = WatchInstance.getInstance().deviceId;
                    WatchInstance.getInstance().mDataNaoZhong.remove(position);
                    setNaozhongPostBean.watchRemindList = WatchInstance.getInstance().mDataNaoZhong;
                    ApiUtils.getApiService_hasdialog().setWatchREMIND(setNaozhongPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    ToastUtil.showAtCenter("删除成功");
                                    initData();
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
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

            }


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
            View iv_delete;
            TextView tv_pinlv;
            ImageView iv_kaiguan_naozhong;


        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }
}
