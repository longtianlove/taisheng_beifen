package com.th.j.commonlibrary.contrarywind.adapter;

import android.content.Context;

import java.util.List;

public class ArrayWheelAdapter2 implements WheelAdapter {
    private Context context;
    private List<String> data;

    public ArrayWheelAdapter2(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getItemsCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int index) {
        return data.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }
}
