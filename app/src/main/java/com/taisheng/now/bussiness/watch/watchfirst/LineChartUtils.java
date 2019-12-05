package com.taisheng.now.bussiness.watch.watchfirst;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineChartUtils {


    private ArrayList<Entry> list1 = new ArrayList<>();  //数据集合
    private ArrayList<Entry> list2 = new ArrayList<>();  //数据集合
    private ArrayList<Entry> list3 = new ArrayList<>();  //数据集合
    LineChart lineChart;
    private LineDataSet set;
    private LineDataSet set1;
    private LineDataSet set2;

    public LineChartUtils(ArrayList<Entry> list1, ArrayList<Entry> list2 , ArrayList<Entry> list3 ,LineChart lineChart) {
        this.list1=list1;
        this.list2=list2;
        this.list3=list3;
        this.lineChart=lineChart;
        setData(list1,list2,list3);
    }
    public LineChartUtils(ArrayList<Entry> list1, ArrayList<Entry> list2 ,LineChart lineChart) {
        this.list1=list1;
        this.list2=list2;
//        this.list3=list3;
        this.lineChart=lineChart;
        setData(list1,list2);
        this.lineChart.invalidate();
    }

    public LineChartUtils(ArrayList<Entry> list1 ,LineChart lineChart,String line1Color,String lineString) {
        this.list1=list1;
//        this.list2=list2;
//        this.list3=list3;
        this.lineChart=lineChart;
        this.lineString=lineString;
        this.line1Color=line1Color;
        setData(list1);
        this.lineChart.invalidate();
    }
    String lineString="心率";
    private void setData(ArrayList<Entry> list1) {
        XwangGe();
        set = new LineDataSet(list1, lineString);
        setLine(set);
//        set1 = new LineDataSet(list2, "功率");
//        setLine2(set1);
//        set2 = new LineDataSet(list3, "过程点折水系数");
//        setLine3(set2);

        //创建一个数据集
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set);
//        dataSets.add(set1);
//        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        //设置数据
        lineChart.setData(data);
        //隐藏图表右下角显示内容
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);


    }


    private void setData(ArrayList<Entry> list1,ArrayList<Entry> list2) {
        XwangGe();
        set = new LineDataSet(list1, "高压");
        setLine(set);
        set1 = new LineDataSet(list2, "低压");
        setLine2(set1);
//        set2 = new LineDataSet(list3, "过程点折水系数");
//        setLine3(set2);

        //创建一个数据集
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set);
        dataSets.add(set1);
//        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        //设置数据
        lineChart.setData(data);
        //隐藏图表右下角显示内容
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);


    }
    private void setData(ArrayList<Entry> list1,ArrayList<Entry> list2,ArrayList<Entry> list3) {
        XwangGe();
        set = new LineDataSet(list1, "瞬时流量");
        setLine(set);
        set1 = new LineDataSet(list2, "功率");
        setLine2(set1);
        set2 = new LineDataSet(list3, "过程点折水系数");
        setLine3(set2);

        //创建一个数据集
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(set);
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);
        //设置数据
        lineChart.setData(data);
        //隐藏图表右下角显示内容
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);


    }

    String line1Color="#44BF4C";

    private void setLine(LineDataSet set) {

        //设置线条的颜色
        set.setColor(Color.parseColor(line1Color));
        //虚线模式下绘制直线
        //set.enableDashedLine(20f, 5f, 0f);
        //点击后高亮线的显示颜色
        //set.enableDashedHighlightLine(50f, 15f, 0f);

        //设置数据小圆点的颜色
        set.setCircleColor(Color.parseColor(line1Color));
        //设置圆点是否有空心
        set.setDrawCircles(true);
        //设置线条的宽度，最大10f,最小0.2f
        set.setLineWidth(1f);
        //设置小圆点的半径，最小1f，默认4f
        set.setCircleRadius(2f);
        //设置是否显示小圆点
        set.setDrawCircles(true);
        //设置字体颜色
        set.setValueTextColor(Color.parseColor("#D4716E"));
        //设置折线为圆滑折线(加在上面的setLine方法里)
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //设置字体大小
        set.setValueTextSize(10f);
        set.setFillColor(Color.parseColor("#D4716E"));
        //设置是否填充
        set.setDrawFilled(false);


    }
    String line2Color="#FF9D3C";
    private void setLine2(LineDataSet set) {
        //设置线条的颜色
        set.setColor(Color.parseColor(line2Color));
        //虚线模式下绘制直线
        //set.enableDashedLine(20f, 5f, 0f);
        //点击后高亮线的显示颜色
        //set.enableDashedHighlightLine(50f, 15f, 0f);
        //设置折线为圆滑折线(加在上面的setLine方法里)
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //设置数据小圆点的颜色
        set.setCircleColor(Color.parseColor(line2Color));
        //设置圆点是否有空心
        set.setDrawCircles(true);
        //设置线条的宽度，最大10f,最小0.2f
        set.setLineWidth(1f);
        //设置小圆点的半径，最小1f，默认4f
        set.setCircleRadius(2f);
        //设置是否显示小圆点
        set.setDrawCircles(true);
        //设置字体颜色
        set.setValueTextColor(Color.parseColor("#6D7C87"));
        //设置字体大小
        set.setValueTextSize(10f);
        //设置是否填充
        set.setDrawFilled(false);

    }
    private void setLine3(LineDataSet set) {
        //设置线条的颜色
        set.setColor(Color.parseColor("#90BCC2"));
        //虚线模式下绘制直线
        //set.enableDashedLine(20f, 5f, 0f);
        //点击后高亮线的显示颜色
        //set.enableDashedHighlightLine(50f, 15f, 0f);
        //设置折线为圆滑折线(加在上面的setLine方法里)
        //set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //设置数据小圆点的颜色
        set.setCircleColor(Color.parseColor("#90BCC2"));
        //设置圆点是否有空心
        set.setDrawCircles(true);
        //设置线条的宽度，最大10f,最小0.2f
        set.setLineWidth(1f);
        //设置小圆点的半径，最小1f，默认4f
        set.setCircleRadius(2f);
        //设置是否显示小圆点
        set.setDrawCircles(true);
        //设置字体颜色
        set.setValueTextColor(Color.parseColor("#90BCC2"));
        //设置字体大小
        set.setValueTextSize(10f);
        //设置是否填充
        set.setDrawFilled(false);

    }
    private void XwangGe(){
        //设置x轴网格线
        XAxis xAxis=lineChart.getXAxis();
        //以虚线模式画网格线
        xAxis.enableGridDashedLine(10f,10f,0f);
        //设置x轴最大值
        xAxis.setAxisMaximum(200f);
        //设置x轴最小值
        xAxis.setAxisMinimum(0f);

        //撤销设置的最大值，让轴自动计算
        xAxis.resetAxisMaximum();
        //撤销设置的最小值，让轴自动计算
        xAxis.resetAxisMinimum();
        //        //设置x轴标签数，默认为6个
        xAxis.setLabelCount(10);
        //        //设置x轴标签数，若强制启用true，可能导致轴上的数字不均匀
        //        xAxis.setLabelCount(10,true);

        //设置x轴之间的最小间隔。用于在图表放大后标签不至于重合
        xAxis.setGranularity(1f);
        //设置x轴轴线的宽度
        xAxis.setAxisLineWidth(1f);
        //设置轴线的颜色
        xAxis.setAxisLineColor(Color.parseColor("#7F7575"));

        //设置x轴显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    }


}
