package com.cloudmachine.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.SaveArrivalNoticeAsync;
import com.cloudmachine.net.task.YunBoxStoreAsync;
import com.cloudmachine.struc.YunBoxStoreVolumeInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MutableHeightViewWrapper;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.WeChatShareUtil;
import com.jsbridge.JsBridgeClient;
import com.jsbridge.core.JsBridgeManager;

/**
 * 项目名称：CloudMachine
 * 类描述：我要云盒子,购买云盒子页面
 * 创建人：shixionglu
 * 创建时间：2017/2/5 下午2:31
 * 修改人：shixionglu
 * 修改时间：2017/2/5 下午2:31
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class WanaCloudBox extends BaseAutoLayoutActivity implements View.OnClickListener, Handler.Callback {

    private Button      mBtnBuyNow;
    private WebView     wvWanaBox;
    private ProgressBar progressBar;
    private Context     mContext;
    private String URLString = "http://h5.cloudm.com/box_detail";
    private TitleView titleLayout;
    private ImageView ivBack;
    private Handler   mHandler;
    private ImageView ivShareBox;
    private long    store_volume = -1;
    private boolean result       = false;
    private JsBridgeManager jsBridgeManager;
    private WeChatShareUtil weChatShareUtil;
    private String proId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wana_cloudbox);
        mContext = this;
        mHandler = new Handler(this);
        new YunBoxStoreAsync(mContext, mHandler).execute();
        initJsBridgeManager();
        initTitleView();
        initView();
        initWebView();
    }

    @Override
    public void initPresenter() {

    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    private void initJsBridgeManager() {

        jsBridgeManager = JsBridgeClient.getJsBridgeManager(WanaCloudBox.this);
    }

    private void initTitleView() {

        titleLayout = (TitleView) findViewById(R.id.title_layout);
        titleLayout.setTitle("云盒子");
        titleLayout.setLeftImage(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call = "javascript:h5_boxDetail_backMainPage()";
                wvWanaBox.loadUrl(call);
            }
        });
    }

    private void initWebView() {
        wvWanaBox = (WebView) findViewById(R.id.wana_box);

        // 设置可以自动加载图片
        wvWanaBox.getSettings().setLoadsImagesAutomatically(true);
        wvWanaBox.getSettings().setBuiltInZoomControls(true);
        wvWanaBox.getSettings().setJavaScriptEnabled(true);
        wvWanaBox.getSettings().setUseWideViewPort(true);
        wvWanaBox.getSettings().setLoadWithOverviewMode(true);
        wvWanaBox.getSettings().setDefaultTextEncodingName("utf-8");
        wvWanaBox.getSettings().setGeolocationEnabled(true);
        wvWanaBox.getSettings().setDomStorageEnabled(true);
        wvWanaBox.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvWanaBox.setWebChromeClient(new MyWebChromeClient());

        if (Build.VERSION.SDK_INT >= 8) {
            wvWanaBox.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
        }
        wvWanaBox.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
               return jsBridgeManager.invokeNative(view, url);
            }

        });
        wvWanaBox.loadUrl(URLString);
    }

    private void initView() {
        //立即购买
        mBtnBuyNow = (Button) findViewById(R.id.buy_now);
        mBtnBuyNow.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.webview_progressbar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ivShareBox = (ImageView) findViewById(R.id.iv_sharebox);
        ivShareBox.setOnClickListener(this);
        weChatShareUtil = WeChatShareUtil.getInstance(WanaCloudBox.this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new MutableHeightViewWrapper(titleLayout), "height", dip2px(48), 0);
                objectAnimator.setDuration(300);
                objectAnimator.start();
            }
        }, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_now:
                if (store_volume > 0 && result) {
                    Constants.toActivity(WanaCloudBox.this, ConfirmInformationActivity.class, null, false);
                } else {
                    if (null != proId) {
                        new SaveArrivalNoticeAsync(mHandler, mContext, proId).execute();
                    }
                    return;
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_sharebox:
                ShareDialog shareDialog = new ShareDialog(WanaCloudBox.this, null, "智能云盒子", "让您的设备与您共享云端服务", null);
                shareDialog.show();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.HANDLER_YUNBOXSTOREVOLUME_SUCCESS:
                YunBoxStoreVolumeInfo data = (YunBoxStoreVolumeInfo) msg.obj;
                store_volume = data.getStore_volume();
                result = data.isResult();
                if (store_volume > 0 && result) {
                    Constants.MyLog("有库存");
                    mBtnBuyNow.setText("立即购买");
                } else {
                    mBtnBuyNow.setText("到货通知");
                }
                break;
            case Constants.HANDLER_YUNBOXSTOREVOLUME_FAILD:
                Constants.MyToast((String) msg.obj);
                break;
            case Constants.HANDLER_SAVEARRIVALNOTICE_SUCCESS:
               Constants.MyLog((String) msg.obj);
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_SAVEARRIVALNOTICE_FAILD:
                Constants.MyLog((String) msg.obj);
                Constants.ToastAction((String) msg.obj);
                break;
        }

        return false;
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

    public void showBuyBtn(String params) {
        if (params.equals("show")) {
            //Constants.ToastAction("显示购买按钮成功");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator showBuyBtnAnimator = ObjectAnimator.ofInt(new MutableHeightViewWrapper(mBtnBuyNow), "height", 0, dip2px(42));
                    showBuyBtnAnimator.setDuration(300);
                    showBuyBtnAnimator.start();
                }
            }, 0);
        } else if (params.equals("hide")) {
          //  Constants.ToastAction("隐藏购买按钮成功");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new MutableHeightViewWrapper(mBtnBuyNow), "height", dip2px(42), 0);
                    objectAnimator.setDuration(300);
                    objectAnimator.start();
                }
            }, 0);
        }
    }
    //返回按钮,标题栏控制
    public void showBackBtn(String params) {
        if (params.equals("show")) {
            //Constants.MyToast("显示返回按钮成功,标题栏隐藏");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivBack.setVisibility(View.VISIBLE);
                    ivShareBox.setVisibility(View.VISIBLE);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new MutableHeightViewWrapper(titleLayout), "height", dip2px(48), 0);
                    objectAnimator.setDuration(300);
                    objectAnimator.start();
                }
            }, 0);

        } else if (params.equals("hide")) {
           // Constants.MyToast("隐藏返回按钮成功,标题栏显示");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivBack.setVisibility(View.GONE);
                    ivShareBox.setVisibility(View.GONE);
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new MutableHeightViewWrapper(titleLayout), "height",0,dip2px(48));
                    objectAnimator.setDuration(300);
                    objectAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            titleLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    objectAnimator.start();
                }
            }, 0);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void show() {
        Constants.ToastAction("拿到id");
    }

}
