package com.jsbridge.core;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jsbridge.bean.JsRequestBean;
import com.jsbridge.module.IModule;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Description: JsBridge的具体实现
 * Copyright  : Copyright (c) 2016
 */
public class JsBridge {

    /**
     * @desc: 调用Native方法
     * @param webView webView的代理类
     * @param requestBean 请求对象
     */
    public void jsCallJava(AppCompatActivity activity,WebView webView, JsRequestBean requestBean) {
        try {
            Class<? extends IModule> targetClass = (Class<? extends IModule>) Class.forName(requestBean.clazz);
            Method[] targetMethods = targetClass.getDeclaredMethods();
            for (Method targetMethod : targetMethods) {
                if (requestBean.method.equals(targetMethod.getName())) {
                    // 在主线程执行
                    doInMainThread(activity,targetMethod,webView,requestBean);
                }
            }
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * @desc: 在主线程执行Native方法
    */
    public void doInMainThread(AppCompatActivity activity,Method method, WebView webView, JsRequestBean requestBean){

        Looper.prepare();
        try {
            method.invoke(null,activity, webView, requestBean.params, new JsCallback(webView, requestBean.callbackId));
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        Looper.loop();
    }

    /**
     * @desc: 调用js
     * @param callback 回调对象
     * @param params 回调参数
     */
    public void javaCallJs(JsCallback callback, JSONObject params) {
        callback.apply(params);
    }


}
