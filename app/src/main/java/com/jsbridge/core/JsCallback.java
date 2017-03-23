package com.jsbridge.core;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.jsbridge.utils.JsBridgeConfig;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONObject;


/**
 * Description: 将native的返回值 return到 js
 * Copyright  : Copyright (c) 2016
 */
public class JsCallback {

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private String callbackId;
    private WebView webView;
    private String execJs;

    public static String tag = "JsCallback";

    public JsCallback(WebView webView, String callbackId) {
        this.webView = webView;
        this.callbackId = callbackId;
    }

    public JsCallback(WebView webView){
        this(webView,null);
    }

    /**
     * @param jsonObject 参数
     * @throws
     * @desc: Native回调js
     * @methodName: apply
     * @returnType void
     */
    public void apply(JSONObject jsonObject) {
//        public static final String CALLBACK_JS_FORMAT="javascript:NGJsBridge.sendMsgToH5(%s,%s);void(0)";
        execJs = String.format(JsBridgeConfig.CALLBACK_JS_FORMAT, callbackId, String.valueOf(jsonObject));
        L.i(tag,"回调指令为:" + execJs);
        if (webView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(execJs);
                }
            });
        }
    }

    /**
     *
     * @param str 回传一个字符串
     */
    public void apply(String  str) {
        execJs = String.format(JsBridgeConfig.CALLBACK_JS_FORMAT, callbackId,str);
        L.i("apply回调指令为:" + execJs);
        if (webView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(execJs);
                }
            });
        }
    }

    /**
     * 跳转到h5页面
     * @param path
     */
    public void goH5ByPathUrl(String path){
        final String pathUrl = String.format(JsBridgeConfig.GO_H5_FORMAT, path);
        if (webView != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(pathUrl);
                }
            });
        }
    }

    /**
     * 将用户信息回调给H5
     * @param userInfo
     */
    public void setLocalStorage(String userInfo){
        final String pathUrl = String.format(JsBridgeConfig.USER_INFO_FOR_H5,userInfo);
        if(webView != null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(pathUrl);
                }
            });
        }
    }

    public void reqeustShareForH5(){
        if(webView!=null){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl(JsBridgeConfig.REQUEST_SHARE_FOR_H5);
                }
            });
        }
    }

    public String getCallbackId() {
        return callbackId;
    }
}
