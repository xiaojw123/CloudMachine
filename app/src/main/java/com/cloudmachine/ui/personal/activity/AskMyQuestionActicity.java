package com.cloudmachine.ui.personal.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.jsbridge.JsBridgeClient;
import com.jsbridge.core.JsBridgeManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 下午1:48
 * 修改人：shixionglu
 * 修改时间：2017/4/12 下午1:48
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class AskMyQuestionActicity extends BaseAutoLayoutActivity {


    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;
    @BindView(R.id.wv_questions)
    WebView     mWvQuestions;
    private Context         mContext;
    private String          URLString;
    private Long            mMyid;
    private JsBridgeManager jsBridgeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askmyquestion);
        ButterKnife.bind(this);
        initJsBridgeManager();
        getIntentData();
        initView();
        initWebView();

    }


    private void getIntentData() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        if (!TextUtils.isEmpty(url)) {
            URLString = url;
        }
    }

    private void initJsBridgeManager() {
        jsBridgeManager = JsBridgeClient.getJsBridgeManager(AskMyQuestionActicity.this);
    }

    private void initView() {
        mContext = this;
        if (MemeberKeeper.getOauth(this) != null) {
            if (MemeberKeeper.getOauth(mContext).getWjdsId() != null) {
                mMyid = MemeberKeeper.getOauth(mContext).getWjdsId();
            } /*else {
                Constants.toLoginActivity(QuestionCommunityActivity.this, 2);
            }*/
        }
    }

    private void initWebView() {
        // 设置可以自动加载图片
        mWvQuestions.getSettings().setLoadsImagesAutomatically(true);
        mWvQuestions.getSettings().setBuiltInZoomControls(true);
        mWvQuestions.getSettings().setJavaScriptEnabled(true);
        mWvQuestions.getSettings().setUseWideViewPort(true);
        mWvQuestions.getSettings().setLoadWithOverviewMode(true);
        mWvQuestions.getSettings().setDefaultTextEncodingName("utf-8");
        mWvQuestions.getSettings().setGeolocationEnabled(true);
        mWvQuestions.getSettings().setDomStorageEnabled(true);
        //优先使用缓存：
        //		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
        mWvQuestions.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //		webview.clearHistory();
        //		webview.clearFormData();
        //		webview.clearCache(true);
        mWvQuestions.setWebChromeClient(new MyWebChromeClient());

        if (Build.VERSION.SDK_INT >= 8) {
            mWvQuestions.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            // webview.getSettings().setPluginsEnabled(true);
        }
        mWvQuestions.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return jsBridgeManager.invokeNative(view, url);
            }

        });
        if (mMyid != null) {
            mWvQuestions.loadUrl(URLString + "?myid=" + mMyid);
            Constants.MyLog("AskMyQuestionActicity链接" + URLString + "?myid=" + mMyid);
        } else {
            mWvQuestions.loadUrl(URLString);
        }


    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(mContext)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.cancel();
                                }
                            })
                    .create()
                    .show();

            return true;
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
            super.onProgressChanged(view, newProgress);
            mWebviewProgressbar.setVisibility(View.VISIBLE);
            mWebviewProgressbar.setProgress(0);
            mWebviewProgressbar.setProgress(newProgress);
            if (newProgress == 100) {
                mWebviewProgressbar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        removeCookie(this);
        super.onDestroy();
    }

    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }


    @Override
    public void initPresenter() {

    }
}
