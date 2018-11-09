package com.cloudmachine.ui.home.activity.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.cloudmachine.R;
import com.cloudmachine.base.BaseFragment;
import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;

import butterknife.BindView;

/**
 * Created by xiaojw on 2017/7/19.
 */

public abstract class BaseMapFragment<T extends BasePresenter, E extends BaseModel> extends BaseFragment<T, E> implements AMap.InfoWindowAdapter, AMap.OnMapClickListener, AMap.OnMarkerClickListener {
    protected static final float ZOOM_DEFAULT = 16;
    protected static final float ZOOM_HOME = 9;
    private final int REQ_FINE_LOCATION = 0x12;
    public static boolean isShowDialog;
    @BindView(R.id.home_mapview)
    protected TextureMapView mMapView;
    protected AMap aMap;
    protected AMapLocationClient mlocClient;
    protected AMapLocationClientOption mLocOption;
    protected Marker curMarker;
    boolean isHidenAble = true;
    boolean isFrobidenClick;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initAMap(savedInstanceState);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (mMapView != null) {
            if (hidden) {
                mMapView.setVisibility(View.INVISIBLE);
            } else {
                mMapView.setVisibility(View.VISIBLE);
            }
        }

    }

    protected void initAMap(Bundle savedInstanceState) {
        if (mMapView == null) {
            return;
        }
        mMapView.onCreate(savedInstanceState);
        mMapView.setDrawingCacheEnabled(true);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        UiSettings settings = aMap.getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setMyLocationButtonEnabled(false);
        setAMap();
    }

    public void setinfoWIndowHiden(boolean isHidenAble) {
        this.isHidenAble = isHidenAble;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    protected void setMapZoomTo(float zoomPixel) {
        if (aMap != null) {
            aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomPixel));
        }
    }

    protected void setAMap() {
    }


    protected void startlocaction(AMapLocationListener listener) {
        if (mLocOption == null) {
            mLocOption = new AMapLocationClientOption();
            mLocOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocOption.setInterval(2000);
        }
        if (mlocClient == null) {
            mlocClient = new AMapLocationClient(getActivity());
            mlocClient.setLocationOption(mLocOption);
            mlocClient.setLocationListener(listener);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (isShowDialog){
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_FINE_LOCATION);
        } else {
            mlocClient.startLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==REQ_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mlocClient.startLocation();
            } else {
                isShowDialog=true;
                CommonUtils.showPermissionDialog(getActivity(),Constants.PermissionType.LOCATION);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    protected MarkerOptions getMarkerOptions(Context context, LatLng latLng, int resid) {
        return getMarkerOptions(context, latLng, resid, "mark");
    }




    protected MarkerOptions getMarkerOptions(Context context, LatLng latLng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(context);
//        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, Constants.MACHINE_ICON_WIDTH),DensityUtil.dip2px(context,Constants.MACHINE_ICON_HEIGHT)));
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setImageResource(resid);
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title(title);
        options.position(latLng);
        return options;
    }

    protected MarkerOptions getMarkerOptions(Context context, LatLng latLng, String url) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(context);
//        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, Constants.MACHINE_ICON_WIDTH),DensityUtil.dip2px(context,Constants.MACHINE_ICON_HEIGHT)));
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        img.setScaleType(ImageView.ScaleType.FIT_XY);
        options.icon(BitmapDescriptorFactory.fromView(img));
//        options.title("mark");
        options.position(latLng);
        Glide.with(context).load(url).into(img);
        return options;
    }


    protected MarkerOptions getNormalMarkerOptions(Context context, LatLng latLng, int resid, String title) {
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
        if (isFrobidenClick) {
            return true;
        }
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
        return false;
    }

}
