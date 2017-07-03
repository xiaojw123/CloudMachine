package com.cloudmachine.ui.home.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.cloudmachine.R;
import com.cloudmachine.activities.AddDeviceActivity;
import com.cloudmachine.activities.HistoricalTrackActivity;
import com.cloudmachine.activities.MapOneActivity;
import com.cloudmachine.activities.OilAmountActivity;
import com.cloudmachine.activities.RepairRecordActivity;
import com.cloudmachine.activities.WorkHoursActivity;
import com.cloudmachine.autolayout.widgets.DynamicWave;
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;
import com.cloudmachine.ui.home.model.DeviceDetailModel;
import com.cloudmachine.ui.home.presenter.DeviceDetailPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.mpchart.ValueFormatUtil;
import com.cloudmachine.utils.widgets.TextProgressBar;
import com.cloudmachine.widget.CommonTitleView;
import com.github.mikephil.charting.utils.AppLog;
import com.navigation.NativeDialog;
import com.navigation.other.Location;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceDetailActivity extends BaseMapActivity<DeviceDetailPresenter, DeviceDetailModel> implements DeviceDetailContract.View, View.OnClickListener, AMapLocationListener {
    McDeviceInfo mcDeviceInfo;
    McDeviceLocation mcDeviceLoc;
    @BindView(R.id.device_detail_location)
    RelativeLayout locRl;
    @BindView(R.id.device_detail_repair_record)
    RelativeLayout recordRl;
    @BindView(R.id.device_fence_layout)
    RelativeLayout fenceRl;
    @BindView(R.id.device_path_layout)
    RelativeLayout pathRl;
    @BindView(R.id.device_detail_title_layout)
    CommonTitleView mTitleView;
    @BindView(R.id.tv_location)
    TextView mLocTv;
    @BindView(R.id.device_detail_bottom)
    LinearLayout bottomLayout;
    @BindView(R.id.devicedetail_oil)
    DynamicWave oilWave;
    @BindView(R.id.devicedetail_worklen_tpb)
    TextProgressBar workLenTpb;
    @BindView(R.id.oil_layout)
    LinearLayout oilLlt;
    @BindView(R.id.worklen_layout)
    LinearLayout workLenLlt;
    @BindView(R.id.work_status_tv)
    TextView workStatusTv;
    long deviceId;
    Location locNow;
    Bundle mBundle;
    ViewGroup.LayoutParams bottomParams;
    String deviceName;
    int oilValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        setinfoWIndowHiden(false);
        startlocaction(this);
        if (UserHelper.isLogin(this)) {
            mPresenter.getDeviceInfo(String.valueOf(deviceId), UserHelper.getMemberId(this));
        }else{
            mPresenter.getDeviceInfo(String.valueOf(deviceId));
        }
    }

    private void initView() {
        initBottomAnim();
        mcDeviceInfo = (McDeviceInfo) getIntent().getSerializableExtra(Constants.MC_DEVICEINFO);
        if (mcDeviceInfo != null) {
            oilValue=(int) mcDeviceInfo.getOilLave();
            oilWave.setLave(oilValue);
            float time = mcDeviceInfo.getWorkTime();
            workLenTpb.setProgress((int) time);
            workLenTpb.setText(ValueFormatUtil.getWorkTime(time, "0时"));
            deviceId = mcDeviceInfo.getId();
            deviceName = mcDeviceInfo.getName();
            mTitleView.setTitleNmae(deviceName);
            mTitleView.setRightText(this);
            mcDeviceLoc = mcDeviceInfo.getLocation();
            mLocTv.setText(mcDeviceLoc.getPosition());
            if (mcDeviceInfo.getWorkStatus()==1){
                workStatusTv.setVisibility(View.VISIBLE);
            }else{
                workStatusTv.setVisibility(View.GONE);
            }
            LatLng latLng = new LatLng(mcDeviceLoc.getLat(), mcDeviceLoc.getLng());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
            Marker marker;
            if (mcDeviceInfo.getId()==0){
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_experience));
            }else{
            if (mcDeviceInfo.getWorkStatus() == 1) {
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_work));
            } else {
                marker = aMap.addMarker(getMarkerOptions(latLng, R.drawable.icon_machine_unwork));
            }
            }
            marker.showInfoWindow();
            mcDeviceInfo.getId();
        }
    }

    private void initBottomAnim() {
        bottomParams = bottomLayout.getLayoutParams();
        int bottomHieght = (int) getResources().getDimension(R.dimen.device_detail_bottom_height);
        ValueAnimator v = ValueAnimator.ofInt(0, bottomHieght);
        v.addUpdateListener(bottomAnimListener);
        v.setDuration(Constants.ANIMATION_DURACTION);
        v.start();
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initAMap() {
        setMapZoomTo(ZOOM_DEFAULT);
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


    @OnClick({R.id.worklen_layout, R.id.oil_layout, R.id.device_detail_repair_record, R.id.device_fence_layout, R.id.device_path_layout})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.common_titleview_right_tv:
                if (mBundle == null) {
                    mBundle = new Bundle();
                }
                mBundle.putBoolean(AddDeviceActivity.DEVICE_SHOW, true);
                mBundle.putInt(Constants.P_ADDMCDEVICETYPE, 0);
                Constants.toActivity(DeviceDetailActivity.this,
                        AddDeviceActivity.class, mBundle);
                break;

            case R.id.navigation_tv:
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
                bundle_repair.putSerializable(Constants.P_DEVICEINFO_MY, mcDeviceInfo);
                bundle_repair.putLong(Constants.P_DEVICEID, deviceId);
                Constants.toActivity(this, RepairRecordActivity.class, bundle_repair);
                break;
            case R.id.device_fence_layout:
                Constants.toActivity(this, MapOneActivity.class, mBundle);
                break;
            case R.id.device_path_layout:
//                Constants.toActivity(this, HistoricalTrackNewActivity.class, mBundle);
                Constants.toActivity(this, HistoricalTrackActivity.class, mBundle);
                break;
            case R.id.worklen_layout:
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_worktime_detai);
                Bundle b_wt = new Bundle();
                b_wt.putLong(Constants.P_DEVICEID, deviceId);
                Constants.toActivity(this, WorkHoursActivity.class, b_wt, false);
                break;
            case R.id.oil_layout:
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
            mBundle.putSerializable(Constants.P_MCDEVICEBASICSINFO, info);
        }
    }

    private ValueAnimator.AnimatorUpdateListener bottomAnimListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int h = (Integer) animation.getAnimatedValue();
            bottomParams.height = h;
            bottomLayout.setLayoutParams(bottomParams);
        }
    };

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        AppLog.print("onLocationChanged____定位信息");
        if (locNow == null) {
            locNow = new Location(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        }
        mlocClient.stopLocation();
    }

}
