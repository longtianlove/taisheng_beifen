package com.taisheng.now.bussiness.market.gouwuche;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taisheng.now.R;
import com.taisheng.now.bussiness.bean.result.xiadanshangpinBean;
import com.taisheng.now.bussiness.market.DingdanInstance;
import com.taisheng.now.bussiness.market.ShangPinxiangqingActivity;
import com.th.j.commonlibrary.utils.LogUtilH;
import com.th.j.commonlibrary.wight.RoundImgView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by AYD on 2016/11/21.
 * <p/>
 * 购物车Adapter
 */
public class ShoppingCartAdapter extends BaseAdapter {



    public int scoreGoods;

    public List<ShoppingCartBean> shoppingCartBeanList=new ArrayList<>();
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;
    private boolean isTure;

    public ShoppingCartAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartBeanList(List<ShoppingCartBean> shoppingCartBeanList) {
        this.shoppingCartBeanList = shoppingCartBeanList;
        this.notifyDataSetChanged();
    }

    public void setTure(boolean ture) {
        isTure = ture;
    }

    public boolean isTure() {
        return isTure;
    }

    public List<ShoppingCartBean> getShoppingCartBeanList() {
        return shoppingCartBeanList;
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return shoppingCartBeanList == null ? 0 : shoppingCartBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_cart_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShoppingCartBean shoppingCartBean = shoppingCartBeanList.get(position);
        xiadanshangpinBean xbean=new xiadanshangpinBean();

        xbean.goodsId=shoppingCartBean.goodsId;
        xbean.name=shoppingCartBean.shoppingName;
        xbean.counterPrice="¥"+shoppingCartBean.price+"";
//        xbean.retailPrice=retailPrice;
        xbean.number=shoppingCartBean.count+"";
        xbean.picUrl=shoppingCartBean.imageUrl;
        xbean.productId=shoppingCartBean.productId;

        holder.ll_all.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShangPinxiangqingActivity.class);
                intent.putExtra("goodsid", xbean.goodsId);

                context.startActivity(intent);
            }
        });

        if (isTure){
            holder.ckOneChose.setButtonDrawable(R.drawable.check_box_style2);
        }else {
            holder.ckOneChose.setButtonDrawable(R.drawable.check_box_style);
        }
        boolean choosed = shoppingCartBean.isChoosed();
        if (choosed){
            holder.ckOneChose.setChecked(true);
        }else{
            holder.ckOneChose.setChecked(false);
        }

        Glide.with(context)
                .load(shoppingCartBean.imageUrl)
                .placeholder(R.drawable.article_default)
                .error(R.drawable.article_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.sdv_article);

        String attribute = shoppingCartBean.getAttribute();
        if (!StringUtil.isEmpty(attribute)){
            holder.tvCommodityAttr.setText(attribute);
        }else{
            holder.tvCommodityAttr.setText(shoppingCartBean.getDressSize()+"");
        }
        holder.tvCommodityName.setText(shoppingCartBean.getShoppingName());
        if (scoreGoods == 1) {
            holder.tvCommodityPrice.setText(shoppingCartBean.getPrice() + "");
        }else{
            holder.tvCommodityPrice.setText(shoppingCartBean.getPrice().multiply(new BigDecimal(100)) + "");

        }
        holder.tvCommodityNum.setText(" X"+shoppingCartBean.getCount()+"");
        holder.tvCommodityShowNum.setText(shoppingCartBean.getCount()+"");
//        ImageLoader.getInstance().displayImage(shoppingCartBean.getImageUrl(),holder.ivShowPic);
        //单选框按钮
        holder.ckOneChose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shoppingCartBean.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                        if(((CheckBox) v).isChecked()){


                                    if (scoreGoods == 1) {

//                                        for (xiadanshangpinBean bean : DingdanInstance.getInstance().putongshangpindingdanList) {
//                                            if (bean.goodsId == xbean.goodsId) {
////                                                DingdanInstance.getInstance().putongshangpindingdanList.remove(bean);
                                                Iterator<xiadanshangpinBean> it= DingdanInstance.getInstance().putongshangpindingdanList.iterator();
                                                while(it.hasNext()) {
                                                    xiadanshangpinBean bean=it.next();
                                                    if (bean.goodsId == xbean.goodsId) {
                                                        it.remove();
                                                    }
                                        }
                                        DingdanInstance.getInstance().putongshangpindingdanList.add(xbean);
                                    } else {
//                                        for (xiadanshangpinBean bean : DingdanInstance.getInstance().jifenshangpindingdanList) {
//                                            if (bean.goodsId == xbean.goodsId) {
//                                                DingdanInstance.getInstance().jifenshangpindingdanList.remove(bean);
//                                            }
//                                        }

                                        Iterator<xiadanshangpinBean> it= DingdanInstance.getInstance().jifenshangpindingdanList.iterator();
                                        while(it.hasNext()) {
                                            xiadanshangpinBean bean = it.next();
                                            if (bean.goodsId == xbean.goodsId) {
                                                it.remove();
                                            }
                                        }
                                        DingdanInstance.getInstance().jifenshangpindingdanList.add(xbean);
                                    }



                        }else{

                                if (scoreGoods == 1) {
//                                    for (xiadanshangpinBean bean : DingdanInstance.getInstance().putongshangpindingdanList) {
//                                        if (bean.goodsId == xbean.goodsId) {
//                                            DingdanInstance.getInstance().putongshangpindingdanList.remove(bean);
//                                        }
//                                    }
                                    Iterator<xiadanshangpinBean> it= DingdanInstance.getInstance().putongshangpindingdanList.iterator();
                                    while(it.hasNext()) {
                                        xiadanshangpinBean bean=it.next();
                                        if (bean.goodsId == xbean.goodsId) {
                                            it.remove();
                                        }
                                    }
                                } else {
//                                    for (xiadanshangpinBean bean : DingdanInstance.getInstance().jifenshangpindingdanList) {
//                                        if (bean.goodsId == xbean.goodsId) {
//                                            DingdanInstance.getInstance().jifenshangpindingdanList.remove(bean);
//                                        }
//                                    }
                                    Iterator<xiadanshangpinBean> it= DingdanInstance.getInstance().jifenshangpindingdanList.iterator();
                                    while(it.hasNext()) {
                                        xiadanshangpinBean bean=it.next();
                                        if (bean.goodsId == xbean.goodsId) {
                                            it.remove();
                                        }
                                    }
                                }

                        }
                    }
                }
        );
        //增加按钮
        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {






                modifyCountInterface.doIncrease(position, holder.tvCommodityShowNum, holder.ckOneChose.isChecked());//暴露增加接口
            }
        });
        //删减按钮
        holder.ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.tvCommodityShowNum, holder.ckOneChose.isChecked());//暴露删减接口
            }
        });

        //判断是否在编辑状态下
//        if (isShow) {
//            holder.tvCommodityName.setVisibility(View.VISIBLE);
//            holder.rlEdit.setVisibility(View.GONE);
//            holder.tvCommodityNum.setVisibility(View.VISIBLE);
//            holder.tvCommodityDelete.setVisibility(View.GONE);
//        } else {
            holder.tvCommodityName.setVisibility(View.VISIBLE);
            holder.tvCommodityNum.setVisibility(View.VISIBLE);
//        }

        return convertView;
    }
    //初始化控件
    class ViewHolder {
        View ll_all;
        TextView tvCommodityName, tvCommodityAttr, tvCommodityPrice, tvCommodityNum, tvCommodityShowNum,ivSub, ivAdd;
        CheckBox ckOneChose;
        RoundImgView sdv_article;;
        public ViewHolder(View itemView) {
            ll_all=itemView.findViewById(R.id.ll_all);
            ckOneChose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            ivSub = (TextView) itemView.findViewById(R.id.iv_sub);
            ivAdd = (TextView) itemView.findViewById(R.id.iv_add);
            tvCommodityName = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            tvCommodityAttr = (TextView) itemView.findViewById(R.id.tv_commodity_attr);
            tvCommodityPrice = (TextView) itemView.findViewById(R.id.tv_commodity_price);
            tvCommodityNum = (TextView) itemView.findViewById(R.id.tv_commodity_num);
            tvCommodityShowNum = (TextView) itemView.findViewById(R.id.tv_commodity_show_num);
            sdv_article=itemView.findViewById(R.id.sdv_article);


        }
    }
    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}
