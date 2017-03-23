package com.cloudmachine.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;

@SuppressLint("SetJavaScriptEnabled") 
public class WebviewActivity extends BaseAutoLayoutActivity implements
		OnClickListener {

	private Context mContext;
	private WebView webview;
	private ProgressBar progressBar;
	private String URLString = "http://www.cloudm.com";
	private String title = "";
	private TitleView title_layout_about;

	/**
	 * android:scrollbars="none" 不设置滚动条
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		this.mContext = this;
		getIntentData();
		initTitle();
		initView();
		initWebView();
		
	}

	@Override
	public void initPresenter() {

	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		URLString = bundle.getString(Constants.P_WebView_Url);
        		title = bundle.getString(Constants.P_WebView_Title);
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }
	private void initTitle(){
		title_layout_about = (TitleView)findViewById(R.id.title_layout_about);
		//title_layout_about.setTitle("我要云盒子");
		title_layout_about.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != webview && webview.canGoBack()) {
					webview.goBack();
				}else{
					finish();
				}
			}

		});
	}
	
	private void initView(){
		progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
	}
	
	private void initWebView(){
		this.webview = (WebView) findViewById(R.id.test_webview);
		// 设置可以自动加载图片
		webview.getSettings().setLoadsImagesAutomatically(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		webview.getSettings().setGeolocationEnabled(true); 
		webview.getSettings().setDomStorageEnabled(true);
		//优先使用缓存：
//		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); 
		//不使用缓存：
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//		webview.clearHistory();
//		webview.clearFormData();
//		webview.clearCache(true);
		webview.setWebChromeClient(new MyWebChromeClient());

		if (Build.VERSION.SDK_INT >= 8) {
			webview.getSettings().setPluginState(PluginState.ON);
		} else {
			// webview.getSettings().setPluginsEnabled(true);
		}
		webview.setWebViewClient(new WebViewClient() {
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
		webview.loadUrl(URLString);
		
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
		progressBar.setVisibility(View.VISIBLE);
		progressBar.setProgress(0);
		progressBar.setProgress(newProgress);
		if (newProgress == 100) {
			progressBar.setVisibility(View.GONE);
		}
	}
 	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		default:
			break;
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
	/*private void cleanData(Context context){
		File file = CacheManager.getCacheFileBaseDir(); 
		   if (file != null && file.exists() && file.isDirectory()) { 
		    for (File item : file.listFiles()) { 
		     item.delete(); 
		    } 
		    file.delete(); 
		   } 
		  context.deleteDatabase("webview.db"); 
		  context.deleteDatabase("webviewCache.db");
	}*/

}
