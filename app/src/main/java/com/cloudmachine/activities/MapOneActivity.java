package com.cloudmachine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CircleFenchDialog;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.bean.McDeviceCircleFence;
import com.cloudmachine.bean.McDeviceLocation;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.MobEvent;
import com.cloudmachine.net.task.AddCircleFenchAsync;
import com.cloudmachine.net.task.DeleteFenceAsync;
import com.cloudmachine.net.task.DevicesBasicsInfoAsync;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.DensityUtil;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.widgets.MapMarkerView;
import com.cloudmachine.widget.CommonTitleView;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapOneActivity extends BaseAutoLayoutActivity implements
        AMap.OnMarkerClickListener,
        AMap.InfoWindowAdapter, AMap.CancelableCallback, Callback, AMap.OnMarkerDragListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String MAP_DRAG = "拖动地图";
    private static final String SET_RADUIS_BY_CLICK_THIS = "点我设置半径";
    private static final int zoomDefault = 15;
    private static final int lineColor = Color.parseColor("#fbb233");
    private static final int polygonColor = Color.parseColor("#15fbb233");// Color.argb(50,
    private static final String NEWSET = "添加";
    private static final String DELETE = "删除";

    private Handler mHandler;

    private MapView mapView;
    private AMap aMap;


    private McDeviceBasicsInfo mDeviceInfo;
    private CommonTitleView titleView;

    private LocationSource.OnLocationChangedListener mListener;

    private Marker myMarker;
    private List<LatLng> listPoint = new ArrayList<LatLng>();
    private List<Polyline> listPolyline = new ArrayList<Polyline>();
    private List<Marker> listPointMarker = new ArrayList<Marker>();
    private Circle circle;
    private Polyline polyline;
    private Polygon polygon;
    private boolean isEnclosure;
    private McDeviceLocation location;
    private boolean isAskDelete = true;
    private String radiu = "100";
    private long deviceId = -1;
    private int deviceType;
    private String mLat;
    private String mLng;
    private int mWorkStatus;
    private CircleFenchDialog mCircleFenchDialog;
    String deviceName;
    Marker locRaiduMark;
    FrameLayout locAddressLayout;
    TextView locAddressTv;
    GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_one);
        initView(savedInstanceState);
    }

    @Override
    public void initPresenter() {

    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        //MobclickAgent.onPageStart(UMengKey.time_machine_fence);
        super.onResume();
        MobclickAgent.onEvent(this, MobEvent.TIME_MACHINE_FENCE);
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
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    private void initFencn() {            //重新初始化围栏的方法
        if (mDeviceInfo != null) {
            //是否有圆形的围栏
            McDeviceCircleFence circleFence = mDeviceInfo.getCircleFence();
            if (circleFence != null) {
                if (!Constants.isNoEditInMcMember(deviceId, deviceType)) {
                    titleView.setRightText(DELETE);
                }
                //重新初始化围栏
                finishDraw();
                //重新初始化围栏半径marker
                addCircleMarkerToMap();
                aMap.reloadMap();
            }

        }
    }

    private void initView(Bundle savedInstanceState) {
        initParams();
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        titleView = (CommonTitleView) findViewById(R.id.title_layout);
        locAddressLayout = (FrameLayout) findViewById(R.id.mapone_loc_layout);
        locAddressTv = (TextView) findViewById(R.id.mapone_cur_location);
        if (!Constants.isNoEditInMcMember(deviceId, deviceType)) {
            titleView.setRightText(NEWSET, rightClickListener);
        }
        initMap();
        if (deviceId != -1) {
            new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
        }
    }

    OnClickListener rightClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String rightTitle = titleView.getRightText();
            if (DELETE.equals(rightTitle)) {
                MobclickAgent.onEvent(MapOneActivity.this, MobEvent.COUNT_FENCE_DELETE);
                deleteFence();
            } else if (NEWSET.equals(rightTitle)) {
                MobclickAgent.onEvent(MapOneActivity.this, MobEvent.COUNT_FENCE_ADD);
                if (myMarker != null && !myMarker.isRemoved()) {
                    myMarker.remove();
                    locRaiduMark = aMap.addMarker(getMarkerLocOptions(MapOneActivity.this, myMarker.getPosition(), MAP_DRAG));
                    titleView.setRightText(null, null);
                } else {
                    ToastUtils.showCenterToast(MapOneActivity.this, "无法获取机器位置");
                }
            }

        }
    };

    private void initParams() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDeviceInfo = (McDeviceBasicsInfo) bundle//拿到设备基本信息
                    .getSerializable(Constants.P_MCDEVICEBASICSINFO);
            if (mDeviceInfo != null) {
                deviceId = mDeviceInfo.getId();//设备id
                deviceType = mDeviceInfo.getType();
                mWorkStatus = mDeviceInfo.getWorkStatus();//工作状态
                McDeviceCircleFence fence = mDeviceInfo.getCircleFence();
                if (fence != null) {
                    radiu = fence.getRadiu();//范围半径
                    mLat = fence.getLat();
                    mLng = fence.getLng();
                }
            }
        }
        mHandler = new Handler(this);
    }

    protected MarkerOptions getMarkerLocOptions(Context context, LatLng latLng, String title) {
        AppLog.print("locRaiduTv marker __getMarkerLocOptions___");
        MarkerOptions options = new MarkerOptions();
        TextView locRaiduTv = new TextView(context);
        locRaiduTv.setTextColor(getResources().getColor(R.color.cor15));
        locRaiduTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) getResources().getDimension(R.dimen.siz6));
        locRaiduTv.setBackground(getResources().getDrawable(R.drawable.bg_loc_text));
        locRaiduTv.setGravity(Gravity.CENTER);
        locRaiduTv.setMaxEms(12);
        int left = DensityUtil.dip2px(this, 15);
        locRaiduTv.setPadding(left, 0, left, DensityUtil.dip2px(this, 19));
        locRaiduTv.setEllipsize(TextUtils.TruncateAt.END);
        locRaiduTv.setSingleLine();

        locRaiduTv.setText(title);
        options.icon(BitmapDescriptorFactory.fromView(locRaiduTv));
        options.title(title);
        options.position(latLng);
        return options;
    }

    private void editFence() {
//        if (null != myMarker && myMarker.isInfoWindowShown()) {
//            myMarker.hideInfoWindow();
//        }
        MobclickAgent.onEvent(this, MobEvent.COUNT_FENCE_EDIT);
        if (!isEnclosure) {
            showEditDialog();
            isEnclosure = true;
//            jumpPoint(myMarker);
            if (null != listPoint && listPoint.size() > 0) {
                MobclickAgent.onEvent(MapOneActivity.this, UMengKey.count_fence_add);
                changePolygon(null);
            }

        } else {
            MobclickAgent.onEvent(MapOneActivity.this, UMengKey.count_fence_edit);
            showEditDialog();
        }
    }

    private void initMap() {
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(false);
            aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
            aMap.setOnMarkerDragListener(this);
            aMap.setOnCameraChangeListener(this);
            addMarkerToMap();// 往地图上添加marker
        }
        initFencn();
    }

    //展示新增/编辑对话框
    public void showEditDialog() {
//        LayoutInflater inflater = LayoutInflater.from(MapOneActivity.this);
//        final View view = inflater.inflate(R.layout.circle_fench_dialog, null);
//        final EditText range = (EditText) view.findViewById(R.id.et_circle_fench);
        mCircleFenchDialog = new CircleFenchDialog(this);
        mCircleFenchDialog.dialog.show();//显示
        if (null != mDeviceInfo.getCircleFence()) {
            mCircleFenchDialog.setEditText(mDeviceInfo.getCircleFence().getRadiu());
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
                    String latStr = mLat;
                    String lngStr = mLng;
                    if (locRaiduMark != null && locRaiduMark.isVisible()) {
                        LatLng pos = locRaiduMark.getPosition();
                        latStr = String.valueOf(pos.latitude);
                        lngStr = String.valueOf(pos.longitude);
                    }
                    MobclickAgent.onEvent(MapOneActivity.this, MobEvent.COUNT_FENCE_CONFIRM);
                    new AddCircleFenchAsync(MapOneActivity.this, mHandler, latStr,
                            lngStr, mDeviceInfo.getId(), radiu).execute();
                }
                mCircleFenchDialog.dialog.dismiss();//关闭
            }
        });
        //取消按钮
        mCircleFenchDialog.setMyDialogCancelOnClick(new CircleFenchDialog.CancelFenchDialogOnClick() {
            @Override
            public void cancel() {
                if (locRaiduMark != null && locRaiduMark.isVisible()) {
                    return;
                }
                if (null != mDeviceInfo.getCircleFence()) {
                    titleView.setRightText(DELETE);
                    finishDraw();
                } else {
                    titleView.setRightText(NEWSET);
                }

            }
        });
    }


    private void addCircleMarkerToMap() {
        //添加显示半径图层
        if (null != mDeviceInfo.getCircleFence() && null != mDeviceInfo) {
            //location = mcDeviceBasicsInfo.getLocation();
//            LatLng circlrLatlng = getLatlng(Float.parseFloat(radiu) / 4000 / 2,
//                    new LatLng(Double.parseDouble(mcDeviceBasicsInfo.getCircleFence().getLat()),
//                            Double.parseDouble(mcDeviceBasicsInfo.getCircleFence().getLng())), 0);
//            mCircleMarkerOptions = new MarkerOptions();
            rauduesTitle = "半径" + mDeviceInfo.getCircleFence().getRadiu() + "米";
//            mCircleMarkerOptions.position(circlrLatlng);
//            mCircleMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMyBitmap(title)));
//            mCircleMarkerOptions.icon(BitmapDescriptorFactory.fromView(getMyView()));
//            mRadiuMarker = aMap.addMarker(mCircleMarkerOptions);
//            mRadiuMarker.setObject("radiuMarker");
//            mRadiuMarker.showInfoWindow();

        }
    }

    String rauduesTitle;

    private View getMyView() {
        String title;
        String rightText = titleView.getRightText();
        if (NEWSET.equals(rightText) || TextUtils.isEmpty(rightText)) {
            if (TextUtils.isEmpty(rightText) && !TextUtils.isEmpty(rauduesTitle)) {
                title = rauduesTitle;
            } else {
                title = deviceName;
            }
        } else {
            title = rauduesTitle;
        }
        if (markerTtileTv == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = DensityUtil.dip2px(this, 7);
            markerTtileTv = new TextView(this);
            markerTtileTv.setGravity(Gravity.CENTER);
            markerTtileTv.setBackground(getResources().getDrawable(R.drawable.bg_marker_win));
            markerTtileTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            int left = DensityUtil.dip2px(this, 12);
            markerTtileTv.setPadding(left, 0, left, DensityUtil.dip2px(this, 7));
            markerTtileTv.setTextColor(getResources().getColor(R.color.black));
            markerTtileTv.setLayoutParams(params);
            markerTtileTv.setMaxEms(12);
            markerTtileTv.setEllipsize(TextUtils.TruncateAt.END);
            markerTtileTv.setSingleLine();
            markerTtileTv.requestFocus();
            markerTtileTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DELETE.equals(titleView.getRightText())) {
                        editFence();
                    }
                }
            });
        }
        markerTtileTv.setText(title);
        return markerTtileTv;
    }

    private void addMarkerToMap() {
        if (null != mDeviceInfo) {
            myMarker = aMap.addMarker(getDeviceMarkerOptions());
            myMarker.showInfoWindow();
            aMap.moveCamera(CameraUpdateFactory
                    .changeLatLng(myMarker
                            .getPosition()));// 18
            scaleMap();
            aMap.moveCamera(CameraUpdateFactory.zoomTo(zoomDefault));
        }
    }

    @NonNull
    private MarkerOptions getDeviceMarkerOptions() {
        MarkerOptions markerOption = new MarkerOptions();
        location = mDeviceInfo.getLocation();
        double lat = 0;
        double lng = 0;
        if (location != null) {
            lat = location.getLat();
            lng = location.getLng();
        }
        LatLng myLatLng = new LatLng(lat, lng);
        markerOption.position(myLatLng);
        deviceName = mDeviceInfo.getDeviceName();
        markerOption.icon(BitmapDescriptorFactory
                .fromView(getDeviceMarkerView()));
        return markerOption;
    }

    @NonNull
    private ImageView getDeviceMarkerView() {
        ImageView img = new ImageView(this);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int resId;
        switch (mWorkStatus) {
            case 1:
                resId = R.drawable.icon_machine_work;
                break;
            case 2:
                resId = R.drawable.icon_machine_online;
                break;
            default:
                resId = R.drawable.icon_machine_unwork;
                break;
        }
        img.setImageResource(resId);
        return img;
    }

    protected MarkerOptions getMarkerOptions(Context context, LatLng latLng, int resid, String title) {
        MarkerOptions options = new MarkerOptions();
        ImageView img = new ImageView(context);
//        img.setLayoutParams(new ViewGroup.LayoutParams(DensityUtil.dip2px(context, Constants.MACHINE_ICON_WIDTH),DensityUtil.dip2px(context,Constants.MACHINE_ICON_HEIGHT)));
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        img.setImageResource(resid);
        options.icon(BitmapDescriptorFactory.fromView(img));
        options.title(title);
        options.position(latLng);
        return options;
    }

    protected Bitmap getMyBitmap(String pm_val) {
       /* Bitmap bitmap = BitmapDescriptorFactory.fromResource(
                R.drawable.none).getBitmap();*/
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_marker_win).copy(Bitmap.Config.ARGB_8888, true);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setTextSize(40f);
        textPaint.setColor(getResources().getColor(R.color.black));
        canvas.drawText(pm_val, 25, 70, textPaint);// 设置bitmap上面的文字位置
        return bitmap;
    }

    @Override
    public View getInfoContents(Marker marker) {
        // TODO Auto-generated method stub
//        View infoWindow = this.getLayoutInflater().inflate(
//                R.layout.custom_info_contents, null);
//
//        render(marker, infoWindow);
        return getInfoWindow(marker);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        AppLog.print("getInfoWindow___isRemoved_" + myMarker.isRemoved());
        if (myMarker != null && myMarker.isVisible()) {
            AppLog.print("myMark_window___show");
            return getMyView();
        }
        return null;
    }


    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        if (SET_RADUIS_BY_CLICK_THIS.equals(arg0.getTitle())) {
            editFence();
        }
        return false;
    }


    TextView markerTtileTv;

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

    @Override
    public boolean handleMessage(Message msg) {                         //222222
        // TODO Auto-generated method stub
        switch (msg.what) {
            case Constants.HANDLER_ADDFENCE_SUCCESS:
                Constants.isChangeDevice = true;
                finishDraw();
                isEnclosure = false;
                titleView.setRightText(DELETE);

                break;
            case Constants.HANDLER_ADDFENCE_FAIL:
                Constants.MyToast((String) msg.obj);
                finishDraw();
                isEnclosure = false;
                titleView.setRightText(DELETE);
                break;
            case Constants.HANDLER_DELETEFENCE_SUCCESS:
                circle.remove();
                //Constants.MyLog("删除了0");
                Constants.isChangeDevice = true;
                clearFence();
                aMap.reloadMap();
                if (myMarker != null) {
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(myMarker.getPosition()));
                }
                isEnclosure = false;
                titleView.setRightText(NEWSET);
                new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
                break;
            case Constants.HANDLER_DELETEFENCE_FAIL:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_SUCCESS:     //获取围栏信息成功
                mDeviceInfo = (McDeviceBasicsInfo) msg.obj;
                if (null != mDeviceInfo) {
                    if (null != mDeviceInfo.getCircleFence()) {
                        radiu = mDeviceInfo.getCircleFence().getRadiu();//范围半径
                        mLat = mDeviceInfo.getCircleFence().getLat();
                        mLng = mDeviceInfo.getCircleFence().getLng();
                        listPoint.clear();
                    }
                    deviceId = mDeviceInfo.getId();//设备id
                    mWorkStatus = mDeviceInfo.getWorkStatus();//工作状态
                    initFencn();
                }
                if (markerTtileTv != null) {
                    if (NEWSET.equals(titleView.getRightText())) {
                        markerTtileTv.setText(deviceName);
                    } else {
                        markerTtileTv.setText(rauduesTitle);
                    }
                }
                break;
            case Constants.HANDLER_GETDEVICEBASICSINFO_FAIL:
                Constants.ToastAction((String) msg.obj);
                break;
            case Constants.HANDLER_ADDCIRCLEFENCH_SUCCESS:          //新增圆形围栏成功
                //Constants.MyLog("成功修改围栏");
                //修改围栏后再次请求组获取最新围栏的信息
                Constants.isChangeDevice = true;
                finishDraw();
                isEnclosure = false;
                titleView.setRightText(DELETE);
                if (locRaiduMark != null && locRaiduMark.isVisible()) {
                    locAddressLayout.setVisibility(View.GONE);
                    locRaiduMark.remove();
                    myMarker = aMap.addMarker(myMarker.getOptions());
                    myMarker.showInfoWindow();
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(locRaiduMark.getPosition()));
                }
                new DevicesBasicsInfoAsync(deviceId, mContext, mHandler).execute();
                break;
            case Constants.HANDLER_ADDCIRCLEFENCH_FAILD:            //新增圆形围栏失败
                Constants.MyToast((String) msg.obj);
//                finishDraw();
//                isEnclosure = false;
//                title_layout.setRightText(CHANGESET);
                break;

        }
        return false;
    }

    private void deleteFence() {
        if (isAskDelete) {
            if (TextUtils.isEmpty(mLat) || TextUtils.isEmpty(mLng)) {
                ToastUtils.showToast(this, "经纬度为空！！！");
                return;
            }
            showPickDialog();
        }
    }


    private void showPickDialog() {
        MobclickAgent.onEvent(mContext, UMengKey.count_fence_delete);
        CustomDialog.Builder builder = new CustomDialog.Builder(MapOneActivity.this);
        builder.setMessage("删除围栏就无法收到位置报警哦");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                return;
            }
        });
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new DeleteFenceAsync(mContext, mHandler)
                        .execute(String.valueOf(mDeviceInfo
                                .getId()));

                dialog.dismiss();

            }
        });
        builder.create().show();


    }

    private void clearFence() {
        if (null != polygon) {
            polygon.remove();
            aMap.reloadMap();// 刷新地图
        }
        if (null != listPoint && listPoint.size() > 0) {
            listPoint.clear();
            aMap.reloadMap();// 刷新地图
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


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        AppLog.print("onCameraChange——————");
        if (locRaiduMark != null) {
            locRaiduMark.setPosition(cameraPosition.target);
            if (MAP_DRAG.equals(locRaiduMark.getTitle())) {
                locRaiduMark.remove();
                locRaiduMark = aMap.addMarker(getMarkerLocOptions(MapOneActivity.this, myMarker.getPosition(), SET_RADUIS_BY_CLICK_THIS));
                if (locAddressLayout.getVisibility() != View.VISIBLE) {
                    locAddressLayout.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (locAddressLayout.getVisibility() != View.VISIBLE) {
            return;
        }
        LatLng latLng = cameraPosition.target;
        if (latLng != null) {
            LatLonPoint point = new LatLonPoint(latLng.latitude, latLng.longitude);
            RegeocodeQuery query = new RegeocodeQuery(point, 200,
                    GeocodeSearch.AMAP);
            geocoderSearch.getFromLocationAsyn(query);
        }

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress address = regeocodeResult.getRegeocodeAddress();
        if (address != null) {
            String formartAddress = address.getFormatAddress();
            String city = address.getTownship();
            String describeAddress;
            int index = formartAddress.indexOf(city) + city.length();
            if (TextUtils.isEmpty(city)) {
                describeAddress = formartAddress;
            } else {
                if (formartAddress.length() > index) {
                    describeAddress = formartAddress.substring(index);
                } else {
                    describeAddress = city;
                }
            }
            locAddressTv.setText(describeAddress);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
