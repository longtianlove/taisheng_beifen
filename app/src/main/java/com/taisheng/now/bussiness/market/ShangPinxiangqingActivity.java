package com.taisheng.now.bussiness.market;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseIvActivity;
import com.taisheng.now.bussiness.bean.post.AddgouwuchePostBean;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.ShangpinxaingqingPostBean;
import com.taisheng.now.bussiness.bean.post.UpdateCartNumberPostBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.bean.result.market.GoodsProductEntities;
import com.taisheng.now.bussiness.bean.result.market.JsonRootBean;
import com.taisheng.now.bussiness.bean.result.market.ValueList;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.market.dizhi.DizhiBianjiActivity;
import com.taisheng.now.bussiness.market.gouwuche.GouwucheActivity;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.GlideImageLoader;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.GuigeLabelWrapLayout;
import com.th.j.commonlibrary.utils.SpanUtil;
import com.th.j.commonlibrary.utils.TextsUtils;
import com.youth.banner.Banner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by dragon on 2019/6/28.
 */

public class ShangPinxiangqingActivity extends BaseIvActivity {


    private Banner bannerContaner;
    public TextView tv_counterprice;
    public TextView tv_jifenlabel;
    public TextView tv_retailprice;
    public TextView tv_name;
    public TextView tv_jianjie;


    public TextView tv_guigeresult;
    public View iv_gouwuche;

    public View ll_guige;
    private PopupWindow popupWindow;
    private View contentView;
    public SimpleDraweeView sdv_shangpin;
    public TextView tv_price;
    public TextView tv_yixuan;
    public TextView tv_guige;
    public GuigeLabelWrapLayout guige_label;
    public List<String> guige_list;
    public TextView tv_yanse;
    public GuigeLabelWrapLayout yanse_label;
    public List<String> yanse_list;
    public View iv_sub;
    public TextView tv_commodity_show_num;
    public View iv_add;
    public View tv_queding;


    public WebView wv_shangpinxiangqing;

    public View tv_addgouwuche;
    public View tv_goumai;

    @Override
    public void initView() {
        setContentView(R.layout.activity_shangpinxiangqing);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        initViews();
        initDatas();
    }

    @Override
    public void addData() {

    }

    @Override
    public void setChangeTitle(TextView tvLeft, TextView tvTitle, TextView tvRight, ImageView ivRight, ImageView ivTitle) {
        tvTitle.setText(getString(R.string.good_detial));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.color333333));
        Drawable drawable = getResources().getDrawable(R.drawable.icon_back);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvLeft.setCompoundDrawables(drawable, null, null, null);
    }

    private void initViews() {
        bannerContaner = findViewById(R.id.bannerContaner);

        tv_guigeresult = findViewById(R.id.tv_guigeresult);

        iv_gouwuche = findViewById(R.id.iv_gouwuche);
        iv_gouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShangPinxiangqingActivity.this, GouwucheActivity.class);
                startActivity(intent);
            }
        });
        ll_guige = findViewById(R.id.ll_guige);
        ll_guige.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//从底部显示
                popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
            }
        });

        //加载弹出框的布局
        contentView = LayoutInflater.from(ShangPinxiangqingActivity.this).inflate(
                R.layout.pop, null);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        sdv_shangpin = contentView.findViewById(R.id.sdv_shangpin);
        tv_price = contentView.findViewById(R.id.tv_price);
        tv_yixuan = contentView.findViewById(R.id.tv_yixuan);

        tv_guige = contentView.findViewById(R.id.tv_guige);
        guige_label = contentView.findViewById(R.id.guige_label);


        tv_yanse = contentView.findViewById(R.id.tv_yanse);
        yanse_label = contentView.findViewById(R.id.yanse_label);

        iv_sub = contentView.findViewById(R.id.iv_sub);
        iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int temp = Integer.parseInt(tv_commodity_show_num.getText().toString());
                if (temp == 1) {
                    return;
                }
                tv_commodity_show_num.setText((temp - 1) + "");
                number = (temp - 1) + "";

            }
        });
        tv_commodity_show_num = contentView.findViewById(R.id.tv_commodity_show_num);
        iv_add = contentView.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                UpdateCartNumberPostBean updateCartNumberPostBean = new UpdateCartNumberPostBean();
                updateCartNumberPostBean.userId = UserInstance.getInstance().getUid();
                updateCartNumberPostBean.token = UserInstance.getInstance().getToken();
                updateCartNumberPostBean.goodsId = goodsid;
                updateCartNumberPostBean.number = Integer.parseInt(number);
                updateCartNumberPostBean.operateType = 1;
                updateCartNumberPostBean.productId = productid;
                ApiUtils.getApiService_hasdialog().fastBuyCheckNumbe(updateCartNumberPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                int temp = Integer.parseInt(tv_commodity_show_num.getText().toString());
                                tv_commodity_show_num.setText((temp + 1) + "");
                                number = (temp + 1) + "";
                                break;
                            case 500:
                                ToastUtil.showAtCenter(message.message);
                                break;

                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


            }
        });
        tv_queding = contentView.findViewById(R.id.tv_queding);
        tv_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                tv_guigeresult.setText("已选" + guige_label.selectString + " " + yanse_label.selectString);

                if (goodsProductEntities != null) {

                    for (int i = 0; i < goodsProductEntities.size(); i++) {
                        GoodsProductEntities bean = goodsProductEntities.get(i);
                        if ("".equals(yanse_label.selectString)) {
                            if (bean.getSpecifications().contains(guige_label.selectString)) {
                                productid = bean.getId();
                                counterPrice = bean.price;
                                return;
                            }
                        }
                        if (bean.getSpecifications().contains(guige_label.selectString) && bean.getSpecifications().contains(yanse_label.selectString)) {
                            productid = bean.getId();
                            counterPrice = bean.price;
                            return;
                        }
                    }
                }


            }
        });

        wv_shangpinxiangqing = findViewById(R.id.wv_shangpinxiangqing);
        wv_shangpinxiangqing.getSettings().setJavaScriptEnabled(true);
        wv_shangpinxiangqing.setWebViewClient(new WebViewClient());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv_shangpinxiangqing.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }


        tv_addgouwuche = findViewById(R.id.tv_addgouwuche);
        tv_addgouwuche.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("请选择".equals(tv_guigeresult.getText().toString())) {
                    ToastUtil.showAtCenter("请选择商品规格");
                    return;
                }
                // 添加到购物车
                AddgouwuchePostBean bean = new AddgouwuchePostBean();
                bean.userId = UserInstance.getInstance().getUid();
                bean.token = UserInstance.getInstance().getToken();
                bean.goodsId = goodsid;
                bean.productId = productid;
                bean.number = number;
                ApiUtils.getApiService_hasdialog().addgouwuche(bean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                ToastUtil.showAtCenter("添加成功");
                                break;
                            default:
                                ToastUtil.showAtCenter(message.message);
                                break;

                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });

            }
        });

        tv_goumai = findViewById(R.id.tv_goumai);
        tv_goumai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ("请选择".equals(tv_guigeresult.getText().toString())) {
                    ToastUtil.showAtCenter("请选择商品规格");
                    return;
                }

                DingdanInstance.getInstance().scoreGoods = scoreGoods;
                if (scoreGoods == 1) {
                    DingdanInstance.getInstance().putongshangpindingdanList.clear();
                    xiadanshangpinBean xbean = new xiadanshangpinBean();
                    xbean.goodsId = goodsid;
                    xbean.name = name;
                    xbean.counterPrice = counterPrice + "";
//                xbean.retailPrice=retailPrice;
                    xbean.number = number;
                    xbean.picUrl = picUrl;
                    xbean.productId = productid;
                    DingdanInstance.getInstance().putongshangpindingdanList.add(xbean);

                    DingdanInstance.getInstance().zongjia = counterPrice.multiply(new BigDecimal((Integer.parseInt(number)))) + "";
                } else {
                    DingdanInstance.getInstance().jifenshangpindingdanList.clear();
                    xiadanshangpinBean xbean = new xiadanshangpinBean();
                    xbean.goodsId = goodsid;
                    xbean.name = name;
                    xbean.counterPrice = counterPrice + "";
//                xbean.retailPrice=retailPrice;
                    xbean.number = number;
                    xbean.picUrl = picUrl;
                    xbean.productId = productid;
                    DingdanInstance.getInstance().jifenshangpindingdanList.add(xbean);

                    DingdanInstance.getInstance().zongjia = counterPrice.multiply(new BigDecimal((Integer.parseInt(number)))) + "";
                }

                UpdateCartNumberPostBean updateCartNumberPostBean = new UpdateCartNumberPostBean();
                updateCartNumberPostBean.userId = UserInstance.getInstance().getUid();
                updateCartNumberPostBean.token = UserInstance.getInstance().getToken();
                updateCartNumberPostBean.goodsId = goodsid;
                updateCartNumberPostBean.number = Integer.parseInt(number);
                updateCartNumberPostBean.operateType = 1;
                updateCartNumberPostBean.productId = productid;
                ApiUtils.getApiService_hasdialog().fastBuyCheckNumbe(updateCartNumberPostBean).enqueue(new TaiShengCallback<BaseBean>() {
                    @Override
                    public void onSuccess(Response<BaseBean> response, BaseBean message) {
                        switch (message.code) {
                            case Constants.HTTP_SUCCESS:
                                next();
                                break;
                            case 500:
                                ToastUtil.showAtCenter(message.message);
                                break;

                        }
                    }

                    @Override
                    public void onFail(Call<BaseBean> call, Throwable t) {

                    }
                });


            }
        });

        tv_counterprice = findViewById(R.id.tv_counterprice);
        tv_jifenlabel = findViewById(R.id.tv_jifenlabel);

        tv_retailprice = findViewById(R.id.tv_retailprice);
        tv_name = findViewById(R.id.tv_name);
        tv_jianjie = findViewById(R.id.tv_jianjie);

    }

    public void next() {
        //获取地址信息

        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 10;

        ApiUtils.getApiService_hasdialog().addressList(bean).enqueue(new TaiShengCallback<BaseBean<DizhilistResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DizhilistResultBean>> response, BaseBean<DizhilistResultBean> message) {

                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:


                        DingdanInstance.getInstance().flag = "N";
                        if (message.result.records != null && message.result.records.size() > 0) {
                            Intent intent = new Intent(ShangPinxiangqingActivity.this, DingdanjiesuanActivity.class);
                            startActivity(intent);
                        } else {
                            DingdanInstance.getInstance().fromDizhi = "2";
                            Intent intent = new Intent(ShangPinxiangqingActivity.this, DizhiBianjiActivity.class);
                            startActivity(intent);
                        }
                        break;
                    default:
                        ToastUtil.showAtCenter(message.message);

                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DizhilistResultBean>> call, Throwable t) {

                DialogUtil.closeProgress();
            }
        });
    }


    public String name;
    public BigDecimal counterPrice;
    public BigDecimal retailPrice;
    public String picUrl;

    public String goodsid;
    public int scoreGoods;

    public List<GoodsProductEntities> goodsProductEntities;
    public String productid = 1 + "";


    public String number = 1 + "";

    void initDatas() {
        Intent intent = getIntent();
        goodsid = intent.getStringExtra("goodsid");

        ShangpinxaingqingPostBean bean = new ShangpinxaingqingPostBean();
        bean.id = goodsid;
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        ApiUtils.getApiService_hasdialog().shangpinxiangqing(bean).enqueue(new TaiShengCallback<BaseBean<JsonRootBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<JsonRootBean>> response, BaseBean<JsonRootBean> message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        //商品轮播图
                        if (message.result.goodsEntity != null) {
                            ArrayList<String> pictureUrls = new ArrayList<>();
                            if ( message.result.goodsEntity.gallery != null && message.result.goodsEntity.gallery.size() > 0){
                                for (String urlTemp : message.result.goodsEntity.gallery) {
                                    pictureUrls.add(urlTemp);
                                }
                            }
                            bannerContaner.setImageLoader(new GlideImageLoader());
                            bannerContaner.setImages(pictureUrls);
                            bannerContaner.start();
                            scoreGoods = message.result.goodsEntity.scoreGoods;
                            if (scoreGoods == 1) {
                                tv_jifenlabel.setVisibility(View.GONE);
                                tv_retailprice.setVisibility(View.VISIBLE);
                                tv_retailprice.setVisibility(View.VISIBLE);
                                tv_retailprice.setText(TextsUtils.span(getString(R.string.mony_code) +  TextsUtils.isEmptys(message.result.goodsEntity.counterPrice+"","0.00")));
                                SpanUtil.create()
                                        .addSection(getString(R.string.mony_code) + TextsUtils.isEmptys(message.result.goodsEntity.retailPrice+"","0.00"))
                                        .setAbsSize(getString(R.string.mony_code) + message.result.goodsEntity.retailPrice + "", 40)
                                        .setRelSize(getString(R.string.mony_code), 0.6f)
                                        .showIn(tv_counterprice);
                            } else {
                                if (TextsUtils.isEmpty(message.result.goodsEntity.retailPrice+"")){
                                    tv_counterprice.setText("0");
                                }else {
                                    tv_counterprice.setText(message.result.goodsEntity.retailPrice.multiply(new BigDecimal(100)) + "");
                                }
                                tv_jifenlabel.setVisibility(View.VISIBLE);
                            }
                            counterPrice = message.result.goodsEntity.retailPrice;
                            retailPrice = message.result.goodsEntity.retailPrice;
                            name = message.result.goodsEntity.name;
                            tv_name.setText(message.result.goodsEntity.name);
                            tv_jianjie.setText(message.result.goodsEntity.brief);
//                            wv_shangpinxiangqing.loadData(Html.fromHtml(message.result.goodsEntity.detail).toString(), "text/html", "UTF-8");
                            String sHead = "<html><head><meta name=\"viewport\" content=\"width=device-width, " +
                                    "initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" />" +
                                    "<style>img{max-width:100% !important;height:auto !important;}</style>"
                                    + "<style>body{max-width:100% !important;}</style>" + "</head><body>";
                            wv_shangpinxiangqing.loadDataWithBaseURL(null, sHead + message.result.goodsEntity.detail + "</body></html>", "text/html", "utf-8", null);


                            Uri uri = Uri.parse(message.result.goodsEntity.picUrl);
                            picUrl = message.result.goodsEntity.picUrl;
                            sdv_shangpin.setImageURI(uri);
                            if (scoreGoods == 1) {
                                tv_price.setText(getString(R.string.mony_code) + message.result.goodsEntity.counterPrice + "");
                            } else {
                                tv_price.setText("" + message.result.goodsEntity.counterPrice.multiply(new BigDecimal(100)) + "");
                            }

                        }
                        //todo 规格个数
                        if (message.result.goodsSpecificationEntities != null && message.result.goodsSpecificationEntities.size() > 0) {
                            tv_guige.setText(message.result.goodsSpecificationEntities.get(0).getName());
                            guige_list = new ArrayList<>();
                            for (int i = 0; i < message.result.goodsSpecificationEntities.size(); i++) {
                                ValueList tempVauleList = message.result.goodsSpecificationEntities.get(i).getValueList().get(0);
                                guige_list.add(tempVauleList.getValue());
                            }


                            goodsProductEntities = message.result.goodsProductEntities;
                            guige_label.setData(guige_list, ShangPinxiangqingActivity.this, 14, 15, 4, 14, 4, 24, 12, 24, 12);
                            guige_label.selectString = guige_list.get(0);
                            tv_yixuan.setText("已选" + guige_list.get(0));

                            guige_label.setMarkClickListener(new GuigeLabelWrapLayout.MarkClickListener() {
                                @Override
                                public void clickMark(int position) {
                                    guige_label.setData(guige_list, ShangPinxiangqingActivity.this, 14, 15, 4, 14, 4, 24, 12, 24, 12);
                                    tv_yixuan.setText("已选" + guige_label.selectString + " " + yanse_label.selectString);

                                }
                            });
                            tv_yanse.setVisibility(View.GONE);
                            yanse_label.setVisibility(View.GONE);
                            if (message.result.goodsSpecificationEntities.size() == 2) {
                                tv_yanse.setVisibility(View.VISIBLE);
                                yanse_label.setVisibility(View.VISIBLE);
                                tv_yanse.setText(message.result.goodsSpecificationEntities.get(1).getName());
                                yanse_list = new ArrayList<>();
                                for (int i = 0; i < message.result.goodsSpecificationEntities.get(1).getValueList().size(); i++) {
                                    ValueList tempVauleList = message.result.goodsSpecificationEntities.get(1).getValueList().get(i);
                                    yanse_list.add(tempVauleList.getValue());
                                }
                                yanse_label.setData(yanse_list, ShangPinxiangqingActivity.this, 14, 15, 4, 14, 4, 24, 12, 24, 12);

                                yanse_label.setMarkClickListener(new GuigeLabelWrapLayout.MarkClickListener() {
                                    @Override
                                    public void clickMark(int position) {
                                        yanse_label.setData(yanse_list, ShangPinxiangqingActivity.this, 14, 15, 4, 14, 4, 24, 12, 24, 12);
                                        tv_yixuan.setText("已选" + guige_label.selectString + " " + yanse_label.selectString);
                                        //更新价格kkk
                                    }
                                });
                                yanse_label.selectString = yanse_list.get(0);
                                tv_yixuan.setText("已选" + guige_label.selectString + " " + yanse_list.get(0));


                            }

                            //选择默认值
                            tv_guigeresult.setText("已选" + guige_label.selectString + " " + yanse_label.selectString);
                            if (goodsProductEntities != null) {

                                for (int i = 0; i < goodsProductEntities.size(); i++) {
                                    GoodsProductEntities bean = goodsProductEntities.get(i);
                                    if ("".equals(yanse_label.selectString)) {
                                        if (bean.getSpecifications().contains(guige_label.selectString)) {
                                            productid = bean.getId();
                                            counterPrice = bean.price;
//                                            tv_price.setText(counterPrice + "");
                                            if (scoreGoods == 1) {
                                                tv_price.setText(getString(R.string.mony_code) + counterPrice + "");

                                            } else {
                                                tv_price.setText("" + counterPrice.multiply(new BigDecimal(100)) + "");

                                            }
                                            return;
                                        }
                                    }
                                    if (bean.getSpecifications().contains(guige_label.selectString) && bean.getSpecifications().contains(yanse_label.selectString)) {
                                        productid = bean.getId();
                                        counterPrice = bean.price;
//                                        tv_price.setText(counterPrice + "");
                                        if (scoreGoods == 1) {
                                            tv_price.setText(getString(R.string.mony_code) + counterPrice + "");

                                        } else {
                                            tv_price.setText("" + counterPrice.multiply(new BigDecimal(100)) + "");

                                        }
                                        return;
                                    }
                                }
                            }

                            //解决选择的价格


                        }


                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<JsonRootBean>> call, Throwable t) {
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wv_shangpinxiangqing != null) {
            wv_shangpinxiangqing.removeAllViews();
            wv_shangpinxiangqing.destroy();
        }
    }
}
