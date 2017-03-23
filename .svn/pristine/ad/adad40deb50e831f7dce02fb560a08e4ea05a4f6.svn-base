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
import com.cloudmachine.struc.SupportInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SupportAsync extends ATask{

	
	private Context context;
	private Handler handler;
	private long blogId;
	private int type;
	private int position;
	public SupportAsync(long blogId,int type,int position,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.blogId = blogId;
		this.type = type;
		this.position = position;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		list.add(new BasicNameValuePair("memberId", String
				.valueOf(MemeberKeeper.getOauth(context).getId())));
		list.add(new BasicNameValuePair("objId",  String.valueOf(blogId)));
		list.add(new BasicNameValuePair("type", String.valueOf(type)));
		String result = null;
		try {
			result = httpRequest.post(URLs.BLOG_SUPPORT, list);
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
			 BaseBO<SupportInfo> baseBo = gson.fromJson(result, new TypeToken<BaseBO<SupportInfo>>(){}.getType());				
//			BaseBO<SupportInfo> baseBO = gson.fromJson(result, BaseBO.class);
//			UIHelper.ToastMessage(context, baseBO.getMessage());
			/*int code = baseBo.getResult().getKey();
			if(code == 0){
				code = 2;
			}else{
				code = 0;
			}*/
			//
//			{"code":800,"result":{"key":0,"goodCount":0},"ok":true,"message":null}
			//key 0:取消点赞  1：点赞    count 总数
//			if(code == 0 || code == 2){//0是点赞成功 2 是取消点赞
				if(position!=-1){
					msg.what = Constants.HANDLER_BLOGSUPPORTLIST_SUCCESS;
				}else{
					msg.what = Constants.HANDLER_BLOGSUPPORT_SUCCESS;
				}
					msg.obj = baseBo.getResult();
					msg.arg1 = position;
//					msg.arg2 = code;
//				}else{
//					msg.what = Constants.HANDLER_BLOGSUPPORT_SUCCESS;
//					msg.arg2 = code;
//				}
//			msg.obj = baseBo.getMessage();
			handler.sendMessage(msg);
			return;
		} 
		if(position!=-1){
			msg.what = Constants.HANDLER_BLOGSUPPORTLIST_FAIL;
		}else{
			msg.what = Constants.HANDLER_BLOGSUPPORT_FAIL;
		}
		msg.obj = message;
		handler.sendMessage(msg);
	}
	
	
	
}


