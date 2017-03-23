package com.cloudmachine.ui.repair.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol;
import com.amap.api.location.AMapLocationListener;
import com.cloudmachine.R;
import com.cloudmachine.activities.CheckMachineActivity;
import com.cloudmachine.activities.EditLayoutActivity;
import com.cloudmachine.activities.SearchPoiActivity;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.autolayout.widgets.CustomDialog;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.listener.RecyclerItemClickListener;
import com.cloudmachine.main.MainActivity;
import com.cloudmachine.net.task.MachineTypesListAsync;
import com.cloudmachine.net.task.SubmitRepairAsync;
import com.cloudmachine.struc.EditListInfo;
import com.cloudmachine.struc.MachineTypeInfo;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.struc.NewRepairInfo;
import com.cloudmachine.struc.ResidentAddressInfo;
import com.cloudmachine.ui.adapter.PhotoAdapter;
import com.cloudmachine.ui.repair.contract.NewRepairContract;
import com.cloudmachine.ui.repair.model.NewRepairModel;
import com.cloudmachine.ui.repair.presenter.NewRepairPresenter;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.PictureUtil;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.cloudmachine.utils.widgets.ClearEditTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.cloudmachine.R.id.recycler_view;

/**
 * 
 * @author shixionglu 新增报修页面
 */
public class NewRepairActivity extends BaseAutoLayoutActivity<NewRepairPresenter,NewRepairModel> implements
		Callback, OnClickListener ,NewRepairContract.View{

	private TitleView title_layout;
	private Context mContext;
	private Handler mHandler;
	private static final int REQUEST_CODE_DEVICENAME = 0x001;
	private McDeviceInfo deviceInfo;
	private TextView tvType;
	private TextView tvBrand;
	private TextView tvModel;
	private String PK_PROD_DEF = "";// 类型
	private String PK_BRAND = "";// 品牌
	private String PK_VHCL_MATERIAL = "";// 型号
	private String deviceType, deviceBrand1, deviceModel;

	private AMapLocationClient locationClient = null;
	private TextView text_address;

	private LinearLayout macopName;
	private LinearLayout macopTel;
	private LinearLayout discription;

	private ArrayList<McDeviceInfo> deviceMacList;// 整体设备列表
	private ArrayList<McDeviceInfo> ownDeviceInfos;// 自己的机器

	private RelativeLayout showButton;
	private TextView tvCheckMac;

	private String vmacopname;
	private String vmacoptel;
	private String pk_prod_def;
	private String pk_brand;
	private String pk_vhcl_materia;
	private String vmachinenum;
	private String vdiscription;
	private String vservicetype;
	private String vworkaddress;
	private String province;
	private String deviceId;

	private int clickPosition;
	private ClearEditTextView cetContactName;
	private ClearEditTextView cetContactMobile;
	private ClearEditTextView cetContactDesc;
	private ClearEditTextView et_rackIdname;
	private McDeviceInfo mcDeviceInfo; // 当前用户选择报修的机器
	private boolean isOwnerDevice;
	private RecyclerView mRecyclerView;

	private PhotoAdapter photoAdapter;
	private ArrayList<String> selectedPhotos = new ArrayList<>();
	List<String> photos = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Res.init(this);
		PublicWay.activityList.add(this);
		setContentView(R.layout.activity_new_repair);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
		initLocation();
		new MachineTypesListAsync(mContext,mHandler).execute();

	}

	@Override
	public void initPresenter() {
		mPresenter.setVM(this,mModel);
	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_repair_create);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_repair_create);
		super.onPause();
	}

	private void initView() {
		initTitleView();
		initNewReapirView();
		Constants.MyLog(MainActivity.deviceMacList.toString());
		deviceMacList = MainActivity.deviceMacList; // 获取所有机器
		getOwnerMac(); // 获取自己拥有的机器
		showData(); // 控制按钮选择机器的显示影藏
	}

	private void getIntentData() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			try {
				mcDeviceInfo = (McDeviceInfo)bundle.getSerializable(Constants.P_DEVICEINFO_MY);
				// deviceId = 100;
			} catch (Exception e) {
				Constants.MyLog(e.getMessage());
			}

		}
	}
	
	
	public void showData() {
		if(mcDeviceInfo == null){
			if (null != ownDeviceInfos && ownDeviceInfos.size() > 0) {
				tvCheckMac.setVisibility(View.VISIBLE);
				showButton.setVisibility(View.VISIBLE);
			} else {
				tvCheckMac.setVisibility(View.GONE);
				showButton.setVisibility(View.GONE);
			}
		}else{
			tvCheckMac.setVisibility(View.GONE);
			showButton.setVisibility(View.GONE);
			showMyDeviceInfo();
			
		}
		
	}

	public void getOwnerMac() {
		ownDeviceInfos = new ArrayList<>();
		for (int i = 0; i < deviceMacList.size(); i++) {
			if (deviceMacList.get(i).getType() == 1) {
				ownDeviceInfos.add(deviceMacList.get(i));
			}
		}
	}

	private void initNewReapirView() {

		//初始化动态recyclerview
		mRecyclerView = (RecyclerView) findViewById(recycler_view);
		photoAdapter = new PhotoAdapter(this, selectedPhotos);
		mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
		mRecyclerView.setAdapter(photoAdapter);
		mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
				new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
							PhotoPicker.builder()
									.setPhotoCount(PhotoAdapter.MAX)
									.setShowCamera(true)
									.setPreviewEnabled(false)
									.setSelected(selectedPhotos)
									.start(NewRepairActivity.this);
						} else {
							PhotoPreview.builder()
									.setPhotos(selectedPhotos)
									.setCurrentItem(position)
									.start(NewRepairActivity.this);
						}
					}
				}));


		text_address = (TextView) findViewById(R.id.text_address);
		RadiusButtonView checkMac = (RadiusButtonView) findViewById(R.id.checkBtn);// 选择机器
		RelativeLayout macopType = (RelativeLayout) findViewById(R.id.iv_type);// 类型
		RelativeLayout macopBrand = (RelativeLayout) findViewById(R.id.iv_brand);// 品牌
		RelativeLayout macopModel = (RelativeLayout) findViewById(R.id.iv_model);// 型号
		RelativeLayout macopAddress = (RelativeLayout) findViewById(R.id.iv_address);// 位置
		RadiusButtonView btnSubmitNow = (RadiusButtonView) findViewById(R.id.btn_bottom_repair);
		showButton = (RelativeLayout) findViewById(R.id.rl_showbutton);
		tvCheckMac = (TextView) findViewById(R.id.tv_checkMac);
		macopName = (LinearLayout) findViewById(R.id.et_name);
		macopTel = (LinearLayout) findViewById(R.id.et_mobile);
		discription = (LinearLayout) findViewById(R.id.et_desc);

		tvType = (TextView) findViewById(R.id.tv_type);
		tvBrand = (TextView) findViewById(R.id.tv_brand);
		tvModel = (TextView) findViewById(R.id.tv_model);

		cetContactName = (ClearEditTextView) findViewById(R.id.et_repairname);
		cetContactMobile = (ClearEditTextView) findViewById(R.id.et_repair_mobile);
		cetContactDesc = (ClearEditTextView) findViewById(R.id.et_des);
		et_rackIdname = (ClearEditTextView) findViewById(R.id.et_rackIdname);

		cetContactName.setText(MyApplication.getInstance().getTempMember().getNickname());
		cetContactMobile.setText(MyApplication.getInstance().getTempMember().getMobile());
		
		macopType.setOnClickListener(this);
		macopBrand.setOnClickListener(this);
		macopModel.setOnClickListener(this);
		macopAddress.setOnClickListener(this);
		checkMac.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Bundle b = new Bundle();
				b.putSerializable(Constants.P_MAC_DEVICE, ownDeviceInfos);
				// Constants.toActivity(NewRepairActivity.this,
				// CheckMachineActivity.class, b,false);
				Constants.toActivityForR(NewRepairActivity.this,
						CheckMachineActivity.class, b, Constants.REQUEST_ToSearchDeviceActivity);
			}
		});
		btnSubmitNow.setOnClickListener(new OnClickListener() {// 立即提交

					@Override
					public void onClick(View arg0) {

						NewRepairInfo newRepairInfo = new NewRepairInfo();
						MobclickAgent.onEvent(mContext,UMengKey.count_submit_repair_now);
						vmacopname = cetContactName.getText().toString().trim();
						vmacoptel = cetContactMobile.getText().toString().trim();
						pk_prod_def = PK_PROD_DEF;
						pk_brand = PK_BRAND;
						pk_vhcl_materia = PK_VHCL_MATERIAL;
						vmachinenum = et_rackIdname.getText().toString().trim();
						
						vservicetype = "1";
						vdiscription = cetContactDesc.getText().toString().trim();

						newRepairInfo.setVmacopname(vmacopname);
						newRepairInfo.setVmacoptel(vmacoptel);
						newRepairInfo.setPk_prod_def(pk_prod_def);
						newRepairInfo.setPk_brand(pk_brand);
						newRepairInfo.setPk_vhcl_material(pk_vhcl_materia);
						newRepairInfo.setVmachinenum(vmachinenum);
						newRepairInfo.setVdiscription(vdiscription);
						newRepairInfo.setVservicetype(vservicetype);
						newRepairInfo.setVworkaddress(vworkaddress);
						newRepairInfo.setProvince(province);
						newRepairInfo.setDeviceId(deviceId);
						if(TextUtils.isEmpty(vmacopname)){
							Constants.MyToast("联系人不能为空！");
						}else if(TextUtils.isEmpty(vmacoptel)){
							Constants.MyToast("联系手机号码不能为空！");
						}else if(TextUtils.isEmpty(pk_prod_def)){
							Constants.MyToast("机器类型不能为空！");
						}else if(TextUtils.isEmpty(pk_brand)){
							Constants.MyToast("机器品牌不能为空！");
						}else if(TextUtils.isEmpty(pk_vhcl_materia)){
							Constants.MyToast("机器型号不能为空！");
						}else if(TextUtils.isEmpty(vmachinenum)){
							Constants.MyToast("机器铭牌号码不能为空！");
						}else if(TextUtils.isEmpty(vdiscription)){
							Constants.MyToast("故障描述不能为空！");
						}else if(TextUtils.isEmpty(province)){
							Constants.MyToast("机器位置信息不能为空！");
						}else{
							
							showDialog(newRepairInfo);
						}
						
					}
				});

	}

	private void initTitleView() {
		

			title_layout = (TitleView) findViewById(R.id.title_layout);
			title_layout.setTitle(getResources().getString(R.string.new_repair));
			title_layout.setLeftOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		

	}
	private void showDialog(final NewRepairInfo newRepairInfo){
		CustomDialog.Builder builder =  new CustomDialog.Builder(NewRepairActivity.this);
		builder.setMessage("请确认"+vmacoptel+"的手机号码保持畅通，我们会在3分钟内联系您，请耐心等候", "");
		builder.setTitle("提示");
		builder.setLeftButtonColor(getResources().getColor(R.color.public_title_color));
		builder.setRightButtonColor(getResources().getColor(R.color.public_title_color));
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				return;
			}
		});
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new SubmitRepairAsync(mContext, mHandler, newRepairInfo)
				.execute();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	private void initLocation() {
		// 初始化client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		// 设置定位参数
		locationClient.setLocationOption(getDefaultOption());
		// 设置定位监听
		locationClient.setLocationListener(locationListener);
		locationClient.startLocation();
	}

	private void stopLocation() {
		// 停止定位
		locationClient.stopLocation();
	}

	private void destroyLocation() {
		if (null != locationClient) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			// locationClient.stopAssistantLocation();
			stopLocation();
			locationClient.onDestroy();
			locationClient = null;
		}
	}

	
	private AMapLocationClientOption getDefaultOption() {
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);// 可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(false);// 可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);// 可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(2000);// 可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);// 可选，设置是否返回逆地理地址信息。默认是ture
		mOption.setOnceLocation(false);// 可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);// 可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP);// 可选，
																				// 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		return mOption;
	}

	/**
	 * 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation loc) {
			if (null != loc) {
				// 解析定位结果
				province = loc.getProvince();
				vworkaddress = loc.getProvider();
				setTextToView(text_address,loc.getPoiName());
				stopLocation();
				// String result = Utils.getLocationStr(loc);
				// Constants.MyLog(result);
				// tvReult.setText(result);
			} else {
				// tvReult.setText("定位失败，loc is null");
			}
		}
	};
	private void setTextToView(TextView view, String str){
		view.setText(Constants.toViewString(str));
		view.setTextColor(mContext.getResources().getColor(
				R.color.public_black));
	}

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case Constants.HANDLER_NEWREPAIR_SUCCESS:
			Constants.MyLog(String.valueOf(msg.obj));
			finish();
			break;
		case Constants.HANDLER_NEWREPAIR_FAILD:
			Constants.MyLog(String.valueOf(msg.obj));
			break;
		case Constants.HANDLER_GETMACHINETYPES_SUCCESS:
			List<MachineTypeInfo> mTypeInfo = (List<MachineTypeInfo>)msg.obj;
			if(null != mTypeInfo && mTypeInfo.size()>0){
				MachineTypeInfo mInfo = mTypeInfo.get(0);
				if(null != mInfo){
					deviceType = mInfo.getName();
					setTextToView(tvType,deviceType);
					PK_PROD_DEF = mInfo.getPk_PROD_DEF();
					deviceModel = "";
				}
			}
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Constants.MyLog("进来了");
		Constants.MyLog("requestCode"+requestCode);
		Constants.MyLog("resultCode"+resultCode);
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Constants.REQUEST_ToSearchActivity) {
			if (null != data && null != data.getExtras()) {
				ResidentAddressInfo addressInfo = (ResidentAddressInfo) data.getExtras().getSerializable(Constants.P_SEARCHINFO);
				if (null != addressInfo) {
					province = addressInfo.getProvince();
					vworkaddress = addressInfo.getPosition();
					setTextToView(text_address, addressInfo.getTitle());
				}
			}
		}

			if (resultCode == RESULT_OK &&
					(requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
				Constants.MyLog("照片回显");
				if (data != null) {
					photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
				}
				selectedPhotos.clear();

				if (photos != null&&photos.size()>0) {
					String fileName = String.valueOf(System.currentTimeMillis());
					Bitmap smallBitmap = PictureUtil.getSmallBitmap(photos.get(0));
					String filename = FileUtils.saveBitmap(smallBitmap, fileName);
					mPresenter.upLoadPhotoRequest(filename);
					selectedPhotos.addAll(photos);
				}
				photoAdapter.notifyDataSetChanged();
			}

		
		switch (resultCode) {
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			int editType = bundle.getInt(Constants.P_EDITTYPE);
			int itemType = bundle.getInt(Constants.P_ITEMTYPE);
			switch (itemType) {
			case Constants.E_ITEMS_deviceName:
				String name = bundle.getString(Constants.P_EDITRESULTSTRING);
				if (!TextUtils.isEmpty(name)) {
					// device_name.setContent(name);
				} else {
					Constants.ToastAction(ResV
							.getString(R.string.add_device_name_null));
				}
				break;
			case Constants.E_ITEMS_category:
				EditListInfo eInfoType = (EditListInfo) bundle
						.getSerializable(Constants.P_EDITRESULTITEM);
				if (null != eInfoType) {
					deviceType = eInfoType.getName();
					setTextToView(tvType,deviceType);
					PK_PROD_DEF = eInfoType.getPK_PROD_DEF();
					deviceModel = "";
					// device_model.setContent(deviceModel);
					// device_model.initEditiHint();
				}
				break;
			case Constants.E_ITEMS_brand:
				EditListInfo eInfoBrand = (EditListInfo) bundle
						.getSerializable(Constants.P_EDITRESULTITEM);
				if (null != eInfoBrand) {
					deviceBrand1 = eInfoBrand.getName();
					setTextToView(tvBrand,deviceBrand1);
					PK_BRAND = eInfoBrand.getPK_BRAND();
					deviceModel = "";
					// device_model.setContent(deviceModel);
					// device_model.initEditiHint();
				}
				break;
			case Constants.E_ITEMS_model:
				EditListInfo eInfoModel = (EditListInfo) bundle
						.getSerializable(Constants.P_EDITRESULTITEM);
				if (null != eInfoModel) {
					deviceModel = eInfoModel.getName();
					setTextToView(tvModel,deviceModel);
					PK_VHCL_MATERIAL = eInfoModel.getPK_VHCL_MATERIAL();
				}
				break;
			

			default:
				break;
			}
			break;
		case Constants.CLICK_POSITION:
			isOwnerDevice = true;
			clickPosition = data.getIntExtra("click_position", -1);
			if (clickPosition != -1) {
				mcDeviceInfo = ownDeviceInfos.get(clickPosition);
				showMyDeviceInfo();
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Constants.removeActivity(this);
		destroyLocation();
	}

	private void showMyDeviceInfo(){
		if(null != mcDeviceInfo){
			PK_PROD_DEF = mcDeviceInfo.getPk_PROD_DEF();
			PK_BRAND = mcDeviceInfo.getPk_BRAND();
			PK_VHCL_MATERIAL = mcDeviceInfo.getPk_VHCL_MATERIAL();
			vmachinenum = mcDeviceInfo.getRackId();
			et_rackIdname.setText(vmachinenum);
			vworkaddress = mcDeviceInfo.getLocation().getPosition();
			province = mcDeviceInfo.getLocation().getProvince();
			deviceId = String.valueOf(mcDeviceInfo.getLocation()
					.getDeviceId());
			setTextToView(tvType,mcDeviceInfo.getCategory());
			setTextToView(tvBrand,mcDeviceInfo.getBrand());
			setTextToView(tvModel,mcDeviceInfo.getModel());
			setTextToView(text_address,vworkaddress);
		}
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_type:
			// Intent typeIntent = new
			// Intent(NewRepairActivity.this,MachineTypeActivity.class);
			if(!isOwnerDevice){
				gotoEditActivity(Constants.E_DEVICE_LIST,
						Constants.E_ITEMS_category, "", "", "", "机型");
			}else{
				Constants.MyToast("已安装云盒子机器信息不可修改");
			}
			
			break;
		case R.id.iv_brand:
			// Intent brandIntent = new
			// Intent(NewRepairActivity.this,MachineBrandActivity.class);
			if(!isOwnerDevice){
				gotoEditActivity(Constants.E_DEVICE_LIST, Constants.E_ITEMS_brand,
						PK_PROD_DEF, "", "", "品牌");
			}else{
				Constants.MyToast("已安装云盒子机器信息不可修改");
			}
			
			break;
		case R.id.iv_model:
			// Intent modelIntent = new
			// Intent(NewRepairActivity.this,MachineModelActivity.class);
			if(!isOwnerDevice){
				if (!TextUtils.isEmpty(PK_PROD_DEF) && ! TextUtils.isEmpty(PK_BRAND)) {
					gotoEditActivity(Constants.E_DEVICE_LIST, Constants.E_ITEMS_model,
							PK_PROD_DEF, PK_BRAND, "", "型号");
				}else {
					//Constants.ToastAction("请先选择产品和品牌");
					Toast.makeText(mContext, "请先选择产品和品牌", Toast.LENGTH_LONG).show();
					Constants.MyLog("请先选择产品和品牌");
				}
				
			}else{
				Constants.MyToast("已安装云盒子机器信息不可修改");
			}
			break;
		case R.id.iv_address:
			Intent intent = new Intent(this, SearchPoiActivity.class);
			startActivityForResult(intent, Constants.REQUEST_ToSearchActivity);

			break;
		default:
			break;
		}
	}

	// v1用来传nc类型id，v2用来传nc品牌id，v4用来传类型id
	private void gotoEditActivity(int editType, int itemType, String v1,
			String v2, String v3, String titleName) {

		Bundle bundle = new Bundle();
		bundle.putString(Constants.P_TITLETEXT, titleName);
		// bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
		bundle.putInt(Constants.P_EDITTYPE, editType);
		bundle.putInt(Constants.P_ITEMTYPE, itemType);
		bundle.putString(Constants.P_EDIT_LIST_VALUE1, v1);
		bundle.putString(Constants.P_EDIT_LIST_VALUE2, v2);
		Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
	}

	@Override
	public void returnUploadPhoto(String url) {
		Constants.MyLog("打印的图片链接为"+url);
	}
}

// device_name = (CanBeEditItemView) findViewById(R.id.device_name);
// device_name.isEdit(true);
//
// device_type = (CanBeEditItemView) findViewById(R.id.device_type);
// device_brand1 = (CanBeEditItemView) findViewById(R.id.device_brand1);
// device_model = (CanBeEditItemView) findViewById(R.id.device_model);
// repair_place = (CanBeEditItemView) findViewById(R.id.repair_place);
// mobile = (CanBeEditItemView) findViewById(R.id.mobile);
// mobile.isEdit(true);
// //description = (CanBeEditItemView) findViewById(R.id.);
// description.isEdit(true);
// // CanBeEditItemView deviceID = findViewById(R.id.device_id);
// device_name.getImageView().setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Constants.MyLog("设备名称的点击事件");
// Intent intent = new Intent(NewRepairActivity.this,
// DeviceListActivity.class);
// startActivityForResult(intent, REQUEST_CODE_DEVICENAME);
// }
// });
// device_type.getImageView().setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Constants.MyLog("机型的点击事件");
// }
// });
// device_brand1.getImageView().setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Constants.MyLog("品牌名称的点击事件");
// }
// });
// device_model.getImageView().setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Constants.MyLog("型号名称的点击事件");
// }
// });
// repair_place.getImageView().setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// Constants.MyLog("位置名称的点击事件");
// }
// });

// device_name.setOnClickListener(this);
// device_type.setOnClickListener(this);
// device_brand1.setOnClickListener(this);
// device_model.setOnClickListener(this);
// repair_place.setOnClickListener(this);
// mobile.setOnClickListener(this);
// description.setOnClickListener(this);
// super.onActivityResult(requestCode, resultCode, data);
// switch (requestCode) {
// case REQUEST_CODE_DEVICENAME:
// deviceInfo = (McDeviceInfo) data
// .getSerializableExtra("McDeviceInfo");
// initNewRepairLayout(); // 获取数据，自动填写机器的基本信息
// break;
//
// default:
// break;
// }