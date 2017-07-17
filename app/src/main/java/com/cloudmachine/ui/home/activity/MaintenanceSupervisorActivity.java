package com.cloudmachine.ui.home.activity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapUtils;
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
import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.struc.UnfinishedBean;
import com.cloudmachine.ui.home.contract.MSupervisorContract;
import com.cloudmachine.ui.home.model.MSupervisorModel;
import com.cloudmachine.ui.home.model.SiteBean;
import com.cloudmachine.ui.home.presenter.MSupervisorPresenter;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.github.mikephil.charting.utils.AppLog;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MaintenanceSupervisorActivity extends BaseMapActivity<MSupervisorPresenter, MSupervisorModel> implements MSupervisorContract.View, AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String CURRENT_LOC = "当前位置";
    @BindView(R.id.repair_btn)
    RadiusButtonView repairBtn;
    @BindView(R.id.maintence_cur_location)
    TextView curLocTv;
    @BindView(R.id.maintenance_order_container)
    RelativeLayout orderRl;
    @BindView(R.id.maintenance_brand_tv)
    TextView brandTv;
    @BindView(R.id.maintenance_model_tv)
    TextView modelTv;
    @BindView(R.id.maintenance_date_tv)
    TextView dateTv;
    @BindView(R.id.maintenance_desc_tv)
    TextView descTv;
    @BindView(R.id.maintenance_status_tv)
    TextView statusTv;
    @BindView(R.id.maintence_cardview)
    CardView curLocCv;



    Marker centerMarker;
    GeocodeSearch geocoderSearch;
    LatLng lastLatLng;
    UnfinishedBean unfinishedBean;
    List<Marker> markerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.onEvent(this, UMengKey.time_repair_soldiers);
        startlocaction(this);
        initGeocoder();
        setinfoWIndowHiden(false);
        mPresenter.getRepairItemView(UserHelper.getMemberId(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initGeocoder() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }


    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }

    @Override
    protected void initAMap() {
        setMapZoomTo(ZOOM_DEFAULT);
        aMap.setOnCameraChangeListener(this);
    }


    @Override
    public int getLayoutResID() {
        return R.layout.activity_maintenance_supervisor;
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            AppLog.print("onLocationChanged___lat:" + aMapLocation.getLatitude() + "，  lng:" + aMapLocation.getLongitude() + ", address:" + aMapLocation.getAddress());
            mlocClient.stopLocation();
            double lat = aMapLocation.getLatitude();
            double lng = aMapLocation.getLongitude();
            LatLng loclatLng = new LatLng(lat, lng);
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(loclatLng));
        }
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        AppLog.print("onCameraChange______");
        if (centerMarker != null) {
            centerMarker.setPosition(cameraPosition.target);
        } else {
            centerMarker = aMap.addMarker(getNormalMarkerOptions(this,cameraPosition.target, R.drawable.icon_cur_repair_loc, CURRENT_LOC));
        }
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        AppLog.print("onCameraChangeFinish______");
        LatLng lat = cameraPosition.target;
        if (lastLatLng == null) {
            lastLatLng = lat;
        } else {
            float distance = AMapUtils.calculateLineDistance(lastLatLng, lat);
            if (distance > 500) {
                lastLatLng = lat;
                AppLog.print("开始请求站点。。。distance=" + distance);
                for (Marker marker : markerList) {
                    marker.remove();
                }
                if (markerList.size() > 0) {
                    markerList.clear();
                }
                mPresenter.updateStationView(lat.longitude, lat.latitude);
            }
        }

        LatLonPoint point = new LatLonPoint(lat.latitude, lat.longitude);
        RegeocodeQuery query = new RegeocodeQuery(point, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        AppLog.print("onRegeocodeSearched___");
        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
        if (address != null) {
            String formartAddress = address.getFormatAddress();
            String city = address.getTownship();
            String describeAddress = formartAddress.substring(formartAddress.indexOf(city) + city.length());
            curLocTv.setText(describeAddress);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    @Override
    public void returnStationView(SiteBean siteBean) {
        if (siteBean != null) {
            List<SiteBean.RepairStationListBean> repairsStatiionList = siteBean.getRepairStationList();
            List<SiteBean.RepairStationListBean> serviceStatiionList = siteBean.getServiceSiteList();
            for (SiteBean.RepairStationListBean bean : repairsStatiionList) {
                Marker siteMarker = aMap.addMarker(getMarkerOptions(bean.getLat(), bean.getLng(), R.drawable.icon_work_order, null));
                markerList.add(siteMarker);
            }
            for (SiteBean.RepairStationListBean bean : serviceStatiionList) {
                Marker stationMarker = aMap.addMarker(getMarkerOptions(bean.getLat(), bean.getLng(), R.drawable.icon_station, null));
                markerList.add(stationMarker);
            }
        }
    }

    @Override
    public void returnRepairItemView(UnfinishedBean bean, String status) {
        orderRl.setVisibility(View.VISIBLE);
        unfinishedBean = bean;
        statusTv.setText(status);
        brandTv.setText(bean.getVbrandname());
        modelTv.setText(bean.getVmaterialname());
        dateTv.setText(bean.getDopportunity());
        descTv.setText(bean.getVdiscription());

    }

    @OnClick({R.id.maintence_cardview,R.id.maintenance_order_container, R.id.radius_button_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.maintence_cardview:
                break;
            case R.id.maintenance_order_container:
                if (unfinishedBean == null) {
                    return;
                }
//                Intent intent = new Intent(this, RepairBasicInfomationActivity.class);
//                intent.putExtra("orderNum", unfinishedBean.getOrderNum());
//                intent.putExtra("flag", unfinishedBean.getFlag());
//                startActivity(intent);
                Constants.toActivity(this,RepairRecordNewActivity.class,null);
                break;
            case R.id.radius_button_text:
                Bundle bundle = new Bundle();
                bundle.putString(NewRepairActivity.DEFAULT_LOCAITOIN, curLocTv.getText().toString());
                Constants.toActivity(this, NewRepairActivity.class, bundle);
                break;
        }
    }
}
