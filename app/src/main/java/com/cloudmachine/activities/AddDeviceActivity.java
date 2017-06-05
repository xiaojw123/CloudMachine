package com.cloudmachine.activities;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.CanBeEditItemView;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.main.photo.AlbumActivity;
import com.cloudmachine.net.task.ImageUploadAsync;
import com.cloudmachine.net.task.updateDeviceInfoAsync;
import com.cloudmachine.net.task.updateDeviceInfoByKeyAsync;
import com.cloudmachine.struc.EditListInfo;
import com.cloudmachine.struc.McDeviceBasicsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ResV;
import com.cloudmachine.utils.UMengKey;
import com.cloudmachine.utils.photo.util.Bimp;
import com.cloudmachine.utils.photo.util.FileUtils;
import com.cloudmachine.utils.photo.util.ImageItem;
import com.cloudmachine.utils.photo.util.PublicWay;
import com.cloudmachine.utils.photo.util.Res;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * 查看页面基本信息
 */
public class AddDeviceActivity extends BaseAutoLayoutActivity implements Callback, OnClickListener {
    public static final String DEVICE_SHOW = "device_show";
    private static final int TAKE_PICTURE = 0x000001;
    private final static int SCANNIN_GREQUEST_CODE = 678;
    private Context mContext;
    private Handler mHandler;
    private TitleView title_layout;
    //	private DeviceInfo deviceInfo;
    private CanBeEditItemView device_name, device_type, device_brand1, device_model, device_year, device_card,
            device_owner, device_phone_number, device_rackId, device_workTime;
    //	private int addDeviceType ; //0:查看基本信息  1:新增
    private int status;
    private String deviceType, deviceBrand1, deviceModel;
    private int typeId, brand1Id, modeId;
    private long deviceId;
    private int deviceTypeId;

    private View parentView;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    public static Bitmap bimap;
    private ImageView decive_image, name_image, engine_image;
    private String decive_image_url, name_image_url, engine_image_url;
    private int addImageType; //0:机器照片  1:铭牌 2:发动机号照片
    private String nameImageUrl, engineImageUrl;
    private String nameImagePath, engineImagePath;
    private McDeviceBasicsInfo mcDeviceBasicsInfo;
    private RadiusButtonView change_owner;
    private FrameLayout memberLayout;
    private boolean showDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//		Constants.addActivity(this);
        Res.init(this);
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_nm);
        PublicWay.activityList.add(this);
        parentView = getLayoutInflater().inflate(R.layout.activity_add_device, null);
        setContentView(parentView);
        mContext = this;
        mHandler = new Handler(this);
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
                showDevice = bundle.getBoolean(DEVICE_SHOW);
                mcDeviceBasicsInfo = (McDeviceBasicsInfo) bundle.getSerializable(Constants.P_MCDEVICEBASICSINFO);
                if (null != mcDeviceBasicsInfo) {
                    deviceId = mcDeviceBasicsInfo.getId();
                    deviceTypeId = mcDeviceBasicsInfo.getType();
                    deviceType = mcDeviceBasicsInfo.getCategory();
                    typeId = mcDeviceBasicsInfo.getTypeId();
                    deviceBrand1 = mcDeviceBasicsInfo.getBrand();
                    brand1Id = mcDeviceBasicsInfo.getBrandId();
                    deviceModel = mcDeviceBasicsInfo.getModel();
                    modeId = mcDeviceBasicsInfo.getModelId();
                    String[] imageUrl = mcDeviceBasicsInfo.getDevicePhoto();
                    if (null != imageUrl) {
                        Bimp.tempSelectBitmap.clear();
                        int size = imageUrl.length;
                        for (int i = 0; i < size; i++) {
                            ImageItem item = new ImageItem();
                            item.setImageUrl(imageUrl[i]);
                            item.setSelected(true);
                            Bimp.tempSelectBitmap.add(item);
                        }
                    }
                }
            } catch (Exception e) {
                Constants.MyLog(e.getMessage());
            }
        }
    }

    private void initView() {
        initTitleLayout();
        initAddDeviceItemView();
        initPhotoGridView();
        change_owner = (RadiusButtonView) findViewById(R.id.change_owner);
        if (!Constants.isNoEditInMcMember(deviceId, deviceTypeId)) {
            change_owner.setVisibility(View.VISIBLE);
            change_owner.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.P_SEARCHLISTTYPE, 2);
                    bundle.putLong(Constants.P_DEVICEID, deviceId);
                    Constants.toActivity(AddDeviceActivity.this, SearchActivity.class, bundle);
                }
            });
        } else {
            change_owner.setVisibility(View.GONE);
        }


    }

    private void initTitleLayout() {

        title_layout = (TitleView) findViewById(R.id.title_layout);

        title_layout.setTitle(ResV.getString(R.string.addDevice_title));
        title_layout.setLeftOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void initAddDeviceItemView() {
        memberLayout = (FrameLayout) findViewById(R.id.deivce_member);
        device_name = (CanBeEditItemView) findViewById(R.id.device_name);
        device_type = (CanBeEditItemView) findViewById(R.id.device_type);
        device_brand1 = (CanBeEditItemView) findViewById(R.id.device_brand1);
        device_model = (CanBeEditItemView) findViewById(R.id.device_model);
        device_year = (CanBeEditItemView) findViewById(R.id.device_year);
        device_card = (CanBeEditItemView) findViewById(R.id.device_card);
        device_owner = (CanBeEditItemView) findViewById(R.id.device_owner);
        device_phone_number = (CanBeEditItemView) findViewById(R.id.device_phone_number);
        device_rackId = (CanBeEditItemView) findViewById(R.id.device_rackId);
        device_workTime = (CanBeEditItemView) findViewById(R.id.device_workTime);
        if (null != mcDeviceBasicsInfo) {
            device_name.setContent(mcDeviceBasicsInfo.getDeviceName());
            device_type.setContent(mcDeviceBasicsInfo.getCategory());
            device_brand1.setContent(mcDeviceBasicsInfo.getBrand());
            device_model.setContent(mcDeviceBasicsInfo.getModel());
            device_year.setContent(mcDeviceBasicsInfo.getFactoryTime());
            device_card.setContent(mcDeviceBasicsInfo.getLicense());
            device_rackId.setContent(mcDeviceBasicsInfo.getRackId());
            device_workTime.setContent(String.valueOf(mcDeviceBasicsInfo.getWorkTime()) + "时");
        }
        memberLayout.setOnClickListener(this);
        device_name.setOnClickListener(this);
            /*device_type.setOnClickListener(this);
            device_brand1.setOnClickListener(this);
			device_model.setOnClickListener(this);
			device_year.setOnClickListener(this);
			device_card.setOnClickListener(this);
			device_owner.setOnClickListener(this);
			device_phone_number.setOnClickListener(this);*/

//			device_name.isEdit(true);
        if (showDevice) {
            memberLayout.setVisibility(View.VISIBLE);
        }
    }

    private void initPhotoGridView() {

        pop = new PopupWindow(this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                photo();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddDeviceActivity.this,
                        AlbumActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });


        decive_image = (ImageView) findViewById(R.id.decive_image);
        decive_image.setOnClickListener(this);
        name_image = (ImageView) findViewById(R.id.name_image);
        name_image.setOnClickListener(this);
        engine_image = (ImageView) findViewById(R.id.engine_image);
        engine_image.setOnClickListener(this);
        if (null != mcDeviceBasicsInfo && null != mcDeviceBasicsInfo.getDevicePhoto() && mcDeviceBasicsInfo.getDevicePhoto().length > 0) {
            decive_image_url = mcDeviceBasicsInfo.getDevicePhoto()[0];
            ImageLoader.getInstance().displayImage(decive_image_url, decive_image,
                    Constants.displayDeviceImageOptions, null);
        }
        if (null != mcDeviceBasicsInfo && null != mcDeviceBasicsInfo.getNameplatePhoto() && mcDeviceBasicsInfo.getNameplatePhoto().length > 0) {
            name_image_url = mcDeviceBasicsInfo.getNameplatePhoto()[0];
            ImageLoader.getInstance().displayImage(name_image_url, name_image,
                    Constants.displayDeviceImageOptions, null);
        } else {
        }

        if (null != mcDeviceBasicsInfo && null != mcDeviceBasicsInfo.getEnginePhoto()) {
            engine_image_url = mcDeviceBasicsInfo.getEnginePhoto();
            ImageLoader.getInstance().displayImage(engine_image_url, engine_image,
                    Constants.displayDeviceImageOptions, null);
        } else {
        }
        /*noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					addImageType = 0;
					ll_popup.startAnimation(AnimationUtils.loadAnimation(AddDeviceActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
//					Constants.gotoImageBrower(AddDeviceActivity.this, "1", arg2);
					if(Bimp.tempSelectBitmap.size()>0)
						Constants.gotoImageBrowerType(AddDeviceActivity.this, arg2, Bimp.tempSelectBitmap,true,1);
				}
			}
		});*/
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Bimp.tempSelectBitmap.clear();
        Bimp.max = 0;
        super.onDestroy();
        Constants.removeActivity(this);
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        // MobclickAgent.onPageEnd(UMengKey.time_machine_info);
        super.onPause();
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddDeviceActivity.this.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {

            case R.id.deivce_member:
                Bundle bundle_member = new Bundle();
                bundle_member.putLong(Constants.P_DEVICEID, deviceId);
                if (null != mcDeviceBasicsInfo)
                    bundle_member.putInt(Constants.P_DEVICETYPE,
                            mcDeviceBasicsInfo.getType());
                Constants.toActivity(this, DeviceMcMemberActivity.class, bundle_member);

                break;
            case R.id.device_name:
                //MobclickAgent.onEvent(mContext,UMengKey.time_machine_info_edit);
                gotoEditActivity(device_name, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
            case R.id.device_type:
                gotoEditActivity(device_type, Constants.E_DEVICE_LIST, Constants.E_ITEMS_category, typeId, 0);
                break;
            case R.id.device_brand1:
                gotoEditActivity(device_brand1, Constants.E_DEVICE_LIST, Constants.E_ITEMS_brand, brand1Id, 0);
                break;
            case R.id.device_model:
                gotoEditActivity(device_model, Constants.E_DEVICE_LIST, Constants.E_ITEMS_model, typeId, brand1Id);
                break;
            case R.id.device_year:
                String yearStr = device_year.getContent();
                int year = 2010;
                int mouth = 1;
                int data = 1;
                if (!TextUtils.isEmpty(yearStr)) {
                    String[] list = yearStr.split("-");
                    if (list.length == 1) {
                        year = Integer.valueOf(list[0]);
                    } else if (list.length == 2) {
                        year = Integer.valueOf(list[0]);
                        mouth = Integer.valueOf(list[1]);
                    } else if (list.length == 3) {
                        year = Integer.valueOf(list[0]);
                        mouth = Integer.valueOf(list[1]);
                        data = Integer.valueOf(list[2]);
                    }
                }
                DatePickerDialog dialog = new DatePickerDialog(AddDeviceActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        device_year.setContent(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, mouth - 1, data);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    DatePicker datePicker = dialog.getDatePicker();
                    datePicker.setMaxDate(System.currentTimeMillis());
                }
                dialog.show();
                break;
            case R.id.device_card:
                gotoCapture(Constants.CODE_license);
                break;
            case R.id.name_image:
                Constants.gotoImageBrowerType(this, 1, new String[]{name_image_url}, false, 0);
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
//			addImageType = 1;
//			photo();
                break;
            case R.id.decive_image:
                Constants.gotoImageBrowerType(this, 1, new String[]{decive_image_url}, false, 0);
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
                break;
            case R.id.engine_image:
                Constants.gotoImageBrowerType(this, 1, new String[]{engine_image_url}, false, 0);
                MobclickAgent.onEvent(mContext, UMengKey.count_machine_watch_lardgeimage);
//			addImageType = 2;
//			photo();
                break;
            case R.id.device_owner:
                gotoEditActivity(device_owner, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
            case R.id.device_phone_number:
                gotoEditActivity(device_phone_number, Constants.E_DEVICE_TEXT, Constants.E_ITEMS_deviceName);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
        /*case Constants.HANDLER_SAVEDEVICE_SUCCESS:
			DeviceInfo deviceInfo = (DeviceInfo)msg.obj;
			if(null == deviceInfo){
				deviceInfo = this.deviceInfo;
			}
			int state = deviceInfo.getStatus();
			Bundle b=new Bundle();
			b.putSerializable(Constants.P_DEVICEINFO, deviceInfo); 
			if(state == 1 || state == 2){
				Constants.toActivity(AddDeviceActivity.this, SensorActivity.class, b,true);
			}else if(state == 3){
				Constants.toActivity(AddDeviceActivity.this, SearchActivity.class, b, true);
			}else if(state == 5){
				Constants.toActivity(AddDeviceActivity.this, DemarcateActivity.class, b,true);
			}else{
				
			}
//			Bundle b=new Bundle();
//			b.putSerializable(Constants.P_DEVICEINFO, (DeviceInfo)msg.obj); 
//			Constants.toActivity(AddDeviceActivity.this, SensorActivity.class, b,true);
			break;
		case Constants.HANDLER_SAVEDEVICE_FAIL:
			String message = (String)msg.obj;
			Constants.ToastAction(null!=message?message:"新增失败！");
			break;*/
            case ImageUploadAsync.ImageUpload_Success:
                String rObjStr = (String) msg.obj;
                String[] rStr = rObjStr.split(Constants.S_FG);
                if (null != rStr && rStr.length == 2) {
                    if (rStr[0].equals(nameImagePath)) {
                        nameImageUrl = rStr[1];
                    } else if (rStr[0].equals(engineImagePath)) {
                        engineImageUrl = rStr[1];
                    }

                }
                break;
            case Constants.HANDLER_UPDATEDEVICEBYKEY_SUCCESS:
                String message_u = (String) msg.obj;
                Constants.ToastAction(message_u);
                Constants.isChangeDevice = true;
                break;
            case Constants.HANDLER_UPDATEDEVICEBYKEY_FAIL:
                String message_u_f = (String) msg.obj;
                Constants.ToastAction(null != message_u_f ? message_u_f : "修改失败！");
                break;
		/*case ImageUploadAsync.ImageUpload_Success:
			String url = (String)msg.obj;
			devicePhoto = Constants.arrayCopyString(devicePhoto,url);
			image_array_view.setImageUrl(devicePhoto);
//			if(addDeviceType == 1){
				image_array_view.setVisibility(View.VISIBLE);
				addPhotoButton.setVisibility(View.GONE);
//			}
			if(addDeviceType == 0){
				updateDeviceInfoByKey(Constants.P_DEVICEINFO_devicePhoto,Constants.stringArray2string(devicePhoto));
			}
			break;
		case ImageUploadAsync.ImageUpload_Fail:
			Constants.ToastAction("上传失败！");
//			if(addDeviceType == 1){
				image_array_view.setVisibility(View.VISIBLE);
				addPhotoButton.setVisibility(View.GONE);
//			}
			break;
		case Constants.HANDLER_ADDDEVICE_SUCCESS:
			String message1 = (String)msg.obj;
			Constants.isAddDevice = true;
			Constants.ToastAction(null!=message1?message1:"新增机器成功！");
			finish();
			break;
		case Constants.HANDLER_ADDDEVICE_FAIL:
			String message = (String)msg.obj;
			Constants.ToastAction(null!=message?message:"新增失败！");
			break;
		*/

        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK && addImageType == 1) {
                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    nameImagePath = FileUtils.saveBitmap(bm, fileName);
                    name_image.setImageBitmap(bm);
                    name_image.setVisibility(View.VISIBLE);
                    new ImageUploadAsync(mHandler, nameImagePath).execute();
                } else if (resultCode == RESULT_OK && addImageType == 2) {

                    String engineFileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    engineImagePath = FileUtils.saveBitmap(bm, engineFileName);
                    engine_image.setImageBitmap(bm);
                    engine_image.setVisibility(View.VISIBLE);
                    new ImageUploadAsync(mHandler, engineImagePath).execute();
                } else if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    String fPath = FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
//				takePhoto.setBitmap(bm);
                    takePhoto.setImagePath(fPath);
                    takePhoto.setSelected(true);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
		/*case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				String rStr = bundle.getString("result");
				if(null != rStr){
					if(!RegExp.licenseMatcher(rStr)){
						Constants.ToastAction(getResources().getString(R.string.QR_RegExp_License_Error));
						return;
					}
					device_card.setContent(rStr);
					
				}
			}
			break;*/
            default:
                switch (resultCode) {
                    case RESULT_OK:
                        Bundle bundle = data.getExtras();
                        int editType = bundle.getInt(Constants.P_EDITTYPE);
                        int itemType = bundle.getInt(Constants.P_ITEMTYPE);
                        switch (itemType) {
                            case Constants.E_ITEMS_deviceName:
                                String name = bundle.getString(Constants.P_EDITRESULTSTRING);
                                if (!TextUtils.isEmpty(name)) {
                                    device_name.setContent(name);
                                    updateDeviceInfoByKey(Constants.P_DEVICEINFO_deviceName, name);
                                } else {
                                    Constants.ToastAction(ResV.getString(R.string.add_device_name_null));
                                }

                                break;
                            case Constants.E_ITEMS_category:
                                EditListInfo eInfoType = (EditListInfo) bundle.getSerializable(Constants.P_EDITRESULTITEM);
                                if (null != eInfoType) {
                                    deviceType = eInfoType.getName();
                                    typeId = (int) eInfoType.getId();
                                    device_type.setContent(deviceType);

                                    deviceModel = "";
                                    modeId = 0;
                                    device_model.setContent(deviceModel);
                                    device_model.initEditiHint();
                                }
                                break;
                            case Constants.E_ITEMS_brand:
                                EditListInfo eInfoBrand = (EditListInfo) bundle.getSerializable(Constants.P_EDITRESULTITEM);
                                if (null != eInfoBrand) {
                                    deviceBrand1 = eInfoBrand.getName();
                                    brand1Id = (int) eInfoBrand.getId();
                                    device_brand1.setContent(deviceBrand1);

                                    deviceModel = "";
                                    modeId = 0;
                                    device_model.setContent(deviceModel);
                                    device_model.initEditiHint();
                                }
                                break;
                            case Constants.E_ITEMS_model:
                                EditListInfo eInfoModel = (EditListInfo) bundle.getSerializable(Constants.P_EDITRESULTITEM);
                                if (null != eInfoModel) {
                                    deviceModel = eInfoModel.getName();
                                    modeId = (int) eInfoModel.getId();
                                    device_model.setContent(deviceModel);

                                    String key = Constants.P_DEVICEINFO_category + Constants.S_UPDATEDEVICEKEY_FG +
                                            Constants.P_DEVICEINFO_typeId + Constants.S_UPDATEDEVICEKEY_FG +
                                            Constants.P_DEVICEINFO_brand + Constants.S_UPDATEDEVICEKEY_FG +
                                            Constants.P_DEVICEINFO_brandId + Constants.S_UPDATEDEVICEKEY_FG +
                                            Constants.P_DEVICEINFO_model + Constants.S_UPDATEDEVICEKEY_FG +
                                            Constants.P_DEVICEINFO_modelId;
                                    String values = deviceType + Constants.S_UPDATEDEVICEKEY_FG +
                                            typeId + Constants.S_UPDATEDEVICEKEY_FG +
                                            deviceBrand1 + Constants.S_UPDATEDEVICEKEY_FG +
                                            brand1Id + Constants.S_UPDATEDEVICEKEY_FG +
                                            deviceModel + Constants.S_UPDATEDEVICEKEY_FG +
                                            modeId;
                                    updateDeviceInfo(key, values);
                                }
                                break;
                        }

                        break;
                }
                break;
        }

        // TODO Auto-generated method stub
		/*if(requestCode == IMAGE_REQUEST_CODE){
			if (data != null && data.getData() != null) {
				Constants.startPhotoZoom(this,PhotosGallery.getPhotosUri(mContext, data.getData())
						,RESULT_REQUEST_CODE);
			}
		}else if(requestCode == CAMERA_REQUEST_CODE){
			if (FileUtils.isSdcardExist()) {
				File tempFile = new File(
						Environment.getExternalStorageDirectory(),
						imageFileName);
				Constants.startPhotoZoom(this,Uri.fromFile(tempFile),RESULT_REQUEST_CODE);
			} else {
				Constants.ToastAction("没有检测到内存卡");
			}
		}else if(requestCode == RESULT_REQUEST_CODE){
			if (data != null) {
				Constants.setImageToView(data, mHandler);
//				setImageToView(data);
			}
		}else if(requestCode == Constants.REQUEST_ImageActivity){
			if (data != null ) {
				Bundle bundle=data.getExtras(); 
				if(bundle != null){
					boolean isD = bundle.getBoolean(Constants.P_IMAGEBROWERDELETE);
					if(isD){
						devicePhoto = bundle.getStringArray(Constants.P_IMAGEBROWERURLS);
						image_array_view.setImageUrl(devicePhoto);
						updateDeviceInfoByKey(Constants.P_DEVICEINFO_devicePhoto,Constants.stringArray2string(devicePhoto));
					}
				}
			}
		}
		else{
			if(addDeviceType == 1)
				title_layout.setRightTextEdit(true);
			switch (resultCode)
			{
			case RESULT_OK:
				Bundle bundle=data.getExtras(); 
				int editType = bundle.getInt(Constants.P_EDITTYPE);
				int itemType = bundle.getInt(Constants.P_ITEMTYPE);
				switch(itemType){
				case Constants.E_ITEMS_deviceName:
					String name = bundle.getString(Constants.P_EDITRESULTSTRING);
					if(!TextUtils.isEmpty(name)){
						device_name.setContent(name);
						updateDeviceInfoByKey(Constants.P_DEVICEINFO_deviceName,name);
					}else{
						Constants.ToastAction(STR_DEVICE_NAME_NULL);
					}
					
					break;
				case Constants.E_ITEMS_category:
					EditListInfo eInfoType = (EditListInfo)bundle.getSerializable(Constants.P_EDITRESULTITEM);
					if(null != eInfoType){
						deviceType = eInfoType.getName();
						typeId = eInfoType.getId();
						device_type.setContent(deviceType);
						
						deviceModel = "";
						modeId = 0;
						device_model.setContent(deviceModel);
					}
					break;
				case Constants.E_ITEMS_brand:
					EditListInfo eInfoBrand = (EditListInfo)bundle.getSerializable(Constants.P_EDITRESULTITEM);
					if(null != eInfoBrand){
						deviceBrand1 = eInfoBrand.getName();
						brand1Id = eInfoBrand.getId();
						device_brand1.setContent(deviceBrand1);
						
						deviceModel = "";
						modeId = 0;
						device_model.setContent(deviceModel);
					}
					break;
				case Constants.E_ITEMS_model:
					EditListInfo eInfoModel = (EditListInfo)bundle.getSerializable(Constants.P_EDITRESULTITEM);
					if(null != eInfoModel){
						deviceModel = eInfoModel.getName();
						modeId = eInfoModel.getId();
						device_model.setContent(deviceModel);
						String key =Constants.P_DEVICEINFO_category + Constants.S_UPDATEDEVICEKEY_FG +
								Constants.P_DEVICEINFO_typeId + Constants.S_UPDATEDEVICEKEY_FG +
								Constants.P_DEVICEINFO_brand + Constants.S_UPDATEDEVICEKEY_FG +
								Constants.P_DEVICEINFO_brandId + Constants.S_UPDATEDEVICEKEY_FG +
								Constants.P_DEVICEINFO_model + Constants.S_UPDATEDEVICEKEY_FG +
								Constants.P_DEVICEINFO_modelId ;
						String values = deviceType +  Constants.S_UPDATEDEVICEKEY_FG +
								typeId + Constants.S_UPDATEDEVICEKEY_FG +
								deviceBrand1 + Constants.S_UPDATEDEVICEKEY_FG +
								brand1Id+ Constants.S_UPDATEDEVICEKEY_FG +
								deviceModel + Constants.S_UPDATEDEVICEKEY_FG +
								modeId;
						updateDeviceInfo(key,values);
					}
					break;
					default:
						if(addDeviceType == 1){
							mcDeviceBasicsInfo = (McDeviceBasicsInfo) bundle.getSerializable(Constants.P_MCDEVICEBASICSINFO);
						}
						break;
				}
				
				break;
			}
		}*/

    }

    private void updateDeviceInfoByKey(String key, String value) {
        if (null != mcDeviceBasicsInfo && !TextUtils.isEmpty(value)) {
            long deviceId = mcDeviceBasicsInfo.getId();
            new updateDeviceInfoByKeyAsync(mContext, mHandler).execute(String.valueOf(deviceId), key, value);
        }
    }

    private void updateDeviceInfo(String key, String value) {
        if (null != mcDeviceBasicsInfo && !TextUtils.isEmpty(value)) {
            long deviceId = mcDeviceBasicsInfo.getId();
            new updateDeviceInfoAsync(mContext, mHandler).execute(String.valueOf(deviceId), key, value);
        }
    }
	/*private void addDevice(){
		if(null == deviceInfo){
			deviceInfo = new DeviceInfo();
		}
		if(!TextUtils.isEmpty(device_card.getContent())){
			deviceInfo.setLicense(device_card.getContent());
		}else{
			Constants.ToastAction(ResV.getString(R.string.add_device_card_null));
			return;
		}
		if(!TextUtils.isEmpty(device_name.getContent())){
			deviceInfo.setDevieName(device_name.getContent());
		}else{
			Constants.ToastAction(ResV.getString(R.string.add_device_name_null));
			return;
		}
		if(!TextUtils.isEmpty(device_type.getContent())){
			DeviceKindInfo kind = new DeviceKindInfo();
			kind.setId(typeId);
			kind.setName(deviceType);
			deviceInfo.setType(kind);
		}else{
			Constants.ToastAction(ResV.getString(R.string.add_device_type_null));
			return;
		}
		if(!TextUtils.isEmpty(device_brand1.getContent())){
			DeviceBrandInfo brand = new DeviceBrandInfo();
			brand.setId(brand1Id);
			brand.setName(deviceBrand1);
			deviceInfo.setBrand(brand);
		}else{
			Constants.ToastAction(ResV.getString(R.string.add_device_brand_null));
			return;
		}
		if(!TextUtils.isEmpty(device_model.getContent())){
			DeviceModelInfo mode = new DeviceModelInfo();
			mode.setId(modeId);
			mode.setModelName(deviceModel);
			deviceInfo.setModel(mode);
		}else{
			Constants.ToastAction(ResV.getString(R.string.add_device_model_null));
			return;
		}
		int bimpSize = Bimp.tempSelectBitmap.size();
		if(bimpSize>0){
			String[] bimpUrl = new String[bimpSize];
			for(int i=0; i<bimpSize; i++){
				if(null != Bimp.tempSelectBitmap.get(i).getImageUrl())
					bimpUrl[i] = Bimp.tempSelectBitmap.get(i).getImageUrl();
			}
			deviceInfo.setImages(bimpUrl);
		}
		if(!TextUtils.isEmpty(nameImageUrl)){
			String[] nameImage = new String[1];
			nameImage[0] = nameImageUrl;
			
			deviceInfo.setNameplatePhoto(nameImage);
		}else{
			if(null != deviceInfo.getNameplatePhoto() && deviceInfo.getNameplatePhoto().length>0){
			}else{
				Constants.ToastAction(ResV.getString(R.string.add_device_nameplate_null));
				return;
			}
			
		}
		if(!TextUtils.isEmpty(engineImageUrl)){
			deviceInfo.setEnginePhoto(engineImageUrl);
		}else{
			if(null != deviceInfo.getEnginePhoto() && deviceInfo.getEnginePhoto().length()>0){
			}else{
				Constants.ToastAction(ResV.getString(R.string.add_device_Engine_null));
				return;
			}
			
		}
		if(!TextUtils.isEmpty(device_year.getContent())){
			deviceInfo.setFactoryTime(device_year.getContent());
		}
//		if(image_array_view.getImageUrl()!=null && image_array_view.getImageUrl().length>0){
//			mcDeviceBasicsInfo.setDevicePhoto(image_array_view.getImageUrl());
//		}
		new AddDeviceAsync(mContext,mHandler,deviceInfo).execute();
		
	}*/

    private void gotoEditActivity(CanBeEditItemView view, int editType, int itemType) {
        if (status != 0 || editType == Constants.E_DEVICE_DATA ||
                editType == Constants.E_DEVICE_LIST || editType == Constants.E_DEVICE_TEXT) {
            view.requestFocus();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.P_TITLETEXT, view.getTitle());
            bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
            bundle.putInt(Constants.P_EDITTYPE, editType);
            bundle.putInt(Constants.P_ITEMTYPE, itemType);
            Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
        } else {
            if (status == 0)
                title_layout.setRightTextEdit(true);
            view.isEdit(true);
            view.getEdit_view().requestFocus();
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view.getEdit_view(), InputMethodManager.RESULT_SHOWN);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }

    private void gotoEditActivity(CanBeEditItemView view, int editType, int itemType, int v1, int v2) {
        view.requestFocus();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.P_TITLETEXT, view.getTitle());
        bundle.putString(Constants.P_EDITRESULTSTRING, view.getContent());
        bundle.putInt(Constants.P_EDITTYPE, editType);
        bundle.putInt(Constants.P_ITEMTYPE, itemType);
        bundle.putInt(Constants.P_EDIT_LIST_VALUE1, v1);
        bundle.putInt(Constants.P_EDIT_LIST_VALUE2, v2);
        Constants.toActivityForR(this, EditLayoutActivity.class, bundle, 0);
    }

    private void showPickDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("确定放弃新增机器？")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AddDeviceActivity.this.finish();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                    }
                }).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
		/*if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(status == 0){
				showPickDialog();
				return true;
			}
		}*/
        return super.onKeyDown(keyCode, event);
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        //MobclickAgent.onPageStart(UMengKey.time_machine_info);
        super.onResume();

    }

    private void gotoCapture(int codeType) {
//		captureSensorType = sensorType;
		/*Intent intent = new Intent();
		intent.setClass(this, MipcaActivityCapture.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Constants.P_CODE_TYPE, codeType);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);*/
    }


}
