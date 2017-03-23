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
 * 创建时间：2017/3/5 下午11:29
 * 修改人：shixionglu
 * 修改时间：2017/3/5 下午11:29
 * 修改备注：
 */

public class UIInteractiveModel implements IModule {

    public static void showBuyBtn(AppCompatActivity activity, WebView webView, JSONObject params, final JsCallback callback) {
        try {
            String action = params.getString("action");
            WanaCloudBox wanaCloudBoxActivity = (WanaCloudBox) activity;
            switch (action) {
                case "show":
                    wanaCloudBoxActivity.showBuyBtn("show");
                    break;
                case "hide":
                    wanaCloudBoxActivity.showBuyBtn("hide");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBackBtn(AppCompatActivity activity, WebView webView, JSONObject params, final JsCallback callback) {
        try {
            String action = params.getString("action");
            WanaCloudBox wanaCloudBoxActivity = (WanaCloudBox) activity;
            switch (action) {
                case "show":
                    wanaCloudBoxActivity.showBackBtn("show");
                    break;
                case "hide":
                    wanaCloudBoxActivity.showBackBtn("hide");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
