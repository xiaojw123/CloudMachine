package com.cloudmachine.ui.homepage.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.alipay.PayResult;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ResidentAddressInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.ApiConstants;
import com.cloudmachine.ui.home.model.JsBody;
import com.cloudmachine.ui.home.model.JsInteface;
import com.cloudmachine.ui.homepage.contract.QuestionCommunityContract;
import com.cloudmachine.ui.homepage.model.QuestionCommunityModel;
import com.cloudmachine.ui.homepage.presenter.QuestionCommunityPresenter;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.UploadPhotoUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 项目名称：CloudMachine
 * 类描述：问答社区
 * 创建人：shixionglu
 * 创建时间：2017/4/10 下午7:14
 * 修改人：shixionglu
 * 修改时间：2017/4/10 下午7:14
 * 修改备注：
 */
@SuppressLint("SetJavaScriptEnabled")
public class QuestionCommunityActivity extends BaseAutoLayoutActivity<QuestionCommunityPresenter, QuestionCommunityModel>
        implements QuestionCommunityContract.View {
    private static final int REQUEST_PICK_IMAGE = 0x001; //相册选取
    private static final int REQUEST_CAPTURE = 0x002;  //拍照
    private static final int REQUEST_PICTURE_CUT = 0x003;  //剪裁图片
    private static final int REQUEST_PERMISSION = 0x004;  //权限请求
    private static final int REQUEST_PERMISSION_PICK = 0x005;  //权限请求
    private static final int FLUSH_PAGE = 0x1330;
    private static final String PARAMS_KEY_MYID = "myid";
    private static final String PARAMS_KEY_MEMBERID = "memberId";
    private static final String JS_INTERFACE_NAME = "webkit";
    private static final String SHARE = "分享";
    private static final String SHAREDESC = "内容源自云机械APP";
    private static final String[] UPLOAD_PIC_ITEMS = new String[]{"选择本地图片", "拍照"};
    public static final String H5_URL = "h5_url";
    public static final String H5_TITLE = "h5_title";
    public static final String SHARE_LINK = "share_link";
    public static final String SHARE_PIC = "share_pic";
    public static final String GO_TO_MY_ORDER = "gotoMyOrder";
    @BindView(R.id.common_h5_pb)
    ProgressBar mPb;
    @BindView(R.id.common_h5_wv)
    WebView mWebView;
    @BindView(R.id.common_h5_ctv)
    CommonTitleView mWvTitle;
    private PermissionsChecker mPermissionsCheck;
    private Uri imageUri;
    private String mUrl;
    private String mTitle;
    private String imagePath;
    private boolean isClickCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioncommunity);
        ButterKnife.bind(this);
        initParams();
        initView();
        initRxManager();
        MobclickAgent.onEvent(this, MobEvent.TIME_H5_PAGE);
        if (mUrl != null) {
            if (mUrl.equals(ApiConstants.AppCouponHelper)) {
                MobclickAgent.onEvent(this, MobEvent.TIME_H5_COUPON_HELP_PAGE);
            } else {
                if (mUrl.contains("yunbox")) {
                    MobclickAgent.onEvent(this, MobEvent.TIME_H5_YUN_BOX_PAGE);
                }
            }
        }
    }

    private void initRxManager() {
        mRxManager.on(GO_TO_MY_ORDER, new Action1<Object>() {
            @Override
            public void call(Object o) {
                mHandler.sendEmptyMessage(Constants.HANDLER_JUMP_MY_ORDER);
            }
        });

    }


    private void initParams() {
        mPermissionsCheck = new PermissionsChecker(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTitle = bundle.getString(H5_TITLE);
            mUrl = bundle.getString(H5_URL);
            shareLink = bundle.getString(SHARE_LINK);
            sharePic = bundle.getString(SHARE_PIC);
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @SuppressLint("AddJavascriptInterface")
    private void initView() {
        if (!TextUtils.isEmpty(mTitle)) {
            mWvTitle.setTitleName(mTitle);
            mWvTitle.setVisibility(View.VISIBLE);
        }
        initSetting();
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new H5WebClient());
        mWebView.addJavascriptInterface(new JsInteface(this, mHandler), JS_INTERFACE_NAME);
        loadUrl();
        homeUrl = mUrl;
        AppLog.print("Common H5 URL__" + mUrl);
    }


    private void initSetting() {
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
    }

    private class H5WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.print("shoudLoadUrl___" + url + ", homeURL__" + homeUrl);
            if (backUrl == null) {
                backUrl = url;
            }
            if (url.equals(homeUrl) || url.startsWith("cloudm://closeAskPage")) {
                mWvTitle.setCloseVisible(View.GONE);
            } else {
                mWvTitle.setCloseVisible(View.VISIBLE);
            }
            if (!url.startsWith("cloudm://")) {
                mUrl = url;
            }
            return false;
        }
    }

    private String homeUrl;
    private String backUrl;

    public void shoWAlertDialog(String message, final String alertEvent) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(message);
        builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Constants.callJsMethod(mWebView, alertEvent);
                dialog.dismiss();
            }
        });
        builder.create().show();
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
            mPb.setVisibility(View.VISIBLE);
            mPb.setProgress(0);
            mPb.setProgress(newProgress);
            if (newProgress == 100) {
                mPb.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    protected void onDestroy() {
        removeCookie(this);
        super.onDestroy();
    }

    private void removeCookie(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    private void showUpLoadImgDialog() {
        new AlertDialog.Builder(QuestionCommunityActivity.this)
                .setTitle("头像照片")
                .setItems(UPLOAD_PIC_ITEMS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS_PICK)) {
                                        PermissionsActivity.startActivityForResult(QuestionCommunityActivity.this,
                                                REQUEST_PERMISSION_PICK, PERMISSIONS_PICK);
                                    } else {
                                        startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                                REQUEST_PICK_IMAGE);
                                    }
                                } else {
                                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                            REQUEST_PICK_IMAGE);
                                }

                                isClickCamera = false;
                                break;
                            case 1:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS)) {
                                        PermissionsActivity.startActivityForResult(QuestionCommunityActivity.this, REQUEST_PERMISSION,
                                                PERMISSIONS);
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


    //打开系统相机
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.cloudmachine.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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

    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    static final String[] PERMISSIONS_PICK = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_ToSearchActivity:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        ResidentAddressInfo info = (ResidentAddressInfo) bundle.getSerializable(Constants.P_SEARCHINFO);
                        if (info != null) {
                            String locJson = "callbackSelectAddress('{ \"province\": \"" + info.getProvince() + "\",\"location\":{\"longitude\":\"" + info.getLat() + "\",\"latitude\":\"" + info.getLng() + "\"},\"city\": \"" + info.getCity() + "\",\"district\": \"" + info.getDistrict() + "\",\"name\": \"" + info.getPosition() + "\"}')";
                            Constants.callJsMethod(mWebView, locJson);
                        }
                    }
                }

                break;
            case FLUSH_PAGE:
                loadUrl();
                break;
            case REQUEST_PICK_IMAGE://从相册选择
                if (data == null) {
                    return;
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                if (data != null) {
                    Bitmap bitmap;
                    try {
                        if (isClickCamera) {
                            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                        } else {
                            bitmap = BitmapFactory.decodeFile(imagePath);
                        }
                        // iv.setImageBitmap(bitmap);
                        //Bitmap photo = extras.getParcelable("data");
                        String imString = savePhotoToSDCard(bitmap);
                        compressPicture(imString);
//                    new ImageUploadAsync(handler, imString, 1111).execute();
//                    UploadPhotoUtils.getInstance(this).upLoadFile(imString, "http://api.test.cloudm.com/kindEditorUpload",mHandler);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this);
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
            case REQUEST_PERMISSION_PICK:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this);
                } else {
                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                            REQUEST_PICK_IMAGE);
                }
                break;

        }

    }

    private void loadUrl() {
        if (!TextUtils.isEmpty(mUrl)) {
            Member member = MemeberKeeper.getOauth(this);
            if (member != null) {
                fillParams(PARAMS_KEY_MYID, member.getWjdsId());
                fillParams(PARAMS_KEY_MEMBERID, member.getId());
            }
        }
        mWebView.loadUrl(mUrl);
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

    private void compressPicture(String imString) {
        File file = new File(imString);
        Compressor.getDefault(this)
                .compressToFileAsObservable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {   //http:///excamaster
                        Long wjdxId = UserHelper.getWjdxID(mContext);
                        if (wjdxId != null) {
                            UploadPhotoUtils.getInstance(mContext).upLoadForH5File(file, ApiConstants.XIEXIN_HOST + "excamaster/yjxapi/uploadpicture?pictype=1&?myid=" + wjdxId, mHandler);
//                            UploadPhotoUtils.getInstance(mContext).upLoadForH5File(file, "http://121.40.130.218:8980/excamaster/yjxapi/uploadpicture?pictype=1&?myid=" + wjdxId, mHandler);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.error(throwable.getMessage(), true);
                    }
                });
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 将图片保存到sd卡
     *
     * @param bitmap
     * @return
     */
    public static String savePhotoToSDCard(Bitmap bitmap) {
        if (!FileUtils.isSdcardExist()) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        FileUtils.createDirFile(Constants.IMAGE_PATH);

        String fileName = UUID.randomUUID().toString() + ".JPEG";
        String newFilePath = Constants.IMAGE_PATH + fileName;
        File file = FileUtils.createNewFile(newFilePath);
        if (file == null) {
            return null;
        }
        try {
            fileOutputStream = new FileOutputStream(newFilePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e1) {
            return null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
        return newFilePath;
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            AppLog.print("imageUri:" + imageUri + ", imgeUri authority____" + imageUri.getAuthority() + "___scheme__" + imageUri.getScheme());
//            imageUri:content://com.android.externalstorage.documents/document/primary%3APhoto_LJ%2F1493098068425.JPEG,
// imgeUri authority____com.android.externalstorage.documents___scheme__content

            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("com.android.externalstorage.documents".equalsIgnoreCase(imageUri.getAuthority())) {
                String path = imageUri.getPath();
                if (path != null) {
                    if (path.contains(":")) {
                        String name = path.split(":")[1];
                        imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name;
                    }
                }
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }

        cropPhoto();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = this.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {
        File file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }

    /**
     * 19之前版本
     *
     * @param intent
     */
    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    String jsAlerEvent;
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
                        Toast.makeText(QuestionCommunityActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        sendEmptyMessage(Constants.HANDLER_JUMP_MY_ORDER);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(QuestionCommunityActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.HANDLER_JUMP_MY_ORDER:
                    Constants.callJsMethod(mWebView, "callbackDSFPayResultSuccess()");
                    break;
                case Constants.HANDLER_HIDEN_CLOSE_BTN:
                    mWvTitle.setCloseVisible(View.GONE);
                    break;
                case Constants.HANDLER_SHOW_CLOSE_BTN:
                    mWvTitle.setCloseVisible(View.VISIBLE);
                    break;

                case Constants.HANDLER_JS_ALERT:
                    JSONObject job = (JSONObject) msg.obj;
                    String type = job.optString(JsInteface.ALERT_TYPE);
                    if (JsInteface.UPLOAD_IMG.equals(type)) {
                        showUpLoadImgDialog();
                    } else {
                        String tips = job.optString(JsInteface.ALERT_TIPS);
                        String alert_event = job.optString(JsInteface.ALERT_EVENT);
                        shoWAlertDialog(tips, alert_event);
                    }
                    break;
                case Constants.HANDLER_UPLOAD_SUCCESS:
                    String dataStr = (String) msg.obj;
                    Constants.callJsMethod(mWebView, "backLocalImgInfo('" + dataStr + "')");
                    break;
                case Constants.HANDLER_REPAIR:
                    if (UserHelper.isLogin(QuestionCommunityActivity.this)) {
                        Constants.toActivity(QuestionCommunityActivity.this, NewRepairActivity.class, null);
                    } else {
                        Constants.toActivityForR(QuestionCommunityActivity.this, LoginActivity.class, null);
                    }
                    break;

                case Constants.HANDLER_JS_JUMP:
                    String methodStr = (String) msg.obj;
                    if (JsInteface.Go_Login_Page.equals(methodStr)) {
                        Bundle b = new Bundle();
                        b.putInt("flag", 4);
                        Constants.toActivityForR(QuestionCommunityActivity.this, LoginActivity.class, b, FLUSH_PAGE);
                    }
                    break;
                case Constants.HANDLER_JS_BODY:
                    JsBody body = (JsBody) msg.obj;
                    if (!TextUtils.isEmpty(body.getTitle())) {
                        shareTitle = body.getTitle();
                        shareLink = body.getLink();
                        shareDesc = body.getDesc();
                        shareUrl();
                        break;
                    }
                    if (!TextUtils.isEmpty(body.getCenter_title())) {
                        h5Title = body.getCenter_title();
                    }
                    if (!TextUtils.isEmpty(body.getRight_event())) {
                        rightEvent = body.getRight_event();
                    }
                    JsonObject scJobj = body.getShare_content();
                    if (scJobj != null) {
                        shareTitle = scJobj.get("title").getAsString();
                        shareDesc = scJobj.get("desc").getAsString();
                        if (TextUtils.isEmpty(shareLink)) {
                            shareLink = scJobj.get("link").getAsString();
                        }
                        if (TextUtils.isEmpty(sharePic)) {
                            sharePic = scJobj.get("image").getAsString();
                        }
                        AppLog.print("scObj 不为空___title_" + shareTitle + ", desc__" + shareDesc + ", lin__" + shareLink);
                    } else {
                        shareTitle = h5Title;
                        shareDesc = SHAREDESC;
                        if (TextUtils.isEmpty(shareLink)) {
                            shareLink = mUrl;
                        }
                        AppLog.print("scObj___title_" + shareTitle + ", desc__" + shareDesc + ", lin__" + shareLink);
                    }

                    if (!TextUtils.isEmpty(h5Title)) {
                        mWvTitle.setTitleName(h5Title);
                        String rightTitle = body.getRight_title();
                        if (!TextUtils.isEmpty(rightTitle) && !SHARE.equals(rightTitle)) {
                            final String rightEvent = body.getRight_event();
                            if (!TextUtils.isEmpty(rightEvent)) {
                                mWvTitle.setRightText(rightTitle, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Constants.callJsMethod(mWebView, rightEvent);
                                    }
                                });
                            } else {
                                mWvTitle.setRightText(rightTitle);
                            }
                        } else {
                            mWvTitle.setRightText(null, null);
                        }
                        if (SHARE.equals(rightTitle) || !TextUtils.isEmpty(body.getShare_title())) {
                            mWvTitle.setRightImg(R.drawable.ic_share, new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    if (TextUtils.isEmpty(shareLink)) {
                                        Constants.callJsMethod(mWebView, rightEvent);
                                    } else {
                                        shareUrl();
                                    }
                                }

                            });
                        } else {
                            mWvTitle.setRightImg(0, null);
                        }
                        final String leftEvent = body.getLeft_event();
                        if (!TextUtils.isEmpty(leftEvent)) {
                            mWvTitle.setLeftClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Constants.callJsMethod(mWebView, leftEvent);
                                }
                            });
                        }
                    }
                    break;
            }
        }

        private void shareUrl() {
            ShareDialog shareDialog = new ShareDialog(QuestionCommunityActivity.this, shareLink, shareTitle, shareDesc, sharePic);
            shareDialog.setLinkUrl(mUrl);
            shareDialog.show();
            MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                String url = mWebView.getUrl();
                if (url.equals(backUrl) || url.startsWith("cloudm://closeAskPage")) {
                    mWvTitle.setCloseVisible(View.GONE);
                } else {
                    mWvTitle.setCloseVisible(View.VISIBLE);
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private String shareTitle;
    private String shareLink;
    private String shareDesc;
    private String h5Title;
    private String sharePic;
    private String rightEvent;

}
