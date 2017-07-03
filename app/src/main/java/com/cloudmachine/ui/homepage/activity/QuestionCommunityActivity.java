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
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.api.ApiConstants;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.broadcast.MyReceiver;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.homepage.contract.QuestionCommunityContract;
import com.cloudmachine.ui.homepage.model.QuestionCommunityModel;
import com.cloudmachine.ui.homepage.presenter.QuestionCommunityPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UploadPhotoUtils;
import com.github.mikephil.charting.utils.AppLog;
import com.jsbridge.JsBridgeClient;
import com.jsbridge.core.JsBridgeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    @BindView(R.id.webview_progressbar)
    ProgressBar mWebviewProgressbar;
    @BindView(R.id.wv_questions)
    WebView mWvQuestions;

    private Context mContext;
//    private String URLString = "http://h5.test.cloudm.com/n/ask_qlist";
    private String URLString = ApiConstants.H5_HOST+"n/ask_qlist";
    private Long mMyid;
    private JsBridgeManager jsBridgeManager;
    private String notifyUrl;
    String[] items = new String[]{"选择本地图片", "拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioncommunity);
        ButterKnife.bind(this);
        mPermissionsCheck = new PermissionsChecker(this);
        initJsBridgeManager();
        initParams();
        initWebView();
    }



    private void initJsBridgeManager() {
        jsBridgeManager = JsBridgeClient.getJsBridgeManager(QuestionCommunityActivity.this);
    }

    // TODO: 2017/5/2 modify by xjw
    private void initParams() {
        mContext = this;
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        notifyUrl = bundle.getString(MyReceiver.NOTIFY_URL);
        String url = bundle.getString("url");
        if (!TextUtils.isEmpty(url)) {
            URLString = url;
        }
        if (URLString.contains("myid")) {
            return;
        }
        Member member = MemeberKeeper.getOauth(this);
        if (member != null) {
            mMyid = member.getWjdsId();
        }
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWvQuestions.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
                AppLog.print("onReceivedError___");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppLog.print("shoudLoadUrl___" + url);
//                if (PAGE_TAB_QUESTION.equals(pageType)) {
////                    if (!URLString.equals(url)) {
////                        finish();
////                    }
//                    return true;
//                }
                return jsBridgeManager.invokeNative(view, url);
            }

        });
        if (!TextUtils.isEmpty(notifyUrl)) {
            mWvQuestions.loadUrl(notifyUrl);
            return;
        }
        if (mMyid != null) {
            mWvQuestions.loadUrl(URLString + "?myid=" + mMyid + "&hideback=1");
            AppLog.print("QuestionCommunityActivity链接" + URLString + "?myid=" + mMyid + "&hideback=1");
        } else {
            mWvQuestions.loadUrl(URLString);
        }


    }

    public void shoWAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AppLog.print("onJsAlert___url" + url + "___message__" + message + "___JsResult__" + result);
            if ("upload_img".equals(message)) {
                showUpLoadImgDialog();
                result.confirm();
                return true;
            } else {
                shoWAlertDialog(message);
                result.confirm();
                return true;
            }
        }


        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            AppLog.print("onJsPrompt___");
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AppLog.print("onJsConfirm___");
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
            AppLog.print("onGeolocationPermissionsShowPrompt__");
            callback.invoke(origin, true, false);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // TODO Auto-generated method stub
            AppLog.print("onProgressChanged_");
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

    private void showUpLoadImgDialog() {
        new AlertDialog.Builder(QuestionCommunityActivity.this)
                .setTitle("头像照片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS_PICK)) {
                                        PermissionsActivity.startActivityForResult(QuestionCommunityActivity.this,
                                                REQUEST_PERMISSION_PICK,PERMISSIONS_PICK);
//                                        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
//                                                PERMISSIONS);
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
                                //检查权限(6.0以上做权限判断)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsCheck.lacksPermissions(PERMISSIONS)) {
                                        startPermissionsActivity();
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

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
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


    private PermissionsChecker mPermissionsCheck;
    private static final int REQUEST_PICK_IMAGE = 0x001; //相册选取
    private static final int REQUEST_CAPTURE = 0x002;  //拍照
    private static final int REQUEST_PICTURE_CUT = 0x003;  //剪裁图片
    private static final int REQUEST_PERMISSION = 0x004;  //权限请求
    private static final int REQUEST_PERMISSION_PICK = 0x005;  //权限请求
    private boolean isClickCamera;
    private Uri imageUri;
    private String imagePath;
    private String imString;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
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
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {
                        bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    // iv.setImageBitmap(bitmap);
                    //Bitmap photo = extras.getParcelable("data");
                    imString = savePhotoToSDCard(bitmap);
                    compressPicture(imString);
//                    new ImageUploadAsync(handler, imString, 1111).execute();
//                    UploadPhotoUtils.getInstance(this).upLoadFile(imString, "http://api.test.cloudm.com/kindEditorUpload",mHandler);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    this.finish();
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
                    ToastUtils.showToast(QuestionCommunityActivity.this,"权限被拒绝");
                    this.finish();
                } else {
                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                            REQUEST_PICK_IMAGE);
                }
                break;

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
                            UploadPhotoUtils.getInstance(mContext).upLoadForH5File(file, ApiConstants.XIEXIN_HOST+"excamaster/yjxapi/uploadpicture?pictype=1&?myid=" + wjdxId, mHandler);
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
                fileOutputStream.flush();
                fileOutputStream.close();
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.HANDLER_UPLOAD_SUCCESS:
                    String dataStr = (String) msg.obj;
                    AppLog.print("upload result data_" + dataStr);
                    String jsBackImg = "javascript:backLocalImgInfo('" + dataStr + "')";
                    AppLog.print("调用js___" + jsBackImg);
                    mWvQuestions.loadUrl(jsBackImg);
                    break;
            }
        }
    };
}
