package com.taisheng.now.map;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.taisheng.now.R;
import com.taisheng.now.evbusbean.BaiduSearchAddr;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2017/7/9.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.StateHolder> {

    private Context context;
    public List<BaiduSearchAddr> mdatas;

    public int mposition = -1;

    public AddressAdapter(Context context) {
        this.context = context;
    }

    public void setMdatas(List<BaiduSearchAddr> mdatas) {
        this.mdatas = mdatas;
        this.notifyDataSetChanged();
    }

    public List<BaiduSearchAddr> getMdatas() {
        return mdatas;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateHolder(LayoutInflater.from(context).inflate(R.layout.item_address, parent, false));
    }

    @Override
    public void onBindViewHolder(final StateHolder holder, final int position) {
        if (null == mdatas || mdatas.size() == 0) return;
        BaiduSearchAddr searchAddr = mdatas.get(position);

        holder.tv_address.setText(searchAddr.getAddr());
        holder.tv_name.setText(searchAddr.getKey());

        if (position == 0) {
            holder.iv_dot.setBackgroundResource(R.drawable.circle_green);
        } else {
            holder.iv_dot.setBackgroundResource(R.drawable.circle_grey);
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
        public ImageView iv_dot;

        public StateHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            iv_dot = (ImageView) itemView.findViewById(R.id.iv_dot);
        }
    }
}
