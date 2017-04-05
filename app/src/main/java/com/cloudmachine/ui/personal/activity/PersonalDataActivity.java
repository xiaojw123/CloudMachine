package com.cloudmachine.ui.personal.activity;

import android.Manifest;
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
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.ImageUploadAsync;
import com.cloudmachine.net.task.UpdateMemberInfoAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.UIHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class PersonalDataActivity extends BaseAutoLayoutActivity implements View.OnClickListener,Handler.Callback {

    @BindView(R.id.title_layout)
    TitleView       mTitleLayout;
    @BindView(R.id.head_iamge)
    CircleImageView mHeadIamge;
    @BindView(R.id.edit_textPhone)
    TextView        mEditTextPhone;
    @BindView(R.id.phoneLayout)
    RelativeLayout  mPhoneLayout;
    @BindView(R.id.nickname)
    TextView        mNickname;
    @BindView(R.id.nickLayout)
    RelativeLayout  mNickLayout;
    @BindView(R.id.my_pwd)
    RelativeLayout  mMyPwd;
    @BindView(R.id.ll_head_logo)
    RelativeLayout  mLlHeadLogo;
    @BindView(R.id.f5)
    TextView        mF5;
    @BindView(R.id.arrowTip1)
    ImageView       mArrowTip1;

    private int imgsign  = -1;
    private int infoSign = -1;
    private Uri     imageUri;
    private String  imagePath;
    private String  imString;
    private boolean isClickCamera;
    private boolean isUpdateImage;
    private String uploadResult = "";
    private Handler mHandler;
    private Context mContext;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_PICK_IMAGE  = 0x001; //相册选取
    private static final int REQUEST_CAPTURE     = 0x002;  //拍照
    private static final int REQUEST_PICTURE_CUT = 0x003;  //剪裁图片
    private static final int REQUEST_PERMISSION  = 0x004;  //权限请求


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        mContext = this;
        mHandler = new Handler(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        initPermissionChecker();
        initTitleLayout();
        initListener();
    }

    private void initPermissionChecker() {

        mPermissionsChecker = new PermissionsChecker(PersonalDataActivity.this);
    }

    private void initListener() {

        mLlHeadLogo.setOnClickListener(this);
    }

    private void initTitleLayout() {

        mTitleLayout.setTitle("个人资料");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_head_logo:
                showImageDialog();
                break;
        }
    }

    private      String[] items       = new String[]{"选择本地图片", "拍照"};
    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private void showImageDialog() {

        imgsign = 2;
        infoSign = 2;
        new AlertDialog.Builder(PersonalDataActivity.this)
                .setTitle("头像照片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                        REQUEST_PICK_IMAGE);
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
                });
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(PersonalDataActivity.this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    //打开系统相机
    private void openCamera() {
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(PersonalDataActivity.this, "com.cloudmachine.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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

                        bitmap = BitmapFactory.decodeStream(PersonalDataActivity.this.getContentResolver().openInputStream(imageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    // iv.setImageBitmap(bitmap);
                    if (imgsign == 2) {
                        //Bitmap photo = extras.getParcelable("data");
                        mHeadIamge.setImageBitmap(bitmap);//展示到当前页面
                        imString = savePhotoToSDCard(bitmap);
                        isUpdateImage = true;
                        new ImageUploadAsync(handler, imString, 1111).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    PersonalDataActivity.this.finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
        // super.onActivityResult(requestCode, resultCode, data);
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
     * 19之前版本
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
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
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
     * 将图片保存到sd卡
     * @param bitmap
     * @return
     */
    public static String savePhotoToSDCard(Bitmap bitmap) {
        if (!FileUtils.isSdcardExist()) {
            return null;
        }
        FileOutputStream fileOutputStream = null;
        FileUtils.createDirFile(IMAGE_PATH);

        String fileName = UUID.randomUUID().toString() + ".jpg";
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
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                return null;
            }
        }
        return newFilePath;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ImageUploadAsync.ImageUpload_Success) {
                uploadResult = (String) msg.obj;
                new UpdateMemberInfoAsync("logo", uploadResult, mContext, mHandler).execute();
            } else if (msg.what == ImageUploadAsync.ImageUpload_Fail) {
                UIHelper.ToastMessage(PersonalDataActivity.this, "头像上传失败");
            }
        }

    };

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            //上传个人信息成功
            case Constants.HANDLER_UPDATEMEMBERINFO_SUCCESS:
                isUpdateImage = false;
                Constants.MyToast("保存成功");
                break;
            case Constants.HANDLER_UPDATEMEMBERINFO_FAIL:
                isUpdateImage = false;
                Constants.MyToast((String) msg.obj);
                break;
        }
        return false;
    }
}
