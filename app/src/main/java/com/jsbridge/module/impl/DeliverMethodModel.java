package com.jsbridge.module.impl;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.cloudmachine.activities.WanaCloudBox;
import com.jsbridge.core.JsCallback;
import com.jsbridge.module.IModule;

import org.json.JSONObject;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/6 下午3:12
 * 修改人：shixionglu
 * 修改时间：2017/3/6 下午3:12
 * 修改备注：
 */

public class DeliverMethodModel implements IModule {


    public static void deliverProId(AppCompatActivity activity, WebView webView, JSONObject params, final JsCallback callback) {

        try {
            WanaCloudBox wanaCloudBoxActivity = (WanaCloudBox) activity;
            String proId = params.getString("proId");
            if (null != proId) {
                wanaCloudBoxActivity.setProId(proId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
