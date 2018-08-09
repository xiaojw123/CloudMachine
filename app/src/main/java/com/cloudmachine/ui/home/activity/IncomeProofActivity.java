package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.adapter.BaseRecyclerAdapter;
import com.cloudmachine.adapter.PhotoAdapter;
import com.cloudmachine.adapter.PicListAdapter;
import com.cloudmachine.adapter.decoration.SpaceItemDecoration;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.ExtrContract;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PictureUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.utils.UploadPhotoUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.yanzhenjie.recyclerview.swipe.widget.GridItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class IncomeProofActivity extends BaseAutoLayoutActivity implements PicListAdapter.OnItemClickListener, View.OnClickListener {
    public static final String INCOME_PROOF = "income_proof";
    public static final String ENGINEER_CONTRACT = "engineer_contract";
    public static final String MACHINE_OWERSHIP = "machine_owership";
    RecyclerView mRecyclerView;
    RadiusButtonView submitBtn;
    static final String[] PERMISSIONS_PICK = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int REQUEST_PERMISSION_PICK = 0x008;  //权限请求

    PermissionsChecker mChecker;
    Map<String, String> selectPhotosMap = new HashMap<>();
    PicListAdapter mAdapter;
    protected ArrayList<String> selectedPics = new ArrayList<>();
    String pageType;
    String uniqueNo;
    int deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_proof);
        initView();

    }

    private void initView() {
        mChecker = new PermissionsChecker(this);
        pageType = getIntent().getStringExtra(Constants.PAGET_TYPE);
        uniqueNo=getIntent().getStringExtra(Constants.UNIQUEID);
        String titleName = "";
        String promtText = "";
        switch (pageType) {
            case INCOME_PROOF:
                titleName = "收入证明";
                promtText = "拍摄/上传支付宝、微信、银行卡收入流水 (查看示例)";
                break;
            case ENGINEER_CONTRACT:
                titleName = "工程合同";
                promtText = "请上传工程合同 (查看示例)";
                break;
            case MACHINE_OWERSHIP:
                deviceId=getIntent().getIntExtra(Constants.DEVICE_ID,-1);
                titleName = getIntent().getStringExtra(Constants.DEVICE_NAME);
                promtText = "请拍摄该机器清晰的购买合同，发票和机器上铭牌、机架号等照片";
                break;
        }
        CommonTitleView titleView = (CommonTitleView) findViewById(R.id.income_proof_title);
        titleView.setTitleName(titleName);
        mRecyclerView = (RecyclerView) findViewById(R.id.income_proof_rv);
        submitBtn = (RadiusButtonView) findViewById(R.id.income_proof_submit);
        submitBtn.setButtonClickEnable(false);
        submitBtn.setButtonClickListener(this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new PicListAdapter(this, selectedPics);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        TextView promptTv = (TextView) findViewById(R.id.income_proof_prompt);
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
            if (mChecker.lacksPermissions(PERMISSIONS_PICK)) {
                PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION_PICK,
                        PERMISSIONS_PICK);
            } else {
                pickPhotoCamera();
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
        if (selectPhotosMap.containsKey(picKey)) {
            selectPhotosMap.remove(picKey);
        }
        if (selectPhotosMap.size() > 0) {
            submitBtn.setButtonClickEnable(true);
        } else {
            submitBtn.setButtonClickEnable(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_PIC_CAMERA:
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    CommonUtils.showPermissionDialog(this, Constants.PermissionType.STORAGE);
                } else {
                    pickPhotoCamera();
                }
                break;
            case PhotoPicker.REQUEST_CODE:
            case PhotoPreview.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> photos = null;
                    if (data != null) {
                        photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    }
                    selectedPics.clear();
                    Map<String, String> tempMap = new HashMap<>();
                    if (photos != null && photos.size() > 0) {
                        for (int i = 0; i < photos.size(); i++) {
                            if (requestCode == PhotoPicker.REQUEST_CODE) {
                                String fileName = String.valueOf(System.currentTimeMillis());
                                Bitmap smallBitmap = PictureUtil.getSmallBitmap(photos.get(i));
                                String filename = FileUtils.saveBitmap(smallBitmap, fileName);
                                UploadPhotoUtils.getInstance(this).upLoadFile(photos.get(i), filename, URLs.UPLOAD_AVATOR, mHandler);
                            } else {
                                if (selectPhotosMap.size() > 0) {
                                    String key = photos.get(i);
                                    AppLog.print("preive photos  key__" + key);
                                    if (selectPhotosMap.containsKey(key)) {
                                        tempMap.put(key, selectPhotosMap.get(key));
                                    }
                                }
                            }
                        }
                        selectedPics.addAll(photos);
                    }
                    if (requestCode == PhotoPreview.REQUEST_CODE) {
                        selectPhotosMap = tempMap;
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//                String url = (String) msg.obj;
            //向集合添加选中的url
//                selectPhotos.add(url);
            if (msg.what == Constants.HANDLER_UPLOAD_SUCCESS) {
                Map<String, String> paramsMap = (Map<String, String>) msg.obj;
                selectPhotosMap.putAll(paramsMap);
                if (selectPhotosMap.size() > 0) {
                    submitBtn.setButtonClickEnable(true);
                }
            }


        }
    };

    protected void pickPhotoCamera() {
        PhotoPicker.builder()
                .setPhotoCount(PhotoAdapter.MAX)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .setSelected(selectedPics)
                .start(this);
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
        AppLog.print("uniqueNo____"+uniqueNo+" , pictureUrls___"+pictureUrls);
        if (TextUtils.equals(pageType, MACHINE_OWERSHIP)) {
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).machineImgUpload(uniqueNo, pictureUrls, deviceId).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
                @Override
                protected void _onNext(String s) {
                    ToastUtils.showToast(mContext,"提交成功");
                    finish();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext,message);

                }
            }));
        } else {
            int bnsType=-1;
            if (TextUtils.equals(pageType, ENGINEER_CONTRACT)) {
                bnsType = 1;
            } else if (TextUtils.equals(pageType, INCOME_PROOF)) {
                bnsType = 2;
            }
            mRxManager.add(Api.getDefault(HostType.HOST_CLOUDM).perImgUpload(uniqueNo, pictureUrls, bnsType).compose(RxHelper.<String>handleResult()).subscribe(new RxSubscriber<String>(mContext) {
                @Override
                protected void _onNext(String s) {
                    ToastUtils.showToast(mContext,"提交成功");
                    finish();
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(mContext,message);
                }
            }));

        }

    }
}
