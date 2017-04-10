package com.cloudmachine.ui.question.activity;

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
import android.widget.ProgressBar;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.ui.question.contract.QuestionContract;
import com.cloudmachine.ui.question.model.QuestionModel;
import com.cloudmachine.ui.question.presenter.QuestionPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/28 下午4:54
 * 修改人：shixionglu
 * 修改时间：2017/3/28 下午4:54
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class QuestionActivity extends BaseAutoLayoutActivity<QuestionPresenter, QuestionModel> implements QuestionContract.View {

    @BindView(R.id.wv_questions)
    WebView     mWvQuestions;
    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;

    private Context mContext;
    private String URLString = "http://h5.test.cloudm.com/n/ask_qsubmit?";
    private long mMyid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        initWebView();
    }

    private void initView() {

        this.mContext = this;
        if (MemeberKeeper.getOauth(this).getWjdsId() != null) {
            mMyid = MemeberKeeper.getOauth(mContext).getWjdsId();
            Constants.MyLog("传到跳转页面的id"+mMyid);
            Constants.MyLog("主键id" + MemeberKeeper.getOauth(this).getId());
        }
    }

    private void getIntentData() {

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (MemeberKeeper.getOauth(this).getWjdsId() != null) {
            mMyid = MemeberKeeper.getOauth(this).getWjdsId();
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    private void initWebView(){
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
                // TODO Auto-generated method stub
                //				if(urlFilter.filtrate(url)){
                //					return true;
                //				}
                //				url = addToken(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        mWvQuestions.loadUrl(URLString+"myid="+mMyid);

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
