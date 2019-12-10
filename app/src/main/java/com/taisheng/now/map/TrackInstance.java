package com.taisheng.now.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;

import java.util.ArrayList;
import java.util.List;

public class TrackInstance {
    public static TrackInstance instance = new TrackInstance();


    public static TrackInstance getInstance() {
        return instance;
    }


    public void init(Context context, BaiduMap mBaiduMap) {
        // 请求标识
        int tag = 1;
        // 轨迹服务ID
        long serviceId = 218094;
        // 设备标识
        String entityName = "myTrace";
        // 初始化轨迹服务
        Trace mTrace = new Trace(serviceId, entityName, false);
// 初始化轨迹服务客户端
        LBSTraceClient mTraceClient = new LBSTraceClient(context);


        // 创建历史轨迹请求实例
        HistoryTrackRequest historyTrackRequest = new HistoryTrackRequest(tag, serviceId, entityName);

        //设置轨迹查询起止时间
// 开始时间(单位：秒)
        long startTime = System.currentTimeMillis() / 1000 - 12 * 60 * 60;
        // 结束时间(单位：秒)
        long endTime = System.currentTimeMillis() / 1000;
// 设置开始时间
        historyTrackRequest.setStartTime(startTime);
// 设置结束时间
        historyTrackRequest.setEndTime(endTime);


        // 设置需要纠偏
        historyTrackRequest.setProcessed(true);

// 创建纠偏选项实例
        ProcessOption processOption = new ProcessOption();
// 设置需要去噪
        processOption.setNeedDenoise(true);
// 设置需要抽稀
        processOption.setNeedVacuate(true);
// 设置需要绑路
        processOption.setNeedMapMatch(true);
// 设置精度过滤值(定位精度大于100米的过滤掉)
        processOption.setRadiusThreshold(100);
// 设置交通方式为驾车
        processOption.setTransportMode(TransportMode.driving);
// 设置纠偏选项
        historyTrackRequest.setProcessOption(processOption);


// 设置里程填充方式为驾车
        historyTrackRequest.setSupplementMode(SupplementMode.driving);


        // 初始化轨迹监听器
        OnTrackListener mTrackListener = new OnTrackListener() {
            // 历史轨迹回调
            @Override
            public void onHistoryTrackCallback(HistoryTrackResponse response) {
                ArrayList trackPoints=new ArrayList();

                int total = response.getTotal();
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    Toast.makeText(context, "结果为：" + response.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (0 == total) {
                    Toast.makeText(context, "未查询到历史轨迹", Toast.LENGTH_SHORT).show();
                } else {
                    List<TrackPoint> points = response.getTrackPoints();
                    if (null != points) {
                        for (TrackPoint trackPoint : points) {
                            if (!TraceUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                    trackPoint.getLocation().getLongitude())) {
                                trackPoints.add(TraceUtil.convertTrace2Map(trackPoint.getLocation()));
                            }
                        }
                    }
                }
                TraceUtil traceUtil = new TraceUtil();
                traceUtil.drawHistoryTrack(mBaiduMap, trackPoints, SortType.asc);


            }
        };


// 查询历史轨迹
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }


}
