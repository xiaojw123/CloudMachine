package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.Member;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetMemberInfoAsync extends ATask {

	private Context context;
	private Handler handler;
	private String memid = "0";
	private boolean isMyself;
	
	public GetMemberInfoAsync(String memid,Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.memid = memid;
		if(null == memid){
			isMyself = true;
			try{
				this.memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
			}catch(Exception ee){
				
			}
		}
		
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("memberId", memid));
		String result = null;
		try {
			result = httpRequest.post(URLs.MEMBER_INFO, list);
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
				if(null != member){
					if(isMyself){
						MemeberKeeper.saveOAuth(member, context);
					}
					msg.what = Constants.HANDLER_GETMEMBERINFO_SUCCESS;
					msg.obj = member;
					handler.sendMessage(msg);
				}
				
				return;
			}catch(Exception e){
				Constants.MyLog(e.getMessage());
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache && !TextUtils.isEmpty(memid)){
			msg.what = Constants.HANDLER_GETMEMBERINFO_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

	
}
