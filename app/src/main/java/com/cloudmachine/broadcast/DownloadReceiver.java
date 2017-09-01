package com.cloudmachine.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.PhotosGallery;

public class DownloadReceiver extends BroadcastReceiver {

	private static Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		String action = intent.getAction();
		if(null!=action&&action.length()>0){
			if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
				long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
				if(completeDownloadId == MySharedPreferences.getSharedPLong(Constants.KEY_DownloadId)){
					try {
						DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE); 
//						downloadManager.openDownloadedFile(completeDownloadId);
						Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(completeDownloadId));
						c.moveToFirst();
						String str = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

						context.startActivity(getApkFileIntent(str));
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static Intent getApkFileIntent( String param ) {
		 
        Intent intent = new Intent();  
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(param);
        uri = PhotosGallery.getPhotosUri(context,uri);
        intent.setDataAndType(uri,"application/vnd.android.package-archive"); 
        return intent;
    }
 
}
