package com.cloudmachine.ui.home.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.cloudmachine.R;
import com.cloudmachine.activities.PermissionsActivity;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.utils.PermissionsChecker;
import com.cloudmachine.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseMapActivity<T extends BasePresenter, E extends BaseModel> extends BaseAutoLayoutActivity<T, E> implements AMap.OnMarkerClickListener, AMap.OnMapClickListener, LocationSource {
    private static final int ZOOM_DEFAULT = 17;
    protected boolean isFirstLoc = true;
    private final int REQ_FINE_LOCATION = 0x12;
    @BindView(R.id.home_mapview)
    protected MapView mMapView;
    protected AMap aMap;
    protected AMapLocationClient mlocClient;
    protected AMapLocationClientOption mLocOption;
    PermissionsChecker permissionsChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        initMap(savedInstanceState);
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


    private void initMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mMapView.setDrawingCacheEnabled(true);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setMyLocationEnabled(true);
        }
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(ZOOM_DEFAULT));
        initAMap();
        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        //定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);
        if (permissionsChecker.lacksPermissions(Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionsActivity.startActivityForResult(this,
                    REQ_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startlocaction(AMapLocationListener listener) {
        mLocOption = new AMapLocationClientOption();
        mLocOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocOption.setInterval(2000);
        mlocClient = new AMapLocationClient(this);
        mlocClient.setLocationOption(mLocOption);
        mlocClient.setLocationListener(listener);
        permissionsChecker = new PermissionsChecker(this);
        mlocClient.startLocation();
    }


    public abstract int getLayoutResID();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_FINE_LOCATION) {
            if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                ToastUtils.showToast(this, "权限被拒绝");
            }
        }
    }

    protected MarkerOptions getMarkerOptions(double lat, double lng, int resid) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(this);
        img.setImageResource(resid);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title("mark");
        //位置
        options.position(new LatLng(lat, lng));


        return options;
    }

    protected MarkerOptions getMarkerOptions(LatLng latLng, int resid) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(this);
        img.setImageResource(resid);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title("mark");
        //位置
        options.position(latLng);


        return options;
    }

    protected MarkerOptions getMarkerOptions(double lat, double lng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(this);
        img.setImageResource(resid);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title(title);
        //位置
        options.position(new LatLng(lat, lng));


        return options;
    }

}
