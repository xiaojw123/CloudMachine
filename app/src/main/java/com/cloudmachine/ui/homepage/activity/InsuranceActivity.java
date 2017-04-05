package com.cloudmachine.ui.homepage.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
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
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.jsbridge.JsBridgeClient;
import com.jsbridge.core.JsBridgeManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/29 上午8:07
 * 修改人：shixionglu
 * 修改时间：2017/3/29 上午8:07
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class InsuranceActivity extends BaseAutoLayoutActivity {


    @BindView(R.id.title_layout)
    TitleView   mTitleLayout;
    @BindView(R.id.m_webview)
    WebView     mMWebview;
    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;
    private Context         mContext;
    private JsBridgeManager jsBridgeManager;
    private String URLString = "http://h5.test.cloudm.com/Insurance";
    /*http://h5.test.cloudm.com/Insurance*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        ButterKnife.bind(this);
        initJsBridgeManager();
        initWebView();
        initTitleView();
        initView();

    }

    private void initWebView() {
        // 设置可以自动加载图片
        mMWebview.getSettings().setLoadsImagesAutomatically(true);
        mMWebview.getSettings().setBuiltInZoomControls(true);
        mMWebview.getSettings().setJavaScriptEnabled(true);
        mMWebview.getSettings().setUseWideViewPort(true);
        mMWebview.getSettings().setLoadWithOverviewMode(true);
        mMWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mMWebview.getSettings().setGeolocationEnabled(true);
        mMWebview.getSettings().setDomStorageEnabled(true);
        mMWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mMWebview.setWebChromeClient(new MyWebChromeClient());

        if (Build.VERSION.SDK_INT >= 8) {
            mMWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
        }
        mMWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return jsBridgeManager.invokeNative(view, url);
            }

        });
        mMWebview.loadUrl(URLString);

    }

    private void initTitleView() {

        mTitleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = "javascript:prev()";
                mMWebview.loadUrl(call);
               /* if (mMWebview.canGoBack()) {
                    mMWebview.goBack();
                } else {
                    finish();
                }*/

            }
        });
    }

    private void initJsBridgeManager() {

        jsBridgeManager = JsBridgeClient.getJsBridgeManager(InsuranceActivity.this);
    }

    private void initView() {

        mContext = this;

    }

    @Override
    public void initPresenter() {

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

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void backPage(String params) {
        if (params.equals("back")) {
            Constants.MyLog("当前所在的线程"+Thread.currentThread().getName());
            finish();
        }
    }
}
