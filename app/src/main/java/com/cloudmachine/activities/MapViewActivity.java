package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.navigation.NativeDialog;
import com.navigation.other.Location;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author shixionglu
 *         展示我的机器在地图上的位置
 */
public class MapViewActivity extends BaseAutoLayoutActivity implements InfoWindowAdapter, AMapLocationListener {
    private static final int zoomDefault = 14;
    private Context mContext;
    private MapView mapView;
    private AMap aMap;
    private TitleView title_layout;
    private McDeviceBasicsInfo mcDeviceBasicsInfo;
    private McDeviceLocation location;
    private MarkerOptions markerOption;
    private LatLng myLatLng;
    private Marker myMarker;
    private RadiusButtonView electronic_button, historical_button;
    private long deviceId;

    private Location loc_now; /*new Location(30.671249, 104.098863, "s");*/
    private Location loc_end;  /*new Location(30.862644, 103.663077, "e");*/

    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        this.mContext = this;
        if (null == mapView) {
            mapView = (MapView) findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
        }
        getIntentData();
        initView();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                mcDeviceBasicsInfo = (McDeviceBasicsInfo) bundle
                        .getSerializable(Constants.P_MCDEVICEBASICSINFO);
                deviceId = bundle.getLong("deviceId");
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    private void initView() {
        initLocation();
        initTitleLayout();
        initMap();
        electronic_button = (RadiusButtonView) findViewById(R.id.electronic_button);
        electronic_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                        mcDeviceBasicsInfo);
                bundle.putLong("deviceId", deviceId);
                Constants.toActivity(MapViewActivity.this, MapOneActivity.class, bundle);
            }
        });
        historical_button = (RadiusButtonView) findViewById(R.id.historical_button);
        historical_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Bundle bHistorical = new Bundle();
                bHistorical.putSerializable(Constants.P_MCDEVICEBASICSINFO,
                        mcDeviceBasicsInfo);
                Constants.toActivity(MapViewActivity.this, HistoricalTrackActivity.class,
                        bHistorical);
            }
        });
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

    }

    private void initTitleLayout() {

        title_layout = (TitleView) findViewById(R.id.title_layout);
//		if(null != mcDeviceBasicsInfo && !TextUtils.isEmpty(mcDeviceBasicsInfo.getDeviceName())){
//			title_layout.setTitle(mcDeviceBasicsInfo.getDeviceName());
//		}
        title_layout.setTitle("当前位置");
        title_layout.setLeftOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        title_layout.setRightText(getResources().getColor(R.color.white), "去这里", new OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, UMengKey.count_navigation);
                //loc_now = new Location(30.671249, 104.098863, "s");
                loc_end = new Location(location.getLat(), location.getLng(), "e");
                NativeDialog msgDialog = new NativeDialog(MapViewActivity.this, loc_now, loc_end);
                msgDialog.show();
            }
        });
    }

    private void setUpMap() {
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        addMarkerToMap();// 往地图上添加marker
    }

    private void addMarkerToMap() {

        if (null != mcDeviceBasicsInfo) {
            location = mcDeviceBasicsInfo.getLocation();

            markerOption = new MarkerOptions();
            String allTiel = "";
            if (location != null) {
                myLatLng = new LatLng(location.getLat(), location.getLng());
                markerOption.position(myLatLng);
                allTiel = mcDeviceBasicsInfo.getDeviceName() + Constants.S_FG + location.getPosition() + Constants.S_FG + mcDeviceBasicsInfo.getId();
            } else {
                allTiel = mcDeviceBasicsInfo.getDeviceName() + Constants.S_FG + Constants.S_DEVICE_LOCATION_NO + Constants.S_FG + mcDeviceBasicsInfo.getId();
            }
            markerOption.title(allTiel);
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

    @Override
    public View getInfoContents(Marker marker) {
        // TODO Auto-generated method stub
        View infoWindow = this.getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        View infoWindow = this.getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        ImageView arrow_r_image = (ImageView) view.findViewById(R.id.arrow_r_image);
        arrow_r_image.setVisibility(View.GONE);
        String allStr[] = marker.getTitle().split(Constants.S_FG);
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        titleUi.setText(allStr.length > 1 ? allStr[0] : "");
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        snippetUi.setText(allStr.length > 2 ? allStr[1] : "");
    }

    @Override
    protected void onResume() {
        //MobclickAgent.onPageStart(UMengKey.time_machine_location);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //	MobclickAgent.onPageEnd(UMengKey.time_machine_location);
        super.onPause();
    }

    public void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                loc_now = new Location(amapLocation.getLatitude(), amapLocation.getLongitude(), "s");
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
               // Constants.MyLog("定位错误~~~~~~~~~~~~~~~~~~~");
            }
        }
    }
}
