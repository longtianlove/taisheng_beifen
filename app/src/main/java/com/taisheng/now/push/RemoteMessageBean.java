package com.taisheng.now.push;

import java.util.HashMap;

/**
 * Created by long on 2017/5/3.
 */

public class RemoteMessageBean {
    /**
     * 推送类型：
     * user:用户相关
     * device:设备相关
     * pet:宠物相关
     * other:其他
     */
//    public long petId;
    public String type;

    public String signal;

    public HashMap data;



}
