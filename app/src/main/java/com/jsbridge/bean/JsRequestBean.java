package com.jsbridge.bean;

import org.json.JSONObject;

/**
 * Description: js执行bean
 * Copyright  : Copyright (c) 2016
 * Company    : 年糕妈妈
 * Author     : 段宇鹏
 * Date       : 10/18/16
 */
public class JsRequestBean {

    public String clazz;    // 类名
    public String method;   // 方法名称
    public JSONObject params;   // 参数
    public String callbackId;     // 返回值
    public String action;   // 注册的行动

    @Override
    public String toString() {
        return "UIInterface{" +
                "clazz='" + clazz + '\'' +
                ", method='" + method + '\'' +
                ", params='" + params + '\'' +
                ", port='" + callbackId + '\'' +
                '}';
    }
}
