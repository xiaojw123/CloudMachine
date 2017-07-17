package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseMapActivity<T extends BasePresenter, E extends BaseModel> extends BaseAutoLayoutActivity<T, E> implements AMap.InfoWindowAdapter, AMap.OnMapClickListener, AMap.OnMarkerClickListener {
    protected static final float ZOOM_DEFAULT = 16;
    protected static final float ZOOM_BIG=22;
    protected boolean isFirstLoc = true;
    private final int REQ_FINE_LOCATION = 0x12;
    @BindView(R.id.home_mapview)
    protected MapView mMapView;
    protected AMap aMap;
    protected AMapLocationClient mlocClient;
    protected AMapLocationClientOption mLocOption;
    PermissionsChecker permissionsChecker;
    protected Marker curMarker;
    boolean isHidenAble = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        initMap(savedInstanceState);
    }

    public void setinfoWIndowHiden(boolean isHidenAble) {
        this.isHidenAble = isHidenAble;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    protected void initAMap() {
    }

    protected void setMapZoomTo(float zoomPixel) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomPixel));
    }


    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mMapView.setDrawingCacheEnabled(true);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
//        aMap.setInfoWindowAdapter(this);
//        aMap.setOnMapClickListener(this);
//        aMap.setOnMarkerClickListener(this);
        initAMap();
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(false);
    }

    protected void startlocaction(AMapLocationListener listener) {
        mLocOption = new AMapLocationClientOption();
        mLocOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocOption.setInterval(2000);
        mlocClient = new AMapLocationClient(this);
        mlocClient.setLocationOption(mLocOption);
        mlocClient.setLocationListener(listener);
        permissionsChecker = new PermissionsChecker(this);
        if (permissionsChecker.lacksPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionsActivity.startActivityForResult(this,
                    REQ_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            mlocClient.startLocation();
        }
    }


    public abstract int getLayoutResID();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FINE_LOCATION) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                ToastUtils.showToast(this, "权限被拒绝");
            } else {
                mlocClient.startLocation();
            }
        }
    }


    protected MarkerOptions getMarkerOptions(Context context,LatLng latLng, int resid) {
        return getMarkerOptions(context,latLng,resid,"mark");
    }


    protected MarkerOptions getMarkerOptions(Context context,LatLng latLng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        ImageView img=new ImageView(context);
        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, Constants.MACHINE_ICON_WIDTH),DensityUtil.dip2px(context,Constants.MACHINE_ICON_HEIGHT)));
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setImageResource(resid);
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title(title);
        options.position(latLng);
        return options;
    }

    protected MarkerOptions getNormalMarkerOptions(Context context,LatLng latLng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(resid));
        options.title(title);
        options.position(latLng);
        return options;
    }
    protected MarkerOptions getMarkerOptions(double lat, double lng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(resid));
        if (!TextUtils.isEmpty(title)) {
            options.title(title);
        }
        options.position(new LatLng(lat, lng));
        return options;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        curMarker = marker;
        return getMarkerInfoView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getInfoWindow(marker);
    }

    public View getMarkerInfoView(Marker marker) {

        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (!isHidenAble) {
            return;
        }
        if (curMarker != null && curMarker.isInfoWindowShown()) {
            curMarker.hideInfoWindow();
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
        return false;
    }
}
