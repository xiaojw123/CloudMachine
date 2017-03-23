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
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;

public class MessageUpdateStatusAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	private String messageId;
	private long inviteId;
	private int position;
	
	public MessageUpdateStatusAsync(int type,long messageId,long inviteId,int position,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.type = type;
		this.messageId = String.valueOf(messageId);
		this.inviteId = inviteId;
		this.position = position;
		//缓存数据第1步
	}


	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memberId = "";
		try{
			memberId = String.valueOf(MemeberKeeper
					.getOauth(context).getId());
		}catch(Exception ee){
		}
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("messageId", messageId));
		String result = null;
		try {
			if(type==1){
				result = httpRequest.post(URLs.ACCEPTE, list);		
			}else if(type==2){
				result = httpRequest.post(URLs.REFUSE, list);					
			}else if(type==3){
				result = httpRequest.post(URLs.UPDATEMESSAGESTATUS, list);					
			}
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
					    if(type==1){
						 msg.what = Constants.HANDLER_MESSAGEACCEPTE_SUCCESS;
						}
						if(type==2){
							msg.what = Constants.HANDLER_MESSAGEREFUSE_SUCCESS;
						}
						if(type == 3){
							msg.what = Constants.HANDLER_MESSAGEUPSTATE_SUCCESS;
						}
					 	msg.obj = inviteId;
					 	msg.arg1 = position;
						handler.sendMessage(msg);
						return;
			} catch (Exception e) {
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_MESSAGEACTION_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}


	
	
}
