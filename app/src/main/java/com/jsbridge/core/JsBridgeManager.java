package com.jsbridge.core;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.webkit.WebView;

import com.cloudmachine.ui.homepage.activity.InsuranceActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.github.mikephil.charting.utils.AppLog;
import com.jsbridge.bean.JsRequestBean;
import com.jsbridge.module.IModule;
import com.jsbridge.parse.CommonParse;
import com.jsbridge.utils.JsBridgeConfig;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Description: JsBridge管理类,负责调用JsBridge
 * Copyright  : Copyright (c) 2016
 */
public class JsBridgeManager {
    private String mUrl;
    private JsBridge jsBridge;
    /**
     * 注册的方法集合,key为action,value为具体的class和method。
     */
    public static Map<String, Map<Class<? extends IModule>, String>> registerMethods = new HashMap<>();
    private final ExecutorService executorService;  // 执行Native线程池
    public static ArrayList<String> actionNames = new ArrayList<String>();
    private Activity activity;


    public JsBridgeManager(Activity activity) {
        jsBridge = new JsBridge();
        this.activity = activity;
        executorService = Executors.newCachedThreadPool();
    }

    /**
     * @param action      h5动作名
     * @param ModuleClass Native接口所在的类
     * @param methodName  Native接口名称
     * @desc: register Native Method,只有register的Native Method才可以被调用
     */
    public  void registerMethod(String action, Class<? extends IModule> ModuleClass, String methodName) {
        Constants.MyLog("方法没有执行");
        AppLog.print("registerMethod___");
        // 注册信息的合法性校验
        if (TextUtils.isEmpty(action) && ModuleClass == null && TextUtils.isEmpty(methodName)) {
            return;
        }
        Constants.MyLog("注册信息基础检测通过");
        if (!registerMethods.containsKey(action)) {
            // 判断方法是否在对应的Module类中
            Method[] methods = ModuleClass.getMethods();
            for (Method method : methods) {
                if (methodName.equals(method.getName())) {
                    // 方法遵循的规则一:
                    if (method.getModifiers() != (Modifier.PUBLIC | Modifier.STATIC) || method.getName() == null) {
                        continue;
                    }
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    // 方法遵循的规则二:
                    if (parameterTypes != null && parameterTypes.length == 4) {
                        // 方法遵循的规则三:
                        if (parameterTypes[0] == Activity.class && parameterTypes[1] == WebView.class && parameterTypes[2] == JSONObject.class && parameterTypes[3] == JsCallback.class) {
                            Map<Class<? extends IModule>, String> map = new HashMap<>();
                            map.put(ModuleClass, methodName);
                            // 注册方法
                            AppLog.print("putAction___action="+action);
                            registerMethods.put(action, map);
                        }
                    }
                }
            }
        }
    }


    /**
     * @desc: 验证js发出的请求, 判断请求的action是否已经注册, 调用JsBridge的接口执行Native方法
     * @returnType Boolean js格式验证是否通过
     */
    public boolean invokeNative(WebView webView, String url) {
        Constants.MyLog("进入拦截协议第1步url__" + url);
        AppLog.print("invokeNative___");
        // 是否是指定的协议
        if (!url.startsWith("cloudm://")) {
            Constants.MyLog("进入拦截协议第1步url__" + url.startsWith("tel:"));
            if (url.startsWith("tel:")) {
                mUrl = url;
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                String telNUM = url.split(":")[1];
                builder.setMessage(telNUM);
                builder.setCancelable(false);
                builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, InsuranceActivity.REQUEST_PERMISSION_CALL);
                            } else {
                                callTelNum();
                            }
                        } else {
                            callTelNum();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
                return true;

            }
            return false;
        }
        Constants.MyLog("进入拦截协议第2步");
        // 参数是否合法
        if (webView == null || TextUtils.isEmpty(url)) {
            return false;
        }
        Constants.MyLog("进入拦截协议第3步");
        // 是否符合商定的协议
        if (!checkUrl(url)) {
            return false;
        }
        // action是否注册
        Uri uriRequest = Uri.parse(url);
        String action = uriRequest.getHost();
        AppLog.print("action=="+action+"   action is container__" + registerMethods.containsKey(action));
        if (!registerMethods.containsKey(action)) {
            return false;
        }
        Constants.MyLog("进入拦截协议第5步");
        // 获取注册信息,key是类名,value是方法名
        Map<Class<? extends IModule>, String> registInfo = getResiterInfo(action);
        // action没有对应的注册信息,直接返回
        if (registInfo != null) {
            // 从注册信息,获取class & method
            Set<Map.Entry<Class<? extends IModule>, String>> classAndMethods = registInfo.entrySet();
            if (classAndMethods.size() != 1) {
            }
            for (Map.Entry<Class<? extends IModule>, String> entry : classAndMethods) {
                Class<? extends IModule> targetClass = entry.getKey();
                String targetMethod = entry.getValue();
                // 封装并调用
                if (targetClass != null && targetMethod != null) {

                    // 生成可执行对象,解析参数
                    // 生成可执行对象
                    JsRequestBean jsRequestBean = new JsRequestBean();
                    // 解析Params和callback
                    parseJsRequestUri(uriRequest, jsRequestBean);
                    jsRequestBean.action = action;
                    jsRequestBean.clazz = targetClass.getName();
                    jsRequestBean.method = targetMethod;
                    AppLog.print("webview jsRequst__" + jsRequestBean);
                    // 异步调用Native接口
                    InvokeNative invokeNative = new InvokeNative(webView, jsRequestBean);
                    invokeNative.invoke();
                    return true;
                }
            }

        }
        Constants.MyLog("进入拦截协议第6步");
        return false;
    }

    public void callTelNum() {
        AppLog.print("callTelNum___" + mUrl);
        if (mUrl == null) {
            ToastUtils.warning("为获取到联系方式", true);
            return;
        }
        //tel:13333333
        AppLog.print("开始打电话" + mUrl);
        Uri uri = Uri.parse(mUrl);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        activity.startActivity(intent);


    }

    /**
     * 解析js命令,获取以下二个参数
     * 1. params
     * 2. callbackId
     *
     * @param uriRequest
     * @return
     */
    public JsRequestBean parseJsRequestUri(Uri uriRequest, JsRequestBean jsRequestBean) {
        L.d("action", "要解析的js命令为:" + uriRequest.toString());
        return CommonParse.parseBasicParams(uriRequest, jsRequestBean);
    }

    /**
     * Description: 异步执行单元
     * Copyright  : Copyright (c) 2016
     */
    private class InvokeNative {

        private WebView webView;
        private JsRequestBean requestBean;

        public InvokeNative(WebView webView, JsRequestBean requestBean) {
            this.webView = webView;
            this.requestBean = requestBean;
        }

        public void invoke() {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsBridge.jsCallJava(activity, webView, requestBean);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    /**
     * @param url js请求
     * @throws
     * @desc: 验证js请求url, 是否符合商定的协议规则
     * @methodName: checkUrl
     * @returnType Boolean true:验证成功 false:验证失败
     */
    public boolean checkUrl(String url) {
        if (url.startsWith(JsBridgeConfig.NGMM_SCHEMA)) {
            return true;
        }
        return false;
    }

    /**
     * @param action 注册key
     * @throws
     * @desc: 从已注册集合中获取注册信息(value)
     * @methodName: getResiterInfo
     * @returnType
     */
    public Map<Class<? extends IModule>, String> getResiterInfo(String action) {
        return registerMethods.get(action);
    }

}
