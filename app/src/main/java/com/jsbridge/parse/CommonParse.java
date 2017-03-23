package com.jsbridge.parse;

import android.net.Uri;
import android.text.TextUtils;

import com.jsbridge.bean.JsRequestBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Description: JsBridge通用的解析类
 * Copyright  : Copyright (c) 2016
 * Company    : 年糕妈妈
 * Author     : 段宇鹏
 * Date       : 12/1/16
 */
public class CommonParse {

    public static final String CALLBACK = "callback";
    public static final String PARAMS = "params";

    public static String tag = "CommonParse";

    /**
     * 根据通用性规则开始解析
     * {
     * callback:''
     * params:{}
     * }
     *
     * @param jsRequest
     * @return
     */
    public static JsRequestBean parseBasicParams(Uri jsRequest, JsRequestBean requestBean) {

        String strRequestQuery = jsRequest.getQuery(); // 获取uri中的请求参数
        if (TextUtils.isEmpty(strRequestQuery)) {
            return null;
        }

        JSONObject jsonRequest = getJsonObectFromQuery(strRequestQuery);
        try {
            if (jsonRequest.has(CALLBACK)) {
                String callbackId = jsonRequest.getString("callback");
                if (!TextUtils.isEmpty(callbackId)) {
                    requestBean.callbackId = callbackId;
                }
            }
            if (jsonRequest.has(PARAMS)) {
                Object strParams = jsonRequest.get("params");
                if (strParams != null) {
                    requestBean.params = new JSONObject((String) strParams);
                }
            }
        } catch (JSONException e) {

        }
        return requestBean;
    }

    /**
     * @desc: String 'a=1&b=2&c=3' -> json
     */
    public static JSONObject getJsonObectFromQuery(String query){
        HashMap maps=new HashMap();
        String[] params = query.split("&");
        for(String param:params){
            String[] keyValues = param.split("=");
            maps.put(keyValues[0],keyValues[1]);
        }
        return new JSONObject(maps);
    }
}
