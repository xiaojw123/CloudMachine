package com.cloudmachine.helper;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.cloudmachine.chart.utils.AppLog;

/**
 * Created by xiaojw on 2018/6/27.
 */

public class ImageHepler {


    public static  String getPickImgePath(Context context,Intent data){
        if (Build.VERSION.SDK_INT >= 19) {
            return  handleImageOnKitKat(context,data);
        }else{
            return handleImageBeforeKitKat(context,data);
        }
    }
    @TargetApi(19)
    private static  String handleImageOnKitKat(Context context,Intent data) {
        String  imagePath = null;
        Uri imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(context, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            AppLog.print("imageUri:" + imageUri + ", imgeUri authority____" + imageUri.getAuthority() + "___scheme__" + imageUri.getScheme());
//            imageUri:content://com.android.externalstorage.documents/document/primary%3APhoto_LJ%2F1493098068425.JPEG,
// imgeUri authority____com.android.externalstorage.documents___scheme__content

            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri, null);
            } else if ("com.android.externalstorage.documents".equalsIgnoreCase(imageUri.getAuthority())) {
                String path = imageUri.getPath();
                if (path != null) {
                    if (path.contains(":")) {
                        String name = path.split(":")[1];
                        imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name;
                    }
                }
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(context,imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }
        return  imagePath;

    }
    private static String handleImageBeforeKitKat(Context context,Intent intent) {
        Uri imageUri = intent.getData();
        return getImagePath(context,imageUri, null);
    }
    private static  String getImagePath(Context context,Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
