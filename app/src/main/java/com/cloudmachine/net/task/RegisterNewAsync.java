package com.cloudmachine.net.task;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RegisterNewAsync extends ATask {

	private Handler handler;
	private String mobile;
	private String pwd;
	private String code;
	private String inviteCode;
	
	
	public RegisterNewAsync(String mobile,String pwd,String code,Context context,Handler handler,String inviteCode){
		this.handler = handler;
		this.mobile = mobile;
		this.pwd = pwd;
		this.code = code;
		this.inviteCode = inviteCode;
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobile", mobile));
		list.add(new BasicNameValuePair("pwd", pwd));
    	list.add(new BasicNameValuePair("code", code));
		if (inviteCode.equals("-1")) {

		} else {
			list.add(new BasicNameValuePair("inviteCode", inviteCode));
		}
		String result = null;
		try {
			result = httpRequest.post(URLs.REGISTNEW_URL, list);
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
				BaseBO updateResult = gson.fromJson(result, BaseBO.class);
					msg.what = Constants.HANDLER_REGISTER_SUCCESS;
					msg.obj = updateResult.getMessage();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_REGISTER_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

	
}
