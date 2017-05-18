package com.cloudmachine.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cloudmachine.R;
import com.cloudmachine.adapter.CheckReportAdapter;
import com.cloudmachine.autolayout.widgets.DynamicWave;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.cache.LocationSerializable;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.net.task.DeleteDeviceAsync;
import com.cloudmachine.net.task.DeviceReportAsync;
import com.cloudmachine.net.task.DevicesBasicsInfoAsync;
import com.cloudmachine.net.task.MachineDetailAsync;
import com.cloudmachine.struc.FaultWarnInfo;
import com.cloudmachine.struc.FaultWarnListInfo;
import com.cloudmachine.struc.MachineDetailInfo;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.struc.McDeviceScanningInfo;
import com.cloudmachine.struc.ScanningAlarmInfo;
import com.cloudmachine.struc.ScanningOcdInfo;
import com.cloudmachine.struc.ScanningOilLevelInfo;
import com.cloudmachine.struc.ScanningWTInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.mpchart.ValueFormatUtil;
import com.cloudmachine.utils.widgets.CustomDialog;
import com.cloudmachine.utils.widgets.Dialog.MyDialog;
import com.cloudmachine.utils.widgets.TextProgressBar;
import com.cloudmachine.utils.widgets.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 机器内页
 */
public class DeviceMcActivity extends BaseAutoLayoutActivity implements
        OnClickListener, Callback, IXListViewListener, OnItemClickListener,
        OnMapClickListener, OnMarkerClickListener {

    private static final int zoomDefault = 14;
    public static final int HANDLER_TIMER = 1;
    public static final int HANDLER_DELETE_DEVICE = HANDLER_TIMER + 1;

    private static final int OcdAddLayoutNum = 1;
    private static final int EdAddLayoutNum = 1;
    private static final int HsdAddLayoutNum = 6;
    private static final int AddLayoutSum = OcdAddLayoutNum + EdAddLayoutNum
            + HsdAddLayoutNum;

    private static final int Activity_Result_Sensor = 20;

    private final int WATING_DETECTING = 0x001;
    private final int IS_DETECTING = 0x002;
    private final int COMPLETE_DETECTING = 0x003;
    private int detectState = WATING_DETECTING;

    private Handler mHandler;
    private Context mContext;
    private TitleView title_layout;
    private long deviceId;
    private String deviceName = "";
    private ImageView scanning_image;
    private AnimationDrawable animationDrawable;
    private Timer myTimer;
    private TextView testing_off;
    private View testing_off_layout;
    private boolean isStopScaning = true;
    private LinearLayout oil_add_layout, engine_add_layout,
            hydraulic_add_layout, oil_amount_add_layout;
    private TextView oil_line_d, engine_line_d, hydraulic_line_d,
            oil_amount_line_d;
    private McDeviceScanningInfo mcDeviceSInfo;
    private int BB = 50;
    private int addLayoutGap;
    private int lastaddLayoutGap;
    private int addLayoutNum;
    private int lastNum = 0;
    private int successTimeId;
    private ScrollView myScrollView;
    private long collectionDate;

    private McDeviceBasicsInfo mcDeviceBasicsInfo;

    // private RadiusButtonView testing_on_button;
    private String mcDeviceMac;
    private int checkRandom = -1;
    private int oilLave = -1;
    private ScanningOilLevelInfo[] oilLeve;
    private int residualOil;
    private ListView xlvCheckReport;
    private CheckReportAdapter checkReportAdapter;
    private ArrayList<FaultWarnListInfo> mList = new ArrayList<FaultWarnListInfo>();
    private MachineDetailInfo machineDetailInfo;

    //private boolean isScanning = false; // 默认未扫描
    private ImageView ivOvalDetecting;
    private float workTime;
    private LinearLayout llBasicInfo;
    private LinearLayout llDetecting;
    private ImageView ivOvalDetect;
    private Button btnDetectNow;
    private Animation animation;
    private LayoutAnimationController lac;
    private View rl_scanning, test_report, ailAmount_layout,
            workTime_layout, no_test_data_layout, member_layout;
    private RadiusButtonView button_ok, iKnow_button;
    private FaultWarnInfo faultWarnInfo;
    private TextProgressBar workHours;
    private DynamicWave residualOilView;

    private MapView mapView;
    private AMap aMap;
    private MarkerOptions markerOption;
    private McDeviceLocation location;
    private LatLng myLatLng;
    private Marker myMarker;
    private View mc_main_guide_layout;
    private ImageView mc_main_guide_image;
    private LinearLayout ll_repair_record;
    private McDeviceInfo mcDeviceInfo;
    private int mWorkState;
    private boolean haveDetectExperience = false, haveDetectMachine = false, isHaveDetectExperienceComplete = false,
            isHaveDetectMachineComplete = false;
    private LinearLayout mLlDeviceLocation;
    private TextView mTvDeviceLocation;
    private TextView mTvLocation;
    private String mMacAddress;
    private TextView mTvWorkStatus;
    private String mDevicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mc_device);
        mContext = this;
        mHandler = new Handler(this);
        if (null == mapView) {
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
        }
        getIntentData();
        initView();
        initMap();
        getBasicsInfo();

        // showAlertDialog(); //第一次检测展示的dialog******************
    }

    @Override
    public void initPresenter() {

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private void addMarkerToMap() {

        if (null != mcDeviceBasicsInfo) {
            location = mcDeviceBasicsInfo.getLocation();

            markerOption = new MarkerOptions();
            aMap.clear();
//				String allTiel = "";
            if (location != null) {
                myLatLng = new LatLng(location.getLat(), location.getLng());
                markerOption.position(myLatLng);
//					allTiel = mcDeviceBasicsInfo.getDeviceName()+Constants.S_FG+location.getPosition()+Constants.S_FG+mcDeviceBasicsInfo.getId();
            } else {
//					allTiel = mcDeviceBasicsInfo.getDeviceName()+Constants.S_FG+Constants.S_DEVICE_LOCATION_NO+Constants.S_FG+mcDeviceBasicsInfo.getId();
            }
//				markerOption.title(allTiel);
            if (mcDeviceBasicsInfo.getWorkStatus() == 0) {
                markerOption.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mc_workstatus_0));

            } else {

                markerOption.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mc_workstatus_1));
            }

            myMarker = aMap.addMarker(markerOption);
            myMarker.showInfoWindow();
            if (null != myMarker.getPosition())
                aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        myMarker.getPosition(), zoomDefault, 0, 30)));//18
        }

    }

    private void showGuide() {
        if (!MySharedPreferences.getSharedPBoolean(Constants.KEY_isMcMainGuide)) {
            MySharedPreferences.setSharedPBoolean(Constants.KEY_isMcMainGuide, true);
            mc_main_guide_layout.setVisibility(View.VISIBLE);
        } else {
            mc_main_guide_layout.setVisibility(View.GONE);
        }
    }

    private void showAlertDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        CustomDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                280, getResources().getDisplayMetrics());// 宽高可设置具体大小
        lp.height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 200, getResources()
                        .getDisplayMetrics());
        dialog.getWindow().setAttributes(lp);
    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                mcDeviceInfo = (McDeviceInfo) bundle.getSerializable(Constants.P_DEVICEINFO_MY);
                if (null != mcDeviceInfo) {
                    deviceName = mcDeviceInfo.getName();
                    deviceId = mcDeviceInfo.getId();
                    mcDeviceMac = mcDeviceInfo.getMacAddress();
                    mWorkState = mcDeviceInfo.getWorkStatus();
                    mMacAddress = mcDeviceInfo.getMacAddress();
                    mDevicePosition = mcDeviceInfo.getLocation().getPosition();
                } else {
                    deviceName = bundle.getString("deviceName");
                    deviceId = bundle.getLong("deviceId");
                    mcDeviceMac = bundle.getString("deviceMac");
                    mWorkState = bundle.getInt("deviceWorkState");
                    mMacAddress = bundle.getString("deviceMac");
                    mDevicePosition = bundle.getString("devicePosition");
                }
                /*deviceName = bundle.getString(Constants.P_DEVICENAME);
                deviceId = bundle.getLong(Constants.P_DEVICEID);
                mcDeviceMac = bundle.getString(Constants.P_DEVICEMAC);
                mWorkState = bundle.getInt(Constants.P_WORKSTATES);
                mMacAddress = mcDeviceInfo.getMacAddress();
                mDevicePosition = mcDeviceInfo.getLocation().getPosition();*/
                // deviceId = 100;
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    @Override
    protected void onResume() {
        // MobclickAgent.onPageStart(UMengKey.time_machine_detection);
        if (Constants.isChangeDevice) {
            getBasicsInfo();
        }
        if (null != mapView)
            mapView.onResume();
        super.onResume();
        showGuide();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // MobclickAgent.onPageEnd(UMengKey.time_machine_detection);
        if (null != mapView)
            mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (null != mapView)
            mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.member_layout:
                Bundle bundle_member = new Bundle();
                bundle_member.putLong(Constants.P_DEVICEID, deviceId);
                if (null != mcDeviceBasicsInfo)
                    bundle_member.putInt(Constants.P_DEVICETYPE,
                            mcDeviceBasicsInfo.getType());
                Constants.toActivity(this, DeviceMcMemberActivity.class, bundle_member);
                break;
            case R.id.rl_scanning:
                if (detectState == COMPLETE_DETECTING) {
                    switchLayout(detectState);
                    detectState = WATING_DETECTING;
                    switchScannState(detectState);
                }

//			if(detectState != IS_DETECTING)
//				switchLayout(detectState);// 切换布局
                break;
            case R.id.test_report:
//			faultWarnInfo

                if (null == faultWarnInfo) {
                    faultWarnInfo = (FaultWarnInfo) LocationSerializable.getSerializable2File(LocationSerializable.TestReportInfo + deviceId);
                    if (null == faultWarnInfo) {
                        no_test_data_layout.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                Bundle b = new Bundle();
                b.putLong(Constants.P_DEVICEID, deviceId);
                b.putString(Constants.P_DEVICENAME, deviceName);
                b.putInt(Constants.P_WORKSTATES, mWorkState);
                b.putSerializable(Constants.P_DEVICEINFO_faultWarnInfo, faultWarnInfo);
                Constants.toActivity(this, FaultDitailsActivity.class, b, false);

              /*  else {
                    Constants.MyToast("机器未工作,无法获取当前检测报告");
                }*/
                break;
            case R.id.ailAmount_layout:
                Bundle b_ail = new Bundle();
                b_ail.putLong(Constants.P_DEVICEID, deviceId);
                b_ail.putString(Constants.P_DEVICENAME, deviceName);
                b_ail.putInt(Constants.P_OILLAVE, residualOil);
                Constants.toActivity(this, OilAmountActivity.class, b_ail, false);
                break;
            case R.id.workTime_layout:
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_worktime_detai);
                Bundle b_wt = new Bundle();
                b_wt.putLong(Constants.P_DEVICEID, deviceId);
                Constants.toActivity(this, WorkHoursActivity.class, b_wt, false);
                break;
            case R.id.mc_main_guide_image:
                if (detectState != IS_DETECTING) {
                    showGuide();
//				new DeviceReportAsync(mContext, mHandler, deviceId)
//						.execute();
//				startAnimation();
                }
                break;
            case R.id.repair_record:
                Bundle bundle_repair = new Bundle();
                bundle_repair.putSerializable(Constants.P_DEVICEINFO_MY, mcDeviceInfo);
                bundle_repair.putLong(Constants.P_DEVICEID, deviceId);
                if (null != mcDeviceBasicsInfo)
                    bundle_repair.putInt(Constants.P_DEVICETYPE,
                            mcDeviceBasicsInfo.getType());
                Constants.toActivity(this, RepairRecordActivity.class, bundle_repair);
                break;
            case R.id.device_location:
                MobclickAgent.onEvent(mContext, UMengKey.count_device_location);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                        mcDeviceBasicsInfo);
                bundle.putLong("deviceId", deviceId);
                Constants.toActivity(this,
                        MapViewActivity.class, bundle);
                break;
            default:
                break;
        }
    }

    private void initView() {
        initTitleLayout();
        mLlDeviceLocation = (LinearLayout) findViewById(R.id.device_location);
        mLlDeviceLocation.setOnClickListener(this);
        mTvDeviceLocation = (TextView) findViewById(R.id.tv_device_location);
        mTvLocation = (TextView) findViewById(R.id.tv_location);
        mTvWorkStatus = (TextView) findViewById(R.id.work_status);
        if (mWorkState == 1) {
            mTvWorkStatus.setVisibility(View.VISIBLE);
        } else {
            mTvWorkStatus.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mMacAddress)) {
            mTvLocation.setText(mDevicePosition);
        }
        mc_main_guide_layout = findViewById(R.id.mc_main_guide_layout);
        mc_main_guide_layout.setOnClickListener(this);
        mc_main_guide_image = (ImageView) findViewById(R.id.mc_main_guide_image);
        mc_main_guide_image.setOnClickListener(this);
        llBasicInfo = (LinearLayout) findViewById(R.id.ll_basic_info); // 扫面页面下面，显示基本信息布局
        llDetecting = (LinearLayout) findViewById(R.id.ll_detecting); // 显示扫描信息
        ll_repair_record = (LinearLayout) findViewById(R.id.repair_record);
        ll_repair_record.setOnClickListener(this);
        initScanningLayout(); // 初始化扫描布局
        initXListView();

        workHours = (TextProgressBar) findViewById(R.id.work_hours);
        residualOilView = (DynamicWave) findViewById(R.id.iv_residual_oil); // 初始化剩余油位

        rl_scanning = findViewById(R.id.rl_scanning);
        rl_scanning.setOnClickListener(this);
        test_report = findViewById(R.id.test_report);
        test_report.setOnClickListener(this);
        ailAmount_layout = findViewById(R.id.ailAmount_layout);
        ailAmount_layout.setOnClickListener(this);
        workTime_layout = findViewById(R.id.workTime_layout);
        workTime_layout.setOnClickListener(this);
        no_test_data_layout = findViewById(R.id.no_test_data_layout);
        no_test_data_layout.setOnClickListener(this);
        member_layout = findViewById(R.id.member_layout);
        member_layout.setOnClickListener(this);
        button_ok = (RadiusButtonView) findViewById(R.id.button_ok);
        button_ok.setVisibility(View.GONE);
        button_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (detectState == COMPLETE_DETECTING) {
                    MobclickAgent.onEvent(mContext, UMengKey.count_machine_check_completed);
                    switchLayout(detectState);
                    detectState = WATING_DETECTING;
                    switchScannState(detectState);
                    button_ok.setVisibility(View.INVISIBLE);
                }
//				if(detectState != IS_DETECTING)
//					Constants.MyLog("被点击了！！！！！！！！！！！！！");
//					switchLayout(detectState);// 切换布局
            }
        });
        iKnow_button = (RadiusButtonView) findViewById(R.id.iKnow_button);
        iKnow_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                no_test_data_layout.setVisibility(View.GONE);
            }
        });
    }

    private void initXListView() {
        xlvCheckReport = (ListView) findViewById(R.id.xlv_check_report);
        checkReportAdapter = new CheckReportAdapter(mContext, mList);

        animation = AnimationUtils.loadAnimation(this, R.anim.list_anim);

        //startListViewAnimation();
        xlvCheckReport.setAdapter(checkReportAdapter);
        xlvCheckReport.setOnItemClickListener(this);

    }

    public void startListViewAnimation() {
        lac = new LayoutAnimationController(animation);
        // 设置控件显示的顺序；
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        // 设置控件显示间隔时间；
        lac.setDelay(1);
        // 为ListView设置LayoutAnimationController属性；
        xlvCheckReport.setLayoutAnimation(lac);
        xlvCheckReport
                .setLayoutAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivOvalDetecting.clearAnimation();
                        //isScanning = false;
                        detectState = COMPLETE_DETECTING;

                        //switchLayout(detectState);
                        button_ok.setVisibility(View.VISIBLE);
                        startBtnOkAnimation();
                        switchScannState(detectState);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
    }


    public void startBtnOkAnimation() {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(button_ok, "alpha", 0f, 1f);
        float currentY = button_ok.getTranslationY();
        ObjectAnimator transAnimator = ObjectAnimator.ofFloat(button_ok, "translationY", 500f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, transAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void initScanningLayout() {
        ivOvalDetecting = (ImageView) findViewById(R.id.iv_oval_detecting);
        ivOvalDetect = (ImageView) findViewById(R.id.iv_oval_detect);
        btnDetectNow = (Button) findViewById(R.id.btn_detect_now);
        btnDetectNow.setTextColor(mContext.getResources().getColor(R.color.tag_unchecked_color));
        switchLayout(detectState);// 切换布局
        switchScannState(detectState);// 切换扫描栏文字
        btnDetectNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_check);

                if (detectState == COMPLETE_DETECTING) {
                    MobclickAgent.onEvent(mContext, UMengKey.count_machine_check_complete);
                    switchLayout(detectState);
                    detectState = WATING_DETECTING;
                    button_ok.setVisibility(View.INVISIBLE);
                    switchScannState(detectState);
                } else {
                    if (detectState != IS_DETECTING) {
                        showGuide();
                        new DeviceReportAsync(mContext, mHandler, deviceId)
                                .execute();
                        startAnimation();
                    }
                }

            }
        });
    }

    protected void startAnimation() {
        //isScanning = true;
        detectState = IS_DETECTING;
        //switchLayout(detectState);
        switchScannState(detectState);// 切换扫描栏文字
        Animation scanAnimation = AnimationUtils.loadAnimation(
                DeviceMcActivity.this, R.anim.detect_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        scanAnimation.setInterpolator(lin);

        if (null != scanAnimation) {
            ivOvalDetecting.startAnimation(scanAnimation);
        }

    }

    public void switchLayout(int detectState) {
        if (detectState == IS_DETECTING) {
            llDetecting.setVisibility(View.VISIBLE);
            llBasicInfo.setVisibility(View.GONE);
        } else if (detectState == COMPLETE_DETECTING) {
            llDetecting.setVisibility(View.GONE);
            llBasicInfo.setVisibility(View.VISIBLE);
        } else if (detectState == WATING_DETECTING) {
            llDetecting.setVisibility(View.GONE);
            llBasicInfo.setVisibility(View.VISIBLE);
        }
    }

    public void switchScannState(int detectState) {
        if (detectState == IS_DETECTING) {
            ivOvalDetecting.setVisibility(View.VISIBLE);
            ivOvalDetect.setVisibility(View.GONE);
            btnDetectNow.setText("检测中...");
        } else if (detectState == WATING_DETECTING) {
            ivOvalDetecting.setVisibility(View.GONE);
            ivOvalDetect.setVisibility(View.VISIBLE);
            btnDetectNow.setText("一键检测");
        } else if (detectState == COMPLETE_DETECTING) {
            btnDetectNow.setText("检测完成");
            ivOvalDetecting.setVisibility(View.GONE);
            ivOvalDetect.setVisibility(View.VISIBLE);
        }
    }

    private void initTitleLayout() {

        title_layout = (TitleView) findViewById(R.id.title_layout);
        title_layout.setTitle(deviceName);
        title_layout.setLeftOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        title_layout.setRightText(-1,
                getResources().getString(R.string.mc_basic_information),
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                                mcDeviceBasicsInfo);
                        bundle.putInt(Constants.P_ADDMCDEVICETYPE, 0);
                        Constants.toActivity(DeviceMcActivity.this,
                                AddDeviceActivity.class, bundle);
                    }
                });
        title_layout.setRightTextEdit(true);
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case HANDLER_TIMER:

            case HANDLER_DELETE_DEVICE:
                new DeleteDeviceAsync(mContext, mHandler).execute((String) msg.obj);
                break;
            case Constants.HANDLER_GETDEVICEINFO_SUCCESS:

            case Constants.HANDLER_GETDEVICEINFO_FAIL:
                if (checkRandom != msg.arg1)
                    return false;
                break;

            case Constants.HANDLER_SCROLLVIEW_DOWN:
                myScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_SUCCESS:
                mcDeviceBasicsInfo = (McDeviceBasicsInfo) msg.obj;
                if (null != mcDeviceBasicsInfo) {
                    deviceName = mcDeviceBasicsInfo.getDeviceName();
                    title_layout.setTitle(deviceName);
                    McDeviceLocation location = mcDeviceBasicsInfo.getLocation();
                    if (location != null) {
                        mTvLocation.setText(location.getPosition());
                    }
                    updateWorkStatus(mcDeviceBasicsInfo.getWorkStatus());

                    addMarkerToMap();// 往地图上添加marker
                }

                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_FAIL:

                break;
            case Constants.HANDLER_DELETEDEVICE_SUCCESS:
                String message1 = (String) msg.obj;
                Constants.MyToast(null != message1 ? message1 : "成功删除该机器！");
                finish();
                Constants.isDeleteDevice = true;
                break;
            case Constants.HANDLER_DELETEDEVICE_FAIL:
                String message2 = (String) msg.obj;
                Constants.MyToast(null != message2 ? message2 : "删除机器失败！");
                break;
            case Constants.HANDLER_GETMACHINEDETAIL_SUCCESS:
                machineDetailInfo = (MachineDetailInfo) msg.obj;
                if (null != machineDetailInfo) {
                    residualOil = machineDetailInfo.getOilLave();
                    workTime = machineDetailInfo.getWorkTime();
                    changeOilTime(residualOil, workTime);
                }
                break;
            case Constants.HANDLE_GETCHECKREPORT_SUCCESS:
                switchLayout(detectState);
                faultWarnInfo = (FaultWarnInfo) msg.obj;
                LocationSerializable.setSerializable2File(faultWarnInfo, LocationSerializable.TestReportInfo + deviceId);
                if (null != faultWarnInfo) { // 拿到检测报告数据
                    mList.clear();
                    mList.addAll(faultWarnInfo.getReList());
                    if (null != mList && mList.size() > 0) {
                        startListViewAnimation();
                        checkReportAdapter.notifyDataSetChanged(); // 刷新listview重新加载动画
                    }

                }
                break;
            case Constants.HANDLE_GETCHECKREPORT_FAILD:
                Constants.MyToast((String) msg.obj);
                ivOvalDetecting.clearAnimation();
                // detectState = COMPLETE_DETECTING;
                detectState = WATING_DETECTING;
                //switchLayout(detectState);
                switchScannState(detectState);
                break;
        }
        return false;
    }

    // 成功获取数据后，初始化油位信息
    private void changeOilTime(int oil, float time) {

        residualOilView.setLave(oil);
        workHours.setProgress((int) time);
        workHours.setText(ValueFormatUtil.getWorkTime(time, "0时"));
    }

    class ScanningTimerTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what = HANDLER_TIMER;
            mHandler.sendMessage(message);
        }

    }

    private void changeScanningText(int n) {
        /*
         * if(n == 0){//显示分数 scanning_text_b.setVisibility(View.GONE);
		 * scanning_text_f.setVisibility(View.VISIBLE);
		 * scanning_text_fn.setVisibility(View.VISIBLE); }else{//显示百分比
		 * scanning_text_b.setVisibility(View.VISIBLE);
		 * scanning_text_f.setVisibility(View.GONE);
		 * scanning_text_fn.setVisibility(View.GONE); }
		 */
    }

    private void stopScanning() {

        isStopScaning = true;

        stopAnimation();

        // scanning_text_fn.setText(scanning_F_nub+"");
        // testing_off.setText("返回");
        // cancelScanning()
        // isStopScaning = false;
        testing_off.setText("取消检测");
        // testing_on_button.setVisibility(View.VISIBLE);
        testing_off_layout.setVisibility(View.GONE);
    }

    private void stopAnimation() {
        animationDrawable = (AnimationDrawable) scanning_image.getDrawable();
        if (animationDrawable.isRunning())
            animationDrawable.stop();
        if (myTimer != null)
            myTimer.cancel(); // 退出计时器
    }

    private void addOcdAmountLayout(int n) {
        if (n == 0) {
            oil_amount_line_d.setVisibility(View.VISIBLE);
            oil_amount_add_layout.setVisibility(View.VISIBLE);
            // RelativeLayout.LayoutParams lp = new
            // RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 60);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.device_info_add_layout,
                    null);
            TextView name_text = (TextView) layout.findViewById(R.id.left_text);
            TextView name_v = (TextView) layout.findViewById(R.id.right_text);
            name_text.setText(getResources().getString(R.string.amount_text1));
            if (null != oilLeve && oilLeve.length > 0) {
                oilLave = oilLeve[oilLeve.length - 1].getLevel();
                name_v.setText(Constants.float2String(oilLave)
                        + Constants.S_SCANNING_PER);
            } else if (null != mcDeviceSInfo
                    && null != mcDeviceSInfo.getLastLevel()) {
                name_v.setText(Constants.float2String(mcDeviceSInfo
                        .getLastLevel().getLevel()) + Constants.S_SCANNING_PER);
            } else {
                name_v.setText(ResV.getString(R.string.amount_text2));
            }

            oil_amount_add_layout.addView(layout);
        }

    }

    private void addOcdLayout(int n) {
        if (null != mcDeviceSInfo && null != mcDeviceSInfo.getOcdList()
                && mcDeviceSInfo.getOcdList().length > 0) {
            ScanningOcdInfo ocdInfo = mcDeviceSInfo.getOcdList()[0];
            String name, val;
            oil_line_d.setVisibility(View.VISIBLE);
            oil_add_layout.setVisibility(View.VISIBLE);
            // RelativeLayout.LayoutParams lp = new
            // RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 60);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.device_info_add_layout,
                    null);
            TextView name_text = (TextView) layout.findViewById(R.id.left_text);
            TextView name_v = (TextView) layout.findViewById(R.id.right_text);
            switch (n) {
                case 0:
                    name = "剩余油量";
                    val = Constants.float2String(mcDeviceSInfo.getOilLave())
                            + Constants.S_SCANNING_PER;
                    break;
                case 1:
                    name = "时均消耗";
                    val = Constants.float2String(ocdInfo.getHourOilConsume())
                            + Constants.S_SCANNING_CONSUME;
                    break;
                case 2:
                    name = "时均花费";
                    val = Constants.float2String(ocdInfo.getHourOilPay())
                            + Constants.S_SCANNING_PAY;
                    break;
                case 3:
                    name = "当日消耗";
                    val = Constants.float2String(ocdInfo.getDayOilConsume())
                            + Constants.S_SCANNING_CONSUME;
                    break;
                case 4:
                    name = "当日花费";
                    val = Constants.float2String(ocdInfo.getDayOilPay())
                            + Constants.S_SCANNING_PAY;
                    break;
                default:
                    name = "";
                    val = "";
                    break;
            }
            name_text.setText(name);
            name_v.setText(val);
            if (n != 0)
                oil_add_layout.addView(layout);
        }
    }

    private void addWTLayout(int n) {
        if (null != mcDeviceSInfo && null != mcDeviceSInfo.getWhdList()
                && mcDeviceSInfo.getWhdList().length > 0) {
            ScanningWTInfo edInfo = mcDeviceSInfo.getWhdList()[mcDeviceSInfo
                    .getWhdList().length - 1];
            String name, val;
            engine_line_d.setVisibility(View.VISIBLE);
            engine_add_layout.setVisibility(View.VISIBLE);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.device_info_add_layout,
                    null);
            TextView name_text = (TextView) layout.findViewById(R.id.left_text);
            TextView name_v = (TextView) layout.findViewById(R.id.right_text);
            switch (n - OcdAddLayoutNum) {
                case 0:
                    name = "今日时长";
                    val = ValueFormatUtil
                            .getWorkTime(edInfo.getDayWorkHour(), "0时");
                    break;
                default:
                    name = "";
                    val = "";
                    break;
            }
            name_text.setText(name);
            name_v.setText(val);
            engine_add_layout.addView(layout);
        }

    }

    /*
     * private void addEdLayout(int n){ if(null != mcDeviceSInfo && null !=
     * mcDeviceSInfo.getEdList() && mcDeviceSInfo.getEdList().length>0){
     * ScanningEdInfo edInfo = mcDeviceSInfo.getEdList()[0]; String name,val;
     * engine_line_d.setVisibility(View.VISIBLE);
     * engine_add_layout.setVisibility(View.VISIBLE); LayoutInflater inflater =
     * (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE); View layout =
     * inflater.inflate(R.layout.device_info_add_layout,null); TextView
     * name_text = (TextView)layout.findViewById(R.id.left_text); TextView
     * name_v = (TextView)layout.findViewById(R.id.right_text);
     * switch(n-OcdAddLayoutNum){ case 0: name = "机油压力"; val =
     * Constants.float2String(edInfo.getOilPress())+Constants.S_SCANNING_PREE;
     * break; case 1: name = "冷却水温度"; val =
     * Constants.float2String(edInfo.getWaterTemper
     * ())+Constants.S_SCANNING_TEMPER; break; default: name = ""; val = "";
     * break; } name_text.setText(name); name_v.setText(val);
     * engine_add_layout.addView(layout); }
     *
     * }
     */
    private void addHsdLayout(int n) {
        if (null != mcDeviceSInfo && null != mcDeviceSInfo.getWdList()
                && mcDeviceSInfo.getWdList().length > 0) {
            int listSize = mcDeviceSInfo.getWdList().length;
            if (n - OcdAddLayoutNum - EdAddLayoutNum >= listSize)
                return;
            ScanningAlarmInfo hsdInfo = mcDeviceSInfo.getWdList()[n
                    - OcdAddLayoutNum - EdAddLayoutNum];
            String name, val;
            hydraulic_line_d.setVisibility(View.VISIBLE);
            hydraulic_add_layout.setVisibility(View.VISIBLE);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.device_info_add_layout,
                    null);
            TextView name_text = (TextView) layout.findViewById(R.id.left_text);
            TextView name_v = (TextView) layout.findViewById(R.id.right_text);
            switch (n - OcdAddLayoutNum - EdAddLayoutNum) {
                default:
                    name = hsdInfo.getWarning();
                    val = "";
                    break;
            }
            name_text.setText(name);
            name_v.setText(val);
            hydraulic_add_layout.addView(layout);
        }

    }

	/*
     * private void addHsdLayout(int n){ if(null != mcDeviceSInfo && null !=
	 * mcDeviceSInfo.getHsdList() && mcDeviceSInfo.getHsdList().length>0){
	 * ScanningHsdInfo hsdInfo = mcDeviceSInfo.getHsdList()[0]; String name,val;
	 * hydraulic_line_d.setVisibility(View.VISIBLE);
	 * hydraulic_add_layout.setVisibility(View.VISIBLE); LayoutInflater inflater
	 * = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE); View layout
	 * = inflater.inflate(R.layout.device_info_add_layout,null); TextView
	 * name_text = (TextView)layout.findViewById(R.id.left_text); TextView
	 * name_v = (TextView)layout.findViewById(R.id.right_text);
	 * switch(n-OcdAddLayoutNum-EdAddLayoutNum){ case 0: name = "先导压力"; val =
	 * Constants
	 * .float2String(hsdInfo.getLeaderPress())+Constants.S_SCANNING_PREE; break;
	 * case 1: name = "主泵压力1"; val =
	 * Constants.float2String(hsdInfo.getPumpPress1
	 * ())+Constants.S_SCANNING_PREE; break; case 2: name = "主泵压力2"; val =
	 * Constants
	 * .float2String(hsdInfo.getPumpPress2())+Constants.S_SCANNING_PREE; break;
	 * case 3: name = "回油压力"; val =
	 * Constants.float2String(hsdInfo.getBackOilPress
	 * ())+Constants.S_SCANNING_PREE; break; case 4: name = "回油温度"; val =
	 * Constants
	 * .float2String(hsdInfo.getOilTemper())+Constants.S_SCANNING_TEMPER; break;
	 * case 5: name = "主泵泄油口温度"; val =
	 * Constants.float2String(hsdInfo.getPumpOilTemper
	 * ())+Constants.S_SCANNING_TEMPER; break; default: name = ""; val = "";
	 * break; } name_text.setText(name); name_v.setText(val);
	 * hydraulic_add_layout.addView(layout); }
	 * 
	 * }
	 */

    private void showPickDialog(String msg) {
        MyDialog dialog = new MyDialog(mContext, R.style.MyDialog,
                new MyDialog.LeaveMyDialogListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.negative_button:
                                Message msg = new Message();
                                msg.what = HANDLER_DELETE_DEVICE;
                                msg.obj = String.valueOf(deviceId);
                                mHandler.sendMessage(msg);
                                break;
                            default:
                                break;
                        }
                    }
                });
        dialog.show();
        dialog.setText(msg);
        dialog.setNegativeText("确定");

    }

    private int getScanningF() {
        int f = 0;
        if (null != mcDeviceSInfo) {
            f = mcDeviceSInfo.getScore();
            /*
			 * if(mcDeviceSInfo.getOcdList()!=null &&
			 * mcDeviceSInfo.getOcdList().length>0){ ScanningOcdInfo ocdInfo =
			 * mcDeviceSInfo.getOcdList()[0]; f +=ocdInfo.getScore(); }
			 * if(mcDeviceSInfo.getEdList()!=null &&
			 * mcDeviceSInfo.getEdList().length>0){ ScanningEdInfo edInfo =
			 * mcDeviceSInfo.getEdList()[0]; f +=edInfo.getScore(); }
			 * if(mcDeviceSInfo.getHsdList()!=null &&
			 * mcDeviceSInfo.getHsdList().length>0){ ScanningHsdInfo hsdInfo =
			 * mcDeviceSInfo.getHsdList()[0]; f +=hsdInfo.getScore(); }
			 */
        }
        return f;
    }

    private void getBasicsInfo() {
        new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
        // new MachineDetailAsync(deviceId,mContext,mHandler).execute();
        new MachineDetailAsync(deviceId, mContext, mHandler).execute();
    }

    private boolean isHaveFilt() {
        if (!TextUtils.isEmpty(mcDeviceMac)) {
            return true;
        } else {
            Constants.MyToast(getResources().getString(R.string.no_permission));// no_filt_message
            return false;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity_Result_Sensor) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        mcDeviceMac = bundle.getString(Constants.P_DEVICEMAC);
                    }
                }
            }

        }
    }

    private void updateWorkStatus(int workStatus) {
		/*
		 * if (null != title_layout && null != deviceName) { if (workStatus ==
		 * 0) { // title_layout.setTitle(deviceName);
		 * title_layout.setTitleRemarks(""); //
		 * work_status_image.setBackgroundResource(R.drawable.mc_workstatus_0);
		 * } else { title_layout.setTitleRemarks(getResources().getString(
		 * R.string.work_status_name)); //
		 * work_status_image.setBackgroundResource(R.drawable.mc_workstatus_1);
		 * } }
		 */
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<FaultWarnListInfo> dataList = faultWarnInfo.getReList();
        if (!dataList.get(position).getBexcess()) {
            Bundle b = new Bundle();
            b.putLong(Constants.P_DEVICEID, deviceId);
            b.putLong(Constants.P_DEVICEINFO_collectionDate, faultWarnInfo.getCheckDate());
            b.putInt(Constants.P_ID, dataList.get(position).getId());
            b.putString(Constants.P_DEVICENAME, dataList.get(position).getItem());
            Constants.toActivity(this, FaultDitailsListActivity.class, b, false);
        }
    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                mcDeviceBasicsInfo);
        bundle.putLong("deviceId", deviceId);
        Constants.toActivity(this,
                MapViewActivity.class, bundle);
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                mcDeviceBasicsInfo);
        Constants.toActivity(this,
                MapViewActivity.class, bundle);
        return false;
    }

}
