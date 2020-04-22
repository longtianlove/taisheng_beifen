package com.taisheng.now.bussiness.watch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.ZXing.ZXingActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.watch.bean.result.WatchListBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.SPUtil;
import com.taisheng.now.view.WithScrolleViewListView;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.th.j.commonlibrary.wight.CircleImageView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class WatchsListActivity extends BaseIvActivity {


    @BindView(R.id.lv_watchslist)
    WithScrolleViewListView lvWatchslist;
    @BindView(R.id.tv_adddevice)
    LinearLayout tvAdddevice;
    private MyPingjiaAdapter madapter;
    private int PAGE_NO = 1;
    private int PAGE_SIZE = 10;

    @Override
    public void initView() {
        setContentView(R.layout.activity_watchslist);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        madapter = new MyPingjiaAdapter(this);
        lvWatchslist.setAdapter(madapter);
    }

    @Override
    public void addData() {
        getMyPingjias();
    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.device_binding));
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setBackgroundResource(R.drawable.btn_add);
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingActivity.skipToAsResult(WatchsListActivity.this, REQUEST_SWEEP_CODE);
            }
        });
    }

    @OnClick(R.id.tv_adddevice)
    public void onViewClicked() {
        ZXingActivity.skipToAsResult(WatchsListActivity.this, REQUEST_SWEEP_CODE);
    }

    private void getMyPingjias() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        ApiUtils.getApiService_hasdialog().queryDeviceBinding(bean).enqueue(new TaiShengCallback<BaseBean<WatchListResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<WatchListResultBean>> response, BaseBean<WatchListResultBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
//                            lv_watchslist.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
//                            if (message.result.size() < 10) {
//                                lv_watchslist.setHasLoadMore(false);
//                                lv_watchslist.setLoadAllViewText("暂时只有这么多手表");
//                                lv_watchslist.setLoadAllFooterVisible(true);
//                            } else {
//                                lv_watchslist.setHasLoadMore(true);
//                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
//                            lv_watchslist.setHasLoadMore(false);
//                            lv_watchslist.setLoadAllViewText("暂时只有这么多手表");
//                            lv_watchslist.setLoadAllFooterVisible(true);
                        }


                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<WatchListResultBean>> call, Throwable t) {

            }
        });

    }


    public static final int REQUEST_SWEEP_CODE = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_SWEEP_CODE == requestCode && RESULT_OK == resultCode) {
            parseSweepResult(data);
        }
    }


    private void parseSweepResult(Intent data) {
        if (null == data) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (null == bundle) {
            return;
        }
        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
            String Imei = bundle.getString(CodeUtils.RESULT_STRING);
            WatchInstance.getInstance().preDeviceNumber = Imei;
            Intent intent = new Intent(WatchsListActivity.this, BindMessageActivity.class);
            startActivity(intent);
            finish();

        }
    }

    class MyPingjiaAdapter extends BaseAdapter {

        public Context mcontext;

        List<WatchListBean> mData = new ArrayList<WatchListBean>();

        public MyPingjiaAdapter(Context context) {
            this.mcontext = context;
        }

        @Override
        public int getCount() {
            if (mData == null) {
                return 0;
            } else {
                return mData.size();
            }
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
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_watchslist, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_header = convertView.findViewById(R.id.sdv_header);
                util.tv_nickname = convertView.findViewById(R.id.tv_nickname);
                util.tv_zhanghao = convertView.findViewById(R.id.tv_zhanghao);
                util.tv_relationship = convertView.findViewById(R.id.tv_relationship);
                util.tv_time = convertView.findViewById(R.id.tv_time);
                util.tv_default = convertView.findViewById(R.id.tv_default);
                convertView.setTag(util);
            } else {
                util = (Util) convertView.getTag();
            }
            WatchListBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchInstance.getInstance().deviceId = bean.deviceId;
                    SPUtil.putDeviced(WatchInstance.getInstance().deviceId);
                    WatchInstance.getInstance().deviceNickName = bean.nickName;
                    WatchInstance.getInstance().relationShip = bean.deviceRelation;
                    WatchInstance.getInstance().realName = bean.holderName;
                    WatchInstance.getInstance().idcard = bean.idcard;
                    WatchInstance.getInstance().phoneNumber = bean.mobilePhone;

                    WatchInstance.getInstance().createTime = bean.createTime;
                    WatchInstance.getInstance().headUrl = bean.url;

                    Intent intent = new Intent(WatchsListActivity.this, WatchMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            Glide.with(mcontext)
                    .load(Constants.Url.File_Host + bean.url)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.article_default)
                            .error(R.drawable.article_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(util.sdv_header);
            util.tv_nickname.setText(bean.nickName);
            if (!TextsUtils.isEmpty(bean.deviceRelation)){
                util.tv_relationship.setText("(我是TA的" + bean.deviceRelation + ")");
            }
            StringBuffer stringBuffer = new StringBuffer(bean.mobilePhone);
            stringBuffer.insert(3, " ");
            stringBuffer.insert(8, " ");
            util.tv_zhanghao.setText(getString(R.string.phonenum) + ":" + stringBuffer.toString());
            util.tv_time.setText(getString(R.string.activation_time) + ":" + bean.createTime);
//            if (position == 0) {
//                util.tv_default.setVisibility(View.VISIBLE);
//            } else {
//                util.tv_default.setVisibility(View.GONE);
//            }
            return convertView;
        }


        class Util {
            View ll_all;
            CircleImageView sdv_header;
            TextView tv_nickname;
            TextView tv_zhanghao;
            TextView tv_relationship;
            TextView tv_time;
            TextView tv_default;


        }
    }

}
