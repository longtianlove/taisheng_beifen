package com.taisheng.now.bussiness.watch.watchme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.WatchInstance;
import com.taisheng.now.bussiness.watch.bean.post.GetWatchPhoneBookPostBean;
import com.taisheng.now.bussiness.watch.bean.result.GetWatchPhoneBookResultBean;
import com.taisheng.now.bussiness.watch.bean.result.TongxunluliistBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.WithScrolleViewListView;
import com.th.j.commonlibrary.global.Global;
import com.th.j.commonlibrary.wight.CircleImageView;

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

public class WatchMeTongxunluActivity extends BaseIvActivity implements ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_naozhongs)
    WithScrolleViewListView lvNaozhongs;
    @BindView(R.id.tv_addnaozhong)
    TextView tvAddnaozhong;
    private ArticleAdapter madapter;
    private boolean bianji = false;
    public int nowphxName = 0;
    public static List<TongxunluliistBean> data;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchme_tongxunlu);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        data = new ArrayList<>();
        madapter = new ArticleAdapter(this);
        lvNaozhongs.setAdapter(madapter);
        lvNaozhongs.setOnItemClickListener(this);
    }

    @Override
    public void addData() {
        getData();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.watch_msg09));
        tvRight.setText(getString(R.string.edit));
        tvRight.setVisibility(View.GONE);
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

    @OnClick(R.id.tv_addnaozhong)
    public void onViewClicked() {
        Intent intent = new Intent(WatchMeTongxunluActivity.this, WatchMeTongxunluxinzengActivity.class);
        intent.putExtra("position", -1);

        intent.putExtra(Global.INTENT_TYPE, Global.MAIL_ADD);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (data != null && data.size() > 0) {
            TongxunluliistBean bean = data.get(position);
            Intent intent = new Intent(WatchMeTongxunluActivity.this, WatchMeTongxunluxinzengActivity.class);
            intent.putExtra("position", position);

            intent.putExtra("avatarUrl", bean.avatarUrl);
            intent.putExtra("phbxName", bean.name);
            intent.putExtra("phbxTelephone", bean.mobilePhone);
            intent.putExtra(Global.INTENT_TYPE, Global.MAIL_UPDATA);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
//        DianhuabenPostbean bean = new DianhuabenPostbean();
//        bean.userId = UserInstance.getInstance().getUid();
//        bean.token = UserInstance.getInstance().getToken();
//        bean.deviceId = WatchInstance.getInstance().deviceId;
//        bean.pageNo = 1;
//        bean.pageSize = 10;
//        ApiUtils.getApiService().getWatchPhbxList(bean).enqueue(new TaiShengCallback<BaseBean<TongxunluResultBean>>() {
//            @Override
//            public void onSuccess(Response<BaseBean<TongxunluResultBean>> response, BaseBean<TongxunluResultBean> message) {
//                switch (message.code) {
//                    case Constants.HTTP_SUCCESS:
//                        if (message.result != null && message.result.records.size() >= 0) {
//
//                            if (message.result.records.size() >= 10) {
//                                tvAddnaozhong.setVisibility(View.GONE);
//                            } else {
//                                nowphxName = (message.result.records.size());
//                                tvAddnaozhong.setVisibility(View.VISIBLE);
//                            }
//                            //有消息
////                            PAGE_NO++;
//                            data=message.result.records;
//                            madapter.setmData(data);
//
////                            if(message.result.size()<10){
////                                lv_articles.setHasLoadMore(false);
////                                lv_articles.setLoadAllViewText("暂时只有这么多文章");
////                                lv_articles.setLoadAllFooterVisible(false);
////                            }else{
////                                lv_articles.setHasLoadMore(true);
////                            }
//                        } else {
////                            //没有消息
////                            lv_articles.setHasLoadMore(false);
////                            lv_articles.setLoadAllViewText("暂时只有这么多文章");
////                            lv_articles.setLoadAllFooterVisible(false);
//                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onFail(Call<BaseBean<TongxunluResultBean>> call, Throwable t) {
//
//            }
//        });


        GetWatchPhoneBookPostBean bean = new GetWatchPhoneBookPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.deviceId = WatchInstance.getInstance().deviceId;
        bean.pageNo = 1;
        bean.pageSize = 10;
        bean.type = "1";
        ApiUtils.getApiService_hasdialog().getWatchPhoneBook(bean).enqueue(new TaiShengCallback<BaseBean<GetWatchPhoneBookResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GetWatchPhoneBookResultBean>> response, BaseBean<GetWatchPhoneBookResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result != null && message.result.records.size() >= 0) {

                            if (message.result.records.size() >= 10) {
                                tvAddnaozhong.setVisibility(View.GONE);
                            } else {
                                nowphxName = (message.result.records.size());
                                tvAddnaozhong.setVisibility(View.VISIBLE);
                            }
                            //有消息
//                            PAGE_NO++;
                            if (message.result.records != null) {
                                data = message.result.records;
                                madapter.setmData(data);
                            }


//                            }
                        } else {

                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GetWatchPhoneBookResultBean>> call, Throwable t) {

            }
        });
    }


    class ArticleAdapter extends BaseAdapter {

        private Context mcontext;

        private List<TongxunluliistBean> mData;

        public void setmData(List<TongxunluliistBean> mData) {
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
            Util util = null;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_tongxunlu, null);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_phone = convertView.findViewById(R.id.tv_phone);
                util.sdv_header = convertView.findViewById(R.id.sdv_header);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            TongxunluliistBean bean = mData.get(position);
            //todo 图片路径前添加host
            Glide.with(mcontext)
                    .load(Constants.Url.File_Host_head + bean.avatarUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ad_sculpture)
                            .error(R.drawable.ad_sculpture)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(util.sdv_header);
            util.tv_name.setText(bean.name);
            util.tv_phone.setText(bean.mobilePhone);

            return convertView;
        }


        class Util {
            TextView tv_name;
            CircleImageView sdv_header;
            TextView tv_phone;


        }
    }


}
