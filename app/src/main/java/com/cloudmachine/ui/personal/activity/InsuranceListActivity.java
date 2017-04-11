package com.cloudmachine.ui.personal.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
import com.cloudmachine.utils.MemeberKeeper;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cloudmachine.R.id.webview;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/11 下午1:51
 * 修改人：shixionglu
 * 修改时间：2017/4/11 下午1:51
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class InsuranceListActivity extends BaseAutoLayoutActivity {

    @BindView(R.id.title_layout)
    TitleView   mTitleLayout;
    @BindView(webview)
    WebView     mWebview;
    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;
    private Context mContext;
    private String URLString = "http://h5.test.cloudm.com/InsuranceList";
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurancelist);
        ButterKnife.bind(this);
        mContext = this;
        if (MemeberKeeper.getOauth(mContext) != null) {
            userID = String.valueOf(MemeberKeeper.getOauth(mContext).getId());
        }
        URLString = "http://h5.test.cloudm.com/InsuranceList?userID=" + userID;
        initTitle();
        initView();
        initWebView();
    }

    private void initView() {


    }

    private void initTitle() {

        mTitleLayout.setTitle("保险咨询");
        mTitleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void initPresenter() {

    }

    private void initWebView(){
        // 设置可以自动加载图片
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setDefaultTextEncodingName("utf-8");
        mWebview.getSettings().setGeolocationEnabled(true);
        mWebview.getSettings().setDomStorageEnabled(true);
        //优先使用缓存：
        //		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存：
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //		webview.clearHistory();
        //		webview.clearFormData();
        //		webview.clearCache(true);
        mWebview.setWebChromeClient(new MyWebChromeClient());

        if (Build.VERSION.SDK_INT >= 8) {
            mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            // webview.getSettings().setPluginsEnabled(true);
        }
        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //				if(urlFilter.filtrate(url)){
                //					return true;
                //				}
                //				url = addToken(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        Constants.MyLog("执行到这里了么？？？");
        Constants.MyLog(URLString);
        mWebview.loadUrl(URLString);
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(mContext)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    result.cancel();
                                }
                            })
                    .create()
                    .show();

            return true;
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       android.webkit.GeolocationPermissions.Callback callback) {
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
}
