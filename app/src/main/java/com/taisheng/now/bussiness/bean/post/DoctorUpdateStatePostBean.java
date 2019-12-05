package com.taisheng.now.bussiness.bean.post;

/**
 * Created by dragon on 2019/7/16.
 */

public class DoctorUpdateStatePostBean extends BasePostBean {
    public String doctorId;
    public String roomId;
    /** 挂断类型. 0:用户挂断  1:自动挂断 */
    public String offType;
}
