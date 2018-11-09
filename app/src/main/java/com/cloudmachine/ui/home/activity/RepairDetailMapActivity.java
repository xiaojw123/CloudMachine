package com.cloudmachine.ui.home.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cloudmachine.R;
import com.cloudmachine.bean.RepairDetail;
import com.cloudmachine.bean.ScreenInfo;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.helper.OrderStatus;
import com.cloudmachine.ui.home.contract.RepairDetailContract;
import com.cloudmachine.ui.home.model.RepairDetailModel;
import com.cloudmachine.bean.SiteBean;
import com.cloudmachine.ui.home.presenter.RepairDetailPresenter;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class RepairDetailMapActivity extends BaseMapActivity<RepairDetailPresenter, RepairDetailModel> implements RepairDetailContract.View, GeocodeSearch.OnGeocodeSearchListener {
    GeocodeSearch geocoderSearch;
    @BindView(R.id.repair_detail_ctv)
    CommonTitleView repairDetailCtv;
    @BindView(R.id.rd_img)
    CircleImageView rdImg;
    @BindView(R.id.rd_title_tv)
    TextView rdTitleTv;
    @BindView(R.id.rd_call_img)
    ImageView rdCallImg;
    @BindView(R.id.rd_devicename_tv)
    TextView rdDevicenameTv;
    @BindView(R.id.rd_divider1)
    View rdDivider1;
    @BindView(R.id.rd_deviceno_tv)
    TextView rdDevicenoTv;
    @BindView(R.id.rd_description_tv)
    TextView rdDescriptionTv;
    @BindView(R.id.rdm_detail_layout)
    RelativeLayout rdmDetilaLayout;

    LatLngBounds.Builder LatLngBuilder;
    boolean isRepairing;
    String mobile;
    int left, top;
    String nstatus;
    RepairDetail mDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        left = ScreenInfo.screen_width / 4;
        top = ScreenInfo.screen_height / 4;
        setinfoWIndowHiden(false);
        initGeocoder();
        String orderNum = getIntent().getStringExtra(Constants.ORDER_NO);
        nstatus = getIntent().getStringExtra("nstatus");
        repairDetailCtv.setTitleName(nstatus);
        String status = getIntent().getStringExtra("orderStatus");
        isRepairing = "2".equals(status);
        mPresenter.getRepairDetail(orderNum);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OrderStatus.WAIT.equals(nstatus)) {
            MobclickAgent.onEvent(this, MobEvent.REPAIR_WAIT_ACCEPT);
        } else if (OrderStatus.ING.equals(nstatus)) {
            MobclickAgent.onEvent(this, MobEvent.REPAIR_REPARING);
        }
    }

    @Override
    protected void initAMap() {
        isForbidenClick = true;
        aMap.setOnMarkerClickListener(this);

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
    public int getLayoutResID() {
        return R.layout.activity_repair_detail_map;
    }


    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    @Override
    public void returnStationView(SiteBean siteBean) {
        if (siteBean != null) {
            List<SiteBean.RepairStationListBean> serviceStatiionList = siteBean.getServiceSiteList();
//            for (SiteBean.RepairStationListBean bean : repairsStatiionList) {
//                double lat = bean.getLat();
//                double lng = bean.getLng();
//                LatLng latLng = new LatLng(lat, lng);
//                LatLngBuilder.include(latLng);
//                aMap.addMarker(getMarkerOptions(bean.getLat(), bean.getLng(), R.drawable.icon_station, null));
//            }
            if (serviceStatiionList != null && serviceStatiionList.size() > 0) {
                SiteBean.RepairStationListBean bean = serviceStatiionList.get(0);
                double lat = bean.getLat();
                double lng = bean.getLng();
                LatLng latLng = new LatLng(lat, lng);
                LatLngBuilder.include(latLng);
                aMap.addMarker(getMarkerOptions(bean.getLat(), bean.getLng(), R.drawable.icon_station, null));
            }
//            aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBuilder.build(), ));

            aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBuilder.build(), left, left, top, top));
        }
    }

    @Override
    public void returnStationError() {
        aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBuilder.build(), left, left, top, top));

    }


    @OnClick({R.id.rd_call_img, R.id.rdm_detail_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rd_call_img:
                CommonUtils.callPhone(this, mobile);
                break;
            case R.id.rdm_detail_layout:
                Bundle bundle = new Bundle();
                if (mDetail != null) {
                    bundle.putParcelable(Constants.REPAIR_DETAIL, mDetail);
                }
                Constants.toActivity(this, PayDeviceInfoActivity.class, bundle);
                break;

        }


    }


    @Override
    public void updateRepairDetail(RepairDetail detail) {
        mDetail = detail;
        if (detail != null) {
            int resdId;
            String title;
            double dLat = 0;
            double dLng = 0;
            String l1 = detail.getOrderLat();
            String l2 = detail.getOrderLng();
            if (!TextUtils.isEmpty(l1) && !TextUtils.isEmpty(l2)) {
                dLat = Double.parseDouble(l1);
                dLng = Double.parseDouble(l2);
            }
            LatLng curLatlng = new LatLng(dLat, dLng);
            if (dLat == 0 || dLng == 0) {
                //定位失败
                aMap.addMarker(getMarkerLocOptions(this, curLatlng, "定位失败"));
            } else {
                aMap.addMarker(getMarkerLocOptions(this, curLatlng, detail.getAddressDetail()));
            }
            LatLngBuilder = new LatLngBounds.Builder();
            LatLngBuilder.include(curLatlng);
            rdDevicenameTv.setText(detail.getBrandName());
            rdDevicenoTv.setText(detail.getModelName());
            rdDescriptionTv.setText(detail.getServiceDesc());
            if (isRepairing) {
                resdId = R.drawable.icon_head_technician;
                title = detail.getArtificerName();
                mobile = detail.getArtificerMobile();
                List<LatLng> latLngList = new ArrayList<>();
                String serviceLat = detail.getArtificerLat();
                String serviceLon = detail.getArtificerLng();
                if (!TextUtils.isEmpty(serviceLat) && !TextUtils.isEmpty(serviceLon)) {
                    double la = Double.parseDouble(serviceLat);
                    double lo = Double.parseDouble(serviceLon);
                    LatLng latLng = new LatLng(la, lo);
                    latLngList.add(curLatlng);
                    latLngList.add(latLng);
                    LatLngBuilder.include(latLng);
                    aMap.addMarker(getTechnicianMarkerOptions(la, lo, R.drawable.icon_work_order, null));
                    aMap.addPolyline(new PolylineOptions().addAll(latLngList).width(8).color(Color.parseColor("#7bb4f5")));
                }
            } else {
                resdId = R.drawable.ic_repair_detail_station;
                title = detail.getStationName();
                mobile = detail.getAllianceStationMobile();
            }
            rdTitleTv.setText(title);
            rdImg.setImageResource(resdId);
            if (!isRepairing) {
                if (dLat != 0 && dLng != 0) {
//                    mPresenter.updateStationView(dLng, dLat);
                } else {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBuilder.build(), left, left, top, top));
                }
            } else {
                aMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(LatLngBuilder.build(), left, left, top, top));
            }

        }
    }
}
