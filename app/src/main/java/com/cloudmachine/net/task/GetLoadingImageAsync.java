package com.cloudmachine.net.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.LoadingImageInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whl.helper.activity.DownloadListener;
import com.whl.helper.activity.MultiThreadDownload;

public class GetLoadingImageAsync extends ATask  {

	private Context context;
	private Handler handler;
	
	public GetLoadingImageAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETROOTNODES, list);
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
			try {
				Gson gson = new Gson();
				 BaseBO<LoadingImageInfo> baseBO = gson.fromJson(result, new TypeToken<BaseBO<LoadingImageInfo>>(){}.getType());				
					 saveImage(baseBO.getResult());
//					 msg.what = Constants.HANDLER_GETROOTNODES_SUCCESS;
//						msg.obj = baseBO.getResult();
//						handler.sendMessage(msg);
						return;
			} catch (Exception e) {
			}
		} else {
		}
//		if(!isHaveCache){
//			msg.what = Constants.HANDLER_GETDEVICEMEMBER_FAIL;
//			msg.obj = message;
//			handler.sendMessage(msg);
//		}
	}
	/*options = new DisplayImageOptions.Builder()  
    .showStubImage(R.drawable.ic_stub)          // 设置图片下载期间显示的图片  
    .showImageForEmptyUri(R.drawable.ic_empty)  // 设置图片Uri为空或是错误的时候显示的图片  
    .showImageOnFail(R.drawable.ic_error)       // 设置图片加载或解码过程中发生错误显示的图片      
    .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
    .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
    .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片  
    .build();                                   // 创建配置过得DisplayImageOption对象  
*/	
	private void saveImage(LoadingImageInfo info){
		 String imageUrl = info.getImageUrl();
		 String savePath = context.getFilesDir()+File.separator+"loadingImage"+File.separator;  
	     String fileName = "loadingImage.jpg";  
	     MultiThreadDownload multiThreadDownload =  new MultiThreadDownload(context,imageUrl, savePath, fileName, new DownloadListener() {
			
			@Override
			public void onPause(boolean isCompelted, boolean isPause, boolean isCancel,
					int fileSize, int completedSize, int downloadedSize,
					float downloadPercent, float downloadSpeed) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDownloading(boolean isCompelted, boolean isPause,
					boolean isCancel, int fileSize, int completedSize,
					int downloadedSize, float downloadPercent, float downloadSpeed) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancelDownload() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBeforeDownload(boolean isCompleted, boolean isPause,
					boolean isCancel, int fileSize) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterDownload(boolean isCompleted, boolean isPause,
					boolean isCancel, int fileSize) {
				// TODO Auto-generated method stub
				
			}
		}); 
	     multiThreadDownload.start();
	}
	
}
