package com.cloudmachine.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.UploadResult;
import com.cloudmachine.chart.utils.AppLog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.cloudmachine.net.api.Api.CONNECT_TIME_OUT;
import static com.cloudmachine.net.api.Api.READ_TIME_OUT;

/**
 * 项目名称：CloudMachine
 * 类描述：图片上传工具类
 * 创建人：shixionglu
 * 创建时间：2017/3/23 下午2:34
 * 修改人：shixionglu
 * 修改时间：2017/3/23 下午2:34
 * 修改备注：
 */

public class UploadPhotoUtils {

    private volatile static UploadPhotoUtils mInstance;

    private OkHttpClient mOkHttpClient;

    private Handler mHandler;

    private Context mContext;

    public final OkHttpClient okHttpClient;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
    private String url;

    private UploadPhotoUtils(Context context) {
        super();

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        //设置拦截日志，拦截请求体
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request build = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("osPlatform", "Android")
                        .addHeader("osVersion", VersionU.getVersionName())
                        .build();
                return chain.proceed(build);
            }
        };

        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logInterceptor)
                .retryOnConnectionFailure(false)
                .build();
    }

    public static UploadPhotoUtils getInstance(Context context) {
        UploadPhotoUtils temp = mInstance;
        if (temp == null) {
            synchronized (UploadPhotoUtils.class) {
                temp = mInstance;
                if (temp == null) {
                    temp = new UploadPhotoUtils(context);
                    mInstance = temp;
                }
            }
        }
        return temp;
    }

    public void upLoadFile(String filename, String uploadurl, final Handler mHandler) {
        final Message msg = Message.obtain();
        File file = new File(filename);
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("imgFile", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(uploadurl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Gson gson = new Gson();
                UploadResult uploadResult = gson.fromJson(result,
                        UploadResult.class);

                if (uploadResult.getError() == 0) {
                    // 返回url

                    url = uploadResult.getUrl();
                    msg.what = Constants.HANDLER_UPLOAD_SUCCESS;
                    msg.obj = url;
                } else {
                    msg.what = Constants.HANDLER_UPLOAD_FAILD;
                    msg.obj = "图片上传失败";
                }
                mHandler.sendMessage(msg);
            }
        });

    }


    public void upLoadFile(File file, String uploadurl, final Handler mHandler) {
        final Message msg = Message.obtain();
        // File file = new File(filename);
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("imgFile", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(uploadurl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                AppLog.print("Call onResponse response___" + result);
                if (result != null) {
                    Gson gson = new Gson();
                    UploadResult uploadResult = gson.fromJson(result,
                            UploadResult.class);

                    if (uploadResult.getError() == 0) {
                        // 返回url
                        url = uploadResult.getUrl();
                        msg.what = Constants.HANDLER_UPLOAD_SUCCESS;
                        msg.obj = url;
                    } else {
                        msg.what = Constants.HANDLER_UPLOAD_FAILD;
                        msg.obj = "图片上传失败";
                    }
                    mHandler.sendMessage(msg);
                }
            }
        });

    }

    public void upLoadForH5File(File file, String uploadurl, final Handler mHandler) {
        final Message msg = Message.obtain();
        // File file = new File(filename);
        MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        mbody.addFormDataPart("imgFile", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        RequestBody requestBody = mbody.build();
        final Request request = new Request.Builder()
                .url(uploadurl)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                AppLog.print("onFailure___e__" + e + ", message__" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                try {
                    JSONObject resultJobj = new JSONObject(result);
                    String dataStr = resultJobj.optString("data");
                    AppLog.print("sss__dataStr：" + dataStr);
                    msg.what = Constants.HANDLER_UPLOAD_SUCCESS;
                    msg.obj = dataStr;
                    mHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

}
