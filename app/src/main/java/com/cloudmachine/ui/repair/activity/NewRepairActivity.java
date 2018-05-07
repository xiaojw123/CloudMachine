package com.cloudmachine.ui.repair.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.activities.CheckMachineActivity;
import com.cloudmachine.activities.EditLayoutActivity;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.activities.SearchPoiActivity;
import com.cloudmachine.adapter.PhotoAdapter;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.EditListInfo;
import com.cloudmachine.bean.MachineTypeInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.NewRepairInfo;
import com.cloudmachine.bean.ResidentAddressInfo;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.DeviceHelper;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.net.task.MachineTypesListAsync;
import com.cloudmachine.net.task.SubmitRepairAsync;
import com.cloudmachine.ui.repair.contract.NewRepairContract;
import com.cloudmachine.ui.repair.model.NewRepairModel;
import com.cloudmachine.ui.repair.presenter.NewRepairPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.PictureUtil;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.utils.UploadPhotoUtils;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;


/**
 * @author shixionglu 新增报修页面
 */
public class NewRepairActivity extends BaseAutoLayoutActivity<NewRepairPresenter, NewRepairModel> implements
        Callback, OnClickListener, NewRepairContract.View {
    public static final String DEFAULT_LOCAITOIN = "defualt_location";
    public static final String DEFAULT_PROVINCE = "defualt_province";
    public static final String KEY_LOC_LNG="loc_lng";
    public static final String KEY_LOC_LAT="loc_lat";
    private static final int REQUEST_PERMISSION_PICK = 0x008;  //权限请求
    private Handler mHandler;
    private TextView tvType;
    private TextView tvBrand;
    private TextView tvModel;
    private String PK_PROD_DEF = "";// 类型
    private String PK_BRAND = "";// 品牌
    private String PK_VHCL_MATERIAL = "";// 型号
    private String deviceType, deviceBrand1, deviceModel;

    private AMapLocationClient locationClient = null;
    private TextView text_address;
    private RelativeLayout showButton;
    private TextView tvCheckMac;

    private String vmacopname;
    private String vmacoptel;
    private String pk_prod_def;
    private String pk_brand;
    private String pk_vhcl_materia;
    private String vmachinenum;
    private String vdiscription;
    private String vservicetype;
    private String vworkaddress;
    private String province;
    private String deviceId;
    double locLng, locLat;

    private ClearEditTextView cetContactName;
    private ClearEditTextView cetContactMobile;
    private ClearEditTextView cetContactDesc;
    private ClearEditTextView et_rackIdname;
    private McDeviceInfo mcDeviceInfo; // 当前用户选择报修的机器
    private boolean isOwnerDevice;
    private RecyclerView mRecyclerView;

    private PhotoAdapter photoAdapter;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    List<String> photos = null;
    private ArrayList<String> selectPhotos;
    Map<String, String> selectPhotosMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Res.init(this);
        PublicWay.activityList.add(this);
        setContentView(R.layout.activity_new_repair);
        mHandler = new Handler(this);
        if (mPermissionsChecker == null) {
            mPermissionsChecker = new PermissionsChecker(this);
        }
        getIntentData();
        initView();
        initLocation();
        new MachineTypesListAsync(mContext, mHandler).execute();

    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_repair_create);
        super.onResume();
        MobclickAgent.onEvent(NewRepairActivity.this, MobEvent.TIME_REPAIR_CREATE);
    }

    @Override
    protected void onPause() {
        //MobclickAgent.onPageEnd(UMengKey.time_repair_create);
        super.onPause();
    }

    private void initView() {
        initNewReapirView();
        showData(); // 控制按钮选择机器的显示影藏
    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mcDeviceInfo = (McDeviceInfo) bundle.getSerializable(Constants.P_DEVICEINFO_MY);
            vworkaddress = bundle.getString(DEFAULT_LOCAITOIN);
            province = bundle.getString(DEFAULT_PROVINCE);
            locLng=bundle.getDouble(KEY_LOC_LNG);
            locLat=bundle.getDouble(KEY_LOC_LAT);
        }
    }


    public void showData() {
        showMyDeviceInfo();
    }


    static final String[] PERMISSIONS_PICK = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private void initNewReapirView() {

        selectPhotos = new ArrayList<>();
        //初始化动态recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mRecyclerView.setAdapter(photoAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            if (mPermissionsChecker.lacksPermissions(PERMISSIONS_PICK)) {
                                PermissionsActivity.startActivityForResult(NewRepairActivity.this, REQUEST_PERMISSION_PICK,
                                        PERMISSIONS_PICK);
                            } else {
                                pickCamera();
                            }
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(NewRepairActivity.this);
                        }
                    }
                }));


        text_address = (TextView) findViewById(R.id.text_address);
        RadiusButtonView checkMac = (RadiusButtonView) findViewById(R.id.checkBtn);// 选择机器
        RelativeLayout macopType = (RelativeLayout) findViewById(R.id.iv_type);// 类型
        RelativeLayout macopBrand = (RelativeLayout) findViewById(R.id.iv_brand);// 品牌
        RelativeLayout macopModel = (RelativeLayout) findViewById(R.id.iv_model);// 型号
        RelativeLayout macopAddress = (RelativeLayout) findViewById(R.id.iv_address);// 位置
        RadiusButtonView btnSubmitNow = (RadiusButtonView) findViewById(R.id.btn_bottom_repair);
        showButton = (RelativeLayout) findViewById(R.id.rl_showbutton);
        tvCheckMac = (TextView) findViewById(R.id.tv_checkMac);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvBrand = (TextView) findViewById(R.id.tv_brand);
        tvModel = (TextView) findViewById(R.id.tv_model);
        cetContactName = (ClearEditTextView) findViewById(R.id.et_repairname);
        cetContactMobile = (ClearEditTextView) findViewById(R.id.et_repair_mobile);
        cetContactDesc = (ClearEditTextView) findViewById(R.id.et_des);
        et_rackIdname = (ClearEditTextView) findViewById(R.id.et_rackIdname);
        NewRepairInfo repairInfo = DeviceHelper.getRepairInfo(this);
        if (repairInfo != null) {
            cetContactName.setText(repairInfo.getVmacopname());
            cetContactMobile.setText(repairInfo.getVmacoptel());
            tvType.setText(repairInfo.getPk_prod_def());
            et_rackIdname.setText(repairInfo.getVmachinenum());
            PK_BRAND = repairInfo.getPk_brand();
            PK_VHCL_MATERIAL = repairInfo.getPk_vhcl_material();
            setTextToView(tvModel, repairInfo.getModelname());
            setTextToView(tvBrand, repairInfo.getBrandname());
        } else {
            Member member = MyApplication.getInstance().getTempMember();
            if (member != null) {
                cetContactName.setText(member.getNickname());
                cetContactMobile.setText(member.getMobile());
            }
        }
        macopType.setOnClickListener(this);
        macopBrand.setOnClickListener(this);
        macopModel.setOnClickListener(this);
        macopAddress.setOnClickListener(this);
        checkMac.setOnClickListener(checkMacListener);
        btnSubmitNow.setOnClickListener(btnSubmitListener);
        if (UserHelper.getMyDevices().size() > 0) {
            showButton.setVisibility(View.VISIBLE);
            tvCheckMac.setVisibility(View.VISIBLE);
        }


    }

    private OnClickListener checkMacListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            checkMac();
        }
    };
    private OnClickListener btnSubmitListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            submitRepairInfo();
        }
    };

    private void checkMac() {
        Constants.toActivityForR(NewRepairActivity.this,
                CheckMachineActivity.class, null, Constants.REQUEST_ToSearchDeviceActivity);
    }

    private void submitRepairInfo() {
        NewRepairInfo newRepairInfo = new NewRepairInfo();
        MobclickAgent.onEvent(mContext, UMengKey.count_submit_repair_now);
        vmacopname = cetContactName.getText().toString().trim();
        vmacoptel = cetContactMobile.getText().toString().trim();
        pk_prod_def = PK_PROD_DEF;
        pk_brand = PK_BRAND;
        pk_vhcl_materia = PK_VHCL_MATERIAL;
        String modelName = tvModel.getText().toString();
        String brandName = tvBrand.getText().toString();
        vmachinenum = et_rackIdname.getText().toString().trim();

        vservicetype = "1";
        vdiscription = cetContactDesc.getText().toString();
        if (vworkaddress != null) {
            vworkaddress = vworkaddress.trim();
        }
        newRepairInfo.setModelname(modelName);
        newRepairInfo.setBrandname(brandName);
        newRepairInfo.setVmacopname(vmacopname);
        newRepairInfo.setVmacoptel(vmacoptel);
        newRepairInfo.setPk_prod_def(pk_prod_def);
        newRepairInfo.setPk_brand(pk_brand);
        newRepairInfo.setPk_vhcl_material(pk_vhcl_materia);
        newRepairInfo.setVmachinenum(vmachinenum);
        newRepairInfo.setVdiscription(vdiscription);
        newRepairInfo.setVservicetype(vservicetype);
        newRepairInfo.setVworkaddress(vworkaddress);
        newRepairInfo.setProvince(province);
        newRepairInfo.setDeviceId(deviceId);
        newRepairInfo.setLat(locLat);
        newRepairInfo.setLng(locLng);

        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < selectPhotos.size(); i++) {
//            if (i < selectPhotos.size() - 1) {
//                sb.append(selectPhotos.get(i)).append(",");
//            }
//            if (i == selectPhotos.size() - 1) {
//                sb.append(selectPhotos.get(i));
//            }
//        }
        for (String value : selectPhotosMap.values()) {
            AppLog.print("submit  value___" + value);
            sb.append(value).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        newRepairInfo.setLogo_address(sb.toString());
        String locAddress = text_address.getText().toString();
        if (TextUtils.isEmpty(vmacopname)) {
            Constants.MyToast("联系人不能为空！");
        } else if (TextUtils.isEmpty(vmacoptel)) {
            Constants.MyToast("联系手机号码不能为空！");
        } else if (TextUtils.isEmpty(pk_prod_def)) {
            Constants.MyToast("机器类型不能为空！");
        } else if (TextUtils.isEmpty(pk_brand)) {
            Constants.MyToast("机器品牌不能为空！");
        } else if (TextUtils.isEmpty(pk_vhcl_materia)) {
            Constants.MyToast("机器型号不能为空！");
        } else if (TextUtils.isEmpty(vmachinenum)) {
            Constants.MyToast("机器铭牌号码不能为空！");
        }
//                else if (TextUtils.isEmpty(vdiscription)) {
//                    Constants.MyToast("故障描述不能为空！");
//                }

        else if (TextUtils.isEmpty(locAddress)) {
            Constants.MyToast("机器位置信息不能为空！");
        } else {
            // TODO: 2018/4/11 bug240
            long memberId=-1;
            if (UserHelper.isLogin(this)) {
                memberId=UserHelper.getMemberId(this);
            }
            mPresenter.getWarnMessage(memberId, vmacoptel, newRepairInfo);
//            showDialog(newRepairInfo);
        }
    }

    private void pickCamera() {
        PhotoPicker.builder()
                .setPhotoCount(PhotoAdapter.MAX)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .setSelected(selectedPhotos)
                .start(NewRepairActivity.this);
    }


    private void showDialog(String message, final NewRepairInfo newRepairInfo) {
        CustomDialog.Builder builder = new CustomDialog.Builder(NewRepairActivity.this);
        builder.setMessage(message);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                return;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                new SubmitRepairAsync(mContext, mHandler, newRepairInfo)
                        .execute();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private PermissionsChecker mPermissionsChecker;
    private static final int REQUEST_PERMISSION = 0x00141;  //权限请求

    private void initLocation() {
        if (!TextUtils.isEmpty(vworkaddress)) {
            setTextToView(text_address, vworkaddress);
            return;
        }
        // 初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        // 设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsChecker.lacksPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
                PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                        Manifest.permission.ACCESS_FINE_LOCATION);
            } else {
                locationClient.startLocation();
            }
        } else {
            locationClient.startLocation();
        }
    }

    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            // locationClient.stopAssistantLocation();
            stopLocation();
            locationClient.onDestroy();
            locationClient = null;
        }
    }


    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);// 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);// 可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(false);// 可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);// 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);// 可选，
        // 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                // 解析定位结果
                province = loc.getProvince();
                vworkaddress = loc.getAddress();
                locLng = loc.getLongitude();
                locLat = loc.getLatitude();
                setTextToView(text_address, loc.getPoiName());
                stopLocation();
                // String result = Utils.getLocationStr(loc);
                // Constants.MyLog(result);
                // tvReult.setText(result);
            } else {
                // tvReult.setText("定位失败，loc is null");
            }
        }
    };

    private void setTextToView(TextView view, String str) {
        view.setText(Constants.toViewString(str));
        view.setTextColor(mContext.getResources().getColor(
                R.color.public_black));
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case Constants.HANDLER_NEWREPAIR_SUCCESS:
                ToastUtils.showToast(this, "报修成功");
                FileUtils.clearComprressFile();
                finish();
                break;
            case Constants.HANDLER_NEWREPAIR_FAILD:
                ToastUtils.showToast(this, (String) msg.obj);
                break;
            case Constants.HANDLER_GETMACHINETYPES_SUCCESS:
                List<MachineTypeInfo> mTypeInfo = (List<MachineTypeInfo>) msg.obj;
                if (null != mTypeInfo && mTypeInfo.size() > 0) {
                    MachineTypeInfo mInfo = mTypeInfo.get(0);
                    if (null != mInfo) {
                        deviceType = mInfo.getName();
                        setTextToView(tvType, deviceType);
                        PK_PROD_DEF = mInfo.getPk_PROD_DEF();
                        deviceModel = "";
                    }
                }
                break;
            case Constants.HANDLER_UPLOAD_SUCCESS:
//                String url = (String) msg.obj;
                //向集合添加选中的url
//                selectPhotos.add(url);
                Map<String, String> paramsMap = (Map<String, String>) msg.obj;
                selectPhotosMap.putAll(paramsMap);
                for (Map.Entry<String, String> entry : selectPhotosMap.entrySet()) {
                    AppLog.print("uploadsucess____entry key__" + entry.getKey() + "__value__" + entry.getValue());

                }

                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_PICK) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showPermissionDialog(this,Constants.PermissionType.STORAGE);
            } else {
                pickCamera();
            }
            return;
        }
        if (requestCode == REQUEST_PERMISSION) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                CommonUtils.showPermissionDialog(this,Constants.PermissionType.LOCATION);
            } else {
                locationClient.startLocation();
            }
            return;
        }
        if (requestCode == Constants.REQUEST_ToSearchActivity) {
            if (null != data && null != data.getExtras()) {
                ResidentAddressInfo addressInfo = (ResidentAddressInfo) data.getExtras().getSerializable(Constants.P_SEARCHINFO);
                if (null != addressInfo) {
                    province = addressInfo.getProvince();
                    String city = addressInfo.getCity();
                    String address = "";
                    if (!TextUtils.isEmpty(province) && province.equals(city)) {
                        address += province;
                    } else {
                        address += province;
                        address += city;
                    }
                    address += addressInfo.getPosition();
//                    vworkaddress = addressInfo.getPosition();
                    vworkaddress = address;
                    locLng=addressInfo.getLng();
                    locLat=addressInfo.getLat();
                    setTextToView(text_address, addressInfo.getTitle());
                }
            }
        }

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            AppLog.print("date");
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            selectedPhotos.clear();
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
                selectedPhotos.addAll(photos);
            }
            if (requestCode == PhotoPreview.REQUEST_CODE) {
                selectPhotosMap = tempMap;
            }
            photoAdapter.notifyDataSetChanged();
        }


        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();
                int editType = bundle.getInt(Constants.P_EDITTYPE);
                int itemType = bundle.getInt(Constants.P_ITEMTYPE);
                switch (itemType) {
                    case Constants.E_ITEMS_deviceName:
                        String name = bundle.getString(Constants.P_EDITRESULTSTRING);
                        if (!TextUtils.isEmpty(name)) {
                            // device_name.setContent(name);
                        } else {
                            Constants.ToastAction(ResV
                                    .getString(R.string.add_device_name_null));
                        }
                        break;
                    case Constants.E_ITEMS_category:
                        EditListInfo eInfoType = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoType) {
                            deviceType = eInfoType.getName();
                            String type = tvType.getText().toString();
                            if (!TextUtils.isEmpty(type) && !type.equals(deviceType)) {
                                tvBrand.setText(null);
                                tvModel.setText(null);
                            }
                            setTextToView(tvType, deviceType);
                            PK_PROD_DEF = eInfoType.getPK_PROD_DEF();
                            deviceModel = "";
                            // device_model.setContent(deviceModel);
                            // device_model.initEditiHint();
                        }
                        break;
                    case Constants.E_ITEMS_brand:
                        EditListInfo eInfoBrand = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoBrand) {
                            deviceBrand1 = eInfoBrand.getName();
                            String brand = tvBrand.getText().toString();
                            if (!TextUtils.isEmpty(brand) && !brand.equals(deviceBrand1)) {
                                tvModel.setText(null);
                            }
                            setTextToView(tvBrand, deviceBrand1);
                            PK_BRAND = eInfoBrand.getPK_BRAND();
                            deviceModel = "";
                            // device_model.setContent(deviceModel);
                            // device_model.initEditiHint();
                        }
                        break;
                    case Constants.E_ITEMS_model:
                        EditListInfo eInfoModel = (EditListInfo) bundle
                                .getSerializable(Constants.P_EDITRESULTITEM);
                        if (null != eInfoModel) {
                            deviceModel = eInfoModel.getName();
                            setTextToView(tvModel, deviceModel);
                            PK_VHCL_MATERIAL = eInfoModel.getPK_VHCL_MATERIAL();
                        }
                        break;


                    default:
                        break;
                }
                break;
            case Constants.CLICK_POSITION:
//                isOwnerDevice = true;
                McDeviceInfo item = (McDeviceInfo) data.getSerializableExtra("selInfo");
                if (item != null) {
                    mcDeviceInfo = item;
//                    mcDeviceInfo = ownDeviceInfos.get(clickPosition);
                    showMyDeviceInfo();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.removeActivity(this);
        destroyLocation();
    }

    private void showMyDeviceInfo() {
        if (null != mcDeviceInfo) {
            PK_PROD_DEF = mcDeviceInfo.getPk_PROD_DEF();
            PK_BRAND = mcDeviceInfo.getPk_BRAND();
            PK_VHCL_MATERIAL = mcDeviceInfo.getPk_VHCL_MATERIAL();
            vmachinenum = mcDeviceInfo.getRackId();
            et_rackIdname.setText(vmachinenum);
            vworkaddress = mcDeviceInfo.getLocation().getPosition();
            province = mcDeviceInfo.getLocation().getProvince();
            locLat=mcDeviceInfo.getLocation().getLat();
            locLng=mcDeviceInfo.getLocation().getLng();
            deviceId = String.valueOf(mcDeviceInfo.getLocation()
                    .getDeviceId());
            setTextToView(tvType, mcDeviceInfo.getCategory());
            setTextToView(tvBrand, mcDeviceInfo.getBrand());
            setTextToView(tvModel, mcDeviceInfo.getModel());
            setTextToView(text_address, vworkaddress);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_type:
                MobclickAgent.onEvent(this, MobEvent.TIME_REPAIR_CREATE_ATTRIBUTE);
                // Intent typeIntent = new
                // Intent(NewRepairActivity.this,MachineTypeActivity.class);
                if (!isOwnerDevice) {
                    gotoEditActivity("选择机器类型", Constants.E_DEVICE_LIST,
                            Constants.E_ITEMS_category, "", "", "", "机型", tvType.getText().toString());
                } else {
                    Constants.MyToast("已安装云盒子机器信息不可修改");
                }

                break;
            case R.id.iv_brand:
                MobclickAgent.onEvent(this, MobEvent.TIME_REPAIR_CREATE_ATTRIBUTE);
                // Intent brandIntent = new
                // Intent(NewRepairActivity.this,MachineBrandActivity.class);
                if (!isOwnerDevice) {
                    gotoEditActivity("选择品牌", Constants.E_DEVICE_LIST, Constants.E_ITEMS_brand,
                            PK_PROD_DEF, "", "", "品牌", tvBrand.getText().toString());
                } else {
                    Constants.MyToast("已安装云盒子机器信息不可修改");
                }

                break;
            case R.id.iv_model:
                MobclickAgent.onEvent(this, MobEvent.TIME_REPAIR_CREATE_ATTRIBUTE);
                // Intent modelIntent = new
                // Intent(NewRepairActivity.this,MachineModelActivity.class);
                if (!isOwnerDevice) {
                    if (!TextUtils.isEmpty(PK_PROD_DEF) && !TextUtils.isEmpty(PK_BRAND)) {
                        gotoEditActivity("选择型号", Constants.E_DEVICE_LIST, Constants.E_ITEMS_model,
                                PK_PROD_DEF, PK_BRAND, "", "型号", tvModel.getText().toString());
                    } else {
                        //Constants.ToastAction("请先选择产品和品牌");
                        Toast.makeText(mContext, "请先选择产品和品牌", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Constants.MyToast("已安装云盒子机器信息不可修改");
                }
                break;
            case R.id.iv_address:
                Intent intent = new Intent(this, SearchPoiActivity.class);
                startActivityForResult(intent, Constants.REQUEST_ToSearchActivity);

                break;
            default:
                break;
        }
    }

    // v1用来传nc类型id，v2用来传nc品牌id，v4用来传类型id
    private void gotoEditActivity(String titleName, int editType, int itemType, String v1,
                                  String v2, String v3, String text, String itemName) {

        Bundle bundle = new Bundle();
        bundle.putString(Constants.P_TITLENAME, titleName);
        bundle.putString(Constants.P_TITLETEXT, text);
        // bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
        bundle.putInt(Constants.P_EDITTYPE, editType);
        bundle.putInt(Constants.P_ITEMTYPE, itemType);
        bundle.putString(Constants.P_EDIT_LIST_VALUE1, v1);
        bundle.putString(Constants.P_EDIT_LIST_VALUE2, v2);
        bundle.putString(Constants.P_EDIT_LIST_ITEM_NAME, itemName);
        Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
    }

    @Override
    public void returnUploadPhoto(String url) {
    }

    @Override
    public void returnGetWarnMessage(NewRepairInfo info, String message) {
        if (TextUtils.isEmpty(message)) {
            message = "请确认" + vmacoptel + "的手机号码保持通畅，我们会在服务响应时间(08:00-20:00)10分钟内联系你，请耐心等候";
        }
        showDialog(message, info);
    }

}
