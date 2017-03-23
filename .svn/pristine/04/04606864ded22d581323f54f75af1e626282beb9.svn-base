package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.app.MyApplication;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.LocationXY;
import com.cloudmachine.struc.News;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LocusListAsync extends ATask {

	private Context context;
	private Handler handler;
	private long deviceId;
	private long startTime;
	private long endTime;
	
	public LocusListAsync(long deviceId, long startTime,long endTime,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.deviceId = deviceId;
		this.startTime = startTime;
		this.endTime = endTime;
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
		}
		//缓存数据第1步
	}


	@Override
	protected String doInBackground(String... params) {
		
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("memberId",memberId));
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		list.add(new BasicNameValuePair("startTime", String.valueOf(startTime)));
		list.add(new BasicNameValuePair("endTime", String.valueOf(endTime)));

		String result = null;
		try {
			result = httpRequest.post(URLs.LOCUS,list);
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
				 BaseBO<List<LocationXY>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<LocationXY>>>(){}.getType());	
					 msg.what = Constants.HANDLER_LOCUS_SUCCESS;
					 msg.obj = baseBO.getResult();
					 handler.sendMessage(msg);
					return;
			} catch (Exception e) {
			}
		} 
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_LOCUS_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}


	
	
}
