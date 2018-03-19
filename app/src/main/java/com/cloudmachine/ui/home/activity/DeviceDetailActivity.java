package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.cloudmachine.R;
import com.cloudmachine.activities.AddDeviceActivity;
import com.cloudmachine.activities.HistoricalTrackActivity;
import com.cloudmachine.activities.MapOneActivity;
import com.cloudmachine.activities.OilAmountActivity;
import com.cloudmachine.activities.WorkTimeActivity;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.navigation.NativeDialog;
import com.cloudmachine.navigation.other.Location;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;
import com.cloudmachine.ui.home.model.DeviceDetailModel;
import com.cloudmachine.ui.home.presenter.DeviceDetailPresenter;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.widget.CommonTitleView;
import com.cloudmachine.widget.ReboundScrollView;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class DeviceDetailActivity extends BaseMapActivity<DeviceDetailPresenter, DeviceDetailModel> implements DeviceDetailContract.View, View.OnClickListener, AMapLocationListener {
    McDeviceLocation mcDeviceLoc;
    @BindView(R.id.device_detail_repair_record)
    FrameLayout recordFl;
    @BindView(R.id.device_fence_layout)
    FrameLayout fenceFl;
    @BindView(R.id.device_path_layout)
    FrameLayout pathfl;
    @BindView(R.id.device_detail_title_layout)
    CommonTitleView mTitleView;
    @BindView(R.id.device_info_loc)
    TextView mLocTv;
    //    @BindView(R.id.device_detail_bottom)
//    LinearLayout bottomLayout;
    @BindView(R.id.device_info_oil)
    TextView oilWaveTv;
    @BindView(R.id.device_info_timelen)
    TextView timeLenTv;
    @BindView(R.id.device_info_status)
    TextView workStatusTv;
    @BindView(R.id.device_detail_bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.device_info_cotainer)
    LinearLayout infoCotainer;
    @BindView(R.id.device_detail_item_container)
    LinearLayout itemContainer;
    @BindView(R.id.device_detail_rsv)
    ReboundScrollView mScrollView;
    @BindView(R.id.device_detail_work_tv)
    TextView workVideoTv;
    @BindView(R.id.device_detail_guide_layout)
    RelativeLayout guideLayout;
    @BindView(R.id.device_detail_guide_sure_btn)
    Button sureBtn;


    long deviceId;
    Location locNow;
    Bundle mBundle;
    String deviceName;
    int oilValue;
    String snId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setinfoWIndowHiden(false);
        startlocaction(this);
        initMyLocation();
        initView();
        updateDeviceName();


    }

    private void initMyLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.transparent));
        myLocationStyle.strokeColor(getResources().getColor(R.color.transparent));
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);
    }

    private void updateDeviceName() {
        mRxManager.on(Constants.UPDATE_DEVICE_NAME, new Action1<String>() {
            @Override
            public void call(String o) {
                deviceName = o;
                mTitleView.setTitleName(deviceName);

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_DETECTION);
        if (UserHelper.isLogin(this)) {
            mPresenter.getDeviceInfo(String.valueOf(deviceId), UserHelper.getMemberId(this));
        } else {
            mPresenter.getDeviceInfo(String.valueOf(deviceId));
        }
    }

    private void initView() {
        initBottomAnim();
        Bundle bundle = getIntent().getExtras();
        boolean isNowData = bundle.getBoolean(Constants.DEVICE_DETAIL_NOW, false);
        if (isNowData) {
            String deviceIdStr = bundle.getString(Constants.DEVICE_ID);
            deviceId = Long.parseLong(deviceIdStr);
            mPresenter.getDeviceNowData(deviceIdStr);
        } else {
            McDeviceInfo mcDeviceInfo = (McDeviceInfo) bundle.getSerializable(Constants.MC_DEVICEINFO);
            if (mcDeviceInfo == null) {
                mcDeviceInfo = new McDeviceInfo();
                String position = bundle.getString("devicePosition");
                mcDeviceInfo.setName(bundle.getString("deviceName"));
                mcDeviceInfo.setId(bundle.getLong("deviceId"));
                mcDeviceInfo.setWorkStatus(bundle.getInt("deviceWorkState"));
                McDeviceLocation loc = new McDeviceLocation();
                loc.setPosition(position);
                mcDeviceInfo.setLocation(loc);
            }
            deviceId = mcDeviceInfo.getId();
            updateDeviceDetail(mcDeviceInfo);
        }

    }

    private void initBottomAnim() {
//        mScrollView.setScrollAnimListener(animListener);
        mScrollView.setScrollVisible(true);
        bottomLayout.startAnimation(CommonUtils.getTraslateAnim());
        bottomLayout.setVisibility(View.VISIBLE);
    }
//    Animation.AnimationListener animListener= new Animation.AnimationListener() {
//        @Override
//        public void onAnimationStart(Animation animation) {
//            if (mScrollView.deltaY > 0) {
//                itemContainer.setVisibility(View.GONE);
//            } else if (mScrollView.deltaY<0){
//                itemContainer.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            AppLog.print("onAnimationEnd___");
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }
//    };

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initAMap() {
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);

    }


    @Override
    public int getLayoutResID() {
        return R.layout.activity_device_detail;
    }


    @Override
    public View getMarkerInfoView(Marker marker) {
        View view = LayoutInflater.from(this).inflate(R.layout.devicedetail_marker_window, null);
        TextView navTv = (TextView) view.findViewById(R.id.navigation_tv);
        navTv.setOnClickListener(this);
        return view;

    }


    @OnClick({R.id.device_detail_guide_sure_btn, R.id.device_detail_work_tv, R.id.device_info_timelen, R.id.device_info_oil, R.id.device_detail_repair_record, R.id.device_fence_layout, R.id.device_path_layout})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.device_detail_guide_sure_btn:
                guideLayout.setVisibility(View.GONE);
                break;

            case R.id.device_detail_work_tv:
                if (UserHelper.isLogin(this)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.SN_ID, snId);
                    bundle.putString(Constants.DEVICE_ID,String.valueOf(deviceId));
//                    Constants.toActivity(this, WorkPicListActivity.class, bundle);
                    Constants.toActivity(this, WorkVideoActivity.class, bundle);
                } else {
                    Constants.toActivity(this, LoginActivity.class, null);
                }
                break;
            case R.id.common_titleview_right_tv:
                if (mBundle == null) {
                    mBundle = new Bundle();
                }
                mBundle.putBoolean(AddDeviceActivity.IMG_TITLE_SHOW, true);
                mBundle.putBoolean(AddDeviceActivity.DEVICE_SHOW, true);
                mBundle.putInt(Constants.P_ADDMCDEVICETYPE, 0);
                Constants.toActivity(DeviceDetailActivity.this,
                        AddDeviceActivity.class, mBundle);
                break;

            case R.id.navigation_tv:
                MobclickAgent.onEvent(this, MobEvent.COUNT_LOCATION_NAVIGATION);
                AppLog.print("nav start lat__" + mcDeviceLoc.getLat() + ", lng___" + mcDeviceLoc.getLng() + ", position___" + mcDeviceLoc.getPosition());
                Location locEnd = new Location(mcDeviceLoc.getLat(), mcDeviceLoc.getLng(), mcDeviceLoc.getPosition());
                if (locNow == null) {
                    ToastUtils.showToast(this, "无法定位当前位置");
                    return;
                }
                NativeDialog msgDialog = new NativeDialog(this, locNow, locEnd);
                msgDialog.show();
                break;
            case R.id.device_detail_repair_record:
                Bundle bundle_repair = new Bundle();
                bundle_repair.putLong(Constants.MC_DEVICEID, deviceId);
                Constants.toActivity(this, RepairRecordNewActivity.class, bundle_repair);
                break;
            case R.id.device_fence_layout:
                Constants.toActivity(this, MapOneActivity.class, mBundle);
                break;
            case R.id.device_path_layout:
//                Constants.toActivity(this, HistoricalTrackNewActivity.class, mBundle);
                Constants.toActivity(this, HistoricalTrackActivity.class, mBundle);
                break;
            case R.id.device_info_timelen:
                MobclickAgent.onEvent(mContext, MobEvent.COUNT_WORKTIME_TIME);
                Bundle b_wt = new Bundle();
                b_wt.putLong(Constants.P_DEVICEID, deviceId);
                b_wt.putString(Constants.P_DEVICENAME,deviceName);
                Constants.toActivity(this, WorkTimeActivity.class, b_wt, false);
                break;
            case R.id.device_info_oil:
                Bundle b_ail = new Bundle();
                b_ail.putLong(Constants.P_DEVICEID, deviceId);
                b_ail.putString(Constants.P_DEVICENAME, deviceName);
                b_ail.putInt(Constants.P_OILLAVE, oilValue);
                Constants.toActivity(this, OilAmountActivity.class, b_ail, false);
                break;
        }


    }

    @Override
    public void retrunDeviceInfo(McDeviceBasicsInfo info) {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        mBundle.putSerializable(Constants.P_MCDEVICEBASICSINFO, info);
    }

    @Override
    public void updateDeviceDetail(McDeviceInfo info) {
        if (info == null) {
            return;
        }
        snId = info.getSnId();
        if (CommonUtils.isHConfig(snId)) {
            workVideoTv.setVisibility(View.VISIBLE);
            if (!UserHelper.getHConfigGuideTag(this)) {
                UserHelper.insertHConfigGuideTag(this, true);
                guideLayout.setVisibility(View.VISIBLE);
            }
        }
        oilValue = info.getOilLave();
        oilWaveTv.setText(CommonUtils.formatOilValue(oilValue));
        float time = info.getWorkTime();
        timeLenTv.setText(CommonUtils.formatTimeLen(time));
        deviceName = info.getName();
        mTitleView.setTitleName(deviceName);
        mTitleView.setRightText(this);
        mcDeviceLoc = info.getLocation();
        mLocTv.setText(mcDeviceLoc.getPosition());
        if (info.getWorkStatus() == 1) {
            workStatusTv.setVisibility(View.VISIBLE);
        } else {
            workStatusTv.setVisibility(View.GONE);
        }
        LatLng latLng = new LatLng(mcDeviceLoc.getLat(), mcDeviceLoc.getLng());
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(latLng);
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(builder.build(), 0, 0, DensityUtil.dip2px(this,100), 0));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
        setMapZoomTo(ZOOM_DEFAULT);
        aMap.moveCamera(CameraUpdateFactory.scrollBy(0, DensityUtil.dip2px(this, 100)));
        Marker marker;

        if (info.getWorkStatus() == 1) {
            marker = aMap.addMarker(getMarkerOptions(this, latLng, R.drawable.icon_machine_work));
        } else {
            marker = aMap.addMarker(getMarkerOptions(this, latLng, R.drawable.icon_machine_unwork));
        }

        marker.showInfoWindow();
    }

    @Override
    public void updateDeviceDetailError(String message) {
        ToastUtils.showToast(this, message);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppLog.print("onLocationChanged____定位信息");
        if (locNow == null) {
            locNow = new Location(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        }
        mlocClient.stopLocation();

    }


}

