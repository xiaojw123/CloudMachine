package com.cloudmachine.activities;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CanBeEditItemView;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.EditListInfo;
import com.cloudmachine.bean.LarkDeviceBasicDetail;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.cloudmachine.widget.ReboundScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看页面基本信息
 */
public class AddDeviceActivity extends BaseAutoLayoutActivity implements  OnClickListener {
    public static final int UPATE_INFO = 0x1313;
    private Context mContext;
    private CanBeEditItemView device_name, device_type, device_brand1, device_model, device_year,
            device_owner, device_phone_number, device_rackId, device_snId, device_workTime;
    private String deviceType, deviceBrand, deviceModel;
    private int typeId, brandId, modeId;
    private long deviceId;

    public static Bitmap bimap;
    private ImageView decive_image, name_image, engine_image;
    private String decive_image_url, name_image_url, engine_image_url;
    private RadiusButtonView change_owner;
    private FrameLayout memberLayout;
    private TextView imgTv1, imgTv2, imgTv3;
    private LinearLayout deveimgCotainer;
    CanBeEditItemView licenseNoEiv;
    ArrayList<String> imgList = new ArrayList<>();
    RelativeLayout btnContainer;
    ReboundScrollView mRsv;
    TextView mErrorTv;
    boolean isOwner;
    LarkDeviceBasicDetail mDetail;
    Bundle mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		Constants.addActivity(this);
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_nm);
        PublicWay.activityList.add(this);
        setContentView(R.layout.activity_add_device);
        mContext = this;
        mData = getIntent().getExtras();
        deviceId = mData.getLong(Constants.DEVICE_ID, -1);
        isOwner = mData.getBoolean(Constants.IS_OWNER, false);
        initView();
        updateData(deviceId);
    }

    private void updateData(final long deviceId) {
        mRxManager.add(Api.getDefault(HostType.HOST_LARK).getDeviceBasicDetail(deviceId).compose(RxSchedulers.<BaseRespose<LarkDeviceBasicDetail>>io_main()).subscribe(new RxSubscriber<BaseRespose<LarkDeviceBasicDetail>>(mContext) {
            @Override
            protected void _onNext(BaseRespose<LarkDeviceBasicDetail> br) {
                if (br.isSuccess()) {
                    mDetail = br.getResult();
                    deviceType = mDetail.getCategory();
                    typeId = mDetail.getTypeId();
                    deviceBrand = mDetail.getBrandName();
                    brandId = mDetail.getBrandId();
                    deviceModel = mDetail.getModelName();
                    modeId = mDetail.getModelId();
                    List<String> imageUrls = mDetail.getDevicePhotoS();
                    if (null != imageUrls) {
                        Bimp.tempSelectBitmap.clear();
                        for (String url : imageUrls) {
                            ImageItem item = new ImageItem();
                            item.setImageUrl(url);
                            item.setSelected(true);
                            Bimp.tempSelectBitmap.add(item);

                        }
                    }
                    initAddDeviceItemView();
                    initPhotoGridView();
                    ViewGroup container = (ViewGroup) change_owner.getParent();
                    if (isOwner) {
                        container.setVisibility(View.VISIBLE);
                        change_owner.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.P_SEARCHLISTTYPE, 2);
                                bundle.putLong(Constants.DEVICE_ID, deviceId);
                                Constants.toActivity(AddDeviceActivity.this, SearchActivity.class, bundle);
                            }
                        });
                    } else {
                        container.setVisibility(View.GONE);
                    }
                } else {
                    _onError(br.getMessage());
                }
            }

            @Override
            protected void _onError(String message) {
                mRsv.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                mErrorTv.setVisibility(View.VISIBLE);
                mErrorTv.setText(message);
            }
        }));
    }

    @Override
    public void initPresenter() {

    }

    private void initView() {
        mErrorTv = (TextView) findViewById(R.id.basic_error_tv);
        mRsv = (ReboundScrollView) findViewById(R.id.basic_info_rsv);
        btnContainer = (RelativeLayout) findViewById(R.id.basic_info_btncontainer);
        licenseNoEiv = (CanBeEditItemView) findViewById(R.id.device_license);
        imgTv1 = (TextView) findViewById(R.id.mac_img_tv1);
        imgTv2 = (TextView) findViewById(R.id.mac_img_tv2);
        imgTv3 = (TextView) findViewById(R.id.mac_img_tv3);
        memberLayout = (FrameLayout) findViewById(R.id.deivce_member);
        device_name = (CanBeEditItemView) findViewById(R.id.device_name);
        device_type = (CanBeEditItemView) findViewById(R.id.device_type);
        device_brand1 = (CanBeEditItemView) findViewById(R.id.device_brand1);
        device_model = (CanBeEditItemView) findViewById(R.id.device_model);
        device_year = (CanBeEditItemView) findViewById(R.id.device_year);
        device_owner = (CanBeEditItemView) findViewById(R.id.device_owner);
        device_phone_number = (CanBeEditItemView) findViewById(R.id.device_phone_number);
        device_rackId = (CanBeEditItemView) findViewById(R.id.device_rackId);
        device_snId = (CanBeEditItemView) findViewById(R.id.device_snId);
        device_workTime = (CanBeEditItemView) findViewById(R.id.device_workTime);
        deveimgCotainer = (LinearLayout) findViewById(R.id.device_img_cotainer);
        decive_image = (ImageView) findViewById(R.id.decive_image);
        decive_image.setOnClickListener(this);
        name_image = (ImageView) findViewById(R.id.name_image);
        name_image.setOnClickListener(this);
        engine_image = (ImageView) findViewById(R.id.engine_image);
        engine_image.setOnClickListener(this);
        change_owner = (RadiusButtonView) findViewById(R.id.change_owner);
    }



    private void initAddDeviceItemView() {
        memberLayout.setOnClickListener(this);
        imgTv1.setVisibility(View.VISIBLE);
        imgTv2.setVisibility(View.VISIBLE);
        imgTv3.setVisibility(View.VISIBLE);
        if (null != mDetail) {
            licenseNoEiv.setContent(mDetail.getLicense());
            device_name.setContent(mDetail.getDeviceName());
            device_type.setContent(deviceType);
            device_brand1.setContent(deviceBrand);
            device_model.setContent(deviceModel);
            device_year.setContent(mDetail.getFactoryTime());
            device_rackId.setContent(mDetail.getRackId());
            if (!TextUtils.isEmpty(mDetail.getSnId())) {
                device_snId.setVisibility(View.VISIBLE);
                device_snId.setContent(mDetail.getSnId());
            }
            device_workTime.setContent(String.valueOf(mDetail.getWorkTime()) + "时");
            if (isOwner) {
                device_name.isArrow(true);
                device_name.setOnClickListener(this);
            }
            mRsv.setVisibility(View.VISIBLE);
        } else {
            mRsv.setVisibility(View.GONE);
            btnContainer.setVisibility(View.GONE);
            mErrorTv.setVisibility(View.VISIBLE);
            mErrorTv.setText("没有详情数据");
        }
    }

    private void initPhotoGridView() {
        if (imgList.size() > 0) {
            imgList.clear();
        }
        List<String> devicePhotos = mDetail.getDevicePhotoS();
        List<String> nameplatePhotoS = mDetail.getNameplatePhotoS();
        List<String> enginePhotoS = mDetail.getEnginePhotoS();
        if (devicePhotos != null && devicePhotos.size() > 0) {
            decive_image_url = devicePhotos.get(0);
            if (!TextUtils.isEmpty(decive_image_url)) {
                imgList.add(decive_image_url);
                deveimgCotainer.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(decive_image_url, decive_image,
                        Constants.displayDeviceImageOptions, null);
            }
        }
        if (nameplatePhotoS != null && nameplatePhotoS.size() > 0) {
            name_image_url = nameplatePhotoS.get(0);
            if (!TextUtils.isEmpty(name_image_url)) {
                imgList.add(name_image_url);
                deveimgCotainer.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(name_image_url, name_image,
                        Constants.displayDeviceImageOptions, null);
            }

        }
        if (enginePhotoS != null && enginePhotoS.size() > 0) {
            engine_image_url = enginePhotoS.get(0);
            if (!TextUtils.isEmpty(engine_image_url)) {
                imgList.add(engine_image_url);
                deveimgCotainer.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(engine_image_url, engine_image,
                        Constants.displayDeviceImageOptions, null);
            }
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Bimp.tempSelectBitmap.clear();
        Bimp.max = 0;
        super.onDestroy();
        Constants.removeActivity(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.deivce_member:
                Constants.toActivity(this, DeviceMcMemberActivity.class, mData);

                break;
            case R.id.device_name:
                //MobclickAgent.onEvent(mContext,UMengKey.time_machine_info_edit);
                MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_INFO_EDIT);
                gotoEditActivity(device_name, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
            case R.id.device_type:
                gotoEditActivity(device_type, Constants.E_DEVICE_LIST, Constants.E_ITEMS_category, typeId, 0);
                break;
            case R.id.device_brand1:
                gotoEditActivity(device_brand1, Constants.E_DEVICE_LIST, Constants.E_ITEMS_brand, brandId, 0);
                break;
            case R.id.device_model:
                gotoEditActivity(device_model, Constants.E_DEVICE_LIST, Constants.E_ITEMS_model, typeId, brandId);
                break;
            case R.id.device_year:
                String yearStr = device_year.getContent();
                int year = 2010;
                int mouth = 1;
                int data = 1;
                if (!TextUtils.isEmpty(yearStr)) {
                    String[] list = yearStr.split("-");
                    if (list.length == 1) {
                        year = Integer.valueOf(list[0]);
                    } else if (list.length == 2) {
                        year = Integer.valueOf(list[0]);
                        mouth = Integer.valueOf(list[1]);
                    } else if (list.length == 3) {
                        year = Integer.valueOf(list[0]);
                        mouth = Integer.valueOf(list[1]);
                        data = Integer.valueOf(list[2]);
                    }
                }
                DatePickerDialog dialog = new DatePickerDialog(AddDeviceActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        device_year.setContent(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, mouth - 1, data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    DatePicker datePicker = dialog.getDatePicker();
                    datePicker.setMaxDate(System.currentTimeMillis());
                }
                dialog.show();
                break;
            case R.id.name_image:
//                Constants.gotoImageBrowerType(this, 1, new String[]{name_image_url}, false, 0);
                if (imgList != null) {
                    Bundle bundle1 = new Bundle();
                    bundle1.putStringArrayList(BigPicActivity.BIG_PIC_URLS, imgList);
                    bundle1.putInt(BigPicActivity.POSITION, imgList.indexOf(name_image_url));
                    Constants.toActivity(this, BigPicActivity.class, bundle1);
                    MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
                }
//			addImageType = 1;
//			photo();
                break;
            case R.id.decive_image:
//                Constants.gotoImageBrowerType(this, 1, new String[]{decive_image_url}, false, 0);
                if (imgList != null) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putStringArrayList(BigPicActivity.BIG_PIC_URLS, imgList);
                    bundle2.putInt(BigPicActivity.POSITION, imgList.indexOf(decive_image_url));
                    Constants.toActivity(this, BigPicActivity.class, bundle2);
                    MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
                }
                break;
            case R.id.engine_image:
//                Constants.gotoImageBrowerType(this, 1, new String[]{engine_image_url}, false, 0);
                if (imgList != null) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putStringArrayList(BigPicActivity.BIG_PIC_URLS, imgList);
                    bundle3.putInt(BigPicActivity.POSITION, imgList.indexOf(engine_image_url));
                    Constants.toActivity(this, BigPicActivity.class, bundle3);
                    MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
                }
//			addImageType = 2;
//			photo();
                break;
            case R.id.device_owner:
                gotoEditActivity(device_owner, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
            case R.id.device_phone_number:
                gotoEditActivity(device_phone_number, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case UPATE_INFO:
                if (data != null) {
                    String deviceName = data.getStringExtra(Constants.P_DEVICENAME);
                    device_name.setContent(deviceName);
                    mRxManager.post(Constants.UPDATE_DEVICE_NAME, deviceName);
                }
                break;
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                int itemType = bundle.getInt(Constants.P_ITEMTYPE, -1);
                switch (itemType) {
                    case Constants.E_ITEMS_category:
                        EditListInfo eInfoType = (EditListInfo) bundle.getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoType) {
                            deviceType = eInfoType.getName();
                            typeId = Integer.parseInt(eInfoType.getId());
                            device_type.setContent(deviceType);

                            deviceModel = "";
                            modeId = 0;
                            device_model.setContent(deviceModel);
                            device_model.initEditiHint();
                        }
                        break;
                    case Constants.E_ITEMS_brand:
                        EditListInfo eInfoBrand = (EditListInfo) bundle.getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoBrand) {
                            deviceBrand = eInfoBrand.getName();
                            brandId = Integer.parseInt(eInfoBrand.getId());
                            device_brand1.setContent(deviceBrand);

                            deviceModel = "";
                            modeId = 0;
                            device_model.setContent(deviceModel);
                            device_model.initEditiHint();
                        }
                        break;
                }

                break;
        }
    }


    private void gotoEditActivity(CanBeEditItemView view, int editType, int itemType) {
        if (editType == Constants.E_DEVICE_DATA ||
                editType == Constants.E_DEVICE_LIST || editType == Constants.E_DEVICE_TEXT) {
            view.requestFocus();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.P_TITLETEXT, view.getTitle());
            bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
            bundle.putInt(Constants.P_EDITTYPE, editType);
            bundle.putInt(Constants.P_ITEMTYPE, itemType);
            bundle.putString(Constants.P_DEVICEID, String.valueOf(deviceId));
            bundle.putString(Constants.P_DEVICENAME, mDetail.getDeviceName());
            bundle.putString(Constants.PAGET_TYPE, Constants.IPageType.PAGE_DEVICE_INFO);
            Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
        } else {
            view.isEdit(true);
            view.getEdit_view().requestFocus();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view.getEdit_view(), InputMethodManager.RESULT_SHOWN);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

    private void gotoEditActivity(CanBeEditItemView view, int editType, int itemType, int v1, int v2) {
        view.requestFocus();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.P_TITLETEXT, view.getTitle());
        bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
        bundle.putInt(Constants.P_EDITTYPE, editType);
        bundle.putInt(Constants.P_ITEMTYPE, itemType);
        bundle.putInt(Constants.P_EDIT_LIST_VALUE1, v1);
        bundle.putInt(Constants.P_EDIT_LIST_VALUE2, v2);
        bundle.putString(Constants.PAGET_TYPE, Constants.IPageType.PAGE_DEVICE_INFO);
        Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
    }



    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_INFO);
    }

}
