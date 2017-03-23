package com.jsbridge.bean;

import org.json.JSONObject;

/**
 * Description: 回调Bean
 * Copyright  : Copyright (c) 2016
 */
public class CallbackBean {

    public int errcode;    // 错误码
    public JSONObject data;    //  请求参数

    @Override
    public String toString() {
        return "CallbackBean{" +
                "errcode=" + errcode +
                ", data=" + data +
                '}';
    }
}
