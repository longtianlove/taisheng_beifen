package com.taisheng.now.base;

import java.io.Serializable;

/**
 * Created by long on 17/4/8.
 */

public   class BaseBean<T> implements Serializable {
    public boolean success;
    public String message;
    public int code;
    public long timestamp;
    public T result;



}
