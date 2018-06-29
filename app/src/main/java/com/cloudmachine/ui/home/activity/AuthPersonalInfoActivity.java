package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.annotation.TargetApi;
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
import android.widget.Toast;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.ImageHepler;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.PicPop;

import java.io.File;
import java.io.FileInputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AuthPersonalInfoActivity extends BaseAutoLayoutActivity implements View.OnClickListener, ClearEditTextView.OnTextChangeListener {
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


    PermissionsChecker mChecker;
    String type;
    PicPop pickPop;


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
            portraitImg.setClickable(false);
            emblemImg.setClickable(false);
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

    @OnClick({R.id.pi_portrait_img, R.id.pi_emblem_img, R.id.pi_guide_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pi_emblem_delete:
                emblemContainer.setVisibility(View.GONE);
                emblemImg.setActivated(false);
                break;
            case R.id.pi_portrait_delete:
                portraitContainer.setVisibility(View.GONE);
                portraitImg.setActivated(false);
                break;
            case R.id.pi_portrait_img:
                type = CameraActivity.TYPE_PIC_FRONT;
                guideImg.setImageResource(R.drawable.ic_guide_shoot1);
                guideCotainer.setVisibility(View.VISIBLE);
                break;
            case R.id.pi_emblem_img:
                type = CameraActivity.TYPE_PIC_BG;
                guideImg.setImageResource(R.drawable.ic_guide_shoot2);
                guideCotainer.setVisibility(View.VISIBLE);
                break;
            case R.id.pi_guide_container:
                guideCotainer.setVisibility(View.GONE);
                if (mChecker.lacksPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_CAMERA,
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                } else {
                    showPickImagePop();
                }
                break;
            case R.id.radius_button_text:
                ToastUtils.showToast(mContext, "提交身份证信息");
                break;
        }
    }

    private void showPickImagePop() {
        if (pickPop == null) {
            pickPop = new PicPop(mContext);
        }
        pickPop.setPicType(type);
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
                    showPickImagePop();
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
                    } else {
                        ToastUtils.showToast(mContext, "拍照失败!");
                    }

                }
                break;
            case PicPop.REQUEST_PICK_IMAGE:
                String imgPath = ImageHepler.getPickImgePath(this, data);
                if (!TextUtils.isEmpty(imgPath)) {
                    updateImage(imgPath, type);
                } else {
                    ToastUtils.showToast(mContext, "图片路径不存在!");
                }
                break;


        }


    }

    private void updateImage(String path, String type) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitImg.setActivated(true);
            portraitContainer.setVisibility(View.VISIBLE);
        } else {
            emblemImg.setActivated(true);
            emblemContainer.setVisibility(View.VISIBLE);
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


}
