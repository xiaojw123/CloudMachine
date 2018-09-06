package com.cloudmachine.ui.home.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.PicPop;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class IdCardHandActivity extends BaseAutoLayoutActivity implements View.OnClickListener, QiniuManager.OnUploadListener {

    @BindView(R.id.hand_shoot_action)
    TextView handShootAction;
    @BindView(R.id.hand_shoot_sample)
    TextView handShootSample;
    @BindView(R.id.hand_sample_preview)
    TextView previewTv;
    @BindView(R.id.hand_delete_img)
    ImageView delteImg;
    @BindView(R.id.hand_shoot_container)
    FrameLayout shootContainer;
    @BindView(R.id.hand_idcard_img)
    ImageView idCardImg;
    @BindView(R.id.hand_shoot_camera)
    TextView shootCamera;
    @BindView(R.id.hand_shooted_img)
    ImageView shootedImg;
    @BindView(R.id.bank_verify_submit_btn)
    RadiusButtonView submitBtn;
    @BindView(R.id.hand_title_ctv)
    CommonTitleView titleView;

    PermissionsChecker mChecker;
    String uniquedNo;
    String imgUrl;
    File handFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_hand);
        ButterKnife.bind(this);
        mChecker = new PermissionsChecker(this);
        uniquedNo = getIntent().getStringExtra(Constants.UNIQUEID);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        titleView.setLeftClickListener(this);
        initPrompt();
    }

    private void initPrompt() {
        Spanned sp0 = Html.fromHtml("<font color='#999999'>请拍摄</font><font color='#333333'>手持身份证照片</font>");
        handShootAction.setText(sp0);
        Spanned sp1 = Html.fromHtml("<font color='#333333'>拍照示例</font><font color='#999999'>(面部五官和身份证信息清晰完整)</font>");
        handShootSample.setText(sp1);
    }

    @Override
    public void initPresenter() {

    }

    @OnClick({R.id.hand_sample_preview, R.id.hand_delete_img, R.id.hand_shoot_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_titleview_back_img:
                if (shootedImg.getVisibility() == View.VISIBLE) {
                    CommonUtils.showBackDialog(this);
                } else {
                    finish();
                }
                break;

            case R.id.radius_button_text:
                submitPic();
                break;
            case R.id.hand_shoot_container:
                if (mChecker.lacksPermissions(Constants.PERMISSIONS_CAMER_SD)) {
                    PermissionsActivity.startActivityForResult(this, REQ_PIC_CAMERA, Constants.PERMISSIONS_CAMER_SD);
                } else {
                    startCamera();
                }
                break;
            case R.id.hand_delete_img:
                CommonUtils.showDeletePicDialog(this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitBtn.setButtonClickEnable(false);
                        shootContainer.setEnabled(true);
                        delteImg.setVisibility(View.GONE);
                        shootedImg.setVisibility(View.GONE);
                        idCardImg.setVisibility(View.GONE);
                        shootCamera.setVisibility(View.VISIBLE);
                        if (handFile != null && handFile.exists()) {
                            handFile.delete();
                        }
                    }
                });
                break;
            case R.id.hand_sample_preview:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PAGET_TYPE, PicPreviewActivity.PREVIEW_IDCARD);
                Constants.toActivity(this, PicPreviewActivity.class, bundle);
                break;
        }
    }

    private void submitPic() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).perImgUpload(uniquedNo, imgUrl, 0, null).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
                builder.setAlertIcon(R.drawable.ic_sync_loading);
                builder.setMessage("提交成功, 请耐心等待审核...");
                builder.setNeutralButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                CustomDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_PIC_CAMERA) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showCameraSDPermissionDialog(this);
            } else {
                startCamera();
            }

        } else if (requestCode == REQ_CAMERA_PROOF) {
            if (resultCode == RESULT_OK) {
                if (!TextUtils.isEmpty(cmFilePath)) {
                    updateImage();
                } else {
                    ToastUtils.showToast(mContext, "拍摄照片失败");
                }

            }

        }
    }


    private void updateImage() {
        AppLog.print("updateImg path__" + cmFilePath);
        shootContainer.setEnabled(false);
        handFile = new File(cmFilePath);
        QiniuManager.uploadFile(mContext,this,handFile,"img_id_card_hand/");
    }

    @Override
    public void uploadSuccess(String picUrl) {
        submitBtn.setButtonClickEnable(true);
        imgUrl =picUrl;
        Glide.with(mContext).load(imgUrl).into(idCardImg);
        idCardImg.setVisibility(View.VISIBLE);
        shootCamera.setVisibility(View.GONE);
        delteImg.setVisibility(View.VISIBLE);
        shootedImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void uploadFailed() {
        shootContainer.setEnabled(true);
    }
}
