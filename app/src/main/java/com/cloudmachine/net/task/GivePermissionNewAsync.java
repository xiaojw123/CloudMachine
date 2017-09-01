package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GivePermissionNewAsync extends ATask {

	private Handler handler;
	private int messageType ;//1 增加成员   2移交机主
	private long toMemberId;
	private long deviceIdS;
	
	public GivePermissionNewAsync(long toMemberId,long deviceIdS,int messageType,Context context,Handler handler){
		this.handler = handler;
		this.toMemberId = toMemberId;
		this.deviceIdS = deviceIdS;
		if (messageType==3){
			messageType=1;
		}
		this.messageType = messageType;
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
	}

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("fromMemberId", memberId));
		list.add(new BasicNameValuePair("toMemberId", String.valueOf(toMemberId)));
		list.add(new BasicNameValuePair("deviceIdS", String.valueOf(deviceIdS)));

		list.add(new BasicNameValuePair("messageType", String.valueOf(messageType)));
		String result = null;
		try {
				result = httpRequest.post(URLs.ADD_MEMBER_NEW, list);
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
				msg.what = Constants.HANDLER_ADDMEMBER_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
				/*JSONObject json = new JSONObject(result);
				if(json.has("code")){
					String code = json.getString("code");
					if(code.equals("1")||code.equals("2")||code.equals("5")){
						msg.what = Constants.HANDLER_ADDMEMBER_SUCCESS;
						msg.obj = json.has("message")?json.getString("message"):"";
						handler.sendMessage(msg);
						return;
					}else if(json.has("message")){
						msg.what = Constants.HANDLER_ADDMEMBER_FAIL;
						msg.obj = json.getString("message");
						handler.sendMessage(msg);
						return;
					}
				}*/
			}catch(Exception e){
			}
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_ADDMEMBER_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	
	
}
