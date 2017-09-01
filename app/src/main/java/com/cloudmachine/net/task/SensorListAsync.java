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
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.SensorListInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SensorListAsync extends ATask {

	private Context context;
	private Handler handler;
	private long deviceId;
	
	public SensorListAsync(long deviceId, Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.deviceId = deviceId;
		//缓存数据第1步
		getCacheName(Constants.URL_MyDevices,String.valueOf(deviceId));
	}


	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETSENSORLIST, list);
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
				 BaseBO<List<SensorListInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<SensorListInfo>>>(){}.getType());				
					 	msg.what = Constants.HANDLER_GETSENSORLIST_SUCCESS;
						msg.obj = baseBO.getResult();
						handler.sendMessage(msg);
						return;
			} catch (Exception e) {
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETSENSORLIST_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}


	
	
}
