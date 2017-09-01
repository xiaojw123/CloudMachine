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
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;

public class CommentBlogAsync extends ATask {

	private Handler handler;
	private String blogId;
	private String content;
	private String parentId;
	private String toMemberId;
	private String memberId = "0";
	
	
	public CommentBlogAsync(String blogId,String content,String parentId,String toMemberId,Context context,Handler handler){
		this.handler = handler;
		this.blogId = blogId;
		this.content = content;
		this.parentId = parentId;
		this.toMemberId = toMemberId;
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
		list.add(new BasicNameValuePair("blogId", blogId));
		if(null != parentId)
			list.add(new BasicNameValuePair("parentId", parentId));
		if(null != toMemberId)
			list.add(new BasicNameValuePair("toMemberId", toMemberId));
		String result = null;
		try {
				result = httpRequest.post(URLs.COMMENT_BLOG,list);
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
					msg.what = Constants.HANDLER_COMMENTBLOG_SUCCESS;
					msg.obj = baseBO.getMessage();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_COMMENTBLOG_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
