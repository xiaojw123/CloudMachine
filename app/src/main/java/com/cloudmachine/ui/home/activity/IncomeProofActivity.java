package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.adapter.PhotoAdapter;
import com.cloudmachine.adapter.PicListAdapter;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AuthDeviceItem;
import com.cloudmachine.bean.AuthInfoDetail;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.CustomImageSpan;
import com.cloudmachine.widget.PicPop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class IncomeProofActivity extends BaseAutoLayoutActivity implements PicListAdapter.OnItemClickListener, View.OnClickListener, PicPop.OnPopUpdateListener, ClearEditTextView.OnTextChangeListener, QiniuManager.OnUploadListener {
    public static final String INCOME_PROOF = "income_proof";
    public static final String ENGINEER_CONTRACT = "engineer_contract";
    public static final String MACHINE_OWERSHIP = "machine_owership";
    LinearLayout mContentLayout;
    RecyclerView mRecyclerView;
    RadiusButtonView submitBtn;
    TextView proveTv;
    TextView promptTv;
    PermissionsChecker mChecker;
    Map<String, String> selectPhotosMap = new HashMap<>();
    PicListAdapter mAdapter;
    ArrayList<String> selectedPics = new ArrayList<>();
    ArrayList<String> lastSelPics = new ArrayList<>();
    String pageType;
    String uniqueNo;
    int deviceId;
    PicPop picPop;
    ArrayList<String> mLastPics = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_proof);
        initView();

    }

    private void initView() {
        mChecker = new PermissionsChecker(this);
        pageType = getIntent().getStringExtra(Constants.PAGET_TYPE);
        uniqueNo = getIntent().getStringExtra(Constants.UNIQUEID);
        mRecyclerView = (RecyclerView) findViewById(R.id.income_proof_rv);
        mContentLayout = (LinearLayout) findViewById(R.id.income_proof_layout);
        submitBtn = (RadiusButtonView) findViewById(R.id.income_proof_submit);
        promptTv = (TextView) findViewById(R.id.income_proof_prompt);
        proveTv = (TextView) findViewById(R.id.income_prove_tv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PicListAdapter(this, selectedPics);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        String titleName = "";
        String promtText = "";
        switch (pageType) {
            case INCOME_PROOF:
                titleName = "收入证明";
                promtText = "请上传证明文件 (查看示例)";
                String prove1 = "  证明文件包括：工程承包收入打款记录(需对应工程施工承揽合同)、租赁方租赁费用打款记录(需对应工程机械设备租赁合同)，或近半年的主要收入银行卡交易流水记录，或微信、支付宝近三个月大额流水记录截图";
                setProveText(prove1);
                getPersonalInfo(uniqueNo, InfoAuthActivity.BNS_TYPE_INCOME);
                break;
            case ENGINEER_CONTRACT:
                resetDecorationView();
                titleName = "工程合同";
                promtText = "请拍摄工程施工承诺合同或工程机械设备租赁合同等 (查看示例)";
                getPersonalInfo(uniqueNo, InfoAuthActivity.BNS_TYPE_ENGINEER);
                break;
            case MACHINE_OWERSHIP:
                resetDecorationView();
                deviceId = getIntent().getIntExtra(Constants.DEVICE_ID, -1);
                titleName = getIntent().getStringExtra(Constants.DEVICE_NAME);
                promtText = "请拍摄所有权证明文件 (查看示例)";
                String prove2 = "  证明文件包括：购买合同和购买发票(或打款凭证)，或按揭证明(如有)，或其他能够证明所有权的凭证(如工程车辆的有效行驶证)和设备上名牌、机架号等照片";
                setProveText(prove2);
                getDevicePic();
                break;
        }
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.income_proof_title);
        titleView.setTitleName(titleName);
        titleView.setLeftClickListener(leftClickLi);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        SpannableStringBuilder ssb = new SpannableStringBuilder(promtText);
        ssb.clearSpans();
        int len = ssb.length();
        int start = len - 5;
        int end = len - 1;
        promptTv.setMovementMethod(LinkMovementMethod.getInstance());
        promptTv.setHighlightColor(Color.TRANSPARENT);
        ssb.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        promptTv.setText(ssb);
    }

    @Override
    protected void returnInfoDetail(AuthInfoDetail detail) {
        List<AuthInfoDetail.PictureBean> picItems = detail.getPicture();
        if (picItems != null && picItems.size() > 0) {
            for (AuthInfoDetail.PictureBean item : picItems) {
                String imgUrl = item.getPicUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    selectedPics.add(imgUrl);
                    Map<String, String> pm = new HashMap<>();
                    pm.put(imgUrl, imgUrl);
                    selectPhotosMap.putAll(pm);
                }
            }
        }
        mLastPics.addAll(selectedPics);
        mAdapter.notifyDataSetChanged();
    }

    private void getDevicePic() {
        if (isNewAdd()) {
            return;
        }
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getDeviceAudit(uniqueNo, deviceId).compose(RxHelper.<List<AuthDeviceItem>>handleResult()).subscribe(new RxSubscriber<List<AuthDeviceItem>>(mContext) {
            @Override
            protected void _onNext(List<AuthDeviceItem> authDeviceItems) {
                if (authDeviceItems != null && authDeviceItems.size() > 0) {
                    isInfoUpdate = true;
                    for (AuthDeviceItem item : authDeviceItems) {
                        String picUrl = item.getDevicePicUrl();
                        if (!TextUtils.isEmpty(picUrl)) {
                            selectedPics.add(picUrl);
                            Map<String, String> pm = new HashMap<>();
                            pm.put(picUrl, picUrl);
                            selectPhotosMap.putAll(pm);
                        }
                    }
                    mLastPics.addAll(selectedPics);
                    mAdapter.notifyDataSetChanged();
                }


            }

            @Override
            protected void _onError(String message) {

            }
        }));


    }

    private void resetDecorationView() {
        mContentLayout.setBackgroundColor(getResources().getColor(R.color.white));
        promptTv.setBackgroundColor(getResources().getColor(R.color.public_bg));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) submitBtn.getLayoutParams();
        params.topMargin = (int) getResources().getDimension(R.dimen.dimen_size_10);

    }

    private void setProveText(String prove) {
        proveTv.setVisibility(View.VISIBLE);
        CustomImageSpan imgSpan = new CustomImageSpan(this, R.drawable.ic_prove);
        SpannableString spanStr = new SpannableString(prove);
        spanStr.setSpan(imgSpan, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        proveTv.setText(spanStr);
    }

    private View.OnClickListener leftClickLi = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (selectedPics != null && selectedPics.size() > 0) {
                if (submitBtn.isClickEnable()) {
                    CommonUtils.showBackDialog(mContext);
                } else {
                    finish();
                }
            } else {
                finish();
            }
        }
    };

    private ClickableSpan clickSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            Bundle bundle = new Bundle();
            if (TextUtils.equals(pageType, INCOME_PROOF)) {
                bundle.putString(Constants.PAGET_TYPE, PicPreviewActivity.PREVIEW_INCOME);
            } else if (TextUtils.equals(pageType, ENGINEER_CONTRACT)) {
                bundle.putString(Constants.PAGET_TYPE, PicPreviewActivity.PRERIVEW_ENGER);
            } else if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
                bundle.putString(Constants.PAGET_TYPE, PicPreviewActivity.PRERIVEW_MACHINE);
            }
            Constants.toActivity(IncomeProofActivity.this, PicPreviewActivity.class, bundle);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(getResources().getColor(R.color.c_ff8901));
        }
    };

    @Override
    public void initPresenter() {

    }

    @Override
    public void onItemClick(View view, int position) {
        int type = mAdapter.getItemViewType(position);
        if (type == PicListAdapter.TYPE_ADD) {
            if (TextUtils.equals(pageType, INCOME_PROOF)) {
                showPicPop();
            } else {
                updateGuideView(PicPop.TYPE_CAMERA);
            }
        } else {
            PhotoPreview.builder()
                    .setPhotos(selectedPics)
                    .setCurrentItem(position)
                    .start(this);
        }


    }

    @Override
    public void onItemDeleteClick(String picKey) {
        if (lastSelPics.contains(picKey)) {
            lastSelPics.remove(picKey);
        }
        if (selectPhotosMap.containsKey(picKey)) {
            selectPhotosMap.remove(picKey);
        }
        updateSubmitEnable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_PIC_CAMERA:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE_CAMERA);
                } else {
                    startCameraOrAlbum();
                }
                break;
            case PhotoPicker.REQUEST_CODE:
            case PhotoPreview.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> photos = null;
                    ArrayList<String> delPhotos = null;
                    if (data != null) {
                        photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                        delPhotos = data.getStringArrayListExtra(PhotoPicker.KEY_DEL_PHOTOS);
                    }
                    if (requestCode == PhotoPicker.REQUEST_CODE) {
                        if (selectedPics.containsAll(lastSelPics)) {
                            for (String key : lastSelPics) {
                                selectPhotosMap.remove(key);
                            }
                            selectedPics.removeAll(lastSelPics);
                        }
                    } else {
                        selectedPics.clear();
                    }
                    Map<String, String> tempMap = new HashMap<>();
                    if (photos != null && photos.size() > 0) {
                        for (int i = 0; i < photos.size(); i++) {
                            String photoKey = photos.get(i);
                            if (requestCode == PhotoPicker.REQUEST_CODE) {
                                QiniuManager.uploadOriginalFile(mContext, this, new File(photoKey), "img_proof/");
                            } else {
                                if (selectPhotosMap.size() > 0) {
                                    if (selectPhotosMap.containsKey(photoKey)) {
                                        tempMap.put(photoKey, selectPhotosMap.get(photoKey));
                                    }
                                }
                            }
                        }
                        if (requestCode == PhotoPicker.REQUEST_CODE) {
                            lastSelPics = photos;
                        } else {
                            if (delPhotos != null && delPhotos.size() > 0) {
                                for (String delPic : delPhotos) {
                                    lastSelPics.remove(delPic);
                                }
                            }
                        }
                        selectedPics.addAll(photos);
                    } else {
                        lastSelPics.clear();
                    }
                    if (requestCode == PhotoPreview.REQUEST_CODE) {
                        selectPhotosMap = tempMap;
                    }
                    updateSubmitEnable();
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case REQ_CAMERA_PROOF:
                if (resultCode == RESULT_OK) {
                    if (cmFilePath != null) {
                        QiniuManager.uploadOriginalFile(mContext, this, new File(cmFilePath), "img_proof/");
                        selectedPics.add(cmFilePath);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(mContext, "添加图片失败");
                    }
                }
                break;
        }


    }


    private void updateSubmitEnable() {
        if (selectPhotosMap.size() > 0) {
            submitBtn.setButtonClickEnable(true);
        } else {
            submitBtn.setButtonClickEnable(false);
        }
    }

    protected void showPicPop() {
        if (picPop == null) {
            picPop = new PicPop(this);
            picPop.setOnPopUpdateListener(this);
        }
        picPop.showAtLocation(getWindow().getDecorView(), 0, 0, Gravity.FILL);
    }

    @Override
    public void onClick(View v) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : selectPhotosMap.entrySet()) {
            sb.append(entry.getValue());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        String pictureUrls = sb.toString();
        AppLog.print("uniqueNo____" + uniqueNo + " , pictureUrls___" + pictureUrls);
        if (isInfoUpdate) {
            String deviceIdStr = null;
            int bnsType = -1;
            if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
                deviceIdStr = String.valueOf(deviceId);
                bnsType = InfoAuthActivity.BNS_TYPE_DEVICE;
            } else if (TextUtils.equals(pageType, ENGINEER_CONTRACT)) {
                bnsType = InfoAuthActivity.BNS_TYPE_ENGINEER;
            } else if (TextUtils.equals(pageType, INCOME_PROOF)) {
                bnsType = InfoAuthActivity.BNS_TYPE_INCOME;
            }
            mRxManager.add(Api.getDefault(HostType.HOST_LARK).updatePersonalInformation(bnsType, uniqueNo, null, null, deviceIdStr, pictureUrls).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
                @Override
                protected void _onNext(String s) {
                    showSuccessDialog();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext, "提交失败！");

                }
            }));
        } else {
            if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
                mRxManager.add(Api.getDefault(HostType.HOST_LARK).machineImgUpload(uniqueNo, pictureUrls, deviceId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    protected void _onNext(String s) {
                        showSuccessDialog();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, message);

                    }
                }));
            } else {
                int bnsType = -1;
                if (TextUtils.equals(pageType, ENGINEER_CONTRACT)) {
                    bnsType = 1;
                } else if (TextUtils.equals(pageType, INCOME_PROOF)) {
                    bnsType = 2;
                }
                mRxManager.add(Api.getDefault(HostType.HOST_LARK).perImgUpload(uniqueNo, pictureUrls, bnsType).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
                    @Override
                    protected void _onNext(String s) {
                        showSuccessDialog();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtils.showToast(mContext, message);
                    }
                }));

            }
        }
    }

    private void showSuccessDialog() {
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
    public void updateGuideView(int actionType) {
        mActionType = actionType;
        if (mChecker.lacksPermissions(Constants.PERMISSIONS_CAMER_SD)) {
            PermissionsActivity.startActivityForResult(this, REQ_PIC_CAMERA,
                    Constants.PERMISSIONS_CAMER_SD);
        } else {
            startCameraOrAlbum();
        }
    }

    private void startCameraOrAlbum() {
        if (mActionType == PicPop.TYPE_CAMERA) {
            startCamera();
        } else if (mActionType == PicPop.TYPE_PICK) {
            int maxCount = PhotoAdapter.MAX2 - selectedPics.size() + lastSelPics.size();
            if (maxCount < 0) {
                maxCount = 0;
            }
            if (maxCount > PhotoAdapter.MAX2) {
                maxCount = PhotoAdapter.MAX2;
            }
            PhotoPicker.builder()
                    .setPhotoCount(maxCount)
                    .setShowCamera(false)
                    .setPreviewEnabled(false)
                    .setSelected(lastSelPics)
                    .start(this);
        }
    }


    @Override
    public void uploadSuccess(String key, String picUrl) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(key, picUrl);
        selectPhotosMap.putAll(paramsMap);
        updateSubmitEnable();
    }

    @Override
    public void uploadFailed() {

    }

    @Override
    public void textChanged(Editable s) {
        updateSubmitEnable();
    }
}
