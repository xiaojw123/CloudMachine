package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.Constants;
import com.github.mikephil.charting.utils.AppLog;

import butterknife.BindView;
import butterknife.OnClick;

public class MaintenanceSupervisorActivity extends BaseMapActivity implements AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String CURRENT_LOC = "当前位置";
    @BindView(R.id.repair_btn)
    RadiusButtonView repairBtn;
    @BindView(R.id.maintence_cur_location)
    TextView curLocTv;
    Marker centerMarker;
    GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }


    @Override
    public void initPresenter() {

    }

    @Override
    protected void initAMap() {
        aMap.setOnCameraChangeListener(this);
    }

    @Override
    protected void initLocationClient() {
        mlocClient.setLocationListener(this);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_maintenance_supervisor;
    }


    @Override
    public void onMapClick(LatLng latLng) {

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
        if (aMapLocation.getErrorCode() == 0) {
            AppLog.print("onLocationChanged___lat:" + aMapLocation.getLatitude() + "，  lng:" + aMapLocation.getLongitude() + ", address:" + aMapLocation.getAddress());
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            if (isFirstLoc) {
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                centerMarker = aMap.addMarker(getMarkerOptions(aMapLocation.getLatitude(), aMapLocation.getLongitude(), R.drawable.icon_cur_repair_loc, CURRENT_LOC));
                aMap.addMarker(getMarkerOptions(30.194103, 120.198859, R.drawable.icon_station));
                aMap.addMarker(getMarkerOptions(30.177613, 120.177134, R.drawable.icon_work_order));
                aMap.addMarker(getMarkerOptions(30.252763, 120.194813, R.drawable.icon_work_order));
                aMap.addMarker(getMarkerOptions(30.268235, 120.101748, R.drawable.icon_work_order));
                aMap.addMarker(getMarkerOptions(30.324301, 120.109294, R.drawable.icon_work_order));
                aMap.addMarker(getMarkerOptions(30.13227, 120.26718, R.drawable.icon_work_order));
                aMap.addMarker(getMarkerOptions(30.263244, 120.21321, R.drawable.icon_work_order));
                isFirstLoc = false;
            }
            mlocClient.stopLocation();
        }
    }


    @OnClick(R.id.repair_btn)
    public void onClick() {
        Constants.toActivity(this, NewRepairActivity.class, null);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng lat = cameraPosition.target;
        if (centerMarker != null) {
            centerMarker.setPosition(lat);
        }
        LatLonPoint point = new LatLonPoint(lat.latitude, lat.longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);


    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress address=regeocodeResult.getRegeocodeAddress();
        curLocTv.setText(address.getFormatAddress());
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
