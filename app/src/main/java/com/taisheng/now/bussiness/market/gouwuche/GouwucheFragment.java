package com.taisheng.now.bussiness.market.gouwuche;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.dingdan.DingdanjiesuanActivity;
import com.taisheng.now.bussiness.market.dizhi.DizhiBianjiActivity;
import com.taisheng.now.bussiness.user.UserInstance;
import com.taisheng.now.http.ApiUtils;
import com.taisheng.now.http.TaiShengCallback;
import com.taisheng.now.util.DialogUtil;
import com.taisheng.now.util.ToastUtil;
import com.taisheng.now.view.TaishengListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GouwucheFragment extends BaseFragment implements View.OnClickListener
        , ShoppingCartAdapter.CheckInterface, ShoppingCartAdapter.ModifyCountInterface {

    private static final String TAG = "ShoppingCartActivity";
    public String assessmentType;
    public int scoreGoods;


    //    View btnBack;
    //全选
    CheckBox ckAll;
    //总额
    TextView tvShowPrice;
    //结算
    TextView tvSettlement;
    //编辑
//    TextView btnEdit;//tv_edit


    //    MaterialDesignPtrFrameLayout ptr_refresh;
    com.taisheng.now.view.TaishengListView list_shopping_cart;
    private ShoppingCartAdapter shoppingCartAdapter;
    private boolean flag = false;
    private List<ShoppingCartBean> shoppingCartBeanList = new ArrayList<>();
    private boolean mSelect;
    private BigDecimal totalPrice = new BigDecimal(0.00);// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.layout_shopping_cart_activity, container, false);


        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {

//        btnBack = rootView.findViewById(R.id.btn_back);
        ckAll = (CheckBox) rootView.findViewById(R.id.ck_all);
        tvShowPrice = (TextView) rootView.findViewById(R.id.tv_show_price);
        tvSettlement = (TextView) rootView.findViewById(R.id.tv_settlement);
//        btnEdit = (TextView) rootView.findViewById(R.id.bt_header_right);


//        ptr_refresh = (MaterialDesignPtrFrameLayout) findViewById(R.id.ptr_refresh);
//        /**
//         * 下拉刷新
//         */
//        ptr_refresh.setPtrHandler(new PtrDefaultHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                PAGE_NO = 1;
////                getDoctors();
//
//            }
//        });

            if (scoreGoods == 1) {
                DingdanInstance.getInstance().putongshangpindingdanList.clear();
            } else {
                DingdanInstance.getInstance().jifenshangpindingdanList.clear();
            }



        list_shopping_cart = (com.taisheng.now.view.TaishengListView) rootView.findViewById(R.id.list_shopping_cart);

//        btnEdit.setOnClickListener(this);
        ckAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
//        btnBack.setOnClickListener(this);


        initData();
    }

    //初始化数据
    protected void initData() {

//        for (int i = 0; i < 2; i++) {
//            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
//            shoppingCartBean.setShoppingName("上档次的T桖");
//            shoppingCartBean.setDressSize(20);
//            shoppingCartBean.setId(i);
//            shoppingCartBean.setPrice(30.6);
//            shoppingCartBean.setCount(1);
//            shoppingCartBean.setImageUrl("https://img.alicdn.com/bao/uploaded/i2/TB1YfERKVXXXXanaFXXXXXXXXXX_!!0-item_pic.jpg_430x430q90.jpg");
//            shoppingCartBeanList.add(shoppingCartBean);
//        }
//        for (int i = 0; i < 2; i++) {
//            ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
//            shoppingCartBean.setShoppingName("瑞士正品夜光男女士手表情侣精钢带男表防水石英学生非天王星机械");
//            shoppingCartBean.setAttribute("黑白色");
//            shoppingCartBean.setPrice(89);
//            shoppingCartBean.setId(i + 2);
//            shoppingCartBean.setCount(3);
//            shoppingCartBean.setImageUrl("https://gd1.alicdn.com/imgextra/i1/2160089910/TB2M_NSbB0kpuFjSsppXXcGTXXa_!!2160089910.jpg");
//            shoppingCartBeanList.add(shoppingCartBean);
//        }
        shoppingCartAdapter = new ShoppingCartAdapter(getActivity());
        shoppingCartAdapter.scoreGoods=scoreGoods;
        shoppingCartAdapter.setCheckInterface(this);
        shoppingCartAdapter.setModifyCountInterface(this);
        list_shopping_cart.setAdapter(shoppingCartAdapter);
        list_shopping_cart.setOnUpLoadListener(new TaishengListView.OnUpLoadListener() {
            @Override
            public void onUpLoad() {
                getDoctors();
            }
        });


        getDoctors();
//        shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
    }

    int PAGE_NO = 1;
    int PAGE_SIZE = 10;

    void getDoctors() {
        GouwucheListPostBean bean = new GouwucheListPostBean();
        bean.userId = UserInstance.getInstance().getUid();
        bean.token = UserInstance.getInstance().getToken();
        bean.pageNo = PAGE_NO;
        bean.pageSize = 10;
        switch (assessmentType) {
            case "1":
                bean.scoreGoods = 1;
                scoreGoods = 1;
                break;
            case "2":
                bean.scoreGoods = 0;
                scoreGoods = 0;
                break;

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
                            list_shopping_cart.setLoading(false);
                            if (PAGE_NO == 1) {
                                shoppingCartAdapter.shoppingCartBeanList.clear();
                            }
                            //有消息
                            PAGE_NO++;
                            for (NewShoppingCartBean bean : message.result.records) {

                                ShoppingCartBean shoppingCartBean = new ShoppingCartBean();
                                shoppingCartBean.setShoppingName(bean.goodsName);
                                shoppingCartBean.setAttribute(bean.specifications);
                                shoppingCartBean.setPrice(bean.price);
                                shoppingCartBean.setId(bean.id);
                                shoppingCartBean.goodsId = bean.goodsId;
                                shoppingCartBean.setCount(bean.number);
                                shoppingCartBean.setImageUrl(bean.picUrl);
                                shoppingCartBean.productId = bean.productId;

                                shoppingCartBeanList.add(shoppingCartBean);

                            }


                            shoppingCartAdapter.setShoppingCartBeanList(shoppingCartBeanList);
                            shoppingCartAdapter.isShow(true);

                            if (message.result.records.size() < 10) {
                                list_shopping_cart.setHasLoadMore(false);
                                list_shopping_cart.setLoadAllViewText("");
                                list_shopping_cart.setLoadAllFooterVisible(true);
                            } else {
                                list_shopping_cart.setHasLoadMore(true);
                            }
//                            shoppingCartAdapter.notifyDataSetChanged();
                        } else {
                            //没有消息
                            list_shopping_cart.setHasLoadMore(false);
                            list_shopping_cart.setLoadAllViewText("");
                            list_shopping_cart.setLoadAllFooterVisible(true);
                        }
                        break;
                }
            }

            @Override
            public void onFail(Call<BaseBean<GouwucheResultBean>> call, Throwable t) {
//                ptr_refresh.refreshComplete();
                DialogUtil.closeProgress();
            }
        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
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
                statistics();
                break;
            case R.id.bt_header_right:
//                flag = !flag;
//                if (flag) {
//                    btnEdit.setText("完成");
//                    shoppingCartAdapter.isShow(false);
//                } else {
//                    btnEdit.setText("管理");
//                    shoppingCartAdapter.isShow(true);
//                }
                break;
            case R.id.tv_settlement: //结算
                lementOnder();
                break;
            case R.id.btn_back:
                getActivity().finish();
                break;
        }
    }


    /**
     * 结算订单、支付
     */
    private void lementOnder() {

        if (scoreGoods == 1) {
            if (DingdanInstance.getInstance().putongshangpindingdanList.isEmpty()) {
                ToastUtil.showAtCenter("请选择商品");
                return;
            }
        } else {
            if (DingdanInstance.getInstance().jifenshangpindingdanList.isEmpty()) {
                ToastUtil.showAtCenter("请选择商品");
                return;
            }
        }


        //选中的需要提交的商品清单
        for (ShoppingCartBean bean : shoppingCartBeanList) {
            boolean choosed = bean.isChoosed();
            if (choosed) {
                String shoppingName = bean.getShoppingName();
                int count = bean.getCount();
                BigDecimal price = bean.getPrice();
                int size = bean.getDressSize();
                String attribute = bean.getAttribute();
                String id = bean.getId();
                Log.d(TAG, id + "----id---" + shoppingName + "---" + count + "---" + price + "--size----" + size + "--attr---" + attribute);
            }
        }
//        ToastUtil.showAtCenter("总价：" + totalPrice);
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
        statistics();
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
    public void statistics() {
        totalCount = 0;
        totalPrice = new BigDecimal(0.00);
        for (int i = 0; i < shoppingCartBeanList.size(); i++) {
            ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice =totalPrice.add(shoppingCartBean.price.multiply(new BigDecimal(shoppingCartBean.getCount())));
            }
        }
        tvShowPrice.setText("合计:" + totalPrice);
        tvSettlement.setText("结算(" + totalCount + ")");
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



        UpdateCartNumberPostBean updateCartNumberPostBean=new UpdateCartNumberPostBean();
        updateCartNumberPostBean.userId=UserInstance.getInstance().getUid();
        updateCartNumberPostBean.token=UserInstance.getInstance().getToken();
        updateCartNumberPostBean.goodsId=shoppingCartBean.goodsId;
        updateCartNumberPostBean.number=shoppingCartBean.count;
        updateCartNumberPostBean.operateType=1;
        updateCartNumberPostBean.productId=shoppingCartBean.productId;
        ApiUtils.getApiService().updateCartNumber(updateCartNumberPostBean).enqueue(new TaiShengCallback<BaseBean>() {
            @Override
            public void onSuccess(Response<BaseBean> response, BaseBean message) {
                switch (message.code) {
                    case Constants.HTTP_SUCCESS:
                        int currentCount = shoppingCartBean.getCount();
                        currentCount++;
                        if(scoreGoods==1){
                            for(xiadanshangpinBean bean:DingdanInstance.getInstance().putongshangpindingdanList){
                                if(bean.goodsId==shoppingCartBean.goodsId){
                                    bean.number=currentCount+"";
                                }
                            }
                        }else{
                            for(xiadanshangpinBean bean:DingdanInstance.getInstance().jifenshangpindingdanList){
                                if(bean.goodsId==shoppingCartBean.goodsId){
                                    bean.number=currentCount+"";
                                }
                            }
                        }
                        shoppingCartBean.setCount(currentCount);
                        ((TextView) showCountView).setText(currentCount + "");
                        shoppingCartAdapter.notifyDataSetChanged();
                        statistics();
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
        if(scoreGoods==1){
            for(xiadanshangpinBean bean:DingdanInstance.getInstance().putongshangpindingdanList){
                if(bean.goodsId==shoppingCartBean.goodsId){
                    bean.number=currentCount+"";
                }
            }
        }else{
            for(xiadanshangpinBean bean:DingdanInstance.getInstance().jifenshangpindingdanList){
                if(bean.goodsId==shoppingCartBean.goodsId){
                    bean.number=currentCount+"";
                }
            }
        }

        shoppingCartBean.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        shoppingCartAdapter.notifyDataSetChanged();
        statistics();
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
        statistics();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
