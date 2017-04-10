package com.jsbridge;

import android.support.v7.app.AppCompatActivity;

import com.jsbridge.core.JsBridgeManager;
import com.jsbridge.module.impl.DeliverMethodModel;
import com.jsbridge.module.impl.UIInteractiveModel;

/**
 * Description: 向外暴露的JsBridge模块
 * Copyright  : Copyright (c) 2016
 */
public class JsBridgeClient {

    private static JsBridgeManager jsBridgeManager;

    public static JsBridgeManager getJsBridgeManager(AppCompatActivity activity) {
        jsBridgeManager = new JsBridgeManager(activity);
        registerNativeMethod();
        return jsBridgeManager;
    }

    /**
     * @desc: 注册与Js命令对应的Native方法
     */
    public static void registerNativeMethod() {
        // 注册大model。model
        //        jsBridgeManager.registerMethod("goNative", GoNativeModule.class, "goNative");
        jsBridgeManager.registerMethod("showBuyBtn", UIInteractiveModel.class, "showBuyBtn");
        jsBridgeManager.registerMethod("showBackBtn", UIInteractiveModel.class, "showBackBtn");
        jsBridgeManager.registerMethod("deliverProId", DeliverMethodModel.class, "deliverProId");
        jsBridgeManager.registerMethod("backPage", UIInteractiveModel.class, "backPage");
        jsBridgeManager.registerMethod("goLoginPage",UIInteractiveModel.class,"goLoginPage");
    }
}
