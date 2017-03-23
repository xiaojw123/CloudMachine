package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.app.MyApplication;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.News;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class NewListAsync extends ATask{

	private Context context;
	private Handler handler;
	private String pageNo= "1";
	private String catId = "0";
	
	public  NewListAsync(String pageNo, String catId,Context context,Handler handler) {
		this.context = context;
		this.handler = handler;
		this.pageNo = pageNo;
		this.catId = catId;
		if(pageNo.equals("1")){
			getCacheName(URLs.COMMUNITY_URL,catId,pageNo);
		}
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("pageNo", pageNo));
		list.add(new BasicNameValuePair("pageSize", String
				.valueOf(MyApplication.getInstance().getPageSize())));
		list.add(new BasicNameValuePair("categoryId", catId));
		if(MemeberKeeper.getOauth(context)!=null){
			list.add(new BasicNameValuePair("memberId",String.valueOf(MemeberKeeper.getOauth(context).getId())));
		}
		String result = null;
		try {
			result = httpRequest.post(URLs.COMMUNITY_URL,list);
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
		if(isSuccess){
			Gson gson = new Gson();
			try{
				BaseBO<List<News>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<News>>>(){}.getType());				
						msg.what = Constants.HANDLER_GETCOMMUNITYLIST_SUCCESS;
						msg.obj = baseBO.getResult();
						handler.sendMessage(msg);
						return;
			}catch(Exception e){
				
			}
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETCOMMUNITYLIST_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
		
		
}
