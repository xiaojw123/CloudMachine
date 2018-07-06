package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.ImageHepler;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PhotosGallery;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.PicPop;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import retrofit2.http.Field;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class AuthPersonalInfoActivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener, PicPop.OnPopUpdateListener {
    public static final int REQUEST_PICK_IMAGE = 0x16;
    @BindView(R.id.pi_portrait_img)
    ImageView portraitImg;
    @BindView(R.id.pi_emblem_img)
    ImageView emblemImg;
    @BindView(R.id.pi_name_edt)
    ClearEditTextView nameEdt;
    @BindView(R.id.pi_id_edt)
    ClearEditTextView idEdt;
    @BindView(R.id.pi_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.pi_guide_container)
    FrameLayout guideCotainer;
    @BindView(R.id.pi_guide_img)
    ImageView guideImg;
    @BindView(R.id.pi_portrait_container)
    FrameLayout portraitContainer;
    @BindView(R.id.pi_emblem_container)
    FrameLayout emblemContainer;
    @BindView(R.id.pi_emblem_delete)
    ImageView emblemDeleteImg;
    @BindView(R.id.pi_portrait_delete)
    ImageView protraitDelteImg;
    @BindView(R.id.emblem_success_img)
    ImageView emblemSuccessImg;
    @BindView(R.id.emblem_failed_tv)
    TextView emblemFailedTv;
    @BindView(R.id.portrait_success_img)
    ImageView portraitSuccessImg;
    @BindView(R.id.portrait_failed_tv)
    TextView portaritFailedTv;
    File portraitPicFile, emblemPicFile;
    PermissionsChecker mChecker;
    String type;
    PicPop pickPop;
    int mActionType;
    String redisUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification_auth);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        boolean isComplted = getIntent().getBooleanExtra(InfoManagerActivity.KEY_COMPLETED, false);
        if (isComplted) {
            portraitImg.setEnabled(false);
            emblemImg.setEnabled(false);
            nameEdt.setEnabled(false);
            idEdt.setEnabled(false);
        }
        mChecker = new PermissionsChecker(mContext);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        nameEdt.setOnTextChangeListener(this);
        idEdt.setOnTextChangeListener(this);
    }


    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.pi_emblem_delete, R.id.pi_portrait_delete, R.id.pi_portrait_img, R.id.pi_emblem_img, R.id.pi_guide_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pi_emblem_delete:
                emblemContainer.setVisibility(View.GONE);
                emblemImg.setEnabled(true);
                if (emblemPicFile != null && emblemPicFile.exists()) {
                    emblemPicFile.delete();
                }
                break;
            case R.id.pi_portrait_delete:
                portraitContainer.setVisibility(View.GONE);
                portraitImg.setEnabled(true);
                if (portraitPicFile != null && portraitPicFile.exists()) {
                    portraitPicFile.delete();
                }
                break;
            case R.id.pi_portrait_img:
                type = CameraActivity.TYPE_PIC_FRONT;
                showPickImagePop();
                break;
            case R.id.pi_emblem_img:
                type = CameraActivity.TYPE_PIC_BG;
                showPickImagePop();
                break;
            case R.id.pi_guide_container:
                guideCotainer.setVisibility(View.GONE);

                if (mChecker.lacksPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_CAMERA,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    gotoPickPage();
                }
                break;
            case R.id.radius_button_text:
                submitUserInfo();
                break;
        }
    }
    private void submitUserInfo(){
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).submitIdUserInfo(UserHelper.getMemberId(this),redisUserId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast(mContext,s);
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);
            }
        }));
    }

    private void gotoPickPage() {
        if (mActionType == PicPop.TYPE_CAMERA) {
            Bundle bundle = new Bundle();
            bundle.putString(CameraActivity.PIC_TYPE, type);
            Constants.toActivityForR((Activity) mContext, CameraActivity.class, bundle, CameraActivity.REQUEST_CODE);
        } else if (mActionType == PicPop.TYPE_PICK) {
            startActivityForResult(PhotosGallery.gotoPhotosGallery(),
                    REQUEST_PICK_IMAGE);
        }
    }

    private void showPickImagePop() {
        if (pickPop == null) {
            pickPop = new PicPop(mContext);
        }
        pickPop.setOnPopUpdateListener(this);
        pickPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HomeActivity.PEM_REQCODE_CAMERA:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    ToastUtils.showToast(this, "需要开启相机和SD卡读写权限！！");
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.CAMERA);
                } else {
                    gotoPickPage();
                }
                break;
            case CameraActivity.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String path = extras.getString(CameraActivity.PIC_PATH);
                    String type = extras.getString(CameraActivity.PIC_TYPE);
                    AppLog.print("takePhoto___path__" + path);
                    if (!TextUtils.isEmpty(path)) {
                        updateImage(path, type);
                    }

                }
                break;
            case REQUEST_PICK_IMAGE:
                String imgPath = ImageHepler.getPickImgePath(this, data);
                if (!TextUtils.isEmpty(imgPath)) {
                    updateImage(imgPath, type);
                }
                break;


        }


    }

    private void updateImage(String path, String type) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitPicFile = new File(path);
            portraitImg.setEnabled(false);
            portraitContainer.setVisibility(View.VISIBLE);
            verifyIdImg(portraitPicFile, type);
        } else {
            emblemPicFile = new File(path);
            emblemImg.setEnabled(false);
            emblemContainer.setVisibility(View.VISIBLE);
            verifyIdImg(emblemPicFile, type);
        }

//        File file = new File(path);
//        FileInputStream inStream = null;
//        try {
//            inStream = new FileInputStream(file);
//            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
//
//            inStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //验证身份证照片
    public void verifyIdImg(File file, final String type) {
        Compressor.getDefault(this)
                .compressToFileAsObservable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        QiniuManager.getUploadManager().put(portraitPicFile, "img_id_card/" + portraitPicFile.getName(), QiniuManager.uptoken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, final JSONObject response) {
                                final String imgUrl = QiniuManager.origin + key;
                                mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM_YJX).verifyOcr(UserHelper.getMemberId(mContext), imgUrl).subscribe(new RxSubscriber<BaseRespose<JsonObject>>(mContext) {
                                    @Override
                                    protected void _onNext(BaseRespose<JsonObject> br) {
                                        if (br.success()) {
                                            JsonObject resultJobj = br.getResult();
                                            JsonElement j1 = resultJobj.get("redisUserId");
                                            if (j1 != null) {
                                                String j1Str = j1.getAsString();
                                                if (!TextUtils.isEmpty(j1Str)) {
                                                    redisUserId = j1Str;
                                                }
                                            }
                                            JsonElement j2 = resultJobj.get("realName");
                                            if (j2 != null) {
                                                String j2Str = j2.getAsString();
                                                if (!TextUtils.isEmpty(j2Str)) {
                                                    nameEdt.setText(j2Str);
                                                }
                                            }
                                            JsonElement j3 = resultJobj.get("idCardNo");
                                            if (j3 != null) {
                                                String j3Str = j3.getAsString();
                                                if (!TextUtils.isEmpty(j3Str)) {
                                                    idEdt.setText(j3Str);
                                                }
                                            }
                                            updateVertifySuccess(type);
                                        } else {
                                            updateVertifyFailed(type, imgUrl);
                                        }
                                    }

                                    @Override
                                    protected void _onError(String message) {
                                        updateVertifyFailed(type, imgUrl);
                                    }
                                }));


                            }
                        }, null);
                    }
                });

    }

    private void updateVertifyFailed(String type, String imgUrl) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitContainer.setSelected(true);
            portraitSuccessImg.setVisibility(View.GONE);
            portaritFailedTv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(imgUrl).into(portraitImg);
        } else {
            emblemContainer.setSelected(true);
            emblemSuccessImg.setVisibility(View.GONE);
            emblemFailedTv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(imgUrl).into(emblemImg);
        }
    }

    private void updateVertifySuccess(String type) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitContainer.setSelected(false);
            portraitSuccessImg.setVisibility(View.VISIBLE);
            portaritFailedTv.setVisibility(View.GONE);
        }else{
            emblemContainer.setSelected(false);
            emblemSuccessImg.setVisibility(View.VISIBLE);
            emblemFailedTv.setVisibility(View.GONE);
        }
    }


    @Override
    public void textChanged(Editable s) {
        String nameStr = nameEdt.getText().toString();
        String idStr = idEdt.getText().toString();
        if (nameStr.length() > 0 && idStr.length() > 0) {
            submitBtn.setButtonClickEnable(true);
        } else {
            submitBtn.setButtonClickEnable(false);
        }
    }


    @Override
    public void updateGuideView(int actionType) {
        mActionType = actionType;
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            guideImg.setImageResource(R.drawable.ic_guide_shoot1);
            guideCotainer.setVisibility(View.VISIBLE);
        } else {
            guideImg.setImageResource(R.drawable.ic_guide_shoot2);
            guideCotainer.setVisibility(View.VISIBLE);
        }


    }
}
