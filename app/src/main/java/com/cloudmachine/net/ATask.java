package com.cloudmachine.net;

import android.os.AsyncTask;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;

public class ATask extends AsyncTask<String, Integer, String>{

	private final static int SYSTEM_ERROR = 100;// 系统类错误
	private final static int PARAM_ERROR = 200;// 参数类错误
	private final static int SUCCESS = 800;// 成功
	private final static String CODE = "code";
	private final static String RESULT = "result";
    private final static String MESSAGE = "message";
	
	////缓存数据一共4步 参考 DevicesListAsync.java
	public String cacheName;
	public boolean isHaveCache ; // 有没有缓存
	public boolean isNeedCache;//是否需要缓存
	public boolean isNetData;//是否网络请求来的数据
	public boolean isSuccess;
	public String message = "操作失败，请稍后重试！";
	private int codeLog = 999;
	public String memberId = "0";
	public boolean isNull;
	
	/**
	 * 接口说明：
	 * 返回code：
	 * -1：失败  
	 * 0：列表返回成功 
	 * 1：请求返回成功
	**/
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		String cache = getCacheData();
		if(null != cache){
			isHaveCache = true;
			decodeJson(cache);
		}else{
			isHaveCache = false;
		}
		
		if (!MyApplication.getInstance().isOpenNetwork(MyApplication.mContext)) {
			UIHelper.ToastMessage(MyApplication.mContext,
					MyApplication.mContext.getResources().getString(R.string.no_network));
			return ;
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		isNetData = true;
	}
	protected void decodeJson(String result){
		if(isNeedCache && isNetData){
			isNetData = false;
			Constants.setObjcet2File(result,cacheName);
		}
		
		try{
//			JSONObject json = new JSONObject(result);
			Gson gson = new Gson();
			BaseBO bbResult = gson.fromJson(result, BaseBO.class);
			//Constants.MyLog("拿到请求返回值"+bbResult.toString());
			message = bbResult.getMessage();
			codeLog = bbResult.getCode();
			if( codeLog == SUCCESS /*&& bbResult.getResult()!=null*/){
				isSuccess = true;
				if(bbResult.getResult()!=null){
					isNull = false;
				}else{
					isNull = true;
				}
				return;
			}
			/*
			if(json.has(CODE)){
				codeLog = json.getInt(CODE);
				if(codeLog == SUCCESS){
					
					if(json.has(RESULT)&&json.get(RESULT)!=null&&!json.get(RESULT).equals("null")){
						isSuccess = true;
						json = null;
						return;
					}
					
				}
			}
			isSuccess = false;
			message = json.getString(MESSAGE);
			json = null;*/
//			if(json.has(CODE)&&json.getString(CODE).equals(SUCCESS)){
//				isSuccess = true;
//			}else{
//				isSuccess = false;
//				message = json.getString(MESSAGE);
//			}
			
		}catch(Exception e){
			
		}
		
		
	}
	
	public String getCacheData(){
			if(null != cacheName){
				String cache = (String)Constants.getObjcet2File(cacheName);
				return cache;
			}else{
				return null;
			}
			
	}
	
	public void getCacheName(String url){
		isNeedCache = true;
		cacheName ="URL_Cache_" + url.substring(url.lastIndexOf("/",url.lastIndexOf("/")-1)+1, url.length());
		cacheName = cacheName.replace("/", "_");
	}
	public void getCacheName(String url,String v){
		getCacheName(url);
		cacheName +="_"+v;
	}
	public void getCacheName(String url,String v,String v1){
		getCacheName(url,v);
		cacheName +="_"+v1;
	}
	public void getCacheName(String url,String v,String v1,String v2){
		getCacheName(url,v,v1);
		cacheName +="_"+v2;
	}

}
