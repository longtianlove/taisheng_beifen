package com.taisheng.now.map;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.taisheng.now.R;


import java.util.ArrayList;

/**
 * Created by long on 2017/7/9.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.StateHolder> {


    private Context context;
    public ArrayList<PoiInfo> mdatas;

    public int mposition = -1;

    public AddressAdapter(Context context, ArrayList<PoiInfo> datas) {
        this.context = context;
        mdatas = datas;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(final StateHolder holder, final int position) {
        if (null == mdatas || mdatas.size() == 0) return;
        PoiInfo bean = mdatas.get(position);


        if (bean.address != null && !"".equals(bean.address)) {
            holder.tv_address.setText(bean.address);
        }
        if (bean.name != null && !"".equals(bean.name)) {
            holder.tv_name.setText(bean.name);
        }

        if (mposition == position) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.iv_selected.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   mposition = position;
                                                   onItemClickListener.OnItemClick(view, holder, holder.getAdapterPosition());

                                               }
                                           }

        );
    }

    @Override
    public int getItemCount() {
        return (mdatas == null) || (mdatas.size() == 0) ? 0 : mdatas.size();
    }


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener { //定义接口，实现Recyclerview点击事件
        void OnItemClick(View view, StateHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) { //实现点击
        this.onItemClickListener = onItemClickListener;
    }


    public class StateHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_address;
        public ImageView iv_selected;

        public StateHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            iv_selected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }
}
