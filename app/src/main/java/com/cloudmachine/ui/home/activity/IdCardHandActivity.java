package com.cloudmachine.ui.home.activity;

import android.app.Activity;
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

public class IdCardHandActivity extends BaseAutoLayoutActivity implements PicPop.OnPopUpdateListener, View.OnClickListener {

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

    PicPop pickPop;
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
        initPrompt();
    }

    private void initPrompt() {
        Spanned sp0 = Html.fromHtml("<font color='#999999'>请拍摄/上传手</font><font color='#333333'>持身份证照片</font>");
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
            case R.id.radius_button_text:
                submitPic();
                break;
            case R.id.hand_shoot_container:
                showPickImagePop();
                break;
            case R.id.hand_delete_img:
                submitBtn.setButtonClickEnable(false);
                shootContainer.setEnabled(true);
                delteImg.setVisibility(View.GONE);
                shootedImg.setVisibility(View.GONE);
                idCardImg.setVisibility(View.GONE);
                shootCamera.setVisibility(View.VISIBLE);
                if (handFile != null && handFile.exists()) {
                    handFile.delete();
                }
                break;
            case R.id.hand_sample_preview:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PAGET_TYPE, PicPreviewActivity.PREVIEW_IDCARD);
                Constants.toActivity(this, PicPreviewActivity.class, bundle);
                break;
        }
    }

    private void submitPic() {
        mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).perImgUpload(uniquedNo, imgUrl, 0).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast(mContext, "提交成功");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
            }
        }));
    }

    private void showPickImagePop() {
        if (pickPop == null) {
            pickPop = new PicPop(mContext);
        }
        pickPop.setPickEnable(false);
        pickPop.setOnPopUpdateListener(this);
        pickPop.showAtLocation(getWindow().getDecorView(), Gravity.FILL, 0, 0);
    }

    @Override
    public void updateGuideView(int actionType) {
        if (mChecker.lacksPermissions(Constants.PERMISSIONS_CAMER_SD)) {
            PermissionsActivity.startActivityForResult(this, HomeActivity.PEM_REQCODE_CAMERA, Constants.PERMISSIONS_CAMER_SD);
        } else {
            gotoCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HomeActivity.PEM_REQCODE_CAMERA) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showCameraSDPermissionDialog(this);
            } else {
                gotoCamera();
            }

        } else if (requestCode == CameraActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                String path = extras.getString(CameraActivity.PIC_PATH);
                if (!TextUtils.isEmpty(path)) {
                    updateImage(path);
                }

            }

        }
    }

    private void gotoCamera() {
        Bundle bundle = new Bundle();
        bundle.putString(CameraActivity.PIC_TYPE, CameraActivity.TYPE_PIC_HAND);
        Constants.toActivityForR((Activity) mContext, CameraActivity.class, bundle, CameraActivity.REQUEST_CODE);
    }

    private void updateImage(String path) {
        AppLog.print("updateImg path__"+path);
        shootContainer.setEnabled(false);
        handFile = new File(path);
        Compressor.getDefault(mContext)
                .compressToFileAsObservable(handFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        QiniuManager.getUploadManager().put(file, "img_id_card_hand/" + file.getName(), QiniuManager.uptoken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, final JSONObject response) {
                                submitBtn.setButtonClickEnable(true);
                                imgUrl = QiniuManager.origin + key;
                                AppLog.print("updateImage  imgPath___"+imgUrl);
                                Glide.with(mContext).load(imgUrl).into(idCardImg);
                                idCardImg.setVisibility(View.VISIBLE);
                                shootCamera.setVisibility(View.GONE);
                                delteImg.setVisibility(View.VISIBLE);
                                shootedImg.setVisibility(View.VISIBLE);

                            }
                        }, null);
                    }
                });

    }

}
