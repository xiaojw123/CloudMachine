package com.cloudmachine.ui.personal.fragment;

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
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.AboutCloudActivity;
import com.cloudmachine.activities.EditPersonalActivity;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.activities.QrCodeActivity;
import com.cloudmachine.activities.SuggestBackActivity;
import com.cloudmachine.activities.UpdatePwdActivity;
import com.cloudmachine.activities.ViewCouponActivity;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.net.task.GetMemberInfoAsync;
import com.cloudmachine.net.task.ImageUploadAsync;
import com.cloudmachine.net.task.ScoreInfoAsync;
import com.cloudmachine.net.task.UpdateMemberInfoAsync;
import com.cloudmachine.struc.Member;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.ui.personal.contract.PersonalContract;
import com.cloudmachine.ui.personal.model.PersonalModel;
import com.cloudmachine.ui.personal.presenter.PersonalPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileStorage;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.ShareDialog;
import com.cloudmachine.utils.UIHelper;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.WeChatShareUtil;
import com.cloudmachine.utils.widgets.Dialog.LoadingDialog;
import com.cloudmachine.utils.widgets.RadiusButtonView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;

import static android.app.Activity.RESULT_OK;

/**
 * 个人信息页面
 */
public class UpdateInfoActivity extends BaseFragment<PersonalPresenter, PersonalModel> implements OnClickListener, Callback ,PersonalContract.View{

    private Context mContext;
    private Handler mHandler;

    private TextView textView4, textView5, score_text;
    private RelativeLayout nickLayout, set_qrcode, phoneLayout, set_suggest, about_cloud;
    private View         my_pwd;
    private LinearLayout exitlayout;
    private ImageView    imageView;
    private View         imageLayout;
    private String       imageFileName;
    private String uploadResult = "";
    private LoadingDialog progressDialog;
    private TitleView     title_layout;
    private final        String   LOG_TAG             = "UpdateInfoActivity";
    // 请求码
    private static final int      IMAGE_REQUEST_CODE  = 0;
    private static final int      CAMERA_REQUEST_CODE = 1;
    private static final int      RESULT_REQUEST_CODE = 2;
    private              String[] items               = new String[]{"选择本地图片", "拍照"};
    private              String[] itemSex             = new String[]{"男", "女"};

    private int infoSign = -1;

    private String backPic, frontPic, headPic;

    private int imgsign = -1;
    private int sex     = 1;
    private RadioButton radioButton1, radioButton2, radioButton3;
    private static final String IMAGE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cloudmachine" + File.separator + "images" + File.separator;
    private String imString;
    private int    infoType, signBetweenTime;
    private int infoMemberId;
    private boolean isMyInfo = true;
    private View             privacyInfo;
    private boolean          isUpdateImage;
    private PopupWindow      mpopupWindow;
    private RadiusButtonView score_button;
    private TextView         score_num;
    private View             score_layout;
    private ScrollView       scrollView;
    private boolean isIntegralAgain = true;

    private RelativeLayout  mAboutAndHelp;
    private RelativeLayout  mShareAPP;
    private WeChatShareUtil weChatShareUtil;
    private RelativeLayout  rlCoupon;
    private Uri imageUri;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    //需要申请的权限数组
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private boolean isClickCamera;
    private String imagePath;
    private String sessionTitle = "云机械";
    private String sessionDescription = "我的工程机械设备都在云机械APP，你的设备在哪里，赶紧加入吧！";
    private String sessionUrl = "http://www.cloudm.com/yjx";

    @Override
    protected void initView() {
        init();
        initRootView();// 控件初始化
        weChatShareUtil = WeChatShareUtil.getInstance(getContext());
    }

    @Override
    protected void initPresenter() {
        mPresenter.setVM(this,mModel);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_personal;
    }

    private void init() {

        mPermissionsChecker =  new PermissionsChecker(this.getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initRootView() {
        mContext = getActivity();
        mHandler = new Handler(this);
        getIntentData();
        initTitleLayout();
        try {
            if (MemeberKeeper.getOauth(getActivity()).getId() != infoMemberId && infoType != 0) {
                isMyInfo = false;
            }
        } catch (Exception e) {
            isMyInfo = false;
        }


        //		nameLayout,mailLayout,phoneLayout,sexLayout,cardnickLayout;
        scrollView = (ScrollView) viewParent.findViewById(R.id.scrollView);//整体滚动

        my_pwd = viewParent.findViewById(R.id.my_pwd);
        my_pwd.setOnClickListener(this);

        score_layout = viewParent.findViewById(R.id.score_layout);
        score_layout.setOnClickListener(this);
        score_num = (TextView) viewParent.findViewById(R.id.score_num);
        score_button = (RadiusButtonView) viewParent.findViewById(R.id.score_button);
        score_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                score_layout.setVisibility(View.GONE);
                scrollView.setBackgroundResource(R.color.commom_bg);
            }
        });

        exitlayout = (LinearLayout) viewParent.findViewById(R.id.exitlayout);
        exitlayout.setOnClickListener(this);

        rlCoupon = (RelativeLayout) viewParent.findViewById(R.id.rl_coupon);
        rlCoupon.setOnClickListener(this);

        //		backImg = (ImageView)findViewById(R.id.backImg);
        about_cloud = (RelativeLayout) viewParent.findViewById(R.id.about_cloud);//关于
        about_cloud.setVisibility(View.GONE);
        nickLayout = (RelativeLayout) viewParent.findViewById(R.id.nickLayout);
        set_qrcode = (RelativeLayout) viewParent.findViewById(R.id.set_qrcode);//二维码
        set_qrcode.setVisibility(View.GONE);
        phoneLayout = (RelativeLayout) viewParent.findViewById(R.id.phoneLayout);
        set_suggest = (RelativeLayout) viewParent.findViewById(R.id.set_suggest);//意见反馈
        set_suggest.setVisibility(View.GONE);
        score_text = (TextView) viewParent.findViewById(R.id.score_text);//
        mAboutAndHelp = (RelativeLayout) viewParent.findViewById(R.id.help);//关于与帮助
        mAboutAndHelp.setOnClickListener(this);
        mShareAPP = (RelativeLayout) viewParent.findViewById(R.id.share_app);
        mShareAPP.setOnClickListener(this);
        //textView4 = (ClearEditTextView)findViewById(R.id.edit_text7);//邮编
        textView4 = (TextView) viewParent.findViewById(R.id.edit_textPhone);//手机号
        textView5 = (TextView) viewParent.findViewById(R.id.nickname);
        //		radioGroup = (RadioGroup) findViewById(R.id.sex);
        //		radioButton1 = (RadioButton)findViewById(R.id.male);
        //		radioButton2 = (RadioButton)findViewById(R.id.famale);
        //		radioButton3 = (RadioButton)findViewById(R.id.unkown);
        //Member member = MemeberKeeper.getOauth(UpdateInfoActivity.this);
        imageFileName = UUID.randomUUID().toString().concat(".jpg");
        imageView = (ImageView) viewParent.findViewById(R.id.image_avator);
        imageLayout = viewParent.findViewById(R.id.imageLayout);
        if (isMyInfo) {
            about_cloud.setOnClickListener(this);
            imageLayout.setOnClickListener(this);
            nickLayout.setOnClickListener(this);
            //			phoneLayout.setOnClickListener(this);
            set_qrcode.setOnClickListener(this);
            set_suggest.setOnClickListener(this);
        }

        //		backImg.setOnClickListener(this);
        /*title_layout.setLeftImage(R.drawable.back_item_selector, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});*/

        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!TextUtils.isEmpty(headPic) && null != headPic) {
                    String[] urls = {headPic};
                    Constants.gotoImageBrowerType(getActivity(), 1, urls, false, 2);
                }
            }
        });
        privacyInfo = viewParent.findViewById(R.id.privacyInfo);
        if (isMyInfo) {
            privacyInfo.setVisibility(View.VISIBLE);
        } else {
            privacyInfo.setVisibility(View.INVISIBLE);
        }
        signBetweenTime = ((MainActivity) getActivity()).getSignBetweenTime();
        new ScoreInfoAsync(signBetweenTime, mContext, mHandler).execute();
    }

    private void initTitleLayout() {

        title_layout = (TitleView) viewParent.findViewById(R.id.title_layout);
        title_layout.setTitle(getResources().getString(R.string.main_bar_text4));
        title_layout.setLeftLayoutVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        if (infoType == 0) {
            new GetMemberInfoAsync(null, mContext, mHandler).execute();
        } else {
            new GetMemberInfoAsync(infoMemberId + "", mContext, mHandler).execute();
        }

        super.onResume();
        //MobclickAgent.onPageStart(this.getClass().getName());
        //MobclickAgent.onPageStart(UMengKey.time_profile);
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //MobclickAgent.onPageEnd(this.getClass().getName());
        //MobclickAgent.onPageEnd(UMengKey.time_profile);
    }

    private void getIntentData() {
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {


                signBetweenTime = bundle.getInt(Constants.P_SignBetweenTime);
                infoType = bundle.getInt(Constants.P_MERMBERTYPE);
                infoMemberId = bundle.getInt(Constants.P_MERMBERID);
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    /*public void showDialogCard(View v) {

        imgsign = 1;
        new AlertDialog.Builder(getActivity())
                .setTitle("证件照片")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                *//*startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                        IMAGE_REQUEST_CODE);*//*
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                                        startPermissionsActivity();
                                    } else {
                                        selectFromAlbum();
                                    }
                                } else {
                                    selectFromAlbum();
                                }
                                isClickCamera = false;
                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储
                                if (hasSdcard()) {
                                    intentFromCapture.putExtra(
                                            MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(new File(Environment
                                                    .getExternalStorageDirectory(),
                                                    imageFileName)));

                                }
                                startActivityForResult(intentFromCapture,
                                        CAMERA_REQUEST_CODE);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }*/


    public void showDialog(View v) {
        imgsign = 2;
        infoSign = 2;
        new AlertDialog.Builder(getActivity())
                .setTitle("头像照片")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                                        IMAGE_REQUEST_CODE);
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
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
    //打开系统相机
    private void openCamera(){
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this.getActivity(), "com.cloudmachine.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this.getActivity(), REQUEST_PERMISSION,
                PERMISSIONS);
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

                        bitmap = BitmapFactory.decodeStream(this.getActivity().getContentResolver().openInputStream(imageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                   // iv.setImageBitmap(bitmap);
                    if (imgsign == 2) {
                        //Bitmap photo = extras.getParcelable("data");
                        imageView.setImageBitmap(bitmap);
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
                    this.getActivity().finish();
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

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this.getActivity(), imageUri)) {
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

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
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

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
        Cursor cursor = this.getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private boolean hasSdcard() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     * @param
     */

    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            if (imgsign == 2) {
                Bitmap photo = extras.getParcelable("data");
                imageView.setImageBitmap(photo);
                imString = savePhotoToSDCard(photo);
                isUpdateImage = true;
                new ImageUploadAsync(handler, imString, 1111).execute();
            }
            //			if (imgsign==1) {
            //				Bitmap photo = extras.getParcelable("data");
            //				img_cardback.setImageBitmap(photo);
            //				imString = savePhotoToSDCard(photo);
            //			}
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ImageUploadAsync.ImageUpload_Success) {
                uploadResult = (String) msg.obj;
                new UpdateMemberInfoAsync("logo", uploadResult, mContext, mHandler).execute();
            } else if(msg.what == ImageUploadAsync.ImageUpload_Fail){
                UIHelper.ToastMessage(getActivity(), "头像上传失败");
            }
        }

    };

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


    private void show() {
        disMiss();
        if (progressDialog == null) {
            progressDialog = LoadingDialog.createDialog(getActivity());
            progressDialog.setMessage("正在加载，请稍后");
            progressDialog.show();
        }
    }

    private void disMiss() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.rl_coupon:
                Constants.toActivity(getActivity(), ViewCouponActivity.class, null, false);
                break;
            case R.id.imageLayout:
                showDialog(v);
                break;
            case R.id.nickLayout:
                Bundle bundle = new Bundle();
                bundle.putInt("sign", 1);
                bundle.putString("info", textView5.getText().toString());
                Constants.toActivity(getActivity(), EditPersonalActivity.class, bundle);
                break;
            case R.id.set_qrcode:
                Constants.toActivity(getActivity(), QrCodeActivity.class, null);
                break;
            case R.id.set_suggest:
                Constants.toActivity(getActivity(), SuggestBackActivity.class, null);
                break;
            case R.id.my_pwd:
                MobclickAgent.onEvent(mContext, UMengKey.count_changepassword);

                Constants.toActivity(getActivity(), UpdatePwdActivity.class, null);
                break;
            case R.id.about_cloud:
                Constants.toActivity(getActivity(), AboutCloudActivity.class, null);
                break;
            case R.id.exitlayout:
                showPopMenu();
                break;
            case R.id.bt_cancel:
                mpopupWindow.dismiss();
                break;
            case R.id.bt_exitLogin:
                mpopupWindow.dismiss();
                Constants.isMcLogin = true;
                JPushInterface.setAliasAndTags(getActivity().getApplicationContext(), "", null, null);
                MemeberKeeper.clearOauth(getActivity());
                ((MainActivity) getActivity()).loginOut();
                break;
            case R.id.help://跳转到关于与帮助页面
                Constants.toActivity(getActivity(), AboutCloudActivity.class, null);
                break;
            case R.id.share_app://分享APP
                ShareDialog shareDialog = new ShareDialog(this.getContext(), sessionUrl, sessionTitle, sessionDescription, -1);
                shareDialog.show();
                MobclickAgent.onEvent(mContext, UMengKey.count_share_app);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_GETMEMBERINFO_SUCCESS:
                disMiss();
                if (isUpdateImage)
                    return false;
                Member member = (Member) msg.obj;
                if (!TextUtils.isEmpty(member.getNickname())) {
                    textView5.setText(member.getNickname());
                }
                if (member.getMobile() != null && !"".equals(member.getMobile())) {
                    textView4.setText(member.getMobile());
                }

                //if(!TextUtils.isEmpty(member.getLogo())){					//imageloader不需要判断非空,否则会沿用本地的照片
                MySharedPreferences.setSharedPString(MySharedPreferences.key_user_image_ + member.getMobile(), member.getLogo());
                headPic = member.getLogo();
                ImageLoader.getInstance().displayImage(member.getLogo(), imageView, Utils.displayImageOptions);
                //}
			/*if(!TextUtils.isEmpty(member.getIcphotoBack())){
				ImageLoader.getInstance().displayImage(member.getIcphotoBack(), img_cardback,Utils.displayImageOptions);
				backPic = member.getIcphotoBack();
			}
			if(!TextUtils.isEmpty(member.getIcphotoFont())){
				ImageLoader.getInstance().displayImage(member.getIcphotoFont(), img_cardfont,Utils.displayImageOptions);
				frontPic = member.getIcphotoFont();
			}*/

                break;
            case Constants.HANDLER_GETMEMBERINFO_FAIL:
                disMiss();
                Constants.MyToast("获取个人信息失败");
			/*Member memberOld = MemeberKeeper.getOauth(UpdateInfoActivity.this);
			if (!TextUtils.isEmpty(memberOld.getNickname())) {
				textView5.setText(memberOld.getNickname()); 
			}
			if (!TextUtils.isEmpty(memberOld.getName())) {
				textView1.setText(memberOld.getName());
			}
			if (!TextUtils.isEmpty(memberOld.getIdCard())) {
				textView2.setText(memberOld.getIdCard());
			}
			if (!TextUtils.isEmpty(memberOld.getEmail())) {
				textView3.setText(memberOld.getEmail());
			}
			if (!TextUtils.isEmpty(memberOld.getMemberBasic().getCode())) {
				textView4.setText(memberOld.getMemberBasic().getCode());
			}
			if(!TextUtils.isEmpty(memberOld.getLogo())){
				ImageLoader.getInstance().displayImage(memberOld.getLogo(), imageView,Utils.displayImageOptions);
			}
			if(memberOld.getMemberBasic()!=null&&memberOld.getMemberBasic().getSex()==1){
//				radioButton1.setChecked(true);
			}else if(memberOld.getMemberBasic()!=null&&memberOld.getMemberBasic().getSex()==2){
//				radioButton2.setChecked(true);
			}else{
//				radioButton3.setChecked(true);
			}*/
                break;
            case Constants.HANDLER_UPDATEMEMBERINFO_SUCCESS:
                isUpdateImage = false;
                disMiss();
                Constants.MyToast("保存成功");
                //			new GetMemberInfoAsync(null,mContext,mHandler).execute();
                break;
            case Constants.HANDLER_UPDATEMEMBERINFO_FAIL:
                isUpdateImage = false;
                disMiss();
                Constants.MyToast((String) msg.obj);
                break;
            case Constants.HANDLER_INTEGRAL_SUCCESS:
                Constants.isGetScore = true;
                ScoreInfo scoreInfo = (ScoreInfo) msg.obj;
                if (signBetweenTime != 0 && null != scoreInfo && scoreInfo.getPoint() > 0) {
                    score_layout.setVisibility(View.VISIBLE);
                    scrollView.setBackgroundResource(R.color.commom_transparent_bg);
                    score_num.setText("+" + scoreInfo.getPoint());
                }
                if (null != scoreInfo) {
                    Member mb = MemeberKeeper.getOauth(mContext);
                    if (null != mb) {
                        MySharedPreferences.setSharedPString(MySharedPreferences.key_score_update_time +
                                        String.valueOf(mb.getId()),
                                scoreInfo.getServerTime());
                    }
                    if (null != getActivity()) {
                        ((MainActivity) getActivity()).setSignBetweenTime(0);
                    }
                    score_text.setText(String.valueOf(scoreInfo.getPointAvailable()));
                }

                break;
            case Constants.HANDLER_INTEGRAL_FAIL:
                Constants.ToastAction((String) msg.obj);
                if (isIntegralAgain) {
                    isIntegralAgain = false;
                    new ScoreInfoAsync(0, mContext, mHandler).execute();
                }
                break;

        }
        return false;
    }

    private void showPopMenu() {
        View view = View.inflate(getActivity().getApplicationContext(), R.layout.popup_menu, null);
        Button bt_cancle = (Button) view.findViewById(R.id.bt_cancel);
        Button bt_exitLogin = (Button) view.findViewById(R.id.bt_exitLogin);
        bt_cancle.setOnClickListener(this);
        bt_exitLogin.setOnClickListener(this);
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                mpopupWindow.dismiss();
            }
        });

        view.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.push_bottom_in));

        if (mpopupWindow == null) {
            mpopupWindow = new PopupWindow(getActivity());
            mpopupWindow.setWidth(LayoutParams.MATCH_PARENT);
            mpopupWindow.setHeight(LayoutParams.MATCH_PARENT);
            mpopupWindow.setBackgroundDrawable(new BitmapDrawable());

            mpopupWindow.setFocusable(true);
            mpopupWindow.setOutsideTouchable(true);
            mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        mpopupWindow.setContentView(view);
        mpopupWindow.showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mpopupWindow.update();
    }


    @Override
    public void returnMemberInfo(Member member) {

    }

    @Override
    public void returnUserScoreInfo(ScoreInfo scoreInfo) {

    }
}
