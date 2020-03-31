package com.taisheng.now.bussiness.market.gouwuche;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;
import com.taisheng.now.Constants;
import com.taisheng.now.R;
import com.taisheng.now.base.BaseBean;
import com.taisheng.now.base.BaseFragment;
import com.taisheng.now.bussiness.bean.post.BaseListPostBean;
import com.taisheng.now.bussiness.bean.post.CartDetePostBean;
import com.taisheng.now.bussiness.bean.post.GouwucheListPostBean;
import com.taisheng.now.bussiness.bean.post.UpdateCartNumberPostBean;
import com.taisheng.now.bussiness.bean.result.GouwucheResultBean;
import com.taisheng.now.bussiness.bean.result.market.DizhilistResultBean;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.login.UserInstance;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.market.dizhi.DizhiBianjiActivity;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.LogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.util.Uiutils;
import com.taisheng.now.view.TaishengListView;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.utils.TextsUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class GouwucheFragment extends BaseFragment implements ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {

    public String assessmentType;
    public int scoreGoods;
    @BindView(R.id.list_shopping_cart)
    TaishengListView listShoppingCart;
    @BindView(R.id.ck_all)
    CheckBox ckAll;
    @BindView(R.id.tv_show_price)
    TextView tvShowPrice;
    @BindView(R.id.tv_settlement)
    TextView tvSettlement;
    private ShoppingCartAdapter shoppingCartAdapter;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private BigDecimal totalPrice = new BigDecimal(0.00);// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.layout_shopping_cart_activity, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        if (scoreGoods == 1) {
            DingdanInstance.getInstance().putongshangpindingdanList.clear();
        } else {
            DingdanInstance.getInstance().jifenshangpindingdanList.clear();
        }
        initData();
    }

    //初始化数据
    protected void initData() {
        shoppingCartAdapter = new ShoppingCartAdapter(getActivity());
        shoppingCartAdapter.scoreGoods = scoreGoods;
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        listShoppingCart.setAdapter(shoppingCartAdapter);
        getDoctors();
    }

    int PAGE_NO = 1;

    private void getDoctors() {
        GouwucheListPostBean bean = new GouwucheListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 1000;
        if ("1".equals(assessmentType)) {
            bean.scoreGoods = 1;
            scoreGoods = 1;
        } else if ("2".equals(assessmentType)) {
            bean.scoreGoods = 0;
            scoreGoods = 0;
        }
        DialogUtil.showProgress(getActivity(), "");
        ApiUtils.getApiService().gouwuchelist(bean).enqueue(new TaiShengCallback<BaseBean<GouwucheResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<GouwucheResultBean>> response, BaseBean<GouwucheResultBean> message) {
//                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        if (message.result.records != null && message.result.records.size() > 0) {
                            listShoppingCart.setLoading(false);
//                            if (PAGE_NO == 1) {
//                                shoppingCartAdapter.shoppingCartBeanList.clear();
//                            }
                            //有消息
                            PAGE_NO++;
                            for (int i = 0; i < message.result.records.size(); i++) {
                                NewShoppingCartBean bean = message.result.records.get(i);
                                ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
                                shoppingCartBean.setShoppingName(bean.goodsName);
                                if (bean.specifications != null) {
                                    shoppingCartBean.setAttribute(bean.specifications.get(0));
                                } else {
                                    shoppingCartBean.setAttribute("");
                                }
                                shoppingCartBean.setPrice(bean.price);
                                shoppingCartBean.setId(bean.id);
                                shoppingCartBean.goodsId = bean.goodsId;
                                shoppingCartBean.setCount(bean.number);
                                shoppingCartBean.setImageUrl(bean.picUrl);
                                shoppingCartBean.productId = bean.productId;

                                shoppingCartBeanList.add(shoppingCartBean);
                            }
                            shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);

                            if (message.result.records.size() < 10) {
                                listShoppingCart.setHasLoadMore(false);
                                listShoppingCart.setLoadAllViewText("");
                                listShoppingCart.setLoadAllFooterVisible(true);
                            } else {
                                listShoppingCart.setHasLoadMore(true);
                            }
//                            shoppingCartAdapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            listShoppingCart.setHasLoadMore(false);
                            listShoppingCart.setLoadAllViewText("");
                            listShoppingCart.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GouwucheResultBean>> call, Throwable t) {
//                ptr_refresh.refreshComplete();
            }
        });


    }

    public void changManager(boolean isTure) {
        if (shoppingCartBeanList != null && shoppingCartBeanList.size() > 0) {
            for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                shoppingCartBeanList.get(i).setChoosed(false);
            }
            shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
            shoppingCartAdapter.setTure(isTure);
            shoppingCartAdapter.notifyDataSetChanged();
            ckAll.setChecked(false);
        }
        statistics(isTure);
    }

    /**
     * 结算订单、支付
     */
    private void lementOnder() {
        if (scoreGoods == 1) {
            if (DingdanInstance.getInstance().putongshangpindingdanList.isEmpty()) {
                Uiutils.showToast("请选择商品");
                return;
            }
        } else {
            if (DingdanInstance.getInstance().jifenshangpindingdanList.isEmpty()) {
                Uiutils.showToast("请选择商品");
                return;
            }
        }

        DingdanInstance.getInstance().zongjia = totalPrice + "";

        DingdanInstance.getInstance().scoreGoods = scoreGoods;

        //跳转到支付界面
        //获取地址信息

        BaseListPostBean bean = new BaseListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = 1;
        bean.pageSize = 10;

        ApiUtils.getApiService().addressList(bean).enqueue(new TaiShengCallback<BaseBean<DizhilistResultBean>>() {
            @Override
            public void onSuccess(Response<BaseBean<DizhilistResultBean>> response, BaseBean<DizhilistResultBean> message) {

                DialogUtil.closeProgress();
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        DingdanInstance.getInstance().flag = "Y";
                        if (message.result.records != null && message.result.records.size() > 0) {
                            Intent intent = new Intent(getActivity(), DingdanjiesuanActivity.class);
                            startActivity(intent);
                        } else {
                            DingdanInstance.getInstance().fromDizhi = "2";
                            Intent intent = new Intent(getActivity(), DizhiBianjiActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<DizhilistResultBean>> call, Throwable t) {

                DialogUtil.closeProgress();
            }
        });
    }

    /**
     * 单选
     *
     * @param position  组元素位置
     * @param isChecked 组元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        shoppingCartBeanList.get(position).setChoosed(isChecked);
        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics(shoppingCartAdapter.isTure());
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {
        for (ShoppingCartBean group : shoppingCartBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics(boolean isTure) {
        totalCount = 0;
        totalPrice = new BigDecimal(0.00);
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                if (shoppingCartBean.price != null) {
                    totalCount++;
                    totalPrice = totalPrice.add(shoppingCartBean.price.multiply(new BigDecimal(shoppingCartBean.getCount())));
                }
            }
        }
        tvShowPrice.setText(totalPrice + "");
        if (isTure) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvSettlement.setText("删除");
                    tvSettlement.setBackgroundResource(R.drawable.item_doctor_zixun2);
                    ckAll.setButtonDrawable(R.drawable.check_box_style2);
                }
            });
        } else {
            tvSettlement.setText("结算(" + totalCount + ")");
            tvSettlement.setBackgroundResource(R.drawable.item_doctor_zixun);
            ckAll.setButtonDrawable(R.drawable.check_box_style);
        }
    }


    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        UpdateCartNumberPostBean updateCartNumberPostBean = new UpdateCartNumberPostBean();
        updateCartNumberPostBean.userId = UserInstance.getInstance().getUid();
        updateCartNumberPostBean.token = UserInstance.getInstance().getToken();
        updateCartNumberPostBean.goodsId = shoppingCartBean.goodsId;
        updateCartNumberPostBean.number = shoppingCartBean.count;
        updateCartNumberPostBean.operateType = 1;
        updateCartNumberPostBean.productId = shoppingCartBean.productId;
        ApiUtils.getApiService().updateCartNumber(updateCartNumberPostBean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        int currentCount = shoppingCartBean.getCount();
                        currentCount++;
                        if (scoreGoods == 1) {
                            for (xiadanshangpinBean bean : DingdanInstance.getInstance().putongshangpindingdanList) {
                                if (bean.goodsId == shoppingCartBean.goodsId) {
                                    bean.number = currentCount + "";
                                }
                            }
                        } else {
                            for (xiadanshangpinBean bean : DingdanInstance.getInstance().jifenshangpindingdanList) {
                                if (bean.goodsId == shoppingCartBean.goodsId) {
                                    bean.number = currentCount + "";
                                }
                            }
                        }
                        shoppingCartBean.setCount(currentCount);
                        ((TextView) showCountView).setText(currentCount + "");
                        shoppingCartAdapter.notifyDataSetChanged();
                        statistics(shoppingCartAdapter.isTure());
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

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        int currentCount = shoppingCartBean.getCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        if (scoreGoods == 1) {
            for (xiadanshangpinBean bean : DingdanInstance.getInstance().putongshangpindingdanList) {
                if (bean.goodsId == shoppingCartBean.goodsId) {
                    bean.number = currentCount + "";
                }
            }
        } else {
            for (xiadanshangpinBean bean : DingdanInstance.getInstance().jifenshangpindingdanList) {
                if (bean.goodsId == shoppingCartBean.goodsId) {
                    bean.number = currentCount + "";
                }
            }
        }

        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics(shoppingCartAdapter.isTure());
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);

        CartDetePostBean bean = new CartDetePostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.cartId = shoppingCartBean.getId() + ",";
        ApiUtils.getApiService().cartDelete(bean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {

            }

            @Override
            public void onFail(Call<BaseBean> call, Throwable t) {

            }
        });
        shoppingCartBeanList.remove(position);
        shoppingCartAdapter.notifyDataSetChanged();
        statistics(shoppingCartAdapter.isTure());
    }


    @OnClick({R.id.ck_all, R.id.tv_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ck_all:
                if (shoppingCartBeanList.size() != 0) {

                    if (scoreGoods == 1) {
                        DingdanInstance.getInstance().putongshangpindingdanList.clear();
                    } else {
                        DingdanInstance.getInstance().jifenshangpindingdanList.clear();
                    }

                    if (ckAll.isChecked()) {

                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(true);
                            ShoppingCartBean beanA = shoppingCartBeanList.get(i);
                            xiadanshangpinBean beanB = new xiadanshangpinBean();
                            beanB.picUrl = beanA.imageUrl;
                            beanB.number = beanA.count + "";
                            beanB.counterPrice = beanA.price + "";
                            beanB.name = beanA.shoppingName;
                            beanB.goodsId = beanA.goodsId;
                            beanB.productId = beanA.productId;


                            if (scoreGoods == 1) {

                                DingdanInstance.getInstance().putongshangpindingdanList.add(beanB);

                            } else {
                                DingdanInstance.getInstance().jifenshangpindingdanList.add(beanB);

                            }

                        }

                        shoppingCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                            shoppingCartBeanList.get(i).setChoosed(false);
                        }
                        shoppingCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics(shoppingCartAdapter.isTure());
                break;
            case R.id.tv_settlement:
                if (shoppingCartAdapter.isTure()) {
                    StringBuffer stringBuffer = new StringBuffer();
                    List<Integer> indext = new ArrayList<>();
                    for (int i = 0; i < shoppingCartBeanList.size(); i++) {
                        if (shoppingCartBeanList.get(i).isChoosed) {
                            stringBuffer.append(shoppingCartBeanList.get(i).getId() + ",");
                            indext.add(i);
                            LogUtilH.e(stringBuffer.toString());
                        }
                    }
                    CartDetePostBean bean = new CartDetePostBean();
                    bean.userId = UserInstance.getInstance().getUid();
                    bean.token = UserInstance.getInstance().getToken();
                    bean.cartId = stringBuffer.toString();
                    ApiUtils.getApiService().cartDelete(bean).enqueue(new TaiShengCallback<BaseBean>() {
                        @Override
                        public void onSuccess(Response<BaseBean> response, BaseBean message) {
                            switch (message.code) {
                                case Constants.HTTP_SUCCESS:
                                    statistics(shoppingCartAdapter.isTure());
                                    if (indext.size() > 0) {
                                        for (int i = 0; i < indext.size(); i++) {
                                            shoppingCartBeanList.remove((int) indext.get(i));
                                        }
                                    }
                                    shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
                                    ckAll.setChecked(false);
                                    break;
                            }
                        }

                        @Override
                        public void onFail(Call<BaseBean> call, Throwable t) {

                        }
                    });
                } else {//结算
                    lementOnder();
                }
                break;
        }
    }
}
