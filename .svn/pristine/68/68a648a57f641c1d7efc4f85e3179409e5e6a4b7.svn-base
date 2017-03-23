package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.app.MyApplication;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.McDeviceScanningInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DevicesInfoAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	private int checkRandom;
	
	public DevicesInfoAsync(Context context,Handler handler,int checkRandom){
		this.context = context;
		this.handler = handler;
		this.checkRandom = checkRandom;
	}
	
	@Override
	protected void onPreExecute() {
		if (!MyApplication.getInstance().isOpenNetwork(context)) {
			UIHelper.ToastMessage(context, "网络连接失败,请检查您的网络设置");
			return ;
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String deviceId = params[0];
		String rowSize = params[1];
		String macAddress = params[2];
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memid = "0";
		try{
			memid = String.valueOf(MemeberKeeper
					.getOauth(context).getId());
		}catch(Exception ee){
			
		}
		list.add(new BasicNameValuePair("memberId", memid));
		list.add(new BasicNameValuePair("deviceId", deviceId));
		list.add(new BasicNameValuePair("rowSize", rowSize));
		list.add(new BasicNameValuePair("gatewayId", macAddress));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_DevicesCheck, list);
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
				JSONObject json = new JSONObject(result);
					if(json.has("result"))
					{
						String gsonStr = json.getString("result");
						if (!TextUtils.isEmpty(gsonStr)&&gsonStr.length()>2) { 
							Gson gson = new Gson();
							McDeviceScanningInfo mcDeviceInfo = gson.fromJson(gsonStr,
									new TypeToken<McDeviceScanningInfo>() {
									}.getType());
							msg.what = Constants.HANDLER_GETDEVICEINFO_SUCCESS;
							msg.obj = mcDeviceInfo;
							msg.arg1 = checkRandom;
							handler.sendMessage(msg);
							return;
						}
					}
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_GETDEVICEINFO_FAIL;
		msg.obj = message;
		msg.arg1 = checkRandom;
		handler.sendMessage(msg);
	}

	
	
}
