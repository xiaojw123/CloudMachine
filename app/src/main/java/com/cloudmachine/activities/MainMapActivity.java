package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.McDeviceInfoList;
import com.cloudmachine.struc.McDeviceLocation;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
/**
 * 账户下所有机器在地图上的位置
 * */
public class MainMapActivity extends BaseAutoLayoutActivity implements OnClickListener,
OnMarkerClickListener,OnInfoWindowClickListener, OnMapLoadedListener, InfoWindowAdapter
,OnMapClickListener {

	private static final int MapBoundsPadding = 10;
	private static final double MapBoundsLatLng = 0.04;
	private Context mContext;
	private TitleView title_layout;
	private MapView mapView;
	private AMap aMap;
	private List<McDeviceInfo> deviceList = new ArrayList<McDeviceInfo>();
	private Marker currentMarker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_map);
		this.mContext = this;
		mapView = (MapView) findViewById(R.id.bmapView);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
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
				McDeviceInfoList list = (McDeviceInfoList)bundle.getSerializable(Constants.P_DEVICELIST);
				if(null != list){
					deviceList.clear();
					deviceList.addAll(list.getDeviceList());
				}
				
			} catch (Exception e) {
				Constants.MyLog(e.getMessage());
			}

		}
	}
	private void initView(){
		initTitleLayout();
		initAMap();
		updateData();
	}
	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle(getResources().getString(R.string.main_bar_text1));
		
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
	}

	private void initAMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
					Constants.HANGZHOU,12,0,30)));
			aMap.setOnCameraChangeListener(new OnCameraChangeListener() {
				
				@Override
				public void onCameraChangeFinish(CameraPosition cameraPosition) {
					
				}
				
				@Override
				public void onCameraChange(CameraPosition arg0) {
					
				}
			});
			
			setUpMap();
		}
	}
	private void setUpMap() {
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//		addMarkersToMap();// 往地图上添加marker
	}
	private void updateData(){
		
		aMap.clear();
		if(deviceList!=null){
			int len = deviceList.size();
			for(int i=0; i<len; i++){
				McDeviceInfo deviceData = deviceList.get(i);
				McDeviceLocation location = deviceData.getLocation();
				
				MarkerOptions markerOption = new MarkerOptions();
				String allTiel = "";
				if(location != null&&location.getLat()!=0&&location.getLng()!=0){
					markerOption.position(new LatLng(location.getLat(),location.getLng()));
					allTiel = deviceData.getName()+Constants.S_FG+
							location.getPosition()+Constants.S_FG+deviceData.getId()+
							Constants.S_FG+deviceData.getMacAddress()
					+Constants.S_FG + deviceData.getWorkStatus();
				}else{
					allTiel = deviceData.getName()+Constants.S_FG+Constants.S_DEVICE_LOCATION_NO+Constants.S_FG+deviceData.getId()+Constants.S_FG+deviceData.getMacAddress();
				}
				markerOption.title(allTiel);
				markerOption.snippet(deviceData.getImage());
					if(deviceData.getWorkStatus() == 0){
						markerOption.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.mc_map_point_0));
					}else{
						markerOption.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.mc_map_point_1));
					}
			 aMap.addMarker(markerOption);
				
			}
		}
		notifyMap();
	}
	
	private void notifyMap(){
		Builder bu = new Builder();
		boolean isHave = false;
		double latMax = 0,latMin = 0,lngMax = 0,lngMin = 0;
		double lat = -1 ,lng = -1;
		if(deviceList!=null){
			int len = deviceList.size();
			for(int i=0; i<len; i++){
				McDeviceInfo deviceData = deviceList.get(i);
				McDeviceLocation location = deviceData.getLocation();
				if(location != null){
					isHave = true;
					lat = location.getLat();
					lng = location.getLng();
					
					if(lat!=0 && lng!=0){
						if(latMax == 0 ){
							latMax = latMin = lat;
							lngMax = lngMin = lng;
						}
						latMax = latMax>lat?latMax:lat;
						latMin = latMin<lat?latMin:lat;
						lngMax = lngMax>lng?lngMax:lng;
						lngMin = lngMin<lng?lngMin:lng;
						bu.include(new LatLng(lat,lng));
					}
				}
			}
			if(len >0 && isHave){
				bu.include(new LatLng(latMax+MapBoundsLatLng,lngMin-MapBoundsLatLng));
				bu.include(new LatLng(latMin-MapBoundsLatLng,lngMax+MapBoundsLatLng));
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bu.build(), MapBoundsPadding));
			}
		}
	}
	
	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		if(null != currentMarker){
			currentMarker.hideInfoWindow();
		}
	}

	/**
	 * 自定义infowinfow窗口
	 */
	public void render(Marker marker, View view) {
		String allStr[] = marker.getTitle().split(Constants.S_FG);
		TextView titleUi = ((TextView) view.findViewById(R.id.title));
		titleUi.setText(allStr.length > 1 ? allStr[0] : "");
		TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
		snippetUi.setText(allStr.length > 2 ? allStr[1] : "");
	}
	
	@Override
	public View getInfoContents(Marker marker) {
		// TODO Auto-generated method stub
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		View infoWindow = getLayoutInflater().inflate(
				R.layout.custom_info_window, null);

		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		MobclickAgent.onEvent(mContext, UMengKey.mc_main_map_infoWindow);
		//Intent intent = new Intent(this,DeviceMcActivity.class);
		Bundle b = new Bundle();
		String[] allStr = marker.getTitle().split(Constants.S_FG);
		if(allStr.length>2){
			long deviceId = Long.valueOf(allStr[2]);
			b.putString("devicePosition",allStr[1]);
			/*intent.putExtra(Constants.P_DEVICEID, deviceId);
			intent.putExtra(Constants.P_DEVICENAME, allStr[0]);*/
			b.putLong("deviceId", deviceId);
			b.putString("deviceName",allStr[0]);
			if(deviceId == 0){
				MobclickAgent.onEvent(mContext, UMengKey.mc_main_map_cloud_device_click);
			}
		}
		if(allStr.length>3){
			//intent.putExtra(Constants.P_DEVICEMAC, allStr[3]);
			b.putString("deviceMac",allStr[3]);
			b.putInt("deviceWorkState",Integer.parseInt(allStr[4]));
		}
		Constants.toActivity(MainMapActivity.this, DeviceMcActivity.class, b, false);
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		currentMarker = arg0;
		MobclickAgent.onEvent(mContext, UMengKey.mc_main_map_marker);
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(null != mapView)
			mapView.onResume();
		//MobclickAgent.onPageStart(UMengKey.time_machine_map);
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if(null != mapView)
			mapView.onPause();
		//MobclickAgent.onPageEnd(UMengKey.time_machine_map);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(null != mapView)
			mapView.onDestroy();
		super.onDestroy();
	}
	
	

}
