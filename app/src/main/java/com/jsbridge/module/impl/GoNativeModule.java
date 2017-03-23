package com.jsbridge.module.impl;

import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jsbridge.core.JsCallback;
import com.jsbridge.module.IModule;

import org.json.JSONException;
import org.json.JSONObject;

public class GoNativeModule implements IModule {

    public static String tag = "GoNativeModule";

    public static void goNative(AppCompatActivity activity,WebView webView, JSONObject params, final JsCallback callback) {

        try {
            String page = params.getString("page");
            switch (page) {
                case "goodDetail":
                    break;
                case "orderReport":
                    break;
                case "shopCar":
                    break;
                case "QYSession":
                    break;
                case "column":
//                    String columnUrl = params.getString("url");
//                    ColumnDetailActivity.goColumnDetailActivity(activity,columnUrl);
                    break;
                case "article":
                    break;
                case "login":
//                    LoginFragment loginFragment = FragmentFactory.getFragmentFactory().getLoginFragment();
//                    loginFragment.setOnLoginListener(new LoginFragment.OnLoginListener() {
//                        @Override
//                        public void doInSuccess(BabyInfo babyInfo) {
//                            long userId = MyApplication.getUserBean().getUserId();
//                            String accessToken = MyApplication.getUserBean().getAccessToken();
//                            JSONObject jsonObject = new JSONObject();
//                            try {
//                                jsonObject.put("userId", userId);
//                                jsonObject.put("accessToken", accessToken);
//                                callback.apply(CallbackTemplate.getSuccessJson(jsonObject));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        @Override
//                        public void doInFail() {
//
//                        }
//                    });
//                    loginFragment.show(activity.getSupportFragmentManager(), "login");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
