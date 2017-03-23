package com.jsbridge.core;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description: 回调Js的模板
 * Copyright  : Copyright (c) 2016
 */
public class CallbackTemplate {

    public static final int SUCCESS_CODE = 10000;   // success
    public static final int FAIL_CODE = 110;    // fail


    public static JSONObject getSuccessJson(){
        return getSuccessJson(null);
    }

    /**
     * @desc: 通用Js正确回调
     */
    public static JSONObject getSuccessJson(JSONObject data) {
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("code", SUCCESS_CODE);
            if (data != null) {
                jsonResult.put("data", data);
            }
        } catch (JSONException e) {

        }
        return jsonResult;
    }

    /**
     * @desc: 通用Js错误回调
     */
    public static JSONObject getErroJson(String desc) {
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("code", FAIL_CODE);
            jsonResult.put("desc", desc == null ? "" : desc);
        } catch (JSONException e) {

        }
        return jsonResult;
    }
}
