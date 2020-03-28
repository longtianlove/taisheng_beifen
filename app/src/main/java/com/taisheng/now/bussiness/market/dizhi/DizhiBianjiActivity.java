package com.taisheng.now.bussiness.market.dizhi;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.AddDizhiPostBean;
import com.taisheng.now.bussiness.bean.post.DeleteDizhiPostBean;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.market.gouwuche.StringUtil;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.ToastUtil;
import com.th.j.commonlibrary.wight.AddressPickerView;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */


public class DizhiBianjiActivity extends BaseIvActivity {
    EditText et_xingming;
    EditText et_phone;
    View ll_dizhi;
    TextView et_dizhi;
    EditText et_xiangxidizhi;
    View ll_dizhidefault;
    ImageView iv_dizhidefault;
    View btn_save;
    View btn_delete;

    public String dizhiid;

    @Override
    public void initView() {
        setContentView(R.layout.activity_dizhibianji);
        initViews();
    }

    @Override
    public void initData() {
        initDatas();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
               tvTitle.setText(getString(R.string.edit_address));
    }

    void initViews() {

        et_xingming = findViewById(R.id.et_xingming);
        et_xingming.addTextChangedListener(watcher);
        et_phone = findViewById(R.id.et_phone);
        et_phone.addTextChangedListener(watcher);
        ll_dizhi = findViewById(R.id.ll_dizhi);
        ll_dizhi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showAddressPickerPop();
            }
        });


        et_dizhi = findViewById(R.id.et_dizhi);
        et_dizhi.addTextChangedListener(watcher);


        et_xiangxidizhi = findViewById(R.id.et_xiangxidizhi);
        et_xiangxidizhi.addTextChangedListener(watcher);


        iv_dizhidefault = findViewById(R.id.iv_dizhidefault);
        iv_dizhidefault.setSelected(false);
        ll_dizhidefault = findViewById(R.id.ll_dizhidefault);
        ll_dizhidefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iv_dizhidefault.isSelected()) {
                    iv_dizhidefault.setSelected(false);
                } else {
                    iv_dizhidefault.setSelected(true);
                }
            }
        });

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(dizhiid)) {


                    AddDizhiPostBean bean = new AddDizhiPostBean();
                    bean.userId = UserInstance.getInstance().getUid();
                    bean.token = UserInstance.getInstance().getToken();
                    bean.addressDetail = et_xiangxidizhi.getText().toString();
                    bean.province = province;
                    bean.city = city;
                    bean.county = district;
                    bean.defaultAddress = (iv_dizhidefault.isSelected() ? "1" : "0");
                    bean.phone = et_phone.getText().toString();
                    bean.name = et_xingming.getText().toString();


                    ApiUtils.getApiService().addressAdd(bean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    ToastUtil.showAtCenter("添加成功");
                                    DingdanInstance.getInstance().addressId = message.message;
                                    DingdanInstance.getInstance().name = bean.name;
                                    DingdanInstance.getInstance().phone = bean.phone;
                                    DingdanInstance.getInstance().address = bean.province + bean.city + bean.county + bean.addressDetail;
                                    if ("1".equals(DingdanInstance.getInstance().fromDizhi)) {
                                        finish();
                                    } else {
                                        Intent intent = new Intent(DizhiBianjiActivity.this, DingdanjiesuanActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });

                } else {


                    AddDizhiPostBean bean = new AddDizhiPostBean();
                    bean.id = dizhiid;
                    bean.userId = UserInstance.getInstance().getUid();
                    bean.token = UserInstance.getInstance().getToken();
                    bean.addressDetail = et_xiangxidizhi.getText().toString();
                    bean.province = province;
                    bean.city = city;
                    bean.county = district;
                    bean.defaultAddress = (iv_dizhidefault.isSelected() ? "1" : "0");
                    bean.phone = et_phone.getText().toString();
                    bean.name = et_xingming.getText().toString();


                    ApiUtils.getApiService().updateAddressById(bean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    ToastUtil.showAtCenter("更新成功");
                                    DingdanInstance.getInstance().addressId = dizhiid;
                                    finish();
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                }
            }
        });


        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DeleteDizhiPostBean bean = new DeleteDizhiPostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.id = dizhiid;
                ApiUtils.getApiService().addressDelete(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("删除成功");
                                finish();
                                break;
                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });
            }
        });


    }

    void initDatas() {
        dizhiid = getIntent().getStringExtra("dizhiid");
        if (StringUtil.isEmpty(dizhiid)) {
            btn_delete.setVisibility(View.GONE);
        } else {
            btn_delete.setVisibility(View.VISIBLE);

        }
        String name = getIntent().getStringExtra("name");
        String phonne = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        String province1 = getIntent().getStringExtra("province");
        province = province1;
        String city1 = getIntent().getStringExtra("city");
        city = city1;
        String county1 = getIntent().getStringExtra("county");
        district = county1;
        String xiangxidizhi = getIntent().getStringExtra("xiangxidizhi");
        String isDeafult = getIntent().getStringExtra("defaultAddress");

        et_xingming.setText(name);
        et_xingming.setSelection(et_xingming.getText().length());

        et_phone.setText(phonne);
        if (!TextUtils.isEmpty(province1)) {
            et_dizhi.setText(province1 + " " + city1 + " " + county1);
        }
        et_xiangxidizhi.setText(xiangxidizhi);
        if ("1".equals(isDeafult)) {
            iv_dizhidefault.setSelected(true);
        } else {
            iv_dizhidefault.setSelected(false);
        }
    }

    boolean check() {
        if (TextUtils.isEmpty(et_xingming.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_phone.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_dizhi.getText())) {
            return false;
        }
        if (TextUtils.isEmpty(et_xiangxidizhi.getText())) {
            return false;
        }
        return true;
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (check()) {
                btn_save.setEnabled(true);
            } else {
                btn_save.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    public String province;
    public String city;
    public String district;


    PopupWindow popupWindow;

    /**
     * 显示地址选择的pop
     */
    private void showAddressPickerPop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        popupWindow = new PopupWindow(this);
        View rootView = View.inflate(this,R.layout.pop_address_picker, null);
        AddressPickerView addressView = rootView.findViewById(R.id.apvAddress);
        LinearLayout llPop = rootView.findViewById(R.id.ll_pop);
        addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
            @Override
            public void onSureClick(String address, String provinceCode, String cityCode, String districtCode) {
                Log.e("lonngtianlove", provinceCode + ":" + cityCode + ":" + districtCode);
                et_dizhi.setText(address);
                province = provinceCode;
                city = cityCode;
                district = districtCode;
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setContentView(rootView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(llPop, Gravity.BOTTOM , 0, 0);
//        popupWindow.showAtLocation(view, Gravity.TOP, 0, y);

    }

}
