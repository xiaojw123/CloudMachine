package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

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

public class BlogCommentsListAsync extends ATask {

	private Context context;
	private Handler handler;
	private long blogId;
	private int pageNo;
	private String memid = null;
	
	public BlogCommentsListAsync(long blogId, int pageNo,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.blogId = blogId;
		this.pageNo = pageNo;
		try{
			memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
		}
		//缓存数据第1步
		if(pageNo == 1){
			getCacheName(URLs.GET_BLODCOMMENT,String.valueOf(blogId));
		}
	}


	@Override
	protected String doInBackground(String... params) {
		
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("blogId", String.valueOf(blogId)));
		list.add(new BasicNameValuePair("pageNo", String
				.valueOf(pageNo)));
		list.add(new BasicNameValuePair("pageSize", String
				.valueOf(MyApplication.getInstance().getPageSize())));

		if(!TextUtils.isEmpty(memid))
			list.add(new BasicNameValuePair("memberId",memid));
		String result = null;
		try {
			result = httpRequest.post(URLs.GET_BLODCOMMENT,list);
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
				 BaseBO<List<News>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<News>>>(){}.getType());	
					 msg.what = Constants.HANDLER_BLOGCOMMENTSLIST_SUCCESS;
					 msg.obj = baseBO.getResult();
					 msg.arg1 = pageNo;
					 handler.sendMessage(msg);
					return;
			} catch (Exception e) {
			}
		} 
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_BLOGCOMMENTSLIST_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}


	
	
}
