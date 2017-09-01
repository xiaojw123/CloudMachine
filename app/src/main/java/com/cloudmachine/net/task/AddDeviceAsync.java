package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

public class AddDeviceAsync extends ATask {

	private Handler handler;
	private McDeviceBasicsInfo mcDeviceInfo;
	
	
	public AddDeviceAsync(Context context,Handler handler,McDeviceBasicsInfo mcDeviceInfo){
		this.handler = handler;
		this.mcDeviceInfo =mcDeviceInfo;
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
	}

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_DEVICESSAVE, initListData());
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		decodeJson(result);
	}

	
	
	@Override
	protected void decodeJson(String result) {
		// TODO Auto-generated method stub
		super.decodeJson(result);

		Message msg = Message.obtain();
		if (isSuccess) {
			try{
				Gson gson = new Gson();
				BaseBO baseBo = gson.fromJson(result,
						BaseBO.class);
				msg.what = Constants.HANDLER_ADDDEVICE_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} 
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_ADDDEVICE_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	private List<NameValuePair> initListData(){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		list.add(Utils.addBasicValue("memberId", memberId));
		if(null != mcDeviceInfo){
//			if(mcDeviceInfo.getDeviceName()!=null){
//				list.add(Utils.addBasicValue("memberId", memberId));
//			}
//			if(deviceId!=0){
//				list.add(Utils.addBasicValue("id", String.valueOf(deviceId)));
//			}
//			list.add(Utils.addBasicValue("checkType", "1"));
//			list.add(Utils.addBasicValue("memberId", String.valueOf(MemeberKeeper.getOauth(AddDeviceActivity.this).getId())));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_devicePhoto, null!= mcDeviceInfo.getDevicePhoto()?Constants.stringArray2string(mcDeviceInfo.getDevicePhoto()):""));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_nameplatePhoto, null!= mcDeviceInfo.getNameplatePhoto()?Constants.stringArray2string(mcDeviceInfo.getNameplatePhoto()):""));
			
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_deviceName, mcDeviceInfo.getDeviceName()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_brand, mcDeviceInfo.getBrand()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_category, mcDeviceInfo.getCategory()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_model, mcDeviceInfo.getModel()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_typeId, String.valueOf(mcDeviceInfo.getTypeId())));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_brandId, String.valueOf(mcDeviceInfo.getBrandId())));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_modelId, String.valueOf(mcDeviceInfo.getModelId())));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_factoryTime, mcDeviceInfo.getFactoryTime()));//出厂时间
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_deviceType, String.valueOf(mcDeviceInfo.getDeviceType())));//心机二手
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_buyTime, mcDeviceInfo.getBuyTime()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_buyPlace, mcDeviceInfo.getBuyPlace()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_buyPrice, String.valueOf(mcDeviceInfo.getBuyPrice())));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_sellerName, mcDeviceInfo.getSellerName()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_sellerPlace,mcDeviceInfo.getSellerPlace()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_sellerContacts, mcDeviceInfo.getSellerContacts()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_sellerMobi, mcDeviceInfo.getSellerMobi()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_sellerEmail, mcDeviceInfo.getSellerEmail()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_insurer, mcDeviceInfo.getInsurer()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_insurerNo, mcDeviceInfo.getInsurerNo()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_company, mcDeviceInfo.getCompany()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_contractNo, mcDeviceInfo.getContractNo()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_serviceName, mcDeviceInfo.getServiceName()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_serviceMobi, mcDeviceInfo.getServiceMobi()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_servicePlace, mcDeviceInfo.getServicePlace()));
			list.add(Utils.addBasicValue(Constants.P_DEVICEINFO_filtNumber, mcDeviceInfo.getFiltNumber()));
			
//			list.add(Utils.addBasicValue("idCardPhoto", SparseArrayToString(resultMap)));
		}
		return list;
	}
}
