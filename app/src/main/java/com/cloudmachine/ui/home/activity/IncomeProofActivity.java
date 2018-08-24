package com.cloudmachine.ui.home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.InputMoney;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PictureUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.utils.UploadPhotoUtils;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.CustomImageSpan;
import com.cloudmachine.widget.PicPop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class IncomeProofActivity extends BaseAutoLayoutActivity implements PicListAdapter.OnItemClickListener, View.OnClickListener, PicPop.OnPopUpdateListener {
    public static final String INCOME_PROOF = "income_proof";
    public static final String ENGINEER_CONTRACT = "engineer_contract";
    public static final String MACHINE_OWERSHIP = "machine_owership";
    LinearLayout mContentLayout;
    LinearLayout mIncomeContainer;
    View incomeLineView;
    ClearEditTextView mIncomeEdt;
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
        mContentLayout = (LinearLayout) findViewById(R.id.income_proof_layout);
        mIncomeContainer = (LinearLayout) findViewById(R.id.income_container);
        submitBtn = (RadiusButtonView) findViewById(R.id.income_proof_submit);
        incomeLineView=findViewById(R.id.income_container_line);
        promptTv = (TextView) findViewById(R.id.income_proof_prompt);
        proveTv = (TextView) findViewById(R.id.income_prove_tv);
        mIncomeEdt = (ClearEditTextView) findViewById(R.id.income_edt);
        mIncomeEdt.setFilters(new InputFilter[]{new InputMoney(mIncomeEdt)});
        mIncomeEdt.setFocusable(true);
        mIncomeEdt.setFocusableInTouchMode(true);
        mIncomeEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                AppLog.print("onEditorAction editonId__" + actionId);
                return false;
            }
        });

        String titleName = "";
        String promtText = "";
        switch (pageType) {
            case INCOME_PROOF:
                mIncomeContainer.setVisibility(View.VISIBLE);
                incomeLineView.setVisibility(View.VISIBLE);
                titleName = "收入证明";
                promtText = "请上传证明文件 (查看示例)";
                String prove1 = "  证明文件包括：工程承包收入打款记录(需对应工程施工承揽合同)、租赁方租赁费用打款记录(需对应工程机械设备租赁合同)，或近半年的主要收入银行卡交易流水记录，或微信、支付宝近三个月大额流水记录截图";
                setProveText(prove1);
                break;
            case ENGINEER_CONTRACT:
                resetDecorationView();
                titleName = "工程合同";
                promtText = "请拍摄工程施工承诺合同或工程机械设备租赁合同等 (查看示例)";
                break;
            case MACHINE_OWERSHIP:
                resetDecorationView();
                deviceId = getIntent().getIntExtra(Constants.DEVICE_ID, -1);
                titleName = getIntent().getStringExtra(Constants.DEVICE_NAME);
                promtText = "请拍摄所有权证明文件";
                String prove2 = "  证明文件包括：购买合同和购买发票(或打款凭证)，或按揭证明(如有)，或其他能够证明所有权的凭证(如工程车辆的有效行驶证)和设备上名牌、机架号等照片";
                setProveText(prove2);
                break;
        }
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.income_proof_title);
        titleView.setTitleName(titleName);
        titleView.setLeftClickListener(leftClickLi);
        mRecyclerView = (RecyclerView) findViewById(R.id.income_proof_rv);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PicListAdapter(this, selectedPics);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
            promptTv.setText(promtText);
        } else {
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
    }

    private void resetDecorationView() {
        mContentLayout.setBackgroundColor(getResources().getColor(R.color.white));
        promptTv.setBackgroundColor(getResources().getColor(R.color.public_bg));
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) submitBtn.getLayoutParams();
        params.topMargin= (int) getResources().getDimension(R.dimen.dimen_size_10);

    }

    private void setProveText(String prove){
        proveTv.setVisibility(View.VISIBLE);
        CustomImageSpan imgSpan=new CustomImageSpan(this,R.drawable.ic_prove);
        SpannableString spanStr=new SpannableString(prove);
        spanStr.setSpan(imgSpan,0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        proveTv.setText(spanStr);
    }

    private View.OnClickListener leftClickLi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedPics != null && selectedPics.size() > 0) {
                CommonUtils.showBackDialog(mContext);
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
                            String key = photos.get(i);
                            if (requestCode == PhotoPicker.REQUEST_CODE) {
                                String fileName = String.valueOf(System.currentTimeMillis());
                                Bitmap smallBitmap = PictureUtil.getSmallBitmap(key);
                                String filename = FileUtils.saveBitmap(smallBitmap, fileName);
                                //TODO: upload img test
                                UploadPhotoUtils.getInstance(this).upLoadFile(photos.get(i), filename, URLs.UPLOAD_AVATOR, mHandler);
                            } else {
                                if (selectPhotosMap.size() > 0) {
                                    if (selectPhotosMap.containsKey(key)) {
                                        tempMap.put(key, selectPhotosMap.get(key));
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
                        String fileName = String.valueOf(System.currentTimeMillis());
                        Bitmap smallBitmap = PictureUtil.getSmallBitmap(cmFilePath);
                        String filename = FileUtils.saveBitmap(smallBitmap, fileName);
                        //TODO: upload img test
                        UploadPhotoUtils.getInstance(this).upLoadFile(cmFilePath, filename, URLs.UPLOAD_AVATOR, mHandler);
                        selectedPics.add(cmFilePath);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToast(mContext, "添加图片失败");
                    }
                }
                break;
        }


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Constants.HANDLER_UPLOAD_SUCCESS) {
                Map<String, String> paramsMap = (Map<String, String>) msg.obj;
                selectPhotosMap.putAll(paramsMap);
                updateSubmitEnable();
            }


        }
    };

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
        if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).machineImgUpload(uniqueNo, pictureUrls, deviceId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
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
        } else {
            int bnsType = -1;
            String annualIncome = null;
            if (TextUtils.equals(pageType, ENGINEER_CONTRACT)) {
                bnsType = 1;
            } else if (TextUtils.equals(pageType, INCOME_PROOF)) {
                bnsType = 2;
                annualIncome = mIncomeEdt.getText().toString();
                if (TextUtils.isEmpty(annualIncome)){
                    ToastUtils.showToast(mContext,"年收入不能为空");
                    return;
                }
            }
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).perImgUpload(uniqueNo, pictureUrls, bnsType, annualIncome).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
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
            int maxCount = PhotoAdapter.MAX - selectedPics.size() + lastSelPics.size();
            if (maxCount < 0) {
                maxCount = 0;
            }
            if (maxCount > PhotoAdapter.MAX) {
                maxCount = PhotoAdapter.MAX;
            }
            PhotoPicker.builder()
                    .setPhotoCount(maxCount)
                    .setShowCamera(false)
                    .setPreviewEnabled(false)
                    .setSelected(lastSelPics)
                    .start(this);
        }
    }


}
