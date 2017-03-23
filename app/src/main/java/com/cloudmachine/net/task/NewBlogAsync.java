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

public class NewBlogAsync extends ATask {

	private Handler handler;
	private String content;
	private String blogImage;
	private String title;
	private String status;
	private String memberId = "0";
	
	
	public NewBlogAsync(String content,String blogImage,String title,String status,Context context,Handler handler){
		this.handler = handler;
		this.content = content;
		this.blogImage = blogImage;
		this.title = title;
		this.status = status;
			try{
				memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
			}catch(Exception ee){
				
			}
		
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("content", content));
		list.add(new BasicNameValuePair("blogImage", blogImage));
		list.add(new BasicNameValuePair("title", title));
		list.add(new BasicNameValuePair("status", status));
		list.add(new BasicNameValuePair("categoryId", String.valueOf(0)));
		String result = null;
		try {
				result = httpRequest.post(URLs.NEW_BLOD,list);
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
				BaseBO baseBO = gson.fromJson(result, BaseBO.class);
					msg.what = Constants.HANDLER_NEWBLOG_SUCCESS;
					msg.obj = baseBO.getMessage();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_NEWBLOG_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
