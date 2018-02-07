package com.cloudmachine.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.FileUtils;
import com.cloudmachine.utils.ToastUtils;
import com.umeng.socialize.sina.helper.MD5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemLongClickedPopWindow extends PopupWindow implements View.OnClickListener {
    private String mImgUrl;
    private Context mContext;

    public ItemLongClickedPopWindow(Context context) {
        super(context);
        mContext = context;
        View popView = LayoutInflater.from(context).inflate(R.layout.list_item_longclicked_img, null);
        TextView saveTv = (TextView) popView.findViewById(R.id.item_longclicked_saveImage);
        TextView cacelTv = (TextView) popView.findViewById(R.id.item_longclicked_cancel);
        saveTv.setOnClickListener(this);
        cacelTv.setOnClickListener(this);
        popView.setOnClickListener(this);
        setContentView(popView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.PopAnimationStyle);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        setBackgroundDrawable(dw);
        setFocusable(true);
        setOutsideTouchable(true);
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_longclicked_cancel:
            case R.id.item_lcpop_view:
                dismiss();
                break;
            case R.id.item_longclicked_saveImage:
                dismiss();
                SaveImage saveTask = new SaveImage();
                saveTask.execute();
                break;


        }


    }


    /***
     * 功能：用线程保存图片
     *
     * @author wangyp
     */
    private class SaveImage extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            boolean result;
            try {
                FileUtils.createDirFile(Constants.SYS_IMG_PATH);
                String fileName = MD5.hexdigest(mImgUrl) + ".jpg";
                String newFilePath = Constants.SYS_IMG_PATH + fileName;
                File file = new File(newFilePath);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                    }
                } else {
                    return true;
                }
                InputStream inputStream = null;
                URL url = new URL(mImgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                if (conn.getResponseCode() == 200) {
                    inputStream = conn.getInputStream();
                }
                byte[] buffer = new byte[4096];
                int len = 0;
                FileOutputStream outStream = new FileOutputStream(file);
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                outStream.close();
                result = true;
                Intent mediaScanIntent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                mContext.sendBroadcast(mediaScanIntent);
            } catch (Exception e) {
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                ToastUtils.showToast(mContext, "图片已保存至相册");
            }
        }
    }
}