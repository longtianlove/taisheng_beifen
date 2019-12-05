package com.taisheng.now.bussiness.watch.watchfirst;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

public class MyXFormatter implements IAxisValueFormatter {
    ArrayList<String> days = new ArrayList<>();



    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if(value<0||value>=days.size()){
            return "0";
        }else {
//            String[] temp=days.get((int) value).split(" ");
//            return temp[1];
            return days.get((int)value);
        }
    }
}
