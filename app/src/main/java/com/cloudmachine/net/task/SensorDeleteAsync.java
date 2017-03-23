package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;

public class SensorDeleteAsync extends ATask {

	private Context context;
	private Handler handler;
	private long deviceId;
	private String sensorId;
	private String memid = "0";
	
	
	public SensorDeleteAsync(long deviceId, String sensorId, 
			Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.deviceId = deviceId;
		this.sensorId = sensorId;
		try{
			memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
		//缓存数据第1步
	}


	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		list.add(new BasicNameValuePair("sensorId", String.valueOf(sensorId)));
		list.add(new BasicNameValuePair("member", memid));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_DELETESENSOR, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		//缓存数据第2步
		super.onPostExecute(result);
		decodeJson(result);
	}


	@Override
	protected void decodeJson(String result) {
		// TODO Auto-generated method stub
		//缓存数据第3步
		super.decodeJson(result);
		Message msg = Message.obtain();
		if (isSuccess) {
			try {
				Gson gson = new Gson();
				BaseBO baseBO = gson.fromJson(result, BaseBO.class);				
					 	msg.what = Constants.HANDLER_DELETESENSOR_SUCCESS;
						msg.obj = baseBO.getMessage();
						handler.sendMessage(msg);
						return;
			} catch (Exception e) {
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_DELETESENSOR_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	
}
