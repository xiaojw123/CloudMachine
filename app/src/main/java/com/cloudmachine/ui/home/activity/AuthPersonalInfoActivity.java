package com.cloudmachine.ui.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.ui.home.contract.AuthPersonalInfoContract;
import com.cloudmachine.ui.home.model.AuthPersonalInfoModel;
import com.cloudmachine.ui.home.presenter.AuthPersonalInfoPresenter;
import com.cloudmachine.ui.homepage.activity.QuestionCommunityActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AuthPersonalInfoActivity extends BaseAutoLayoutActivity<AuthPersonalInfoPresenter, AuthPersonalInfoModel> implements View.OnClickListener, AuthPersonalInfoContract.View, ClearEditTextView.OnTextChangeListener {
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
    @BindView(R.id.pi_describe_tv)
    TextView describeTv;
    File portraitPicFile, emblemPicFile;
    PermissionsChecker mChecker;
    String type;
    String mRedisUserId;
    String portraitImgUrl, emblemImgUrl;
    long memberId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification_auth);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        memberId = UserHelper.getMemberId(this);
        boolean isComplted = getIntent().getBooleanExtra(InfoManagerActivity.KEY_COMPLETED, false);
        if (isComplted) {
            describeTv.setText("已完善个人信息");
            submitBtn.setVisibility(View.GONE);
            protraitDelteImg.setVisibility(View.GONE);
            portraitContainer.setVisibility(View.VISIBLE);
            emblemDeleteImg.setVisibility(View.GONE);
            emblemContainer.setVisibility(View.VISIBLE);
            portraitImg.setEnabled(false);
            emblemImg.setEnabled(false);
            mPresenter.getMemberAuthInfo(memberId);
        } else {
            mChecker = new PermissionsChecker(mContext);
            submitBtn.setButtonClickEnable(false);
            submitBtn.setButtonClickListener(this);
            nameEdt.setOnTextChangeListener(this);
            idEdt.setOnTextChangeListener(this);
        }
        nameEdt.setEnabled(false);
        idEdt.setEnabled(false);
        nameEdt.setNoClearIcon(true);
        idEdt.setNoClearIcon(true);
    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @OnClick({R.id.pi_emblem_delete, R.id.pi_portrait_delete, R.id.pi_portrait_img, R.id.pi_emblem_img, R.id.pi_guide_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pi_emblem_delete:
                emblemContainer.setVisibility(View.GONE);
                emblemImg.setImageResource(R.drawable.ic_id_pic2);
                emblemImg.setEnabled(true);
                if (emblemPicFile != null && emblemPicFile.exists()) {
                    emblemPicFile.delete();
                }
                break;
            case R.id.pi_portrait_delete:
                portraitContainer.setVisibility(View.GONE);
                portraitImg.setImageResource(R.drawable.ic_id_pic1);
                portraitImg.setEnabled(true);
                if (portraitPicFile != null && portraitPicFile.exists()) {
                    portraitPicFile.delete();
                }
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

                if (mChecker.lacksPermissions(Constants.PERMISSIONS_CAMER_SD)) {
                    PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_CAMERA, Constants.PERMISSIONS_CAMER_SD);
                } else {
                    gotoCamera();
                }
                break;
            case R.id.radius_button_text:
                if (portraitSuccessImg.getVisibility() == View.VISIBLE && emblemSuccessImg.getVisibility() == View.VISIBLE) {
                    mPresenter.submitIdUserInfo(memberId, mRedisUserId);
                } else {
                    ToastUtils.showToast(mContext, "正反面身份证都要拍摄，才能提交个人信息！");
                }
                break;
        }
    }


    private void gotoCamera() {
        Bundle bundle = new Bundle();
        bundle.putString(CameraActivity.PIC_TYPE, type);
        Constants.toActivityForR((Activity) mContext, CameraActivity.class, bundle, CameraActivity.REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HomeActivity.PEM_REQCODE_CAMERA:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showCameraSDPermissionDialog(this);
                } else {
                    gotoCamera();
                }
                break;
            case CameraActivity.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    String path = extras.getString(CameraActivity.PIC_PATH);
//                    String type = extras.getString(CameraActivity.PIC_TYPE);
//                    AppLog.print("takePhoto___path__" + path);
                    if (!TextUtils.isEmpty(path)) {
                        updateImage(path);
                    }

                }
                break;


        }


    }

    private void updateImage(String path) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitPicFile = new File(path);
            portraitImg.setEnabled(false);
            mPresenter.uploadFile(portraitPicFile);
        } else {
            emblemPicFile = new File(path);
            emblemImg.setEnabled(false);
            mPresenter.uploadFile(emblemPicFile);
        }
    }


    @Override
    public void updateMemberAuthInfo(String realName, String idCardNo) {
        nameEdt.setText(realName);
        idEdt.setText(idCardNo);
    }

    @Override
    public void returnVerifyOrcSuccess(String redisUserId, String realName, String idCardNo) {
        if (mRedisUserId == null) {
            mRedisUserId = redisUserId;
        }
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitContainer.setVisibility(View.VISIBLE);
            portraitContainer.setSelected(false);
            portraitSuccessImg.setVisibility(View.VISIBLE);
            portaritFailedTv.setVisibility(View.GONE);
        } else {
            emblemContainer.setVisibility(View.VISIBLE);
            emblemContainer.setSelected(false);
            emblemSuccessImg.setVisibility(View.VISIBLE);
            emblemFailedTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(realName) && !TextUtils.isEmpty(idCardNo)) {
            updateMemberAuthInfo(realName, idCardNo);
        }
    }

    @Override
    public void retrunVerifyOrcFailed() {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitContainer.setVisibility(View.VISIBLE);
            portraitContainer.setSelected(true);
            portraitSuccessImg.setVisibility(View.GONE);
            protraitDelteImg.setVisibility(View.VISIBLE);
            portaritFailedTv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(portraitImgUrl).into(portraitImg);
        } else {
            emblemContainer.setVisibility(View.VISIBLE);
            emblemContainer.setSelected(true);
            emblemSuccessImg.setVisibility(View.GONE);
            emblemFailedTv.setVisibility(View.VISIBLE);
            emblemDeleteImg.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(emblemImgUrl).into(emblemImg);
        }
    }

    @Override
    public void uploadFileSuccess(String imgUrl) {
        if (CameraActivity.TYPE_PIC_FRONT.equals(type)) {
            portraitImgUrl = imgUrl;
        } else {
            emblemImgUrl = imgUrl;
        }
        mPresenter.verifyOcr(memberId, imgUrl, mRedisUserId);
    }

    @Override
    public void submitSuccess() {
        ToastUtils.showToast(mContext, "提交成功");
        setResult(QuestionCommunityActivity.UPDATE_TIKCET);
        mRxManager.post(PurseActivity.UPDATE_AUTH_STATUS, null);
        finish();
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
