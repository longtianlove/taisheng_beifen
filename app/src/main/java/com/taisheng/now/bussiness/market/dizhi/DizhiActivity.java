package com.taisheng.now.bussiness.market.dizhi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.view.TaishengListView;
import com.taisheng.now.view.chenjinshi.StatusBarUtil;
import com.taisheng.now.view.refresh.MaterialDesignPtrFrameLayout;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class DizhiActivity extends Activity {
    View iv_back;


    MaterialDesignPtrFrameLayout ptr_refresh;
    TaishengListView lv_dizhis;
    DizhiAdapter madapter;
    View tv_adddizhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dizhi);
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
        ptr_refresh = (MaterialDesignPtrFrameLayout) findViewById(R.id.ptr_refresh);

        /**
         * 下拉刷新
         */
        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                PAGE_NO = 1;
                initData();

            }
        });

        lv_dizhis = findViewById(R.id.lv_dizhis);
        madapter = new DizhiAdapter(this);
        lv_dizhis.setAdapter(madapter);
        lv_dizhis.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                initData();
            }
        });
        tv_adddizhi=findViewById(R.id.tv_adddizhi);
        tv_adddizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DingdanInstance.getInstance().fromDizhi="1";
                Intent intent = new Intent(DizhiActivity.this, DizhiBianjiActivity.class);
                startActivity(intent);
            }
        });

    }


    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void initData() {
        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 10;

        DialogUtil.showProgress(this, "");
        ApiUtils.getApiService().addressList(bean).enqueue(new TaiShengCallback<BaseBean<DizhilistResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DizhilistResultBean>> response, BaseBean<DizhilistResultBean> message) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            lv_dizhis.setLoading(false);
                            if (PAGE_NO == 1) {
                                madapter.mData.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            madapter.mData.addAll(message.result.records);
                            if (message.result.records.size() < 10) {
                                lv_dizhis.setHasLoadMore(false);
                                lv_dizhis.setLoadAllViewText("暂时只有这么多地址");
                                lv_dizhis.setLoadAllFooterVisible(true);
                            } else {
                                lv_dizhis.setHasLoadMore(true);
                            }
                            madapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            lv_dizhis.setHasLoadMore(false);
                            lv_dizhis.setLoadAllViewText("暂时只有这么多地址");
                            lv_dizhis.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DizhilistResultBean>> call, Throwable t) {
                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });
    }


    class DizhiAdapter extends BaseAdapter {

        public Context mcontext;

        List<DizhilistBean> mData = new ArrayList<DizhilistBean>();

        public DizhiAdapter(Context context) {
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
            DizhiAdapter.Util util = null;
            // 中间变量
            final int flag = position;
            if (convertView == null) {
                util = new DizhiAdapter.Util();
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                convertView = inflater.inflate(R.layout.item_dizhi, null);
                util.ll_all = convertView.findViewById(R.id.ll_all);
                util.tv_name = convertView.findViewById(R.id.tv_name);
                util.tv_phone = convertView.findViewById(R.id.tv_phone);
                util.tv_address = convertView.findViewById(R.id.tv_address);
                util.tv_ismdefault = convertView.findViewById(R.id.tv_ismdefault);
                util.tv_bianji = convertView.findViewById(R.id.tv_bianji);
                convertView.setTag(util);
            } else {
                util = (DizhiAdapter.Util) convertView.getTag();
            }
            DizhilistBean bean = mData.get(position);
            util.ll_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    DingdanInstance.getInstance().name=bean.name;
                    DingdanInstance.getInstance().phone=bean.phone;
                    DingdanInstance.getInstance().address=bean.province + bean.city + bean.county + bean.addressDetail;

                    intent.putExtra("name", bean.name);
                    intent.putExtra("phone", bean.phone);
                    intent.putExtra("address", bean.province + bean.city + bean.county + bean.addressDetail);
                    DingdanInstance.getInstance().addressId = bean.id;
                    setResult(1, intent);
                    finish();
                }
            });
            util.tv_name.setText(bean.name);
            util.tv_phone.setText(bean.phone);
            util.tv_address.setText(bean.province + bean.city + bean.county + bean.addressDetail);
            if (bean.defaultAddress == 1) {
                util.tv_ismdefault.setVisibility(View.VISIBLE);
            } else {
                util.tv_ismdefault.setVisibility(View.GONE);
            }

            util.tv_bianji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DizhiActivity.this, DizhiBianjiActivity.class);
                    intent.putExtra("name", bean.name);
                    intent.putExtra("phone", bean.phone);
                    intent.putExtra("province", bean.province);
                    intent.putExtra("city", bean.city);
                    intent.putExtra("county", bean.county);
                    intent.putExtra("xiangxidizhi", bean.addressDetail);
                    intent.putExtra("defaultAddress", bean.defaultAddress+"");
                    intent.putExtra("dizhiid",bean.id);
                    startActivity(intent);
                }
            });


            return convertView;
        }


        class Util {
            View ll_all;
            TextView tv_name;
            TextView tv_phone;
            TextView tv_address;
            View tv_ismdefault;
            View tv_bianji;


        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        initData();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onStop() {
        super.onStop();
        PAGE_NO=1;
    }
}
