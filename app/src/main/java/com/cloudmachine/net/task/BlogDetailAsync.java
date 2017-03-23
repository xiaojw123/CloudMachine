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
import com.cloudmachine.struc.News;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BlogDetailAsync extends ATask{

	
	private Context context;
	private Handler handler;
	private long blogId;
	public BlogDetailAsync(long blogId,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.blogId = blogId;
		getCacheName(URLs.GET_BLOD,String.valueOf(blogId));
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("blogId", String
				.valueOf(blogId)));
		String result = null;
		try {
			result = httpRequest.post(URLs.GET_BLOD,list);
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
			Gson gson = new Gson();					 
			 BaseBO<News> baseBO = gson.fromJson(result, new TypeToken<BaseBO<News>>(){}.getType());
			 
			 News one =baseBO.getResult();
			 if(null != one){
				 msg.what = Constants.HANDLER_BLOGDETAIL_SUCCESS;
			     msg.obj = one;
				 handler.sendMessage(msg);
				return;
			 }
		} 
		if(!isHaveCache){
			msg.what = Constants.HANDLER_BLOGDETAIL_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}
	
	
}


