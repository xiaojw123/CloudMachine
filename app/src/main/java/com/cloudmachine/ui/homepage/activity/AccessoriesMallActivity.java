package com.cloudmachine.ui.homepage.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cloudmachine.R.id.webview;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/12 上午9:57
 * 修改人：shixionglu
 * 修改时间：2017/4/12 上午9:57
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class AccessoriesMallActivity extends BaseAutoLayoutActivity {


    @BindView(R.id.title_name)
    TextView    mTitleName;
    @BindView(R.id.title_left_button)
    ImageView   mTitleLeftButton;
    @BindView(R.id.finish)
    ImageView   mFinish;
    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;
    @BindView(webview)
    WebView     mWebview;

    private Context mContext;
    private String URLString = "https://wap.youzan.com/v2/showcase/homepage?alias=g2vu0cez&sf=wx_menu&redirect_count=2";
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessoriesmall);
        ButterKnife.bind(this);

        getIntentData();
        initTitle();
        //initView();
        initWebView();

    }

    private void initTitle() {
        //
        mTitleLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview.canGoBack()) {
                    mWebview.goBack();
                }
            }
        });

        mFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                URLString = bundle.getString(Constants.P_WebView_Url);
                title = bundle.getString(Constants.P_WebView_Title);
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }
        }
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
