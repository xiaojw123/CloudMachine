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

public class SearchBlogAsync extends ATask {

	private Handler handler;
	private String title;
	
	
	public SearchBlogAsync(String title,Context context,Handler handler){
		this.handler = handler;
		this.title = title;
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
//		list.add(new BasicNameValuePair("title", title));
		list.add(new BasicNameValuePair("content", title));
		String result = null;
		try {
			result = httpRequest.post(URLs.BLOG_SEARCH, list);
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
				if(!isNull){
					Gson gson = new Gson();
					BaseBO<List<News>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<News>>>(){}.getType());		
						msg.what = Constants.HANDLER_SEARCHBLOG_SUCCESS;
						msg.obj = baseBO.getResult();
						handler.sendMessage(msg);
					return;
				}
				
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_SEARCHBLOG_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

	
}
