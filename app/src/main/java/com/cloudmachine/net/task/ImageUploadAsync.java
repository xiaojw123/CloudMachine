package com.cloudmachine.net.task;



import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
	
public class ImageUploadAsync extends AsyncTask<String, String, String>{
	
	public static final int ImageUpload_Success = 0x8; 
	public static final int ImageUpload_Fail = 0x9; 
	private Handler handler;
	private String urlString;
	private int kinds = 0;//传多种图片的时候标明图片的类型 //2:机博图片上传
	
	public ImageUploadAsync(Handler handler, String urlString) {
		super();
		this.handler = handler;
		this.urlString = urlString;
	}

	public ImageUploadAsync(Handler handler, String urlString,int kinds) {
		super();
		this.handler = handler;
		this.urlString = urlString;
		this.kinds = kinds;
	}
	@Override
	protected String doInBackground(String... params) {
		String content = null;
		if(urlString!=null){
			if(kinds==1111){
				content = uploadFile(urlString,URLs.UPLOAD_AVATOR);
			}else{
				content = uploadFile(urlString,URLs.UPLOAD_IMG_PATH);
			}
			
		}
		 if (content != null && content.length() > 10) {
			 Gson gson = new Gson();
			 UploadResult uploadResult = gson.fromJson(content,
			 UploadResult.class);
			 if (uploadResult.getError() == 0) {
				// 返回url
				 return uploadResult.getUrl();
			 }
		 }
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		Message msg = new Message();
		if(result!=null){
			msg.what = ImageUpload_Success;
			if(kinds==Constants.KEY_ImageUpload_Kinds_m1){
				msg.obj = urlString+Constants.S_FG+result;
			}else if(kinds!=0&&kinds!=1111){
				msg.obj = kinds+"#"+result;
			}else{
				msg.obj = result;
			}

		}else{
			msg.what = ImageUpload_Fail;
			Log.e("Uploadimage", urlString+"#"+kinds);
		}
		handler.sendMessage(msg);
		super.onPostExecute(result);
	}
	public static String uploadFile(String fileName, String actionUrl) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(actionUrl);
			MultipartEntity mulentity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			FileBody filebody = new FileBody(new File(fileName));

			mulentity.addPart("imgFile", filebody);

			httpPost.setEntity(mulentity);
			HttpResponse response = httpclient.execute(httpPost);
			// 如果返回状态为200，获得返回的结果
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream inputStream = response.getEntity().getContent();
				return inputStreamToString(inputStream);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public static String inputStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	/**
	 * 上传图片返回的json数据  
	 * @author sj
	 *
	 */
	 class UploadResult {
			/** code  成功返回0 */
			private int error;

			/** 图片上传的路径 */
			private String url;
			
			
			public int getError() {
				return error;
			}
			public void setError(int error) {
				this.error = error;
			}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			@Override
			public String toString() {
				return "UploadResult [error=" + error + ", url=" + url + "]";
			}
		
	}

}
