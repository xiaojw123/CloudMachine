package com.jsbridge.utils;

/**
 * Description: JsBridge中一些通信规则,和一些常量
 * Copyright  : Copyright (c) 2016
 */
public class JsBridgeConfig {

    // h5发送的协议为:cloudm://goNative?page=login&&params="dd"
    // 通信协议
    public final static String NGMM_SCHEMA="cloudm://";

    // JsBridge 验证结果
    public final static String JSBRIDGE_CALL_SUCCESS = "success";
    public final static String JSBRIDGE_CALL_FAIL = "fail";

    // 回调js接口,第一个参数为 js传过来的callback 。第二个参数为 回调函数的参数
    public static final String CALLBACK_JS_FORMAT="javascript:NGJsBridge.NT_sendMsgToH5('%s','%s');void(0)";
    // 跳转h5页面的接口 ,
    public static final String GO_H5_FORMAT = "javascript:NGJsBridge.NT_goH5('%s');void(0);";

    public static final String GO_H5_PATH_URL = "javascript:NGJsBridge.NT_goH5('path@%s');void(0);";

    public static final String USER_INFO_FOR_H5 = "javascript:NGJsBridge.NT_sendUserInfoToH5('%s')";

    public static final String REQUEST_SHARE_FOR_H5 = "javascript:NGJsBridge.NT_share();void(0);";
}
