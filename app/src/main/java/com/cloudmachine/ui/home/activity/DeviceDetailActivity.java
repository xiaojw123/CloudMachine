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
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.cloudmachine.R;
import com.cloudmachine.activities.AddDeviceActivity;
import com.cloudmachine.activities.MapOneActivity;
import com.cloudmachine.activities.RepairRecordActivity;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.home.contract.DeviceDetailContract;
import com.cloudmachine.ui.home.model.DeviceDetailModel;
import com.cloudmachine.ui.home.presenter.DeviceDetailPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.widget.CommonTitleView;
import com.navigation.NativeDialog;
import com.navigation.other.Location;

import butterknife.BindView;
import butterknife.OnClick;

public class DeviceDetailActivity extends BaseMapActivity<DeviceDetailPresenter, DeviceDetailModel> implements AMapLocationListener, DeviceDetailContract.View, View.OnClickListener, AMap.InfoWindowAdapter {
    private boolean isFirstLoc = true;
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

    McDeviceBasicsInfo mInfo;
    long deviceId;
    Location locNow;
    Marker curMarker;
    Bundle mBundle;
    ViewGroup.LayoutParams bottomParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        initBottomAnim();
        mcDeviceInfo = (McDeviceInfo) getIntent().getSerializableExtra(Constants.MC_DEVICEINFO);
        locNow = (Location) getIntent().getSerializableExtra(Constants.MC_LOC_NOW);
        if (mcDeviceInfo != null) {
            deviceId = mcDeviceInfo.getId();
            mTitleView.setTitleNmae(mcDeviceInfo.getName());
            mTitleView.setRightText(this);
            mcDeviceLoc = mcDeviceInfo.getLocation();
            mLocTv.setText(mcDeviceLoc.getPosition());
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(mcDeviceLoc.getLat(), mcDeviceLoc.getLng())));
            aMap.addMarker(getMarkerOptions(mcDeviceLoc.getLat(), mcDeviceLoc.getLng(), R.drawable.icon_machine_work));
            mcDeviceInfo.getId();
            Member member = MemeberKeeper.getOauth(this);
            mPresenter.getDeviceInfo(String.valueOf(mcDeviceInfo.getId()), member.getId());
        }


    }

    private void initBottomAnim() {
        bottomParams=  bottomLayout.getLayoutParams();
        int bottomHieght= (int) getResources().getDimension(R.dimen.device_detail_bottom_height);
        ValueAnimator v=ValueAnimator.ofInt(0,bottomHieght);
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
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnMapClickListener(this);
    }

    @Override
    protected void initLocationClient() {
        mlocClient.setLocationListener(this);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_device_detail;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        curMarker = marker;
        View view = LayoutInflater.from(this).inflate(R.layout.devicedetail_marker_window, null);
        TextView navTv = (TextView) view.findViewById(R.id.navigation_tv);
        navTv.setOnClickListener(this);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getInfoWindow(marker);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (curMarker != null) {
            curMarker.hideInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation.getErrorCode() == 0) {
//            if (isFirstLoc) {
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
//                isFirstLoc = false;
//            }
//            mlocClient.stopLocation();
//        }
    }

    @OnClick({R.id.device_detail_location, R.id.device_detail_repair_record, R.id.device_fence_layout, R.id.device_path_layout})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.common_titleview_right_tv:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                        mInfo);
                bundle.putBoolean(AddDeviceActivity.DEVICE_SHOW, true);
                bundle.putInt(Constants.P_ADDMCDEVICETYPE, 0);
                Constants.toActivity(DeviceDetailActivity.this,
                        AddDeviceActivity.class, bundle);
                break;

            case R.id.navigation_tv:
                if (curMarker != null) {
                    curMarker.hideInfoWindow();
                }
                Location locEnd = new Location(mcDeviceLoc.getLat(), mcDeviceLoc.getLng(), mcDeviceLoc.getPosition());
                NativeDialog msgDialog = new NativeDialog(this, locNow, locEnd);
                msgDialog.show();
                break;

//            case R.id.right_layout:
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
//                        mInfo);
//                bundle.putInt(Constants.P_ADDMCDEVICETYPE, 0);
//                Constants.toActivity(this,
//                        AddDeviceActivity.class, bundle);
//                break;
            case R.id.device_detail_location:
                break;
            case R.id.device_detail_repair_record:
                Bundle bundle_repair = new Bundle();
                bundle_repair.putSerializable(Constants.P_DEVICEINFO_MY, mcDeviceInfo);
                bundle_repair.putLong(Constants.P_DEVICEID, deviceId);
                if (null != mInfo)
                    bundle_repair.putInt(Constants.P_DEVICETYPE,
                            mInfo.getType());
                Constants.toActivity(this, RepairRecordActivity.class, bundle_repair);
                break;
            case R.id.device_fence_layout:
                Constants.toActivity(this, MapOneActivity.class, mBundle);
                break;
            case R.id.device_path_layout:
                Constants.toActivity(this, HistoricalTrackNewActivity.class, mBundle);
                break;

        }


    }

    @Override
    public void retrunDeviceInfo(McDeviceBasicsInfo info) {
        mInfo = info;
        if (mBundle == null) {
            mBundle = new Bundle();
            mBundle.putSerializable(Constants.P_MCDEVICEBASICSINFO, mInfo);
        }
    }
    private ValueAnimator.AnimatorUpdateListener bottomAnimListener=new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int h= (Integer) animation.getAnimatedValue();
            bottomParams.height=h;
            bottomLayout.setLayoutParams(bottomParams);
        }
    };
}
