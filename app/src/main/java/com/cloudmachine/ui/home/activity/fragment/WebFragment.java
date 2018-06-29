package com.cloudmachine.ui.home.activity.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.activity.HomeActivity;
import com.cloudmachine.ui.home.model.JsInteface;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.VersionU;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Created by xiaojw on 2017/12/19.
 */

public class WebFragment extends BaseFragment {
    String mUrl;
    boolean isClickCamera;
    WebView mWebView;
    ProgressBar mProgressBar;
    String mAlertType;
    private static final int FLUSH_PAGE = 0x1330;
    private static final int REQUEST_PERMISSION = 0x004;  //权限请求
    private static final int REQUEST_PERMISSION_PICK = 0x005;  //权限请求
    private static final int REQUEST_PICK_IMAGE = 0x001; //相册选取
    private static final int REQUEST_CAPTURE = 0x002;  //拍照
    private static final String JS_INTERFACE_NAME = "webkit";
    private static final String[] UPLOAD_PIC_ITEMS = new String[]{"选择本地图片", "拍照"};
    private PermissionsChecker mPermissionsCheck;
    private Uri imageUri;
    public static final String PARAMS_KEY_MEMBERID = "memberId";

    public WebFragment(String url) {
        mUrl = url;
    }

    @Override
    protected void initView() {
        mWebView = (WebView) viewParent.findViewById(R.id.framgnet_h5_wv);
        mProgressBar = (ProgressBar) viewParent.findViewById(R.id.framgnet_h5_pb);
        mPermissionsCheck = new PermissionsChecker(getActivity());
        WebSettings settings = mWebView.getSettings();
        settings.setLoadsImagesAutomatically(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebViewClient(new H5WebClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.addJavascriptInterface(new JsInteface(getActivity(), mHandler), JS_INTERFACE_NAME);
        loadUrl();
    }
    private class H5WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }


    public void loadUrl(String loadUrl) {
        mUrl = loadUrl;
        if (UserHelper.isLogin(getActivity())) {
            fillParams(PARAMS_KEY_MEMBERID, UserHelper.getMemberId(getActivity()));
        }
        loadWebUrl();
    }

    private void loadWebUrl() {
        mWebView.loadUrl(CommonUtils.fillParams(mUrl, QuestionCommunityActivity.PARAMS_KEY_VERSION, VersionU.getVersionName()));
    }

    public void loadUrl() {
        if (UserHelper.isLogin(getActivity())) {
            fillParams(PARAMS_KEY_MEMBERID, UserHelper.getMemberId(getActivity()));
        }
        loadWebUrl();
    }


    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    static final String[] PERMISSIONS_PICK = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private void showUpLoadImgDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("上传照片")
                .setItems(UPLOAD_PIC_ITEMS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS_PICK)) {
                                        requestPermissions(PERMISSIONS_PICK, REQUEST_PERMISSION_PICK);
                                    } else {
                                        ((HomeActivity) getActivity()).isForbidenAd = true;
                                        startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                                REQUEST_PICK_IMAGE);
                                    }
                                } else {
                                    ((HomeActivity) getActivity()).isForbidenAd = true;
                                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                            REQUEST_PICK_IMAGE);
                                }
                                isClickCamera = false;
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS)) {
                                        requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
                                    } else {
                                        openCamera();
                                    }
                                } else {
                                    openCamera();
                                }
                                isClickCamera = true;
                                break;
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_PICK) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ((HomeActivity) getActivity()).isForbidenAd = true;
                startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                        REQUEST_PICK_IMAGE);
            } else {
                CommonUtils.showPermissionDialog(getActivity(), Constants.PermissionType.STORAGE);
            }
        } else if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isClickCamera) {
                    openCamera();
                } else {
                    selectFromAlbum();
                }
            } else {
                CommonUtils.showPermissionDialog(getActivity(), Constants.PermissionType.STORAGE);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        ((HomeActivity) getActivity()).isForbidenAd = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }


    //打开系统相机
    private void openCamera() {
        ((HomeActivity) getActivity()).isForbidenAd = true;
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.cloudmachine.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.HANDLER_ALIPAY_RESULT:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付成功", Toast.LENGTH_SHORT).show();
                        sendEmptyMessage(Constants.HANDLER_JUMP_MY_ORDER);
                    } else {
                        Constants.callJsMethod(mWebView, "callbackDSFPayResultCancelOrError()");
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getActivity(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.HANDLER_JUMP_MY_ORDER:
                    Constants.callJsMethod(mWebView, "callbackDSFPayResultSuccess()");
                    break;

                case Constants.HANDLER_JS_ALERT:
                    JSONObject job = (JSONObject) msg.obj;
                    mAlertType = job.optString(JsInteface.ALERT_TYPE);
                    if (JsInteface.UPLOAD_IMG.equals(mAlertType) || JsInteface.UPLOAD_QINIU_IMG.equals(mAlertType)) {
                        showUpLoadImgDialog();
                    } else {
                        String tips = job.optString(JsInteface.ALERT_TIPS);
                        String alert_event = job.optString(JsInteface.ALERT_EVENT);
                        boolean isConfirm = false;
                        String alertImg = job.optString(JsInteface.ALERT_IMAGE);
                        if (JsInteface.ALERT_IMAGE_CONFIRM.equals(alertImg)) {
                            isConfirm = true;
                        }
                        shoWAlertDialog(isConfirm, tips, alert_event);
                    }
                    break;
                case Constants.HANDLER_UPLOAD_SUCCESS:
                    String dataStr = (String) msg.obj;
                    Constants.callJsMethod(mWebView, "backLocalImgInfo('" + dataStr + "')");
                    break;
                case Constants.HANDLER_REPAIR:
                    if (UserHelper.isLogin(getActivity())) {
                        Constants.toActivity(getActivity(), NewRepairActivity.class, null);
                    } else {
                        Constants.toActivityForR(getActivity(), LoginActivity.class, null);
                    }
                    break;
                case Constants.HANDLER_JS_JUMP:
                    String methodStr = (String) msg.obj;
                    if (JsInteface.Go_Login_Page.equals(methodStr)) {
                        Intent loginIntent=new Intent(getActivity(),LoginActivity.class);
                        loginIntent.putExtra("flag", 4);
                        startActivityForResult(loginIntent,FLUSH_PAGE);
                    }
                    break;
            }
        }
    };

    public void shoWAlertDialog(boolean isConfirm, String message, final String alertEvent) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        if (isConfirm) {
            builder.setAlertIcon(R.drawable.icon_sucess);
        }
        builder.setMessage(message);
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        CustomDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Constants.callJsMethod(mWebView, alertEvent);
            }
        });
        dialog.show();
    }


    //补充URL参数
    public void fillParams(String key, Long value) {
        if (!mUrl.contains(key)) {
            if (mUrl.contains("?")) {
                mUrl += "&";
            } else {
                mUrl += "?";
            }
            mUrl += key + "=" + value;
        }
    }


    @Override
    protected void initPresenter() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_h5;
    }

    private final class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       android.webkit.GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(0);
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FLUSH_PAGE:
                loadUrl();
                break;
        }

    }
}

