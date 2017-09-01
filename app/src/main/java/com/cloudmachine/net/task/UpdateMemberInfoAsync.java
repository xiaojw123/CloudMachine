package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.Member;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UpdateMemberInfoAsync extends ATask {

	private Context context;
	private Handler handler;
	private String key;
	private String value;
	private String memid = "0";
	
	
	public UpdateMemberInfoAsync(String key,String value,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.key = key;
		this.value = value;
			try{
				memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
			}catch(Exception ee){
				
			}
		
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("memberId", memid));
		list.add(new BasicNameValuePair("key", key));
    	list.add(new BasicNameValuePair("value", value));
		String result = null;
		try {
			result = httpRequest.post(URLs.EDITINFO_URL, list);
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
				BaseBO<Member> loginBO = gson.fromJson(result, new TypeToken<BaseBO<Member>>(){}.getType());
				Member member = loginBO.getResult();
				msg.what = Constants.HANDLER_UPDATEMEMBERINFO_SUCCESS;
				if(null != member){
					MemeberKeeper.saveOAuth(member, context);
					msg.obj = member;
				}
				handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_UPDATEMEMBERINFO_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

	
}
