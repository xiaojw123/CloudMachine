package com.cloudmachine.helper;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.QiToken;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.ToastUtils;
import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.File;

import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xiaojw on 2017/11/2.
 */

public class QiniuManager {

    private static UploadManager uploadManager;

    public static UploadManager getUploadManager() {
        if (uploadManager == null) {
            Configuration config = new Configuration.Builder()
                    .chunkSize(512 * 1024)        // 分片上传时，每片的大小。 默认256K
                    .putThreshhold(1024 * 1024)   // 启用分片上传阀值。默认512K
                    .connectTimeout(10)           // 链接超时。默认10秒
                    .useHttps(true)               // 是否使用https上传域名
                    .responseTimeout(60)          // 服务器响应超时。默认60秒
                    .zone(FixedZone.zone0)        // 设置区域，指定不同区域的上传域名、备用域名、备用IP。
                    .build();
            uploadManager = new UploadManager(config);
        }
        return uploadManager;
    }

    public static void uploadFile(final Context context, final OnUploadListener listener, final File file, final String dir) {
        Api.getDefault(HostType.HOST_H5).getQinuParams().compose(RxHelper.<QiToken>handleResult()).subscribe(new RxSubscriber<QiToken>(context) {
            @Override
            protected void _onNext(final QiToken qiToken) {
                Compressor.getDefault(context)
                        .compressToFileAsObservable(file)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                getUploadManager().put(file, dir + file.getName(), qiToken.getUptoken(), new UpCompletionHandler() {
                                    @Override
                                    public void complete(String key, ResponseInfo info, final JSONObject response) {
                                        if (info.isOK()) {
                                            if (listener != null) {
                                                listener.uploadSuccess(qiToken.getOrigin() + key);
                                            }
                                        } else {
                                            _onError("图片上传失败，请检查网络后重试");
                                        }
                                    }
                                }, null);
                            }
                        });


            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(context, message);
                if (listener != null) {
                   listener.uploadFailed();
                }
            }
        });
    }

    public interface OnUploadListener {

        void uploadSuccess(String picUrl);
        void uploadFailed();

    }


}
