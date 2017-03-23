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
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;

public class GetMobileCodeAsync extends ATask {

	private Handler handler;
	private String mobile;
	private String type;
	
	
	public GetMobileCodeAsync(String mobile,String type,Context context,Handler handler){
		this.handler = handler;
		this.mobile = mobile;
		this.type = type;
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile", mobile));
		list.add(new BasicNameValuePair("type", type));
		String result = null;
		try {
			result = httpRequest.post(URLs.GETCODE_URL, list);
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
			try{
				Gson gson = new Gson();
				BaseBO updateResult = gson.fromJson(result,
						BaseBO.class);
					msg.what = Constants.HANDLER_GETCODE_SUCCESS;
					msg.obj = updateResult.getMessage();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETCODE_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

	
}
