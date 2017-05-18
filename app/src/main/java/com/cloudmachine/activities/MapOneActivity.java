package com.cloudmachine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMapLongClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource.OnLocationChangedListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CircleFenchDialog;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.AddCircleFenchAsync;
import com.cloudmachine.net.task.DeleteFenceAsync;
import com.cloudmachine.net.task.DevicesBasicsInfoAsync;
import com.cloudmachine.struc.FenceInfo;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.struc.McDeviceCircleFence;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.widgets.MapMarkerView;
import com.cloudmachine.utils.widgets.TitleView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapOneActivity extends BaseAutoLayoutActivity implements OnClickListener,
        OnMarkerClickListener, OnInfoWindowClickListener, OnMapLoadedListener,
        InfoWindowAdapter, CancelableCallback, OnMapClickListener, Callback,
        OnMapLongClickListener, OnMarkerDragListener {

    private static final int zoomMin = 3;
    private static final int zoomMax = 20;
    private static final int zoomDefault = 14;

    private static final int MapBoundsPadding = 0;
    private static final double MapBoundsLatLng = 0.0;

    private static final int lineColor = Color.parseColor("#0c82fb");
    private static final int polygonColor = Color.parseColor("#100c82fb");// Color.argb(50,
    // 1,
    // 1,
    // 1);

    private static final String NEWSET = "新增围栏";
    private static final String RESET = "重置围栏";
//    private static final String CHANGESET = "编辑围栏";
    private static final String FINISH = "完成围栏";

    private Context mContext;
    private Handler mHandler;

    private MapView mapView;
    private AMap aMap;

    private McDeviceBasicsInfo mcDeviceBasicsInfo;
    private TitleView title_layout;

    private OnLocationChangedListener mListener;
    private Marker mGPSMarker;
    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Marker myMarker;
    private List<LatLng> listPoint = new ArrayList<LatLng>();
    private List<Polyline> listPolyline = new ArrayList<Polyline>();
    private List<Marker> listPointMarker = new ArrayList<Marker>();
    private Circle circle;
    private Polyline polyline;
    private LatLng myLatLng;
    private Polygon polygon;
    private boolean isEnclosure;
    private MarkerOptions markerOption;
    private McDeviceLocation location;
    private boolean isAskDelete = true;
    private String radiu = "100";
    private McDeviceLocation fencingCenter;
    private long deviceId = -1;
    private String mLat;
    private String mLng;
    private int mWorkStatus;
    private CircleFenchDialog mCircleFenchDialog;
    private MarkerOptions mCircleMarkerOptions;
    private Marker mRadiuMarker;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_one);
        this.mContext = this;
        mHandler = new Handler(this);

        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        getIntentData();
        initView();
        initMap();
        Constants.MyLog("页面初始化围栏");
        initFencn();
        // init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void initPresenter() {

    }

    private void getIntentData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            try {
                mcDeviceBasicsInfo = (McDeviceBasicsInfo) bundle//拿到设备基本信息
                        .getSerializable(Constants.P_MCDEVICEBASICSINFO);
                if (null != mcDeviceBasicsInfo) {
                    if (null != mcDeviceBasicsInfo.getCircleFence()) {
                        radiu = mcDeviceBasicsInfo.getCircleFence().getRadiu();//范围半径
                        mLat = mcDeviceBasicsInfo.getCircleFence().getLat();
                        mLng = mcDeviceBasicsInfo.getCircleFence().getLng();
                    }
                    deviceId = mcDeviceBasicsInfo.getId();//设备id
                    mWorkStatus = mcDeviceBasicsInfo.getWorkStatus();//工作状态
                }
                new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        //MobclickAgent.onPageStart(UMengKey.time_machine_fence);
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        //MobclickAgent.onPageEnd(UMengKey.time_machine_fence);
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initView() {
        initTitleLayout();

    }

    private void initFencn() {            //重新初始化围栏的方法
        if(null == mcDeviceBasicsInfo)
            return;
        //是否有圆形的围栏
        McDeviceCircleFence circleFence = mcDeviceBasicsInfo.getCircleFence();
        if (null != circleFence && deviceId != -1) {
//            title_layout.setRightText(-1, CHANGESET);
            //重新初始化围栏
            finishDraw();

            //重新初始化围栏半径marker
            addCircleMarkerToMap();
            aMap.invalidate();
        } else {
            //是否有多边形的电子围栏
            List<FenceInfo> fenceInfo = mcDeviceBasicsInfo.getFence();
            if (null != fenceInfo) {
                if (null != mcDeviceBasicsInfo && mcDeviceBasicsInfo.getId() != 0) {//有数据,编辑围栏
//                    title_layout.setRightText(-1, CHANGESET);
                }
                int size = fenceInfo.size();
                for (int i = 0; i < size; i++) {
                    LatLng ll = new LatLng(fenceInfo.get(i).getY(), fenceInfo
                            .get(i).getX());//系统经纬度坐标点
                    listPoint.add(ll);//高德坐标点集合
                }
                if (null != listPoint && listPoint.size() > 0) {
                    // if(null != myMarker && myMarker.isInfoWindowShown()){
                    // myMarker.hideInfoWindow();
                    // }
                    finishDraw();
                    notifyMap();
                    aMap.invalidate();// 刷新地图
                }
            }
        }
    }

    private void initTitleLayout() {

        title_layout = (TitleView) findViewById(R.id.title_layout);
        title_layout.setTitle("电子围栏");

        title_layout.setLeftImage(-1, new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
//        if (null != mcDeviceBasicsInfo && mcDeviceBasicsInfo.getId() != 0) {
//            title_layout.setRightText(-1, NEWSET, new OnClickListener() {
//                @Override
//                public void onClick(View v) {                               //1111111111
//                    // TODO Auto-generated method stub
//
//                    // showEditDialog();
//
//                    if (null != myMarker && myMarker.isInfoWindowShown()) {
//                        myMarker.hideInfoWindow();
//                    }
//                    if (!isEnclosure) {
//                        showEditDialog();
//                        isEnclosure = true;
////                        title_layout.setRightText(-1, FINISH);
//                        jumpPoint(myMarker);
//
//                        if (null != listPoint && listPoint.size() > 0) {
//                            MobclickAgent.onEvent(mContext, UMengKey.count_fence_add);
//                            changePolygon(null);
//                        } else {
//
//                        }
//
//
//                    } else {
//                        MobclickAgent.onEvent(mContext, UMengKey.count_fence_edit);
//                        /*int size = listPoint.size();
//                        if (size >= 3 && size <= 10) {                                      //多边形围栏
//							String fence = "";
//							for (int i = 0; i < size; i++) {
//								if (i == size - 1) {
//									fence += listPoint.get(i).longitude + ","
//											+ listPoint.get(i).latitude;
//								} else {
//									fence += listPoint.get(i).longitude + ","
//											+ listPoint.get(i).latitude + ";";
//								}
//							}
//							new EnclosureUploadAsync(mContext, mHandler)
//									.execute(String.valueOf(mcDeviceBasicsInfo
//											.getId()), fence);
//						}*/
//                        showEditDialog();
//                    }
//                }
//            });
//            title_layout.setRightTextEdit(true);
//        }

    }

    private void initMap() {
        // 初始化传感器
        mSensorManager = (SensorManager) this
                .getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    //展示新增/编辑对话框
    public void showEditDialog() {
//        LayoutInflater inflater = LayoutInflater.from(MapOneActivity.this);
//        final View view = inflater.inflate(R.layout.circle_fench_dialog, null);
//        final EditText range = (EditText) view.findViewById(R.id.et_circle_fench);
        mCircleFenchDialog = new CircleFenchDialog(this);
        mCircleFenchDialog.dialog.show();//显示
        if (null != mcDeviceBasicsInfo.getCircleFence()) {
            mCircleFenchDialog.setEditText(mcDeviceBasicsInfo.getCircleFence().getRadiu());
        }
        //确定按钮
        mCircleFenchDialog.setMyDialogOnClick(new CircleFenchDialog.CircleFenchDialogOnClick() {
            @Override
            public void ok(View view) {
                EditText range = (EditText) view;
                if (TextUtils.isEmpty(range.getText().toString().trim())) {
                    Constants.MyToast("请正确选择围栏半径");
                } else {
                    //range.setText("100");
                    radiu = range.getText().toString().trim();//33
                    new AddCircleFenchAsync(mContext, mHandler, String.valueOf(location.getLat()),
                            String.valueOf(location.getLng()), mcDeviceBasicsInfo.getId(), radiu).execute();
                }
                mCircleFenchDialog.dialog.dismiss();//关闭
            }
        });
        //取消按钮
        mCircleFenchDialog.setMyDialogCancelOnClick(new CircleFenchDialog.CancelFenchDialogOnClick() {
            @Override
            public void cancel() {
                if (null != mcDeviceBasicsInfo.getCircleFence()) {
//                    title_layout.setRightText(-1, CHANGESET);
                    finishDraw();
                } else {
//                    title_layout.setRightText(-1,NEWSET);
                }

            }
        });
    }


    private void setUpMap() {
        mGPSMarker = aMap.addMarker(new MarkerOptions().icon(

                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),
                                R.drawable.location_marker))).anchor(
                (float) 0.5, (float) 0.5));

        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
        aMap.setOnMapLongClickListener(this);
        aMap.setOnMarkerDragListener(this);
        // aMap.setLocationSource(this);// 设置定位监听
        // aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // aMap.setMyLocationEnabled(true);//
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        addMarkerToMap();// 往地图上添加marker
    }

    private void addCircleMarkerToMap() {
        if (null != mRadiuMarker && mRadiuMarker.getObject().equals("radiuMarker")) {
            mRadiuMarker.remove();
        }

        //添加显示半径图层
        if (null != mcDeviceBasicsInfo.getCircleFence() && null != mcDeviceBasicsInfo) {
            //location = mcDeviceBasicsInfo.getLocation();
            LatLng circlrLatlng = getLatlng(Float.parseFloat(radiu) / 1000 / 2,
                    new LatLng(Double.parseDouble(mcDeviceBasicsInfo.getCircleFence().getLat()),
                            Double.parseDouble(mcDeviceBasicsInfo.getCircleFence().getLng())), 180);
            mCircleMarkerOptions = new MarkerOptions();
            String title = "半径:" + mcDeviceBasicsInfo.getCircleFence().getRadiu() + "米";
            mCircleMarkerOptions.position(circlrLatlng);
            mCircleMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMyBitmap(title)));
            mRadiuMarker = aMap.addMarker(mCircleMarkerOptions);
            mRadiuMarker.setObject("radiuMarker");
            mRadiuMarker.showInfoWindow();
        }
    }

    private void addMarkerToMap() {

        if (null != mcDeviceBasicsInfo) {

            location = mcDeviceBasicsInfo.getLocation();
            Constants.MyLog("获得经纬度" + location.getLng());

            markerOption = new MarkerOptions();
            String allTiel = "";
            if (location != null) {
                myLatLng = new LatLng(location.getLat(), location.getLng());
                markerOption.position(myLatLng);
                allTiel = mcDeviceBasicsInfo.getDeviceName() + Constants.S_FG
                        + location.getPosition() + Constants.S_FG
                        + mcDeviceBasicsInfo.getId();
            } else {
                allTiel = mcDeviceBasicsInfo.getDeviceName() + Constants.S_FG
                        + Constants.S_DEVICE_LOCATION_NO + Constants.S_FG
                        + mcDeviceBasicsInfo.getId();
            }
            markerOption.title(allTiel);
            String[] imageUrl = mcDeviceBasicsInfo.getDevicePhoto();
            if (null != imageUrl && imageUrl.length > 0) {
                markerOption.snippet(imageUrl[0]);
            }
            if (mWorkStatus == 0) {
                markerOption.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mc_workstatus_0));
            } else {
                markerOption.icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.mc_workstatus_1));
            }

            /*markerOption.icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.mc_map_point_my));*/


            /*Constants.MyLog(markerOption.getPosition().latitude+"diliwei位置是 ");*/

            myMarker = aMap.addMarker(markerOption);
            myMarker.showInfoWindow();//展示说明弹框
            if (null != myMarker.getPosition())
                aMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition(myMarker
                                .getPosition(), zoomDefault, 0, 30)));// 18
            scaleMap();

        }

    }

    protected Bitmap getMyBitmap(String pm_val) {
       /* Bitmap bitmap = BitmapDescriptorFactory.fromResource(
                R.drawable.none).getBitmap();*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.trans2).copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40f);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(getResources().getColor(R.color.black));
        canvas.drawText(pm_val, 25, 35, textPaint);// 设置bitmap上面的文字位置
        return bitmap;
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

    @Override
    public void onMapLoaded() {
        // TODO Auto-generated method stub

        // aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new
        // CameraPosition(
        // CHENGDU, 18, 0, 30)));

        // aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new
        // CameraPosition(
        // CHENGDU, 18, 0, 30)), 6000, null);
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub
        // Intent intent = new Intent(this,DeviceMcActivity.class);
        // intent.putExtra("deviceId", 0);
        // startActivity(intent);
        // finish();
        if (null != myMarker && myMarker.isInfoWindowShown()) {
            myMarker.hideInfoWindow();
        }
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.badge);
        ImageView arrow_r_image = (ImageView) view
                .findViewById(R.id.arrow_r_image);
        arrow_r_image.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage(marker.getSnippet(), imageView);
        imageView.setImageResource(R.drawable.mc_default_icon);
        String allStr[] = marker.getTitle().split(Constants.S_FG);
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        titleUi.setText(allStr.length > 1 ? allStr[0] : "");
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        snippetUi.setText(allStr.length > 2 ? allStr[1] : "");
    }

    @Override
    public void onCancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish() {
        // TODO Auto-generated method stub

    }

	/*
     * @Override public void onLocationChanged(Location location) { // TODO
	 * Auto-generated method stub if (mListener != null && location != null) {
	 * //mListener.onLocationChanged(aLocation);// 显示系统小蓝点
	 * mGPSMarker.setPosition(new LatLng(
	 * location.getLatitude(),location.getLongitude())); } }
	 * 
	 * @Override public void onStatusChanged(String provider, int status, Bundle
	 * extras) { // TODO Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderEnabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * @Override public void onProviderDisabled(String provider) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

    public static String convertToTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return df.format(date);
    }

    @Override
    public void onMapClick(LatLng point) {
        // TODO Auto-generated method stub
        // Constants.MyToast("LAT:"+point.latitude + "LONG:"+point.longitude);
        /*
		 * options.add(point); options.fillColor(Color.LTGRAY);
		 * options.strokeColor(Color.RED); options.strokeWidth(1);
		 * options.addAll(listPoint); aMap.addPolygon(options);
		 */

        if (null != myMarker && myMarker.isInfoWindowShown()) {
            myMarker.hideInfoWindow();
        }
        /*if (isEnclosure) {                                            //多边形地图
            int size = listPoint.size();
            if (size > 0) {
                // 画红线
                listPolyline.add(aMap.addPolyline((new PolylineOptions())
                        .add(listPoint.get(listPoint.size() - 1), point)
                        .color(lineColor)
                        .width(getResources().getDimension(
                                R.dimen.common_line_width))));
                // 画虚线
                if (size > 1) {
                    if (null != polyline) {
                        polyline.remove();
                    }
                    polyline = aMap.addPolyline((new PolylineOptions())
                            .add(listPoint.get(0), point)
                            .color(lineColor)
                            .width(getResources().getDimension(
                                    R.dimen.common_line_width)));
                    polyline.setDottedLine(true);
                }

            }
            // 画点
            MarkerOptions mOption = new MarkerOptions();
            mOption.position(point);
            // mOption.icon(BitmapDescriptorFactory
            // .fromResource(R.drawable.mc_map_point_lease));
            mOption.icon(BitmapDescriptorFactory.fromView(new MapMarkerView(
                    this)));
            mOption.draggable(true);
            listPointMarker.add(aMap.addMarker(mOption));
            // circle = aMap.addCircle(new CircleOptions().center(point)
            // .radius(100).strokeColor(Color.RED)
            // .fillColor(Color.RED).strokeWidth(1));
            // 点加入队列
            listPoint.add(point);
            // 画区域
            drawPolygon();
        }*/

        // updateLine();
    }


    private void changePolygon(Marker marker) {
        if (null != listPointMarker) {
            int size = listPointMarker.size();
            // if(size<1)
            // return;
            int num = -1;
            if (null != marker) {
                for (int i = 0; i < size; i++) {
                    if (marker.equals(listPointMarker.get(i))) {
                        num = i;
                        break;
                    }
                }
                if (num >= 0)
                    listPoint.set(num, marker.getPosition());
            }

            // 画红线
            if (null != listPolyline) {
                for (int i = 0; i < listPolyline.size(); i++) {
                    listPolyline.get(i).remove();
                }
                listPolyline.clear();
                for (int j = 0; j < listPoint.size() - 1; j++) {
                    listPolyline.add(aMap.addPolyline((new PolylineOptions())
                            .add(listPoint.get(j), listPoint.get(j + 1))
                            .color(lineColor)
                            .width(getResources().getDimension(
                                    R.dimen.common_line_width))));
                }
            }
            // 画虚线
            // if(size >1){
            if (null != polyline) {
                polyline.remove();
            }
            polyline = aMap.addPolyline((new PolylineOptions())
                    .add(listPoint.get(0), listPoint.get(listPoint.size() - 1))
                    .color(lineColor)
                    .width(getResources().getDimension(
                            R.dimen.common_line_width)));
            polyline.setDottedLine(true);
            // }
            // 画点 //
            if (null == marker && size <= 0) {
                int len = listPoint.size();
                for (int i = 0; i < len; i++) {
                    MarkerOptions mOption = new MarkerOptions();
                    mOption.position(listPoint.get(i));
                    mOption.icon(BitmapDescriptorFactory
                            .fromView(new MapMarkerView(this)));
                    mOption.draggable(true);
                    listPointMarker.add(aMap.addMarker(mOption));
                    //
                    // aMap.addCircle(new
                    // CircleOptions().center(listPoint.get(i))
                    // .radius(100).strokeColor(Color.RED)
                    // .fillColor(Color.RED).strokeWidth(1));

                }
            }

            // 画区域
            drawPolygon();
        }
    }

    private void drawPolygon() {
        if (null != polygon) {
            polygon.remove();
        }
        PolygonOptions options = new PolygonOptions();
        options.fillColor(polygonColor);
        options.strokeColor(lineColor);
        options.strokeWidth(1);
        options.addAll(listPoint);
        polygon = aMap.addPolygon(options);
    }

    private void finishDraw() {

        if (null != radiu) {
            if (null != circle) {
                circle.remove();
            }
            //判断围栏中心点不为空
            if (!TextUtils.isEmpty(mLat) && !TextUtils.isEmpty(mLng)) {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng)));
                circleOptions.radius(Double.parseDouble(radiu));
                circleOptions.strokeWidth(getResources().getDimension(
                        R.dimen.common_line_width));
                circleOptions.strokeColor(lineColor);
                circleOptions.fillColor(polygonColor);
                circle = aMap.addCircle(circleOptions);
                scaleMap();     //缩放地图
            } else {
                //第一次设置围栏,以机器的地理位置信息为准
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(location.getLat(), location.getLng()));
                circleOptions.radius(Double.parseDouble(radiu));
                circleOptions.strokeWidth(getResources().getDimension(
                        R.dimen.common_line_width));
                circleOptions.strokeColor(lineColor);
                circleOptions.fillColor(polygonColor);
                circle = aMap.addCircle(circleOptions);
                scaleMap();     //缩放地图
            }

        } else {
            if (null != listPoint && listPoint.size() > 1) {
                if (null != polyline) {//高德边线
                    polyline.remove();
                    listPolyline.add(aMap.addPolyline((new PolylineOptions())
                            .add(listPoint.get(listPoint.size() - 1),
                                    listPoint.get(0))
                            .color(lineColor)
                            .width(getResources().getDimension(
                                    R.dimen.common_line_width))));
                }
                if (null != circle) {
                    circle.remove();
                }
                if (null != polygon) {
                    polygon.remove();
                }
                PolygonOptions options = new PolygonOptions();
                options.fillColor(polygonColor);
                options.strokeColor(lineColor);
                options.strokeWidth(getResources().getDimension(
                        R.dimen.common_line_width));
                options.addAll(listPoint);
                polygon = aMap.addPolygon(options);
            }
        }
    }

    private void updateLine() {
        Builder llb = new Builder();
        int size = listPoint.size();
        for (int i = 0; i < size; i++) {
            llb.include(listPoint.get(i));
        }
        LatLngBounds latLngBounds = llb.build();
        boolean isContain = latLngBounds.contains(myLatLng);
        Constants.MyToast("isContain: " + isContain);
    }

    @Override
    public boolean handleMessage(Message msg) {                         //222222
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_ADDFENCE_SUCCESS:
                Constants.isChangeDevice = true;
                finishDraw();
                isEnclosure = false;
//                title_layout.setRightText(-1, CHANGESET);
                break;
            case Constants.HANDLER_ADDFENCE_FAIL:
                Constants.MyToast((String) msg.obj);
                finishDraw();
                isEnclosure = false;
//                title_layout.setRightText(-1, CHANGESET);
                break;
            case Constants.HANDLER_DELETEFENCE_SUCCESS:
                //Constants.MyLog("删除了0");
                Constants.isChangeDevice = true;
                clearFence();
                //删除围栏后响应的marker也要删除
                if (null != mRadiuMarker && mRadiuMarker.getObject().equals("radiuMarker")) {
                    mRadiuMarker.remove();
                }
                aMap.invalidate();
                isEnclosure = false;
//                title_layout.setRightText(-1, NEWSET);
                new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
                break;
            case Constants.HANDLER_DELETEFENCE_FAIL:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_DELETEFENCE_ASK:
                if (isAskDelete) {
                    LatLng potint = (LatLng) msg.obj;

                    if (null != mcDeviceBasicsInfo.getCircleFence()) {
                        float dist = Float.parseFloat(radiu) / 1000; //km
                        float dealDist = (float) (dist / Math.sin(45 * Math.PI / 180)); //km
                        LatLng center = aMap.getCameraPosition().target;
                        LatLng northeast = getLatlng(dealDist, center, 45);
                        LatLng southwest = getLatlng(dealDist, center, 225);
                        LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);
                        boolean isContain = latLngBounds.contains(potint);
                        if (isContain) {
                            showPickDialog();
                        } else {
                        }
                    } else {
                        if (null != listPoint && listPoint.size() > 0) {
                            Builder bd = new Builder();
                            for (int i = 0; i < listPoint.size(); i++) {
                                bd.include(listPoint.get(i));
                            }
                            LatLngBounds bounds = bd.build();
                            boolean isContain = bounds.contains(potint);// 判断区域内
                            if (isContain) {
                                showPickDialog();
                            } else {
                            }
                        }
                    }
                }
                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_SUCCESS:     //获取围栏信息成功
                mcDeviceBasicsInfo = (McDeviceBasicsInfo) msg.obj;
                if (null == mcDeviceBasicsInfo) {
                    Constants.MyLog("获取围栏信息成功后拿到信息为空");
                }
                if (null != mcDeviceBasicsInfo) {
                    if (null != mcDeviceBasicsInfo.getCircleFence()) {
                        radiu = mcDeviceBasicsInfo.getCircleFence().getRadiu();//范围半径
                        mLat = mcDeviceBasicsInfo.getCircleFence().getLat();
                        mLng = mcDeviceBasicsInfo.getCircleFence().getLng();
                        listPoint.clear();
                    }
                    deviceId = mcDeviceBasicsInfo.getId();//设备id
                    mWorkStatus = mcDeviceBasicsInfo.getWorkStatus();//工作状态
                    initFencn();
                }
                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_FAIL:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_ADDCIRCLEFENCH_SUCCESS:          //新增圆形围栏成功
                //Constants.MyLog("成功修改围栏");
                //修改围栏后再次请求组获取最新围栏的信息
                new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
                Constants.isChangeDevice = true;
                finishDraw();
                isEnclosure = false;
//                title_layout.setRightText(-1, CHANGESET);
                break;
            case Constants.HANDLER_ADDCIRCLEFENCH_FAILD:            //新增圆形围栏失败
                Constants.MyToast((String) msg.obj);
                finishDraw();
                isEnclosure = false;
//                title_layout.setRightText(-1, CHANGESET);
                break;

        }
        return false;
    }

    private void notifyMap() {
        Builder bu = new Builder();
        boolean isHave = false;
        double latMax = 0, latMin = 0, lngMax = 0, lngMin = 0;
        double lat = -1, lng = -1;
        if (listPoint != null) {
            int len = listPoint.size();
            for (int i = 0; i < len; i++) {
                LatLng location = listPoint.get(i);
                if (location != null) {
                    isHave = true;
                    lat = location.latitude;
                    lng = location.longitude;
                    if (latMax == 0) {
                        latMax = latMin = lat;
                        lngMax = lngMin = lng;
                    }
                    latMax = latMax > lat ? latMax : lat;
                    latMin = latMin < lat ? latMin : lat;
                    lngMax = lngMax > lng ? lngMax : lng;
                    lngMin = lngMin < lng ? lngMin : lng;
                    bu.include(new LatLng(lat, lng));
                }
            }
            if (len > 0 && isHave) {
                bu.include(new LatLng(latMax + MapBoundsLatLng, lngMin
                        - MapBoundsLatLng));
                bu.include(new LatLng(latMin - MapBoundsLatLng, lngMax
                        + MapBoundsLatLng));
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bu.build(),
                        MapBoundsPadding));
            }
        }
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        String str = mcDeviceBasicsInfo.getDeviceName();
        if (null != listPoint && listPoint.size() > 0) {
            str = "长按围栏边界点拖动可改变范围";
        }
        String allTitle = "";
        if (location != null) {
            allTitle = str + Constants.S_FG + location.getPosition()
                    + Constants.S_FG + mcDeviceBasicsInfo.getId();
        } else {
            allTitle = str + Constants.S_FG + Constants.S_DEVICE_LOCATION_NO
                    + Constants.S_FG + mcDeviceBasicsInfo.getId();
        }
        myMarker.setTitle(allTitle);
        myMarker.showInfoWindow();
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(myLatLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * myLatLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * myLatLng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                aMap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

    }

    @Override
    public void onMapLongClick(LatLng arg0) {
        // TODO Auto-generated method stub
        Constants.MyLog("LongClick");
        Message msg = Message.obtain();
        msg.what = Constants.HANDLER_DELETEFENCE_ASK;
        msg.obj = arg0;
        mHandler.sendMessageDelayed(msg, 100);
		/*
		 * if(null !=listPoint && listPoint.size()>0){ Builder bd = new
		 * LatLngBounds.Builder(); for(int i=0; i<listPoint.size(); i++){
		 * bd.include(listPoint.get(i)); } LatLngBounds bounds = bd.build();
		 * boolean isContain = bounds.contains(arg0);// 判断区域内 if(isContain){
		 * showPickDialog(); }else{ } }
		 */
    }

    private void showPickDialog() {
        MobclickAgent.onEvent(mContext, UMengKey.count_fence_delete);
        CustomDialog.Builder builder = new CustomDialog.Builder(MapOneActivity.this);
        builder.setLeftButtonColor(getResources().getColor(R.color.black));
        builder.setRightButtonColor(getResources().getColor(R.color.dialog_changeowner_color));
        builder.setMessage("删除围栏就无法收到位置报警哦", "");
        builder.setTitle("提示");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new DeleteFenceAsync(mContext, mHandler)
                        .execute(String.valueOf(mcDeviceBasicsInfo
                                .getId()));
                circle.remove();
                aMap.invalidate();
                dialog.dismiss();
            }
        });
        builder.create().show();


    }

    private void clearFence() {
        if (null != polygon) {
            polygon.remove();
            aMap.invalidate();// 刷新地图
        }
        if (null != listPoint && listPoint.size() > 0) {
            listPoint.clear();
            aMap.invalidate();// 刷新地图
        }
        if (null != polyline) {
            polyline.remove();
        }
        if (null != listPointMarker) {
            for (int i = 0; i < listPointMarker.size(); i++) {
                listPointMarker.get(i).remove();
            }
            listPointMarker.clear();
        }
        if (null != listPolyline & listPolyline.size() > 0) {
            int size = listPolyline.size();
            for (int i = 0; i < size; i++) {
                listPolyline.get(i).remove();
            }
            listPolyline.clear();
        }
    }

    private void drawFence() {

    }

    @Override
    public void onMarkerDrag(Marker arg0) {
        // TODO Auto-generated method stub
        changePolygon(arg0);
    }

    @Override
    public void onMarkerDragEnd(Marker arg0) {
        // TODO Auto-generated method stub
        isAskDelete = true;
    }

    @Override
    public void onMarkerDragStart(Marker arg0) {
        // TODO Auto-generated method stub
        isAskDelete = false;
        if (null != myMarker && myMarker.isInfoWindowShown()) {
            myMarker.hideInfoWindow();
        }
    }


    //根据一个点德尔经纬度,两点间的距离,以及角度确定另一点的经纬度
    public LatLng getLatlng(float distance, LatLng latlngA, double angle) {
        return new LatLng(latlngA.latitude + (distance * Math.cos(angle * Math.PI / 180)) / 111,
                latlngA.longitude + (distance * Math.sin(angle * Math.PI / 180)) / (111 * Math.cos(latlngA.latitude * Math.PI / 180))
        );
    }

    //根据中心点和围栏的半径来缩放地图
    public void scaleMap() {
        if (!TextUtils.isEmpty(mLat) && !TextUtils.isEmpty(mLng)) {
            float dist = Float.parseFloat(radiu) / 1000; //km
            float dealDist = (float) (dist / Math.sin(45 * Math.PI / 180)); //km
            LatLng center = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLng));
            LatLng northeast = getLatlng(dealDist, center, 45);
            LatLng southwest = getLatlng(dealDist, center, 225);
            LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
        } else {
            //第一次新增,以
            float dist = Float.parseFloat(radiu) / 1000; //km
            float dealDist = (float) (dist / Math.sin(45 * Math.PI / 180)); //km
            LatLng center = new LatLng(location.getLat(), location.getLng());
            LatLng northeast = getLatlng(dealDist, center, 45);
            LatLng southwest = getLatlng(dealDist, center, 225);
            LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 10));
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MapOne Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
}
