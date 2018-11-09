package com.cloudmachine.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.utils.Constants;

import java.io.File;

public class DownloadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (completeDownloadId == MySharedPreferences.getSharedPLong(Constants.KEY_DownloadId)) {
                try {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//						downloadManager.openDownloadedFile(completeDownloadId);
                    Cursor c = downloadManager.query(new DownloadManager.Query().setFilterById(completeDownloadId));
                    c.moveToFirst();
//                    String str = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));

                    context.startActivity(getApkFileIntent(context));
//                    UserHelper.insertGuideTag(context,false);
//                    UserHelper.insertHConfigGuideTag(context,false);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }


    }

    public static Intent getApkFileIntent(Context context) {
        File apkFile =
                new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.APK_NAME);
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(context, "com.cloudmachine.fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
//        Uri uri = Uri.parse(param);
//        uri = PhotosGallery.getPhotosUri(context,uri);
//        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

}
