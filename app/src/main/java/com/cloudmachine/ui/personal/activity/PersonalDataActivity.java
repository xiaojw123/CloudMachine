package com.cloudmachine.ui.personal.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cloudmachine.R;
import com.cloudmachine.activities.EditPersonalActivity;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.activities.UpdatePwdActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.Member;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.ui.personal.contract.PersonalDataContract;
import com.cloudmachine.ui.personal.model.PersonalDataModel;
import com.cloudmachine.ui.personal.presenter.PersonalDataPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cloudmachine.utils.Constants.IMAGE_PATH;

/**
 * 项目名称：CloudMachine
 * 类描述：个人资料页面
 * 创建人：shixionglu
 * 创建时间：2017/4/5 上午9:31
 * 修改人：shixionglu
 * 修改时间：2017/4/5 上午9:31
 * 修改备注：
 */
// TODO: 2018/9/13 个人信息

public class PersonalDataActivity extends BaseAutoLayoutActivity<PersonalDataPresenter, PersonalDataModel> implements
        View.OnClickListener, Handler.Callback, PersonalDataContract.View, QiniuManager.OnUploadListener {

    @BindView(R.id.head_iamge)
    CircleImageView mHeadIamge;
    @BindView(R.id.edit_textPhone)
    TextView mEditTextPhone;
    @BindView(R.id.phoneLayout)
    RelativeLayout mPhoneLayout;
    @BindView(R.id.nickname)
    TextView mNicknameTv;
    @BindView(R.id.nickLayout)
    RelativeLayout mNickLayout;
    @BindView(R.id.my_pwd)
    RelativeLayout mMyPwd;
    @BindView(R.id.ll_head_logo)
    RelativeLayout mLlHeadLogo;
    @BindView(R.id.f5)
    TextView mF5;
    @BindView(R.id.arrowTip1)
    ImageView mArrowTip1;
    @BindView(R.id.my_qrcode)
    RelativeLayout myQrCodeRl;
    private int imgsign = -1;
    private Uri imageUri;
    private String imagePath;
    private boolean isClickCamera;
    private Handler mHandler;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_PICK_IMAGE = 0x001; //相册选取
    private static final int REQUEST_CAPTURE = 0x002;  //拍照
    private static final int REQUEST_PICTURE_CUT = 0x003;  //剪裁图片
    private static final int REQUEST_PERMISSION = 0x004;  //权限请求
    private static final int REQUEST_PERMISSION_PICK = 0x008;  //权限请求
    private static final int REQUEST_UPDATE = 0x005;  //权限请求
    private String mLogo;
    private String mNickName;
    private String mWecharNickname;
    private String mWecharLogo;
    private String mUrl;
    private boolean syncWx = false;
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        ButterKnife.bind(this);
        MobclickAgent.onEvent(this, MobEvent.TIME_PROFILE);
        initView();
    }

    private void initView() {
        RadiusButtonView synchButton = (RadiusButtonView) findViewById(R.id.btn_synchronousWxData);
        synchButton.setOnClickListener(this);
        long loginType = MySharedPreferences.getSharedPInt(MySharedPreferences.key_login_type);
        if (loginType == 0) {
            synchButton.setVisibility(View.GONE);
        } else {
            if (mWecharLogo != null && mWecharNickname != null) {
                if (mWecharLogo.equals(mLogo) && mWecharNickname.equals(mNickName)) {
                    synchButton.setVisibility(View.GONE);
                } else {
                    synchButton.setVisibility(View.VISIBLE);
                }
            }
        }
        initPermissionChecker();
        initListener();
        initData();
    }

    private void initData() {
        mHandler = new Handler(this);
        member = MemeberKeeper.getOauth(this);
        if (member == null) {
            return;
        }
        mLogo = member.getLogo();
        String mobile = member.getMobile();
        mNickName = member.getNickname();
        mWecharNickname = member.getWecharNickname();
        mWecharLogo = member.getWecharLogo();
        if (!TextUtils.isEmpty(mLogo)) {
            Glide.with(mContext).load(mLogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.ic_default_head)
                    .into(mHeadIamge);
        }
        if (!TextUtils.isEmpty(mobile)) {
            mEditTextPhone.setText(mobile);
        }
        if (!TextUtils.isEmpty(mNickName)) {
            mNicknameTv.setText(mNickName);
        }
    }


    private void initPermissionChecker() {

        mPermissionsChecker = new PermissionsChecker(PersonalDataActivity.this);
    }

    private void initListener() {

        mLlHeadLogo.setOnClickListener(this);
        mNickLayout.setOnClickListener(this);
        mMyPwd.setOnClickListener(this);
        myQrCodeRl.setOnClickListener(this);
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_qrcode:
                Constants.toActivity(this, MyQRCodeActivity.class, null);
                break;

            case R.id.ll_head_logo:
                syncWx = false;
                showImageDialog();
                break;
            case R.id.nickLayout:
                syncWx = false;
                Bundle bundle = new Bundle();
                bundle.putInt("sign", 1);
                bundle.putString("info", mNicknameTv.getText().toString());
                Constants.toActivityForR(PersonalDataActivity.this, EditPersonalActivity.class, bundle, REQUEST_UPDATE);
                break;
            case R.id.my_pwd:
                MobclickAgent.onEvent(mContext, MobEvent.COUNT_CHANGEPASSWORD);
                Constants.toActivity(PersonalDataActivity.this, UpdatePwdActivity.class, null);
                break;
            //同步微信信息
//            case R.id.btn_synchronousWxData:
            case R.id.radius_button_text:
                syncWx = true;
                mPresenter.modifyMemberInfo(null, mWecharLogo);
                mPresenter.modifyMemberInfo(mWecharNickname, null);
                break;
        }
    }

    private String[] items = new String[]{"选择本地图片", "拍照"};
    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    static final String[] PERMISSIONS_PICK = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private void showImageDialog() {
        imgsign = 2;
        new AlertDialog.Builder(PersonalDataActivity.this)
                .setTitle("头像照片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS_PICK)) {
                                        PermissionsActivity.startActivityForResult(PersonalDataActivity.this, REQUEST_PERMISSION_PICK,
                                                PERMISSIONS_PICK);
                                    } else {
                                        isForbidenAd = true;
                                        startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                                REQUEST_PICK_IMAGE);
                                    }
                                } else {
                                    isForbidenAd = true;
                                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                            REQUEST_PICK_IMAGE);
                                }
                                isClickCamera = false;
                                break;
                            case 1:
                                //检查权限(6.0以上做权限判断)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
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
        PermissionsActivity.startActivityForResult(PersonalDataActivity.this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    //打开系统相机
    private void openCamera() {
        isForbidenAd = true;
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(PersonalDataActivity.this, Constants.FILE_PROVIDER, file);//通过FileProvider创建一个content类型的Uri
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bitmap bitmap = null;
                        try {
                            if (isClickCamera) {
                                bitmap = BitmapFactory.decodeStream(PersonalDataActivity.this.getContentResolver().openInputStream(imageUri));
                            } else {
                                bitmap = BitmapFactory.decodeFile(imagePath);
                            }
                            // iv.setImageBitmap(bitmap);
                            if (imgsign == 2) {
                                //Bitmap photo = extras.getParcelable("data");
//                        mHeadIamge.setImageBitmap(bitmap);//展示到当前页面
                                String imString = savePhotoToSDCard(bitmap);
                                compressPicture(imString);
                                //new ImageUploadAsync(handler, imString, 1111).execute();
                                //UploadPhotoUtils.getInstance(this).upLoadFile(imString, "http://api.test.cloudm.com/kindEditorUpload",mHandler);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.CAMERA);
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
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
                } else {
                    isForbidenAd = true;
                    startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                            REQUEST_PICK_IMAGE);
                }
                break;
            case REQUEST_UPDATE:
                if (data != null) {
                    String nickname = data.getStringExtra(EditPersonalActivity.NICK_NAME);
                    mNicknameTv.setText(nickname);
                    member.setNickname(nickname);
                    MemeberKeeper.saveOAuth(member, this);
                }
                break;
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }

    private void compressPicture(String imString) {
        File file = new File(imString);
        QiniuManager.uploadFile(mContext, this, file, "member/");
    }


    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        isForbidenAd = true;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
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

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(PersonalDataActivity.this, imageUri)) {
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

    /**
     * 通过Uri和selection获取真实的图片路径
     *
     * @param uri
     * @param selection
     * @return
     */
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
        isForbidenAd = true;
        File file = new FileStorage().createCropFile();
        Uri outputUri = Uri.fromFile(file);
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
        FileUtils.createDirFile(IMAGE_PATH);

        String fileName = UUID.randomUUID().toString() + ".JPEG";
        String newFilePath = IMAGE_PATH + fileName;
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
                e.printStackTrace();
            }
        }
        return newFilePath;
    }


    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            //上传个人信息成功
            case Constants.HANDLER_UPDATEMEMBERINFO_SUCCESS:
                Constants.MyToast("保存成功");
                break;
            case Constants.HANDLER_UPDATEMEMBERINFO_FAIL:
                Constants.MyToast((String) msg.obj);
                break;
            case Constants.HANDLER_UPLOAD_SUCCESS:
                mUrl = (String) msg.obj;
                if (mUrl != null) {
                    mPresenter.modifyMemberInfo(null, mUrl);
                }
                break;
            case Constants.HANDLER_UPLOAD_FAILD:
                ToastUtils.showToast(this, "头像上传失败");
                break;

        }
        return false;
    }

    @Override
    public void returnModifyNickName() {
        mNicknameTv.setText(mWecharNickname);
        member.setNickName(mWecharNickname);
        MemeberKeeper.saveOAuth(member, this);
    }

    @Override
    public void returnModifyLogo() {
        if (syncWx) {
            Glide.with(mContext).load(mWecharLogo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.ic_default_head)
                    .into(mHeadIamge);
            member.setLogo(mWecharLogo);
        } else {
            member.setLogo(mUrl);
            Glide.with(mContext).load(mUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.ic_default_head)
                    .into(mHeadIamge);
        }
        MemeberKeeper.saveOAuth(member, this);
    }


    public void exitLogin(View view) {
        MobclickAgent.onEvent(this, MobEvent.COUNT_LOGOUT);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("你确认退出？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Constants.exitAccount();
                if (!mPermissionsChecker.lacksPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    clearWebCahe();
                }
                Intent intent = new Intent(PersonalDataActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create().show();
    }


    @Override
    public void uploadSuccess(String key, String picUrl) {
        Message msg = Message.obtain();
        msg.what = Constants.HANDLER_UPLOAD_SUCCESS;
        msg.obj = picUrl;
        mHandler.sendMessage(msg);
    }

    @Override
    public void uploadFailed() {
        Message msg = Message.obtain();
        msg.what = Constants.HANDLER_UPLOAD_FAILD;
        msg.obj = "图片上传失败";
        mHandler.sendMessage(msg);
    }
}
