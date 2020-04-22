package com.taisheng.now.bussiness.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 韩晓康
 * 2017/6/1
 * 适配器基类
 */

public abstract class BasAdapter<T> extends BaseAdapter {
    private List<T> data;

    public void setData(List<T> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public List<T> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
}

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
