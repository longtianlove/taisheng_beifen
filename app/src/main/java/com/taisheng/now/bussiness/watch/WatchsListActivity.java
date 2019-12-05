package com.taisheng.now.bussiness.watch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.ZXing.ZXingActivity;
import com.taisheng.now.base.BaseActivity;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.BasePostBean;
import com.taisheng.now.bussiness.bean.result.MyPingjiaBean;
import com.taisheng.now.bussiness.doctor.DoctorDetailActivity;
import com.taisheng.now.bussiness.me.MyPingjiaActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.bussiness.watch.bean.result.WatchListBean;
import com.taisheng.now.bussiness.watch.bean.result.WatchListResultBean;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.view.DoctorLabelWrapLayout;
import com.taisheng.now.view.ScoreStar;
import com.taisheng.now.view.StarGrade;
import com.taisheng.now.view.TaishengListView;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class WatchsListActivity extends BaseActivity {
    View iv_back;

    com.taisheng.now.view.WithScrolleViewListView lv_watchslist;
    MyPingjiaAdapter madapter;
    View tv_adddevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchslist);
        initView();
    }

    void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_watchslist = findViewById(R.id.lv_watchslist);
        madapter = new MyPingjiaAdapter(this);
        lv_watchslist.setAdapter(madapter);
//        lv_watchslist.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
//            @Override
//            public void onUpLoad() {
//                getMyPingjias();
//            }
//        });
        tv_adddevice = findViewById(R.id.tv_adddevice);
        tv_adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingActivity.skipToAsResult(WatchsListActivity.this, REQUEST_SWEEP_CODE);

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;


    void initData() {
        PAGE_NO = 1;
        PAGE_SIZE = 10;
        getMyPingjias();
    }


    void getMyPingjias() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = PAGE_SIZE;
        ApiUtils.getApiService().queryDeviceBinding(bean).enqueue(new TaiShengCallback<BaseBean<WatchListResultBean>>() {
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
            //解析成功
            String Imei = bundle.getString(CodeUtils.RESULT_STRING);
//            inputImei.setText(Imei);
//            if (isZXresultCorrect(Imei)) {
//                DeviceInfoInstance.getInstance().bindDevice(InitBindDeviceActivity.this,Imei);
//            } else {
//                showToast("IMEI码错误，请正确扫码！");
//            }


            WatchInstance.getInstance().preDeviceNumber = Imei;
            Intent intent = new Intent(WatchsListActivity.this, BindMessageActivity.class);
            startActivity(intent);
            finish();

        }
    }

    /**
     * 检查IMEI是否符合规范
     *
     * @param result
     * @return
     */
    private boolean isZXresultCorrect(String result) {
        String regex = "([a-zA-Z0-9]{15})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(result);
        return m.matches();
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
            MyPingjiaAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new MyPingjiaAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_watchslist, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.sdv_header = (SimpleDraweeView) convertView.findViewById(R.id.sdv_header);
                util.tv_nickname = convertView.findViewById(R.id.tv_nickname);
                util.tv_zhanghao = convertView.findViewById(R.id.tv_zhanghao);
                convertView.setTag(util);
            } else {
                util = (MyPingjiaAdapter.Util) convertView.getTag();
            }
            WatchListBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchInstance.getInstance().deviceId = bean.clientId;
                    WatchInstance.getInstance().deviceNickName = bean.nickName;
                    WatchInstance.getInstance().relationShip = bean.terminalRelationship;
                    //todo 数据给全
//                    WatchInstance.getInstance().realName = bean.realName;
//                    WatchInstance.getInstance().idcard = bean.idcard;
//                    WatchInstance.getInstance().phoneNumber = bean.phoneNumber;
                    Intent intent = new Intent(WatchsListActivity.this, WatchMainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
//            if (bean.avatar != null) {
//                Uri uri = Uri.parse(bean.avatar);
//                util.sdv_header.setImageURI(uri);
//            }
            util.tv_nickname.setText(bean.nickName);
            util.tv_zhanghao.setText("我是TA的" + bean.terminalRelationship);

            return convertView;
        }


        class Util {
            View ll_all;

            SimpleDraweeView sdv_header;
            TextView tv_nickname;
            TextView tv_zhanghao;

        }
    }

}
